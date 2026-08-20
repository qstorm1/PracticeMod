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

import java.util.function.Function;

/**
 * A limitless instance is created for every single player
 */
public class Limitless implements Sorcery {
    Combo ability1=Combo.createCombo(
            new ComboKey(InitializeBindings.attack1,80),
            new ComboKey(InitializeBindings.attack2,80),
            new ComboKey(InitializeBindings.attack3,80));
    Combo ability2=Combo.createCombo(
            new ComboKey(InitializeBindings.attack3,80),
            new ComboKey(InitializeBindings.attack2,80),
            new ComboKey(InitializeBindings.attack3,80));;
    Combo ability3=Combo.createCombo(
            new ComboKey(InitializeBindings.attack2, 80),
            new ComboKey(InitializeBindings.attack3, 80),
            new ComboKey(InitializeBindings.attack1, 80));;
    Combo ability4=Combo.createCombo(
            new ComboKey(InitializeBindings.attack2,80),
            new ComboKey(InitializeBindings.attack2,80),
            new ComboKey(InitializeBindings.attack1,80));


    public static Limitless[] limitlessPlayers;
    public static String tag= "Limitless User";

    Player player;


    public Limitless(Player player){
        if(!player.getTags().contains(tag)) {
            for(Limitless ls: limitlessPlayers) {
                if(ls.player.equals(player)) {
                    PracticeMod.LOGGER.info("Player was cast to limitless a second time");
                    return;
                }
            }
            this.player = player;
        }

        PracticeMod.LOGGER.info("Player was cast to limitless a second time");

    }




    /**
     * for each client run this tick
     * MUST BE RUN IN THE CORRECT CLIENT
     * @param ls the Limitless technique that has been initialized
     */
    public void initLimitlessPlayer(Limitless ls){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            //code
            if(client.player!=null) {
                if (InitializeBindings.attack1.isDown()) {
                    ClientPlayNetworking.send(new HandleKeybinds.AttackJJK1());
                }
                if (InitializeBindings.attack2.isDown()) {
                    ClientPlayNetworking.send(new HandleKeybinds.AttackJJK2());
                }
                if (InitializeBindings.attack3.isDown()) {
                    ClientPlayNetworking.send(new HandleKeybinds.AttackJJK3());
                }
                if (InitializeBindings.attack4.isDown()) {
                    ClientPlayNetworking.send(new HandleKeybinds.AttackJJK4());
                }
                if (InitializeBindings.domainKey.isDown()) {
                    ClientPlayNetworking.send(new HandleKeybinds.Domain());
                }
            }

        });

    }

    /**
     * for each client run this tick
     * @param player the player that will receive limitless
     */
    public void initLimitlessPlayer(Player player){
        initLimitlessPlayer(new Limitless(player));
    }


    /**
     * tbh does nothing rn its run at the start of the game
     */
    public static void initLimitless(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            for(Limitless l:limitlessPlayers){
                l.player.addTag(tag);
            }
        });
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
