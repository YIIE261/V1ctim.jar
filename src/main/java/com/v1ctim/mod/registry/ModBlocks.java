package com.v1ctim.mod.registry;

import com.v1ctim.mod.V1ctimMod;
import com.v1ctim.mod.block.NightmareExitPortalBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(V1ctimMod.MOD_ID);

    public static final DeferredBlock<Block> VOID_TEXTURE_BLOCK = BLOCKS.register("void_texture_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_MAGENTA)
                    .strength(2.0f)
                    .sound(SoundType.STONE)));

    public static final DeferredBlock<Block> GLITCHED_STONE = BLOCKS.register("glitched_stone",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(1.5f, 6.0f)
                    .sound(SoundType.STONE)));

    public static final DeferredBlock<Block> CORRUPTED_STONE = BLOCKS.register("corrupted_stone",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .strength(1.5f, 6.0f)
                    .sound(SoundType.STONE)));

    public static final DeferredBlock<Block> CORRUPTED_DIRT = BLOCKS.register("corrupted_dirt",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .strength(0.5f)
                    .sound(SoundType.GRAVEL)));

    public static final DeferredBlock<Block> CORRUPTION_EDGE_MARKER = BLOCKS.register("corruption_edge_marker",
            () -> new Block(BlockBehaviour.Properties.of()
                    .noCollission()
                    .noOcclusion()
                    .strength(-1.0f, 3600000.0f)
                    .noLootTable()));

    public static final DeferredBlock<Block> ERROR_STATIC_PANEL = BLOCKS.register("error_static_panel",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .strength(1.0f)
                    .sound(SoundType.METAL)
                    .lightLevel(state -> 3)));

    public static final DeferredBlock<Block> UNSTABLE_FLOOR = BLOCKS.register("unstable_floor",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY)
                    .strength(1.0f)
                    .sound(SoundType.WOOD)));

    public static final DeferredBlock<Block> WATCHING_EYES_BLOCK = BLOCKS.register("watching_eyes_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .strength(2.0f)
                    .sound(SoundType.STONE)
                    .noLootTable()
                    .pushReaction(PushReaction.BLOCK)));

    public static final DeferredBlock<Block> MISSING_FILE_BLOCK = BLOCKS.register("missing_file_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_MAGENTA)
                    .strength(1.5f)
                    .sound(SoundType.STONE)));

    public static final DeferredBlock<Block> WINGED_EYE_BLOCK = BLOCKS.register("winged_eye_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.SNOW)
                    .strength(2.0f)
                    .sound(SoundType.STONE)));

    public static final DeferredBlock<RotatedPillarBlock> TEETH_LOG = BLOCKS.register("teeth_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY)
                    .strength(2.0f)
                    .sound(SoundType.WOOD)));

    public static final DeferredBlock<NightmareExitPortalBlock> NIGHTMARE_EXIT_PORTAL =
            BLOCKS.register("nightmare_exit_portal",
                    () -> new NightmareExitPortalBlock(BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .strength(-1.0f, 3600000.0f)
                            .noOcclusion()
                            .lightLevel(state -> 7)
                            .pushReaction(PushReaction.BLOCK)));

    public static final DeferredBlock<LiquidBlock> BLOOD_WATER_BLOCK = BLOCKS.register("blood_water",
            () -> new LiquidBlock(ModFluids.BLOOD_WATER_SOURCE, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED)
                    .noCollission()
                    .strength(100.0f)
                    .noLootTable()
                    .pushReaction(PushReaction.DESTROY)));
}
