package com.qstorm.key;

import com.mojang.blaze3d.platform.InputConstants;
import com.qstorm.PracticeMod;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public class InitializeBindings {
    public static KeyMapping laser;
    public static KeyMapping domain;
    public static void init(){
        KeyMapping.Category CATEGORY = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"custom_mod_controls"));
        laser = KeyBindingHelper.registerKeyBinding(
                new KeyMapping(
                        "key.laser", InputConstants.MOUSE_BUTTON_LEFT,CATEGORY
                )
        );


        domain = KeyBindingHelper.registerKeyBinding(
                new KeyMapping(
                        "key.domain", InputConstants.KEY_SEMICOLON,CATEGORY
                )
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while(laser.consumeClick()){
                if(client.player!=null){
                    ClientPlayNetworking.send(() ->
                            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"pressed-laser")));
                }
            }
        });
    }
}
