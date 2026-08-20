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
    public static KeyMapping attack1;
    public static KeyMapping attack2;
    public static KeyMapping attack3;
    public static KeyMapping attack4;
    public static KeyMapping domainKey;



    public static void init(){
        KeyMapping.Category CATEGORY = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"custom_mod_controls"));
        laserKey = KeyBindingHelper.registerKeyBinding(
                new KeyMapping(
                        "laser",InputConstants.Type.MOUSE,
                        InputConstants.MOUSE_BUTTON_RIGHT,CATEGORY
                )
        );

        attack1 = KeyBindingHelper.registerKeyBinding(
                new KeyMapping(
                        "attack.jjk.1", InputConstants.KEY_Y,CATEGORY
                )
        );
        attack2 = KeyBindingHelper.registerKeyBinding(
                new KeyMapping(
                        "attack.jjk.2", InputConstants.KEY_U,CATEGORY
                )
        );
        attack3 = KeyBindingHelper.registerKeyBinding(
                new KeyMapping(
                        "attack.jjk.3", InputConstants.KEY_I,CATEGORY
                )
        );
        attack4 = KeyBindingHelper.registerKeyBinding(
                new KeyMapping(
                        "attack.jjk.4", InputConstants.KEY_O,CATEGORY
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
