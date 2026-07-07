package com.v1ctim.mod.entity;

import com.v1ctim.mod.entity.goal.DigTowardsPlayerGoal;
import com.v1ctim.mod.registry.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

/**
 * V1ctim2 (fase 2): a diferencia de V1ctim (que solo golpea una vez y
 * desaparece), este SI es agresivo: persigue jugadores, ataca cuerpo a
 * cuerpo, y puede "minar" bloques que le bloqueen el camino usando
 * DigTowardsPlayerGoal. Al ser golpeado, en vez del sonido de dano
 * normal, suelta un grito saturado/distorsionado.
 */
public class Victim2Entity extends Monster {

    public Victim2Entity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(2, new DigTowardsPlayerGoal(this, 3.0D));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.8D));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.ARMOR, 2.0D);
    }

    /**
     * Sobrescribe el sonido/reaccion al recibir dano: en vez del "hurt sound"
     * normal, reproduce el grito saturado definido en ModSounds.
     */
    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean result = super.hurt(source, amount);
        if (result && !this.level().isClientSide) {
            this.level().playSound(null, this.blockPosition(),
                    ModSounds.VICTIM2_SCREAM.get(), SoundSource.HOSTILE, 1.3f, 0.9f);
        }
        return result;
    }

    @Override
    protected net.minecraft.sounds.SoundEvent getHurtSound(DamageSource source) {
        // Silencia el sonido de dano vanilla; el grito saturado se maneja
        // manualmente en hurt() arriba para tener control total sobre el
        // volumen/pitch y que suene "roto" a proposito.
        return null;
    }

    @Nullable
    @Override
    public net.minecraft.world.entity.SpawnGroupData finalizeSpawn(ServerLevelAccessor level,
            DifficultyInstance difficulty, EntitySpawnReason spawnType,
            @Nullable net.minecraft.world.entity.SpawnGroupData spawnData) {
        // Punto de extension: aqui se podria anadir logica para que
        // V1ctim2 aparezca como "evolucion" de V1ctim tras cierta condicion,
        // en vez de spawnear de forma independiente.
        return super.finalizeSpawn(level, difficulty, spawnType, spawnData);
    }
}
