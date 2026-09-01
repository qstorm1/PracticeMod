package com.qstorm;

import com.qstorm.key.InitializeBindings;
import com.qstorm.packets.Packet;
import com.qstorm.powers.ComboClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class PracticeModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        //initialize all keyboard stuff
        InitializeBindings.init();
        PayloadTypeRegistry.playS2C().register(Packet.limitlessInit.TYPE,Packet.limitlessInit.CODEC);


        //when we recieve a thing saying to make this player a sorceror, render client
        ClientPlayNetworking.registerGlobalReceiver(Packet.limitlessInit.TYPE,(packet,context)->{
            ComboClient.addComboRendererToClient();
        });
        //HudElementRegistry.attachElementBefore(VanillaHudElements.CROSSHAIR, Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"before-thing"),Combo::render);
    }

}
