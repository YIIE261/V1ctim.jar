package com.v1ctim.mod.network;

import com.v1ctim.mod.V1ctimMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * Paquete vacio: servidor -> cliente. Le dice al cliente que muestre una
 * ventana de error falsa con apariencia de sistema operativo (ver
 * client/FakeSystemErrorHandler.java). Al igual que el paquete de crash,
 * no lleva datos: el cliente elige el mensaje al azar por su cuenta.
 *
 * Ver el comentario en TriggerFakeCrashPacket.java sobre por que esto ahora
 * es un CustomPacketPayload en vez de un paquete de SimpleChannel.
 */
public record TriggerFakeSystemErrorPacket() implements CustomPacketPayload {

    public static final Type<TriggerFakeSystemErrorPacket> TYPE =
            new Type<>(V1ctimMod.rl("trigger_fake_system_error"));

    public static final StreamCodec<FriendlyByteBuf, TriggerFakeSystemErrorPacket> STREAM_CODEC =
            StreamCodec.of(
                    (buffer, packet) -> {
                        // Sin datos que codificar.
                    },
                    buffer -> new TriggerFakeSystemErrorPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
