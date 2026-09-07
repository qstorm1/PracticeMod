package com.qstorm;

import com.qstorm.key.InitializeBindings;
import com.qstorm.packets.Packet;
import com.qstorm.powers.ComboClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import static com.qstorm.powers.cursedtechnique.JJKClientRender.initJJKClientRender;

public class PracticeModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        //initialize all keyboard stuff
        InitializeBindings.init();
        PayloadTypeRegistry.playS2C().register(Packet.LimitlessInit.TYPE, Packet.LimitlessInit.CODEC);
        initJJKClientRender();

        //when we recieve a thing saying to make this player a sorceror, render client
        ClientPlayNetworking.registerGlobalReceiver(Packet.LimitlessInit.TYPE,(packet, context)->{
            ComboClient.addComboRendererToClient();
        });

        ClientPlayNetworking.registerGlobalReceiver(Packet.SendClientMessage.TYPE,(packet,context)->{
            context.player().displayClientMessage(Component.literal(packet.message()),false);
        });
        //HudElementRegistry.attachElementBefore(VanillaHudElements.CROSSHAIR, Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"before-thing"),Combo::render);
    }

}
