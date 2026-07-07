package com.v1ctim.mod.entity.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

/**
 * Meta de IA personalizada: si el mob no puede alcanzar al jugador por un
 * bloque en el camino (path bloqueado), lo empieza a "minar" poco a poco
 * hasta romperlo, para poder seguir avanzando.
 *
 * No es un pathfinding real de excavacion (eso requeriria reescribir el
 * sistema de pathfinding de Minecraft) - es una aproximacion practica:
 * cada cierto intervalo revisa el bloque que tiene justo enfrente en
 * direccion al jugador y, si es "minable" (no es bedrock/liquido/etc),
 * lo rompe tras un tiempo de "excavacion".
 */
public class DigTowardsPlayerGoal extends Goal {

    private final Mob mob;
    private final double diggingRange;
    private BlockPos targetBlock;
    private int digProgressTicks;
    private static final int TICKS_TO_BREAK = 60; // ~3 segundos de "excavacion"

    public DigTowardsPlayerGoal(Mob mob, double diggingRange) {
        this.mob = mob;
        this.diggingRange = diggingRange;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = mob.getTarget();
        if (!(target instanceof Player player)) return false;
        if (mob.distanceTo(player) > diggingRange) return false;

        BlockPos candidate = findBlockingBlock(player);
        if (candidate != null) {
            this.targetBlock = candidate;
            return true;
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return targetBlock != null && mob.getTarget() != null
                && isMinable(mob.level(), targetBlock);
    }

    @Override
    public void start() {
        digProgressTicks = 0;
    }

    @Override
    public void stop() {
        targetBlock = null;
        digProgressTicks = 0;
    }

    @Override
    public void tick() {
        if (targetBlock == null) return;

        Level level = mob.level();
        mob.getLookControl().setLookAt(
                targetBlock.getX() + 0.5, targetBlock.getY() + 0.5, targetBlock.getZ() + 0.5);

        digProgressTicks++;

        // Particulas/sonido de excavacion se pueden anadir aqui usando
        // level.levelEvent(2001, targetBlock, Block.getId(state)) para el
        // efecto visual de romper bloque a mitad de progreso si se desea.

        if (digProgressTicks >= TICKS_TO_BREAK) {
            if (!level.isClientSide) {
                level.destroyBlock(targetBlock, false, mob);
            }
            targetBlock = null;
            digProgressTicks = 0;
        }
    }

    private BlockPos findBlockingBlock(Player player) {
        Vec3 direction = player.position().subtract(mob.position()).normalize();
        BlockPos inFront = mob.blockPosition().offset(
                (int) Math.round(direction.x), 0, (int) Math.round(direction.z));
        // Revisa el bloque a la altura de los pies y de la cabeza del mob.
        for (BlockPos pos : new BlockPos[]{inFront, inFront.above()}) {
            if (isMinable(mob.level(), pos)) {
                return pos;
            }
        }
        return null;
    }

    private boolean isMinable(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.isAir()) return false;
        if (state.is(BlockTags.WITHER_IMMUNE)) return false;
        if (state.getDestroySpeed(level, pos) < 0) return false; // irrompible (bedrock, etc)
        return state.getFluidState().isEmpty(); // evita liquidos
    }
}
