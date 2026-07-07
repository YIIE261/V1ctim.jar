package com.v1ctim.mod.event;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.v1ctim.mod.V1ctimMod;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = V1ctimMod.MOD_ID)
public class NightmareCommands {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("v1ctim")
                .then(Commands.literal("nightmare")
                        .requires(source -> source.hasPermission(2))
                        .executes(NightmareCommands::teleportToggle)));
    }

    private static int teleportToggle(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        if (!(source.getEntity() instanceof ServerPlayer player)) {
            source.sendFailure(Component.literal("Este comando solo puede ejecutarlo un jugador."));
            return 0;
        }

        if (player.level().dimension().equals(NightmareDimensionHandler.NIGHTMARE_LEVEL)) {
            // Salir de Nightmare -> overworld
            ServerLevel overworld = player.getServer().overworld();
            player.teleportTo(overworld,
                    player.getX(), 64, player.getZ(),
                    player.getYRot(), player.getXRot());
            player.sendSystemMessage(Component.literal("Escapaste de la pesadilla."));
        } else {
            // Entrar a Nightmare
            ServerLevel nightmare = player.getServer().getLevel(NightmareDimensionHandler.NIGHTMARE_LEVEL);
            if (nightmare == null) {
                source.sendFailure(Component.literal("La dimension Nightmare no existe."));
                return 0;
            }
            player.teleportTo(nightmare,
                    player.getX(), 64, player.getZ(),
                    player.getYRot(), player.getXRot());
            player.sendSystemMessage(Component.literal("Entraste a la pesadilla..."));
        }

        return 1;
    }
}
