package com.v1ctim.mod.registry;

import com.v1ctim.mod.V1ctimMod;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(V1ctimMod.MOD_ID);

    public static final DeferredItem<Item> CORRUPTED_SHARD = ITEMS.register("corrupted_shard",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SIGNAL_SCANNER = ITEMS.register("signal_scanner",
            () -> new Item(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> BROKEN_DISK = ITEMS.register("broken_disk",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> STABILIZER_CORE = ITEMS.register("stabilizer_core",
            () -> new Item(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<BucketItem> BLOOD_WATER_BUCKET = ITEMS.register("blood_water_bucket",
            () -> new BucketItem(ModFluids.BLOOD_WATER_SOURCE, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<BlockItem> WATCHING_EYES_BLOCK_ITEM = ITEMS.register("watching_eyes_block",
            () -> new BlockItem(ModBlocks.WATCHING_EYES_BLOCK.get(), new Item.Properties()));

    public static final DeferredItem<BlockItem> MISSING_FILE_BLOCK_ITEM = ITEMS.register("missing_file_block",
            () -> new BlockItem(ModBlocks.MISSING_FILE_BLOCK.get(), new Item.Properties()));

    public static final DeferredItem<BlockItem> TEETH_LOG_ITEM = ITEMS.register("teeth_log",
            () -> new BlockItem(ModBlocks.TEETH_LOG.get(), new Item.Properties()));
}
