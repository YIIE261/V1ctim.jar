package com.v1ctim.mod.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * "Estatua de Steve de Piedra" - una estructura gigante y silenciosa que
 * puede aparecer en cualquier parte del mapa (superficie). Tiene la forma
 * de un Steve solido hecho de bloques oscuros, de casi el ancho de un
 * chunk y decenas de bloques de alto, con dos pequenos "ojos" pálidos en
 * la cara. No hace nada por si sola (no tiene IA ni loot) - es puramente
 * un elemento de terror ambiental/liminal que se ve desde lejos.
 *
 * Al igual que el tunel minero, se genera de forma procedural (no usa un
 * archivo .nbt), asi que su posicion varia pero su forma es siempre la
 * misma silueta de Steve.
 */
public class StoneSteveStatueFeature extends Feature<NoneFeatureConfiguration> {

    private static final BlockState BODY = Blocks.DEEPSLATE_TILES.defaultBlockState();
    private static final BlockState EYE = Blocks.LIGHT_GRAY_CONCRETE.defaultBlockState();

    public StoneSteveStatueFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos base = context.origin();

        // --- Piernas: dos columnas separadas por un pequeno hueco ---
        fillBox(level, base, -5, 0, -1, -3, 17, 1, BODY);
        fillBox(level, base, 3, 0, -1, 5, 17, 1, BODY);
        ancorarPiernaAlSuelo(level, base, -5, -1, -3, 1);
        ancorarPiernaAlSuelo(level, base, 3, -1, 5, 1);

        // --- Torso ---
        fillBox(level, base, -5, 18, -2, 5, 33, 2, BODY);

        // --- Brazos (cuelgan a los lados, pasando el borde inferior del torso) ---
        fillBox(level, base, -7, 10, -1, -6, 29, 1, BODY);
        fillBox(level, base, 6, 10, -1, 7, 29, 1, BODY);

        // --- Cabeza ---
        fillBox(level, base, -3, 34, -3, 3, 43, 3, BODY);

        // --- Ojos (cara frontal de la cabeza, mirando hacia -Z) ---
        fillBox(level, base, -2, 38, -3, -1, 39, -3, EYE);
        fillBox(level, base, 1, 38, -3, 2, 39, -3, EYE);

        return true;
    }

    /** Rellena una caja solida de blockstate en coordenadas relativas al origen de la estructura. */
    private void fillBox(WorldGenLevel level, BlockPos base, int x1, int y1, int z1, int x2, int y2, int z2, BlockState state) {
        int minX = Math.min(x1, x2), maxX = Math.max(x1, x2);
        int minY = Math.min(y1, y2), maxY = Math.max(y1, y2);
        int minZ = Math.min(z1, z2), maxZ = Math.max(z1, z2);

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    pos.set(base.getX() + x, base.getY() + y, base.getZ() + z);
                    if (level.getBlockState(pos).is(Blocks.BEDROCK)) continue;
                    level.setBlock(pos, state, 2);
                }
            }
        }
    }

    /**
     * Evita que la pierna quede flotando sobre terreno irregular: rellena
     * de piedra cualquier hueco de aire directamente debajo de la base de
     * la pierna, hasta encontrar suelo solido (o un limite de seguridad).
     */
    private void ancorarPiernaAlSuelo(WorldGenLevel level, BlockPos base, int x1, int z1, int x2, int z2) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int x = x1; x <= x2; x++) {
            for (int z = z1; z <= z2; z++) {
                pos.set(base.getX() + x, base.getY() - 1, base.getZ() + z);
                int safety = 0;
                while (level.getBlockState(pos).isAir() && safety < 24) {
                    level.setBlock(pos, BODY, 2);
                    pos.move(net.minecraft.core.Direction.DOWN);
                    safety++;
                }
            }
        }
    }
}
