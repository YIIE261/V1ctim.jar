package com.v1ctim.mod;

import com.v1ctim.mod.registry.ModBlocks;
import com.v1ctim.mod.registry.ModEntities;
import com.v1ctim.mod.registry.ModFeatures;
import com.v1ctim.mod.registry.ModFluids;
import com.v1ctim.mod.registry.ModItems;
import com.v1ctim.mod.registry.ModSounds;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(V1ctimMod.MOD_ID)
public class V1ctimMod {

    public static final String MOD_ID = "v1ctim";

    public V1ctimMod(IEventBus modEventBus) {
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModEntities.ENTITY_TYPES.register(modEventBus);
        ModSounds.SOUND_EVENTS.register(modEventBus);
        ModFeatures.FEATURES.register(modEventBus);
        ModFluids.FLUID_TYPES.register(modEventBus);
        ModFluids.FLUIDS.register(modEventBus);

        modEventBus.addListener(ModEntities::registerAttributes);
    }

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
