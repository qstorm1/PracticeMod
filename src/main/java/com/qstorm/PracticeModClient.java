package com.qstorm;

import com.qstorm.key.InitializeBindings;
import com.qstorm.packets.Packet;
import com.qstorm.powers.Combo;
import com.qstorm.powers.ComboClient;
import com.qstorm.powers.Sorcery;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;

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
