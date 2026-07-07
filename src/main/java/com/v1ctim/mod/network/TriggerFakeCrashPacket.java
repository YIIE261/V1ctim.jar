package com.v1ctim.mod.network;

import com.v1ctim.mod.V1ctimMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * Paquete vacio: servidor -> cliente. Le dice al cliente del jugador que
 * acaba de morir a manos de Victim2 que dispare el "crash" falso (ver
 * client/FakeCrashHandler.java). No lleva datos, es solo una senal.
 *
 * Desde Forge 1.20.2+/1.21, el viejo sistema SimpleChannel + registerMessage
 * fue removido: cada paquete ahora es un CustomPacketPayload (aca, un record
 * sin campos) con un Type (identificador) y un StreamCodec (como leer/escribir
 * sus datos en el buffer). Ver network/ModNetworking.java para el registro.
 */
public record TriggerFakeCrashPacket() implements CustomPacketPayload {

    public static final Type<TriggerFakeCrashPacket> TYPE =
            new Type<>(V1ctimMod.rl("trigger_fake_crash"));

    public static final StreamCodec<FriendlyByteBuf, TriggerFakeCrashPacket> STREAM_CODEC =
            StreamCodec.of(
                    (buffer, packet) -> {
                        // Sin datos que codificar.
                    },
                    buffer -> new TriggerFakeCrashPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
