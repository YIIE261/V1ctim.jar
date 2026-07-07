package com.v1ctim.mod.event;

import com.v1ctim.mod.V1ctimMod;
import com.v1ctim.mod.registry.ModBlocks;
import com.v1ctim.mod.registry.ModEntities;
import com.v1ctim.mod.registry.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.List;
import java.util.Random;

@EventBusSubscriber(modid = V1ctimMod.MOD_ID)
public class CorruptionSpreadHandler {

    private static final Random RANDOM = new Random();

    private static final int CORRUPTION_SCAN_RADIUS = 6;
    private static final double VICTIM_SPAWN_CHANCE_PER_TICK = 0.0002;
    private static final double VICTIM_SPAWN_RADIUS = 12.0;

    private static final long ENTITY_GRACE_PERIOD_TICKS = 7L * 60L * 20L;

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        for (ServerLevel serverLevel : event.getServer().getAllLevels()) {
            boolean entitiesAllowed = serverLevel.getGameTime() >= ENTITY_GRACE_PERIOD_TICKS;

            List<ServerPlayer> players = serverLevel.players();
            for (ServerPlayer player : players) {
                trySpreadCorruptionNear(serverLevel, player);
                playCorruptionCompassHum(serverLevel, player);
                if (entitiesAllowed) {
                    trySpawnVictim(serverLevel, player);
                }
            }
        }
    }

    private static void trySpreadCorruptionNear(ServerLevel level, ServerPlayer player) {
        BlockPos center = player.blockPosition();

        boolean nearCorruption = false;
        for (BlockPos pos : BlockPos.betweenClosed(
                center.offset(-CORRUPTION_SCAN_RADIUS, -2, -CORRUPTION_SCAN_RADIUS),
                center.offset(CORRUPTION_SCAN_RADIUS, 2, CORRUPTION_SCAN_RADIUS))) {
            if (level.getBlockState(pos).is(ModBlocks.CORRUPTION_EDGE_MARKER.get())) {
                nearCorruption = true;
                break;
            }
        }

        if (!nearCorruption) return;

        if (RANDOM.nextDouble() > 0.002) return;

        BlockPos target = center.offset(
                RANDOM.nextInt(CORRUPTION_SCAN_RADIUS * 2) - CORRUPTION_SCAN_RADIUS,
                RANDOM.nextInt(3) - 1,
                RANDOM.nextInt(CORRUPTION_SCAN_RADIUS * 2) - CORRUPTION_SCAN_RADIUS);

        Block current = level.getBlockState(target).getBlock();
        if (current == Blocks.STONE || current == Blocks.GRANITE || current == Blocks.ANDESITE) {
            level.setBlock(target, ModBlocks.CORRUPTED_STONE.get().defaultBlockState(), Block.UPDATE_ALL);
            level.playSound(null, target, ModSounds.CORRUPTION_EXPAND.get(),
                    SoundSource.BLOCKS, 0.5f, 0.8f + RANDOM.nextFloat() * 0.4f);
        } else if (current == Blocks.DIRT || current == Blocks.GRASS_BLOCK) {
            level.setBlock(target, ModBlocks.CORRUPTED_DIRT.get().defaultBlockState(), Block.UPDATE_ALL);
        }
    }

    private static void playCorruptionCompassHum(ServerLevel level, ServerPlayer player) {
        BlockPos center = player.blockPosition();
        boolean nearCorruption = false;
        for (BlockPos pos : BlockPos.betweenClosed(
                center.offset(-CORRUPTION_SCAN_RADIUS, -2, -CORRUPTION_SCAN_RADIUS),
                center.offset(CORRUPTION_SCAN_RADIUS, 2, CORRUPTION_SCAN_RADIUS))) {
            if (level.getBlockState(pos).is(ModBlocks.CORRUPTION_EDGE_MARKER.get())) {
                nearCorruption = true;
                break;
            }
        }

        if (nearCorruption && RANDOM.nextDouble() < 0.01) {
            level.playSound(null, player.blockPosition(),
                    ModSounds.WORLD_HUM_WRONG.get(),
                    SoundSource.AMBIENT, 0.6f, 1.0f);
        }
    }

    private static void trySpawnVictim(ServerLevel level, ServerPlayer player) {
        if (RANDOM.nextDouble() > VICTIM_SPAWN_CHANCE_PER_TICK) return;

        double angle = RANDOM.nextDouble() * Math.PI * 2;
        double dist  = 8.0 + RANDOM.nextDouble() * (VICTIM_SPAWN_RADIUS - 8.0);
        double spawnX = player.getX() + Math.cos(angle) * dist;
        double spawnZ = player.getZ() + Math.sin(angle) * dist;
        int spawnY = level.getHeight(
                net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING,
                (int) spawnX, (int) spawnZ);

        var victimType = ModEntities.VICTIM.get();
        var victim = victimType.create(level);
        if (victim == null) return;
        victim.moveTo(spawnX, spawnY, spawnZ, RANDOM.nextFloat() * 360, 0);
        level.addFreshEntityWithPassengers(victim);
    }
}
