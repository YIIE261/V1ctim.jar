package com.v1ctim.mod.event;

import com.v1ctim.mod.V1ctimMod;
import com.v1ctim.mod.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@EventBusSubscriber(modid = V1ctimMod.MOD_ID)
public class NightmareExitPortalHandler {

    private static final Random RANDOM = new Random();
    private static final int MIN_TICKS = 20 * 60 * 3;
    private static final int MAX_TICKS = 20 * 60 * 7;
    private static final Map<UUID, Integer> TICKS_IN_NIGHTMARE = new HashMap<>();
    private static final Map<UUID, Integer> NEXT_THRESHOLD     = new HashMap<>();

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        ServerLevel nightmare = event.getServer().getLevel(NightmareDimensionHandler.NIGHTMARE_LEVEL);
        if (nightmare == null) return;

        for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
            UUID id = player.getUUID();
            boolean inNightmare = player.level().dimension().equals(NightmareDimensionHandler.NIGHTMARE_LEVEL);

            if (!inNightmare) {
                TICKS_IN_NIGHTMARE.remove(id);
                NEXT_THRESHOLD.remove(id);
                continue;
            }

            int threshold = NEXT_THRESHOLD.computeIfAbsent(id, k -> randomThreshold());
            int ticks     = TICKS_IN_NIGHTMARE.merge(id, 1, Integer::sum);

            if (ticks >= threshold) {
                spawnExitPortalNear(nightmare, player);
                TICKS_IN_NIGHTMARE.put(id, 0);
                NEXT_THRESHOLD.put(id, randomThreshold());
            }
        }
    }

    private static int randomThreshold() {
        return MIN_TICKS + RANDOM.nextInt(MAX_TICKS - MIN_TICKS + 1);
    }

    private static void spawnExitPortalNear(ServerLevel level, ServerPlayer player) {
        int offsetX = (3 + RANDOM.nextInt(6)) * (RANDOM.nextBoolean() ? 1 : -1);
        int offsetZ = (3 + RANDOM.nextInt(6)) * (RANDOM.nextBoolean() ? 1 : -1);

        int x = player.getBlockX() + offsetX;
        int z = player.getBlockZ() + offsetZ;
        int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);

        BlockPos pos = new BlockPos(x, y, z);
        level.setBlock(pos, ModBlocks.NIGHTMARE_EXIT_PORTAL.get().defaultBlockState(), Block.UPDATE_ALL);

        player.sendSystemMessage(Component.literal("Algo se abrio cerca...")
                .withStyle(net.minecraft.ChatFormatting.DARK_PURPLE,
                           net.minecraft.ChatFormatting.ITALIC));
    }
}
