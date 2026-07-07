package com.v1ctim.mod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

/**
 * Walker: se mueve aleatoriamente por el mundo, pero siempre gira para
 * mirar al jugador mas cercano mientras camina. Totalmente inofensivo:
 * no tiene metas de ataque ni hace dano.
 *
 * Textura: mismo estilo que Stalker (Steve con cara pixelada), puede
 * reusar la textura o usar una variante distinta.
 */
public class WalkerEntity extends Monster {

    public WalkerEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        // Prioridad 1 = se evalua primero; LookAtPlayerGoal con prioridad
        // mas alta que el movimiento hace que siga girando la cabeza/cuerpo
        // hacia el jugador incluso mientras camina.
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, net.minecraft.world.entity.player.Player.class, 20.0f));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.6D));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        // Sin goals de tipo MeleeAttackGoal / NearestAttackableTargetGoal:
        // este mob nunca ataca ni persigue con intencion de danar.
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23D)
                .add(Attributes.FOLLOW_RANGE, 20.0D)
                .add(Attributes.ATTACK_DAMAGE, 0.0D);
    }
}
