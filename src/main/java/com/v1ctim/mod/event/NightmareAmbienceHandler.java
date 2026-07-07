package com.v1ctim.mod.event;

import com.v1ctim.mod.V1ctimMod;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@EventBusSubscriber(modid = V1ctimMod.MOD_ID)
public class NightmareAmbienceHandler {

    private static final Random RANDOM = new Random();
    private static final double EVENT_CHANCE_PER_TICK = 1.0 / 400.0;
    private static final List<ScheduledSound> PENDING = new ArrayList<>();

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        processPending();

        ServerLevel nightmare = event.getServer().getLevel(NightmareDimensionHandler.NIGHTMARE_LEVEL);
        if (nightmare == null) return;

        for (ServerPlayer player : nightmare.players()) {
            if (RANDOM.nextDouble() > EVENT_CHANCE_PER_TICK) continue;
            triggerRandomEvent(nightmare, player);
        }
    }

    private static void processPending() {
        Iterator<ScheduledSound> it = PENDING.iterator();
        while (it.hasNext()) {
            ScheduledSound sound = it.next();
            sound.delay--;
            if (sound.delay <= 0) {
                sound.level.playSound(null, sound.pos, sound.event, SoundSource.AMBIENT,
                        sound.volume, sound.pitch);
                it.remove();
            }
        }
    }

    private static void triggerRandomEvent(ServerLevel level, ServerPlayer player) {
        int roll = RANDOM.nextInt(3);
        BlockPos near = randomNearbyPos(player);

        switch (roll) {
            case 0 -> scheduleBlockBreakBurst(level, near);
            case 1 -> scheduleFootsteps(level, near, player);
            case 2 -> scheduleDoorOpen(level, near);
        }
    }

    private static void scheduleBlockBreakBurst(ServerLevel level, BlockPos pos) {
        int count = 4 + RANDOM.nextInt(4);
        int delay = 1;
        for (int i = 0; i < count; i++) {
            PENDING.add(new ScheduledSound(level, pos, SoundEvents.STONE_BREAK,
                    1.0f, 0.7f + RANDOM.nextFloat() * 0.6f, delay));
            delay += 1 + RANDOM.nextInt(2);
        }
    }

    private static void scheduleFootsteps(ServerLevel level, BlockPos pos, ServerPlayer player) {
        int steps = 5 + RANDOM.nextInt(5);
        int delay = 1;
        BlockPos.MutableBlockPos walking = pos.mutable();
        int dx = RANDOM.nextBoolean() ? 1 : -1;
        int dz = RANDOM.nextBoolean() ? 1 : -1;

        for (int i = 0; i < steps; i++) {
            PENDING.add(new ScheduledSound(level, walking.immutable(), SoundEvents.STONE_STEP,
                    0.8f, 0.8f + RANDOM.nextFloat() * 0.3f, delay));
            delay += 5 + RANDOM.nextInt(3);
            walking.move(dx, 0, dz);
        }
    }

    private static void scheduleDoorOpen(ServerLevel level, BlockPos pos) {
        PENDING.add(new ScheduledSound(level, pos, SoundEvents.IRON_DOOR_OPEN,
                0.9f, 0.9f + RANDOM.nextFloat() * 0.2f, 1));
    }

    private static BlockPos randomNearbyPos(ServerPlayer player) {
        return player.blockPosition().offset(
                RANDOM.nextInt(16) - 8,
                RANDOM.nextInt(4) - 2,
                RANDOM.nextInt(16) - 8);
    }

    private static class ScheduledSound {
        final ServerLevel level;
        final BlockPos pos;
        final net.minecraft.sounds.SoundEvent event;
        final float volume;
        final float pitch;
        int delay;

        ScheduledSound(ServerLevel level, BlockPos pos, net.minecraft.sounds.SoundEvent event,
                       float volume, float pitch, int delay) {
            this.level  = level;
            this.pos    = pos;
            this.event  = event;
            this.volume = volume;
            this.pitch  = pitch;
            this.delay  = delay;
        }
    }
}
