package com.v1ctim.mod.event;

import com.v1ctim.mod.V1ctimMod;
import com.v1ctim.mod.registry.ModBlocks;
import com.v1ctim.mod.registry.ModItems;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@EventBusSubscriber(modid = V1ctimMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModCreativeTabHandler {

    @SubscribeEvent
    public static void onBuildContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() != CreativeModeTabs.INGREDIENTS) return;

        event.accept(ModItems.CORRUPTED_SHARD.get());
        event.accept(ModItems.SIGNAL_SCANNER.get());
        event.accept(ModItems.BROKEN_DISK.get());
        event.accept(ModItems.STABILIZER_CORE.get());
        event.accept(ModItems.BLOOD_WATER_BUCKET.get());
        event.accept(ModItems.WATCHING_EYES_BLOCK_ITEM.get());
        event.accept(ModItems.MISSING_FILE_BLOCK_ITEM.get());
        event.accept(ModItems.TEETH_LOG_ITEM.get());

        event.accept(ModBlocks.VOID_TEXTURE_BLOCK.get());
        event.accept(ModBlocks.GLITCHED_STONE.get());
        event.accept(ModBlocks.CORRUPTED_STONE.get());
        event.accept(ModBlocks.CORRUPTED_DIRT.get());
        event.accept(ModBlocks.ERROR_STATIC_PANEL.get());
        event.accept(ModBlocks.UNSTABLE_FLOOR.get());
        event.accept(ModBlocks.WINGED_EYE_BLOCK.get());
    }
}
