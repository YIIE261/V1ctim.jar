package com.v1ctim.mod.event;

import com.v1ctim.mod.V1ctimMod;
import com.v1ctim.mod.network.TriggerFakeSystemErrorPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;
import java.util.Random;

@EventBusSubscriber(modid = V1ctimMod.MOD_ID)
public class RandomHorrorEventHandler {

    private static final Random RANDOM = new Random();

    private static final long GRACE_PERIOD_TICKS = 7L * 60L * 20L;
    private static final double EVENT_CHANCE_PER_TICK = 1.0 / 12000.0;

    private static final List<String> CREEPY_MESSAGES = List.of(
            "Help Me",
            "I can hear you breath",
            "All this was your fault",
            "You condemned us all",
            "Come a little more"
    );

    private static final char[] MORSE_SYMBOLS = {'.', '-'};

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        for (ServerLevel serverLevel : event.getServer().getAllLevels()) {
            if (serverLevel.getGameTime() < GRACE_PERIOD_TICKS) continue;

            for (ServerPlayer player : serverLevel.players()) {
                if (RANDOM.nextDouble() > EVENT_CHANCE_PER_TICK) continue;
                triggerRandomChatEvent(player);
            }
        }
    }

    private static void triggerRandomChatEvent(ServerPlayer player) {
        int roll = RANDOM.nextInt(6);

        if (roll == 1) {
            PacketDistributor.sendToPlayer(player, new TriggerFakeSystemErrorPacket());
            return;
        }

        String text = roll == 0 ? generateRandomMorseGibberish() : randomCreepyMessage();

        Component message = Component.literal(text)
                .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC);

        player.sendSystemMessage(message);
    }

    private static String randomCreepyMessage() {
        return CREEPY_MESSAGES.get(RANDOM.nextInt(CREEPY_MESSAGES.size()));
    }

    private static String generateRandomMorseGibberish() {
        int groups = 3 + RANDOM.nextInt(4);
        StringBuilder builder = new StringBuilder();

        for (int g = 0; g < groups; g++) {
            int symbols = 2 + RANDOM.nextInt(4);
            for (int s = 0; s < symbols; s++) {
                builder.append(MORSE_SYMBOLS[RANDOM.nextInt(MORSE_SYMBOLS.length)]);
            }
            if (g < groups - 1) builder.append("  ");
        }

        return builder.toString();
    }
}
