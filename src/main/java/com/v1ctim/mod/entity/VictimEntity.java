package com.v1ctim.mod.entity;

import com.v1ctim.mod.registry.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * V1ctim: la estrella del mod. No es un mob de combate normal, es un
 * evento sorpresa: aparece cerca de un jugador al azar (ver
 * event/CorruptionSpreadHandler.java para la logica de spawn global),
 * golpea una vez (quita la mitad de la vida actual + ceguera) y desaparece.
 *
 * Textura: modelo humanoide con skin completamente negra, sin rasgos.
 */
public class VictimEntity extends Monster {

    private int lifeTicks = 0;
    private boolean hasStruck = false;

    // Cuanto tiempo (en ticks) vive la entidad antes de auto-eliminarse.
    private static final int MAX_LIFE_TICKS = 60; // ~3 segundos
    private static final double STRIKE_RANGE = 3.0;

    public VictimEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.setNoAi(true); // no camina, no persigue: aparece y actua una vez
        this.setSilent(true); // sin sonidos de mob "normal" (pisadas, ambiente)
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 1000.0D) // no deberia poder ser matado en el forcejeo
                .add(Attributes.MOVEMENT_SPEED, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 0.0D)
                .add(Attributes.ATTACK_DAMAGE, 0.0D);
    }

    @Override
    public void tick() {
        super.tick();
        lifeTicks++;

        if (!this.level().isClientSide) {
            if (!hasStruck) {
                for (Player player : this.level().getEntitiesOfClass(Player.class,
                        this.getBoundingBox().inflate(STRIKE_RANGE))) {
                    strikePlayer(player);
                    hasStruck = true;
                    break;
                }
            }

            // Se elimina solo tras un rato corto, haya golpeado o no.
            if (lifeTicks >= MAX_LIFE_TICKS) {
                this.discard();
            }
        }
    }

    private void strikePlayer(Player player) {
        // Le quita la mitad de la vida ACTUAL del jugador (no un valor fijo).
        float currentHealth = player.getHealth();
        player.setHealth(currentHealth / 2.0f);

        // Ceguera durante unos 6 segundos (120 ticks), amplificador 0.
        player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 120, 0));

        this.level().playSound(null, this.blockPosition(),
                ModSounds.VICTIM_APPEAR_GLITCH.get(), SoundSource.HOSTILE, 1.5f, 1.0f);
    }
}
