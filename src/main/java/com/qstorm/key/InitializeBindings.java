package com.qstorm.key;

import com.mojang.blaze3d.platform.InputConstants;
import com.qstorm.PracticeMod;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public class InitializeBindings {
    public static KeyMapping laserKey;
    public static KeyMapping domainKey;

    private static boolean prevState = false;
    public static void init(){
        KeyMapping.Category CATEGORY = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"custom_mod_controls"));
        laserKey = KeyBindingHelper.registerKeyBinding(
                new KeyMapping(
                        "laser",InputConstants.Type.MOUSE,
                        InputConstants.MOUSE_BUTTON_RIGHT,CATEGORY
                )
        );


        domainKey = KeyBindingHelper.registerKeyBinding(
                new KeyMapping(
                        "domain", InputConstants.KEY_SEMICOLON,CATEGORY
                )
        );





        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(laserKey.isDown()){
                if(client.player!=null) {
                    ClientPlayNetworking.send(new HandleKeybinds.LaserKeyServer());
                }
            }
        });
    }
}
