package com.qstorm.powers.cursedtechnique.limitless;

import com.qstorm.PracticeMod;
import com.qstorm.packets.Packet;
import com.qstorm.powers.Combo;
import com.qstorm.powers.cursedtechnique.Sorcery;
import com.qstorm.powers.cursedtechnique.limitless.power.Blue;
import com.qstorm.powers.cursedtechnique.limitless.power.LimitlessInnate;
import com.qstorm.powers.cursedtechnique.limitless.power.Purple;
import com.qstorm.powers.cursedtechnique.limitless.power.Red;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

/**
 * A limitless instance is created for every single player with the tag of Limitless
 */
public class Limitless extends Sorcery {


    public static String tag= "Limitless User";
    public int innateTechniquePower=10;



    //generate a limitless technique
    private Limitless(ServerPlayer player){
        super(new LimitlessInnate(0),player,10000,10);


        abilities.add(new Blue(1));
        Combo blue = Combo.build(player,"Limitless Blue").addKey1(10).addKey3(100).addKey4(60)
                .setAction(abilities.get(2));
        abilities.get(1).setCombo(blue);


        abilities.add(new Red(2));
        Combo red = Combo.build(player,"Limitless Red").addKey1(100).addKey1(100).addKey4(60).addKey4(10).addKey2(50)
                .setAction((serverPlayer)-> abilities.get(3).Do(serverPlayer));
        abilities.get(2).setCombo(red);

        abilities.add(new Purple(3));
        Combo purple = Combo.build(player,"Limitless Purple").addKey2(4).addKey4(10).addKey1(60).addKey1(10).addKey3(5)
                .setAction((serverPlayer)-> abilities.get(5).Do(serverPlayer));
        abilities.get(3).setCombo(purple);








        //cursedOutput is a percentage

        //limitless innate technique
        ServerTickEvents.END_SERVER_TICK.register(Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"innate-technique"), (context)-> {
            if(innateOn){
                player.level().getAllEntities().forEach((entity) -> {

                });
            }
        });
    }


    /**
     * HAPPENS ONLY ON SERVER SIDE
     * @param player the Limitless player that has been initialized
     */
    public static void initLimitlessPlayer(Player player){
        if(
                (!(player instanceof ServerPlayer))||
                sorcerers.get(player.getUUID())!=null){
            return;
        }

        PracticeMod.LOGGER.info("New Limitless player added!");
        //

        Limitless playerLimitless = new Limitless((ServerPlayer)player);
        sorcerers.put(player.getUUID(),playerLimitless);




        ServerPlayNetworking.send((ServerPlayer) player,new Packet.limitlessInit());



    }




    //Does the action of the player running
    //These could in theory be replaced with statics because origianlly they were meant to get the attacker variable from inside the limitless class during initialization
    //two times as much cursed energy as blue








    @Override
    public void domain(ServerPlayer attacker){

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
