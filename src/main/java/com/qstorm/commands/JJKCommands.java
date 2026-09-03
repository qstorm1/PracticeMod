package com.qstorm.commands;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.qstorm.powers.PlayerInfo;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.commands.DamageCommand;
import net.minecraft.server.commands.KillCommand;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.damagesource.DamageSource;

public class JJKCommands {
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

    public static void init(){

//        dispatcher.register(
//                (Commands.literal("damage").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
//                        .then(Commands.argument("target", EntityArgument.entity())
//                                .then((Commands.argument("amount", FloatArgumentType.floatArg(0.0f))
//                                    .executes(commandContext -> DamageCommand.damage((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntity(commandContext, "target"), FloatArgumentType.getFloat(commandContext, "amount"), ((CommandSourceStack)commandContext.getSource()).getLevel().damageSources().generic())))
//                                    .then(((Commands.argument("damageType", ResourceArgument.resource(context, Registries.DAMAGE_TYPE)).executes(commandContext -> DamageCommand.damage((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntity(commandContext, "target"), FloatArgumentType.getFloat(commandContext, "amount"), new DamageSource(ResourceArgument.getResource(commandContext, "damageType", Registries.DAMAGE_TYPE)))))
//                                            .then(Commands.literal("at")
//                                                    .then(Commands.argument("location", Vec3Argument.vec3()).executes(commandContext -> DamageCommand.damage((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntity(commandContext, "target"), FloatArgumentType.getFloat(commandContext, "amount"), new DamageSource(ResourceArgument.getResource(commandContext, "damageType", Registries.DAMAGE_TYPE), Vec3Argument.getVec3(commandContext, "location")))))))
//                                            .then(Commands.literal("by")
//                                                    .then((Commands.argument("entity", EntityArgument.entity()).executes(commandContext -> DamageCommand.damage((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntity(commandContext, "target"), FloatArgumentType.getFloat(commandContext, "amount"), new DamageSource(ResourceArgument.getResource(commandContext, "damageType", Registries.DAMAGE_TYPE), EntityArgument.getEntity(commandContext, "entity")))))
//                                                            .then(Commands.literal("from")
//                                                                    .then(Commands.argument("cause", EntityArgument.entity()).executes(commandContext -> DamageCommand.damage((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntity(commandContext, "target"), FloatArgumentType.getFloat(commandContext, "amount"), new DamageSource(ResourceArgument.getResource(commandContext, "damageType", Registries.DAMAGE_TYPE), EntityArgument.getEntity(commandContext, "entity"), EntityArgument.getEntity(commandContext, "cause")))
//                                                                    )))
//                                                    )))
//                            )
//                    )
//            );


        //register the /JJKInitPlayerInfo command
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
                    dispatcher.register(
                            (Commands.literal("JJKInitPlayerInfo").requires(commandSourceStack -> commandSourceStack.permissions().hasPermission(Permissions.COMMANDS_ADMIN)))
                                    .executes(JJKCommands::regenPlayerInfo)
                                    .then(Commands.argument("born_luck", IntegerArgumentType.integer(0))
                                            .then(Commands.argument("increased_luck", IntegerArgumentType.integer(0))
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

    }
}
