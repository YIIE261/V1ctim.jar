package com.v1ctim.mod.event;

import com.v1ctim.mod.V1ctimMod;
import com.v1ctim.mod.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;

import java.util.HashSet;
import java.util.Set;

@EventBusSubscriber(modid = V1ctimMod.MOD_ID)
public class NightmareDimensionHandler {

    public static final ResourceKey<Level> NIGHTMARE_LEVEL =
            ResourceKey.create(net.minecraft.core.registries.Registries.DIMENSION,
                    ResourceLocation.fromNamespaceAndPath(V1ctimMod.MOD_ID, "nightmare"));

    private static final Set<Long> PROCESSED_CHUNKS = new HashSet<>();

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        LevelAccessor levelAccessor = event.getLevel();
        if (!(levelAccessor instanceof net.minecraft.server.level.ServerLevel serverLevel)) return;
        if (!serverLevel.dimension().equals(NIGHTMARE_LEVEL)) return;
        if (!(event.getChunk() instanceof LevelChunk chunk)) return;

        reskinChunk(serverLevel, chunk);
    }

    private static void reskinChunk(net.minecraft.server.level.ServerLevel serverLevel, LevelChunk chunk) {
        net.minecraft.world.level.ChunkPos chunkPos = chunk.getPos();
        long key = chunkPos.toLong();
        if (!PROCESSED_CHUNKS.add(key)) return;

        int minY = serverLevel.getMinBuildHeight();
        int maxY = serverLevel.getMaxBuildHeight();

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = chunkPos.getMinBlockX() + x;
                int worldZ = chunkPos.getMinBlockZ() + z;
                for (int y = minY; y < maxY; y++) {
                    BlockPos pos = new BlockPos(worldX, y, worldZ);
                    BlockState current = chunk.getBlockState(pos);
                    BlockState replacement = reskin(current);
                    if (replacement != null) {
                        serverLevel.setBlock(pos, replacement, Block.UPDATE_ALL);
                    }
                }
            }
        }
    }

    private static BlockState reskin(BlockState state) {
        if (state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.DIRT)
                || state.is(Blocks.COARSE_DIRT) || state.is(Blocks.PODZOL)
                || state.is(Blocks.MYCELIUM) || state.is(Blocks.ROOTED_DIRT)) {
            return ModBlocks.WATCHING_EYES_BLOCK.get().defaultBlockState();
        }

        if (state.is(Blocks.OAK_LOG) || state.is(Blocks.OAK_WOOD)
                || state.is(Blocks.STRIPPED_OAK_LOG) || state.is(Blocks.STRIPPED_OAK_WOOD)) {
            BlockState teethLog = ModBlocks.TEETH_LOG.get().defaultBlockState();
            if (state.hasProperty(RotatedPillarBlock.AXIS)) {
                teethLog = teethLog.setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS));
            }
            return teethLog;
        }

        if (state.is(Blocks.OAK_LEAVES)) {
            return ModBlocks.MISSING_FILE_BLOCK.get().defaultBlockState();
        }

        if (state.is(Blocks.WATER)) {
            return ModBlocks.BLOOD_WATER_BLOCK.get().defaultBlockState();
        }

        return null;
    }
}
