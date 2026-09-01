package com.qstorm.key;

import com.mojang.blaze3d.platform.InputConstants;
import com.qstorm.PracticeMod;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.UUID;


//issues:
//1, disconnecting stuff isn't handled
//2, key's are being pressed but multiple times
public class InitializeBindings {
    public static KeyMapping laserKey;
    public static KeyMapping attack1;
    public static KeyMapping attack2;
    public static KeyMapping attack3;
    public static KeyMapping attack4;
    public static KeyMapping domainKey;
    public static KeyMapping innateTechnique;
    public static KeyMapping activateReversed;
    public static KeyMapping energyKey;
    public static KeyMapping.Category CATEGORY = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"custom_mod_controls"));

    //a hash map, each map represents a player and weather their attack key was down
    static HashMap<UUID,Boolean> attack1WasDown=new HashMap<>();
    static HashMap<UUID,Boolean> attack2WasDown=new HashMap<>();
    static HashMap<UUID,Boolean> attack3WasDown=new HashMap<>();
    static HashMap<UUID,Boolean> attack4WasDown=new HashMap<>();
    static HashMap<UUID,Boolean> domainKeyWasDown=new HashMap<>();
    static HashMap<UUID,Boolean> activateReversedWasDown=new HashMap<>();
    static HashMap<UUID,Boolean> innateTechniqueWasDown=new HashMap<>();

    public static void init(){
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

        innateTechnique = KeyBindingHelper.registerKeyBinding(
                new KeyMapping(
                        "innate",InputConstants.KEY_J,CATEGORY
                )
        );

        activateReversed = KeyBindingHelper.registerKeyBinding(
                new KeyMapping(
                        "reversed",InputConstants.KEY_LALT,CATEGORY
                )
        );

        energyKey = KeyBindingHelper.registerKeyBinding(
                new KeyMapping(
                        "energy",InputConstants.KEY_Y,CATEGORY
                )
        );







        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(laserKey.isDown()){
                if(client.player!=null) {
                    ClientPlayNetworking.send(new HandleKeybinds.LaserKeyServer());
                }
            }

        });




        //combo key handler
        //updates maps that require players
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(client.player==null) return;


            //if the player wasn't initialized
            if(!attack1WasDown.containsKey(client.player.getUUID())){
                PracticeMod.LOGGER.info("error player was not added to set");
                return;
            }

            //only happens once and then turned off
            //while(attack 1 is down)
            // if(before = false)
            // set before to true
            //if attack1 was down previously, don't do this
            //if true don't do this


            //if attack is sent, refresh render data
            if(attack1.isDown()){
                if(!InitializeBindings.attack1WasDown.get(client.player.getUUID())) {
                    ClientPlayNetworking.send(new HandleKeybinds.AttackJJK1());
                    InitializeBindings.attack1WasDown.put(client.player.getUUID(), true);
                }
            }
            else InitializeBindings.attack1WasDown.put(client.player.getUUID(),false);



            if(attack2.isDown()){
                if(!InitializeBindings.attack2WasDown.get(client.player.getUUID())) {
                    ClientPlayNetworking.send(new HandleKeybinds.AttackJJK2());
                    InitializeBindings.attack2WasDown.put(client.player.getUUID(), true);

                }
            }
            else InitializeBindings.attack2WasDown.put(client.player.getUUID(),false);



            if(attack3.isDown()){
                if(!InitializeBindings.attack3WasDown.get(client.player.getUUID())) {
                    ClientPlayNetworking.send(new HandleKeybinds.AttackJJK3());
                    InitializeBindings.attack3WasDown.put(client.player.getUUID(), true);

                }
            }
            else InitializeBindings.attack3WasDown.put(client.player.getUUID(),false);

            if(attack4.isDown()){
                if(!InitializeBindings.attack4WasDown.get(client.player.getUUID())) {
                    ClientPlayNetworking.send(new HandleKeybinds.AttackJJK4());
                    InitializeBindings.attack4WasDown.put(client.player.getUUID(), true);

                }
            }
            else InitializeBindings.attack4WasDown.put(client.player.getUUID(),false);

            if(domainKey.isDown()){
                if(!InitializeBindings.domainKeyWasDown.get(client.player.getUUID())) {
                    ClientPlayNetworking.send(new HandleKeybinds.Domain());
                    InitializeBindings.domainKeyWasDown.put(client.player.getUUID(), true);

                }
            }
            else InitializeBindings.domainKeyWasDown.put(client.player.getUUID(),false);

            if(innateTechnique.isDown()){
                if(!InitializeBindings.innateTechniqueWasDown.get(client.player.getUUID())) {
                    ClientPlayNetworking.send(new HandleKeybinds.EnableInnateTechnique());
                    InitializeBindings.innateTechniqueWasDown.put(client.player.getUUID(), true);

                }
            }
            else InitializeBindings.innateTechniqueWasDown.put(client.player.getUUID(),false);

            if(activateReversed.isDown()){
                if(!InitializeBindings.activateReversedWasDown.get(client.player.getUUID())) {
                    ClientPlayNetworking.send(new HandleKeybinds.EnableInnateTechnique());
                    InitializeBindings.activateReversedWasDown.put(client.player.getUUID(), true);

                }
            }
            else InitializeBindings.innateTechniqueWasDown.put(client.player.getUUID(),false);

            if(energyKey.isDown()){
                //if()
            }

        });




    }

    public static void onPlayerJoin(Player player){
        PracticeMod.LOGGER.info("player joined");
        attack1WasDown.put(player.getUUID(), false);
        attack2WasDown.put(player.getUUID(), false);
        attack3WasDown.put(player.getUUID(), false);
        attack4WasDown.put(player.getUUID(), false);
    }


    public static void onPlayerLeave(Player player){
        PracticeMod.LOGGER.info("player left");
        attack1WasDown.remove(player.getUUID());
        attack2WasDown.remove(player.getUUID());
        attack3WasDown.remove(player.getUUID());
        attack4WasDown.remove(player.getUUID());
    }


}
