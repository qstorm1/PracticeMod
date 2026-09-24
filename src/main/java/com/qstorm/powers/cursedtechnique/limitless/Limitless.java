package com.qstorm.powers.cursedtechnique.limitless;

import com.qstorm.PracticeMod;
import com.qstorm.cca.SorceryInfoStorage;
import com.qstorm.packets.Packet;
import com.qstorm.powers.combo.Combo;
import com.qstorm.powers.combo.ComboClient;
import com.qstorm.powers.cursedtechnique.Sorcery;
import com.qstorm.powers.cursedtechnique.limitless.power.Blue;
import com.qstorm.powers.cursedtechnique.limitless.power.LimitlessInnate;
import com.qstorm.powers.cursedtechnique.limitless.power.Purple;
import com.qstorm.powers.cursedtechnique.limitless.power.Red;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.UUID;

/**
 * A limitless instance is created for every single player with the tag of Limitless
 */
public class Limitless extends Sorcery {

    /**
     * The ID of the class;
     * used to check if the Stored Sorcery Type in ComponentSorceryInfoStorage is a limitless type
     */
    public static final String SORCERER_ID = "Limitless User";
    /**
     * The amount of energy it takes per tick innate is active
     */
    public int innateTechniquePower=10;


    //generate a limitless technique
    private Limitless(Player player){
        super(new LimitlessInnate(player.getUUID(),0),player,100000,10);
        UUID playerUUID = player.getUUID();
        addAbility(
                new Blue(playerUUID,0),
                Combo.build(player,"Limitless Blue").addKey1(10).addKey3(100).addKey4(60)
        );


        addAbility(
                new Red(playerUUID,0),
                Combo.build(player,"Limitless Red").addKey1(100).addKey1(200).addKey4(60).addKey4(10).addKey2(50)
        );

        addAbility(
                new Purple(player.getUUID(),3),
                Combo.build(player,"Limitless Purple").addKey2(4).addKey4(10).addKey1(60).addKey1(10).addKey3(5)
        );
    }


    /**
     * THIS CAN RUN ON BOTH SERVER AND CLIENT HOWEVER IT ONLY NEEDS TO BE RAN ON SERVER
     * @param player the Limitless player that has been initialized
     */
    public static void initLimitlessPlayer(Player player){
        //non-resetable
        //TODO: make it resetable (remove all the combos and stuff
        boolean test = isSorcerer(player);
        if(!isSorcerer(player)){
            return;
        }

        PracticeMod.LOGGER.info("New Limitless player added!");
        if(!player.getTags().contains(Sorcery.SORCERER_TAG)) player.addTag(Sorcery.SORCERER_TAG);
        new Limitless(player);





        if(!(player instanceof ServerPlayer serverPlayer)) {
            ComboClient.addComboRendererToClient();
        }
        else{
            //auto init client side
            ServerPlayNetworking.send(serverPlayer,new Packet.LimitlessInit());
        }

    }

    public static Vec3 closestLimitlessPosition=null;

    //TODO: make this a variable that constantly get sent to all clients for more efficiency in mixin stuff
    public static Limitless getClosestLimitless(ServerLevel level, Vec3 position){
        ArrayList<Double> smallestDistance = new ArrayList<>();//uses arraylist cause lambdas are stupid
        Limitless finalValue=null;
        smallestDistance.add(-1.0);
        //need position value, need to know if sorcery is limitless and sorcery.innateOn
        for(ServerPlayer player:PlayerLookup.world(level)){
            Sorcery sorcery = SorceryInfoStorage.sorceryData.get(player).getSorcerer();
            if(sorcery instanceof Limitless limitless && sorcery.innateOn){
                if(smallestDistance.getFirst()<player.position().distanceTo(position)){
                    smallestDistance.set(0,player.position().distanceTo(position));
                    finalValue=limitless;

                }
            }
        }
        return finalValue;
        //ServerPlayNetworking.send(doer,new Packet.SetClosestPlayer(finalValue.x,finalValue.y,finalValue.z));
    }


    public static String getSorcererID() {
        return SORCERER_ID;
    }


    //Does the action of the player running
    //These could in theory be replaced with statics because origianlly they were meant to get the attacker variable from inside the limitless class during initialization
    //two times as much cursed energy as blue

    @Override
    public void tickServer(MinecraftServer server, ServerPlayer contextPlayer){
        super.tickServer(server,contextPlayer);
        //getClosestLimitlessPosition(contextPlayer.level(),contextPlayer,contextPlayer.position());
    }


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
