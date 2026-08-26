package com.qstorm.powers.cursedtechnique;

import com.qstorm.PracticeMod;
import com.qstorm.key.HandleKeybinds;
import com.qstorm.key.InitializeBindings;
import com.qstorm.powers.Combo;
import com.qstorm.powers.ComboKey;
import com.qstorm.powers.Sorcery;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

/**
 * A limitless instance is created for every single player with the tag of Limitless
 */
public class Limitless implements Sorcery {

    public static HashMap<UUID,Limitless> limitlessPlayers = new HashMap<>();
    public static String tag= "Limitless User";

    //I know i'm eventually gonna have to deal with player data and storing but for now i'll use these
    public final int cursedEnergy=999999;
    public final int maxCursedOutput=99;
    public final int cursedOutput=0;


//    public Limitless(Player player){
//        if(!player.getTags().contains(tag)) {
//            for(Limitless ls: limitlessPlayers) {
//                if(ls.player.equals(player)) {
//                    PracticeMod.LOGGER.info("Player was cast to limitless a second time");
//                    return;
//                }
//            }
//            this.player = player;
//        }
//
//        PracticeMod.LOGGER.info("Player was cast to limitless a second time");
//
//    }

    //generate a limtiless technicue
    private Limitless(){

    }


    public Combo redCombo;
    public Combo blueCombo;
    public Combo purpleCombo;


    /**
     * MUST HAPPEN ON BOTH SERVER AND CLIENT SIDE
     * @param player the Limitless player that has been initialized
     */
    public static void initLimitlessPlayer(Player player){
        PracticeMod.LOGGER.info("new instance created");
        limitlessPlayers.put(player.getUUID(),new Limitless());

        limitlessPlayers.get(player.getUUID()).redCombo = Combo.build(player).addKey1(100).addKey3(100);

    }





    //TODO: maybe replace with runnable classes so you can implement methods (like a parent method with combo)
    //two times as much cursed energy as blue
    public void triggerRed(){

    }

    public void triggerBlue(){

    }

    public void triggerPurple(){

    }

    //requires constant cursed energy output
    public void infinityOn(){

    }

    public void infinityOff(){

    }


    public void domain(){

        // Animation is played
        // While playing(){
        // All block updates and entities are paused within 20 blocks of the player (done using mixin)
        // }
        // The domain effect is done with the stop-motion stuff similar to wifies thing using completely black blocks
        // players are allowed to move during the domain effect, once the domain finishes loading all ender pearls attached to the player are nulled
        // the players in the domains range tp to a dimension (idk how I handle the loading screen)
        // once all players are loaded into the dimension, the domain owner imbues their domain technique where an animation happens
        // afterward the players guaranteedHit variable will be set to true


        //domain clash
        // up to 3 domains can "clash"
        // when a domain is created it runs clash(). for each domain clashed with (up to 3) only a bit less then half of the domain is created
        // how the domain is created is based on shape
        // tbh this isn't a now problem






        //COMPLEX VERSION
        // send server packet to initiate domain animation (the player animation),
        // all entities and block updates within the domain range cannot move for the period the animation is happening
        // more specifically a recording of the player animation is created, and while its playing, a new dimention is updated
        // during this period
        // a 0.2 second black void will appear

        // onEnterDomain()
        //create a "fake" area
        // for each player within 20 blocks tp @s to Domain.TYPE



        // on break():
        // all players are paused and their screens get cracked (take whatever image was at the last it is then cracked) until all players are loaded back into the world
        // after which onDomainEnd() is run
    }
}
