package com.v1ctim.mod.client;

import com.v1ctim.mod.V1ctimMod;
import com.v1ctim.mod.registry.ModFluids;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;

@EventBusSubscriber(modid = V1ctimMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientFluids {

    private static final ResourceLocation STILL_TEXTURE   = V1ctimMod.rl("block/blood_water_still");
    private static final ResourceLocation FLOWING_TEXTURE = V1ctimMod.rl("block/blood_water_still");

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerFluidType(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return STILL_TEXTURE;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return FLOWING_TEXTURE;
            }

            @Override
            public int getTintColor() {
                return 0xB0400000;
            }
        }, ModFluids.BLOOD_WATER_TYPE.get());
    }
}
