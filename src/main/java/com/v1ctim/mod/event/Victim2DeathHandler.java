package com.v1ctim.mod.event;

import com.v1ctim.mod.V1ctimMod;
import com.v1ctim.mod.entity.Victim2Entity;
import com.v1ctim.mod.network.TriggerFakeCrashPacket;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = V1ctimMod.MOD_ID)
public class Victim2DeathHandler {

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!(event.getSource().getEntity() instanceof Victim2Entity)) return;

        PacketDistributor.sendToPlayer(player, new TriggerFakeCrashPacket());
    }
}
