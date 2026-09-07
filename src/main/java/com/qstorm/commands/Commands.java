package com.qstorm.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.qstorm.packets.Packet;
import com.qstorm.powers.PlayerInfo;
import com.qstorm.powers.cursedtechnique.Sorcery;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.OutgoingChatMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;

public class Commands {
    public static int regenPlayerInfo2V(int v1,int v2,CommandContext<CommandSourceStack> context){
        context.getSource().sendSuccess(()-> Component.literal("RenderedPlayerInfo"),false);
        if(context.getSource().isPlayer()){
            PlayerInfo.regenPlayerInfo(context.getSource().getPlayer().getUUID(),v1,v2);
        }
        return 1;
    }
    public static int regenPlayerInfo(CommandContext<CommandSourceStack> context){
        context.getSource().sendSuccess(()-> Component.literal("RenderedPlayerInfo"),false);
        if(context.getSource().isPlayer()){
            PlayerInfo.regenPlayerInfo(context.getSource().getPlayer().getUUID());
        }
        return 1;
    }

    public static int showStats(CommandContext<CommandSourceStack> context){
        ServerPlayNetworking.send(context.getSource().getPlayer(),
                new Packet.SendClientMessage(PlayerInfo.playerInfoHashMap.get(context.getSource().getPlayer().getUUID()).toString()));
        context.getSource().sendSuccess(()->Component.literal("stats"),false);
        return 1;
    }

    public static void init(){
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                        (net.minecraft.commands.Commands.literal("JJKInitPlayerInfo").requires(commandSourceStack -> commandSourceStack.permissions().hasPermission(Permissions.COMMANDS_ADMIN)))
                                .executes(Commands::regenPlayerInfo)
                                .then(net.minecraft.commands.Commands.argument("born_luck", IntegerArgumentType.integer(0))
                                        .then(net.minecraft.commands.Commands.argument("increased_luck", IntegerArgumentType.integer(0))
                                                .executes(
                                                        (context)->
                                                                regenPlayerInfo2V(
                                                                        IntegerArgumentType.getInteger(context,"born_luck"),
                                                                        IntegerArgumentType.getInteger(context,"increased_luck")
                                                                        ,context)
                                                )
                                        )
                                )
                );
            }
        );

        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> {
            dispatcher.register(net.minecraft.commands.Commands.literal("stats")
                    .executes(Commands::showStats)

            );
        }));

    }
}
