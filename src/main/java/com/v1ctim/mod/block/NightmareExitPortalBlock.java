package com.v1ctim.mod.block;

import com.v1ctim.mod.event.NightmareDimensionHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;
import java.util.Random;
import java.util.WeakHashMap;

/**
 * Bloque de "salida bugeada" de la dimension Nightmare. Pensado como un
 * primo con textura glitcheada del portal del End, pero simplificado: en vez
 * de replicar el render especial (translucido, sin textura real) del End
 * Portal vanilla -cosa dificil de garantizar sin poder compilar/probar-, es
 * un bloque normal con textura propia que, al pisarlo, teletransporta al
 * jugador a coordenadas aleatorias del Overworld (ver
 * event/NightmareExitPortalHandler.java, que decide cuando y donde aparece).
 */
public class NightmareExitPortalBlock extends Block {

    private static final Random RANDOM = new Random();
    private static final int RANDOM_RANGE = 10000;

    // Evita re-teletransportar al mismo jugador varias veces en el mismo
    // "pisotón" si entityInside se dispara mas de una vez en pocos ticks.
    private static final Map<Entity, Long> LAST_TELEPORT_TICK = new WeakHashMap<>();
    private static final long COOLDOWN_TICKS = 40L;

    public NightmareExitPortalBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide()) return;
        if (!(level instanceof ServerLevel serverLevel)) return;
        if (!(entity instanceof ServerPlayer player)) return;

        long now = serverLevel.getGameTime();
        Long last = LAST_TELEPORT_TICK.get(entity);
        if (last != null && now - last < COOLDOWN_TICKS) return;
        LAST_TELEPORT_TICK.put(entity, now);

        MinecraftServer server = serverLevel.getServer();
        ServerLevel overworld = server.overworld();
        if (overworld == null) return;

        int targetX = RANDOM.nextInt(RANDOM_RANGE * 2) - RANDOM_RANGE;
        int targetZ = RANDOM.nextInt(RANDOM_RANGE * 2) - RANDOM_RANGE;
        int targetY = overworld.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING,
                targetX, targetZ) + 1;

        player.teleportTo(overworld, targetX + 0.5, targetY, targetZ + 0.5,
                player.getYRot(), player.getXRot());
        player.sendSystemMessage(net.minecraft.network.chat.Component.literal("La realidad te absorbe de vuelta...")
                .withStyle(net.minecraft.ChatFormatting.DARK_PURPLE, net.minecraft.ChatFormatting.ITALIC));
    }
}
