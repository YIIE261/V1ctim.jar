package com.v1ctim.mod.worldgen;

import com.mojang.serialization.Codec;
import com.v1ctim.mod.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.SpawnerBlock;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * "Tunel Minero Descendente" - una estructura procedural (no un archivo
 * .nbt fijo) que empieza en la superficie y cava un tunel de seccion 2x2
 * hacia abajo, en zigzag suave, hasta alcanzar capas negativas profundas.
 * Cada tramo tiene antorchas de redstone incrustadas en las paredes, y al
 * final del tunel se genera una camara con varios "spawners" configurados
 * para Stalker y Walker - la "horda masiva" descrita en el diseno.
 *
 * Se genera como Feature de worldgen normal (ver worldgen/placed_feature y
 * forge/biome_modifier en resources para que realmente aparezca en el
 * mundo), por lo que su forma varia un poco cada vez que se genera.
 */
public class MineTunnelFeature extends Feature<NoneFeatureConfiguration> {

    // Profundidad total que cava el tunel antes de terminar en la camara
    // de hordas. El mundo en 1.21.8 llega hasta Y = -64 como minimo.
    private static final int MIN_DEPTH_BELOW_SURFACE = 55;
    private static final int MAX_DEPTH_BELOW_SURFACE = 95;
    private static final int WORLD_MIN_Y_MARGIN = 6;

    // Cada cuantos bloques de descenso se coloca una antorcha de redstone.
    private static final int TORCH_INTERVAL = 5;
    // Cada cuantos bloques de descenso el tunel puede desviarse lateralmente.
    private static final int DRIFT_INTERVAL = 4;

    public MineTunnelFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos.MutableBlockPos cursor = context.origin().mutable();

        int worldMinY = level.getMinBuildHeight() + WORLD_MIN_Y_MARGIN;
        int depth = MIN_DEPTH_BELOW_SURFACE
                + random.nextInt(MAX_DEPTH_BELOW_SURFACE - MIN_DEPTH_BELOW_SURFACE);
        int targetY = Math.max(worldMinY, cursor.getY() - depth);

        Direction[] horizontal = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
        Direction currentDrift = horizontal[random.nextInt(horizontal.length)];

        int stepsSinceTorch = 0;
        int stepsSinceDrift = 0;
        boolean torchOnA = true;

        while (cursor.getY() > targetY) {
            carve2x2(level, cursor);

            if (stepsSinceTorch >= TORCH_INTERVAL) {
                placeWallTorch(level, cursor, currentDrift, torchOnA);
                torchOnA = !torchOnA;
                stepsSinceTorch = 0;
            }

            cursor.move(Direction.DOWN);
            stepsSinceTorch++;
            stepsSinceDrift++;

            if (stepsSinceDrift >= DRIFT_INTERVAL) {
                if (random.nextInt(3) != 0) {
                    cursor.move(currentDrift);
                    carve2x2(level, cursor);
                }
                if (random.nextInt(2) == 0) {
                    currentDrift = horizontal[random.nextInt(horizontal.length)];
                }
                stepsSinceDrift = 0;
            }
        }

        carveHordeChamber(level, cursor, random);
        return true;
    }

    /** Carva un hueco de 2x2 (ancho x profundidad) por 2 de alto en la posicion dada. */
    private void carve2x2(WorldGenLevel level, BlockPos center) {
        for (int dx = 0; dx <= 1; dx++) {
            for (int dz = 0; dz <= 1; dz++) {
                for (int dy = 0; dy <= 1; dy++) {
                    BlockPos pos = center.offset(dx, dy, dz);
                    if (level.getBlockState(pos).is(Blocks.BEDROCK)) continue;
                    level.setBlock(pos, Blocks.CAVE_AIR.defaultBlockState(), 2);
                }
            }
        }
    }

    private void placeWallTorch(WorldGenLevel level, BlockPos center, Direction wallSide, boolean corner) {
        BlockPos wallBase = corner ? center : center.offset(1, 0, 1);
        BlockPos torchPos = wallBase.relative(wallSide.getOpposite());
        BlockState wallState = level.getBlockState(torchPos.relative(wallSide));

        if (!wallState.isAir()) {
            BlockState torchState = Blocks.REDSTONE_WALL_TORCH.defaultBlockState()
                    .setValue(RedstoneTorchBlock.LIT, true)
                    .setValue(net.minecraft.world.level.block.RedstoneWallTorchBlock.FACING, wallSide);
            if (level.getBlockState(torchPos).isAir()) {
                level.setBlock(torchPos, torchState, 2);
            }
        }
    }

    /**
     * Camara final del tunel: un espacio mas amplio con varios spawners
     * de Stalker y Walker para simular una horda al llegar al fondo.
     */
    private void carveHordeChamber(WorldGenLevel level, BlockPos center, RandomSource random) {
        int radius = 3;
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                for (int dy = 0; dy <= 3; dy++) {
                    BlockPos pos = center.offset(dx, dy, dz);
                    if (level.getBlockState(pos).is(Blocks.BEDROCK)) continue;
                    level.setBlock(pos, Blocks.CAVE_AIR.defaultBlockState(), 2);
                }
            }
        }
        // Piso solido bajo la camara.
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                BlockPos floor = center.offset(dx, -1, dz);
                if (level.getBlockState(floor).isAir()) {
                    level.setBlock(floor, Blocks.DEEPSLATE.defaultBlockState(), 2);
                }
            }
        }

        int spawnerCount = 3 + random.nextInt(3); // 3 a 5 spawners: la "horda masiva"
        for (int i = 0; i < spawnerCount; i++) {
            int dx = random.nextInt(radius * 2 + 1) - radius;
            int dz = random.nextInt(radius * 2 + 1) - radius;
            BlockPos spawnerPos = center.offset(dx, 0, dz);

            level.setBlock(spawnerPos, Blocks.SPAWNER.defaultBlockState(), 2);
            if (level.getBlockEntity(spawnerPos) instanceof SpawnerBlockEntity spawnerEntity) {
                boolean stalker = random.nextBoolean();
                spawnerEntity.getSpawner().setEntityId(
                        stalker ? ModEntities.STALKER.get() : ModEntities.WALKER.get(), random);
            }
        }

        // Antorchas de redstone en las esquinas de la camara para que no
        // quede totalmente a oscuras.
        for (Direction dir : new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST}) {
            BlockPos torchPos = center.relative(dir, radius);
            if (!level.getBlockState(torchPos.relative(dir)).isAir()) {
                level.setBlock(torchPos, Blocks.REDSTONE_WALL_TORCH.defaultBlockState()
                        .setValue(net.minecraft.world.level.block.RedstoneWallTorchBlock.FACING, dir.getOpposite()), 2);
            }
        }
    }
}
