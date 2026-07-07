package com.v1ctim.mod.client;

import com.v1ctim.mod.V1ctimMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

@EventBusSubscriber(modid = V1ctimMod.MOD_ID, value = Dist.CLIENT)
public class ClientSetupEvents {

    private static boolean disclaimerShown = false;

    @SubscribeEvent
    public static void onTitleScreenOpen(ScreenEvent.Opening event) {
        if (disclaimerShown) return;
        if (!(event.getNewScreen() instanceof TitleScreen)) return;

        disclaimerShown = true;
        Minecraft.getInstance().execute(() ->
                Minecraft.getInstance().setScreen(new DisclaimerScreen()));
    }
}
