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

    public static HashMap<UUID,Limitless> limitlessPlayers;
    public static String tag= "Limitless User";

    //I know i'm eventually gonna have to deal with player data and storing but for now i'll use these
    public final int cursedEnergy=999999;
    public final int cursedOutput=999999;


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
     * for each client tick run
     * MUST BE RUN IN EVERY CLIENT
     * @param player the Limitless player that has been initialized
     */
    public void initLimitlessPlayer(Player player){
        limitlessPlayers.put(player.getUUID(),new Limitless());
        redCombo = Combo.build(player).addKey1(100);

    }






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
}
