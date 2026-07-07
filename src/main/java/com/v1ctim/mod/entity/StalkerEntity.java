package com.v1ctim.mod.entity;

import com.v1ctim.mod.registry.ModSounds;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

/**
 * Stalker: no se mueve, solo gira para mirar fijamente al jugador mas
 * cercano. Al acercarse el jugador, suena un golpe saturado/distorsionado
 * (ver checkAndPlayHitSound, llamado desde el tick).
 *
 * La textura (skin de Steve con la cara reemplazada por ruido de pixeles)
 * se define en el resource pack: assets/v1ctim/textures/entity/stalker.png
 * junto con su Renderer registrado en el bus de cliente (no incluido aqui
 * porque va en una clase separada solo para el lado cliente).
 */
public class StalkerEntity extends Monster {

    private static final double HIT_SOUND_RANGE = 2.0;
    private int hitSoundCooldown = 0;

    public StalkerEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        // Sin IA de movimiento: se elimina/ignora cualquier goal de caminar.
        this.goalSelector.addGoal(0, new LookAtPlayerGoal(this, net.minecraft.world.entity.player.Player.class, 24.0f));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.0D) // no se mueve
                .add(Attributes.FOLLOW_RANGE, 24.0D)
                .add(Attributes.ATTACK_DAMAGE, 0.0D);
    }

    @Override
    public void tick() {
        super.tick();
        if (hitSoundCooldown > 0) {
            hitSoundCooldown--;
        }
        if (!this.level().isClientSide) {
            for (LivingEntity nearby : this.level().getEntitiesOfClass(LivingEntity.class,
                    this.getBoundingBox().inflate(HIT_SOUND_RANGE))) {
                if (nearby instanceof net.minecraft.world.entity.player.Player && hitSoundCooldown <= 0) {
                    this.level().playSound(null, this.blockPosition(),
                            ModSounds.SATURATED_HIT.get(),
                            net.minecraft.sounds.SoundSource.HOSTILE, 1.2f, 1.0f);
                    hitSoundCooldown = 40; // ~2 segundos antes de poder volver a sonar
                    break;
                }
            }
        }
    }

    @Override
    public boolean isPushable() {
        return false;
    }
}
