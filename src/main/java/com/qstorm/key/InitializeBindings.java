package com.qstorm.key;

import com.mojang.blaze3d.platform.InputConstants;
import com.qstorm.PracticeMod;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.impl.attachment.AttachmentRegistryImpl;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;

public class InitializeBindings {
    public static KeyMapping laserKey;
    public static KeyMapping attack1;
    public static KeyMapping attack2;
    public static KeyMapping attack3;
    public static KeyMapping attack4;
    public static KeyMapping domainKey;

    static HashMap<Player,Boolean> attack1WasDown=new HashMap<>();
    static HashMap<Player,Boolean> attack2WasDown=new HashMap<>();
    static HashMap<Player,Boolean> attack3WasDown=new HashMap<>();
    static HashMap<Player,Boolean> attack4WasDown=new HashMap<>();

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

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(attack1.isDown()){
                if(client.player!=null) {
                    ClientPlayNetworking.send(new HandleKeybinds.AttackJJK1());
                }
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(attack2.isDown()){
                if(client.player!=null) {
                    ClientPlayNetworking.send(new HandleKeybinds.AttackJJK2());
                }
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(attack3.isDown()){
                if(client.player!=null) {
                    ClientPlayNetworking.send(new HandleKeybinds.AttackJJK3());
                }
            }
        });


        //if down but not letGo
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            Boolean attack4WasDown = InitializeBindings.attack4WasDown.get(client.player);
            if(attack4.isDown()&& !attack4WasDown){
                if(client.player!=null) {
                    ClientPlayNetworking.send(new HandleKeybinds.AttackJJK4());
                    attack4WasDown=true;
                }
            }
            else{
                attack4WasDown=false;
            }
        });


    }


}
