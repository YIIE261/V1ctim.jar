package com.v1ctim.mod.network;

import com.v1ctim.mod.V1ctimMod;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

/**
 * Registro de paquetes de red del mod.
 * Los handlers de playToClient solo se ejecutan en el lado cliente,
 * por lo que es seguro llamar directamente codigo de cliente aqui.
 */
@EventBusSubscriber(modid = V1ctimMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModNetworking {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(V1ctimMod.MOD_ID).versioned("1");

        registrar.playToClient(
                TriggerFakeCrashPacket.TYPE,
                TriggerFakeCrashPacket.STREAM_CODEC,
                (packet, ctx) -> ctx.enqueueWork(
                        com.v1ctim.mod.client.FakeCrashHandler::triggerCrash));

        registrar.playToClient(
                TriggerFakeSystemErrorPacket.TYPE,
                TriggerFakeSystemErrorPacket.STREAM_CODEC,
                (packet, ctx) -> ctx.enqueueWork(
                        com.v1ctim.mod.client.FakeSystemErrorHandler::showRandomFakeError));
    }
}
