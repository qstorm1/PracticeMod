package com.qstorm.powers.cursedtechnique.limitless;

import com.qstorm.PracticeMod;
import com.qstorm.powers.Ability;
import com.qstorm.powers.Combo;
import com.qstorm.powers.ComboClient;
import com.qstorm.powers.cursedtechnique.Sorcery;
import com.qstorm.powers.cursedtechnique.limitless.power.Blue;
import com.qstorm.powers.cursedtechnique.limitless.power.LimitlessInnate;
import com.qstorm.powers.cursedtechnique.limitless.power.Purple;
import com.qstorm.powers.cursedtechnique.limitless.power.Red;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.UUID;

/**
 * A limitless instance is created for every single player with the tag of Limitless
 */
public class Limitless extends Sorcery {


    public static final String tag= "Limitless User";
    public int innateTechniquePower=10;


    protected void addAbility(Ability ability,Combo combo){
        ability.id = abilities.getLast().id+1;
        abilities.add(ability);
        if(combo.actionNull()){
            combo.setAction(ability);
        }
        abilities.getLast().setCombo(combo);
    }




    //generate a limitless players technique
    private Limitless(Player player){
        super(new LimitlessInnate(player.getUUID(),0),player,100000,10);


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
     * MUST RUN BOTH ON CLIENT AND SERVER
     * @param player the Limitless player that has been initialized
     */
    public static void initLimitlessPlayer(Level level, Player player){
        //if the sorcerer already has a technique then reset
        if(sorcerers.get(player.getUUID())!=null) return;
        PracticeMod.LOGGER.info("New Limitless player added!");



        Limitless playerLimitless = new Limitless(player);
        sorcerers.put(player.getUUID(),playerLimitless);


        if(level.isClientSide()){
            initClientOnly();
        }
        else if(player instanceof ServerPlayer){
            initServerOnly();
        }

    }


    public static void initClientOnly(){
        ComboClient.addARendererToClient();
    }
    public static void initServerOnly(){

    }

    public static Limitless getClosestLimitlessPosition(Vec3 position){
        ArrayList<Double> smallestDistance = new ArrayList<>();
        ArrayList<UUID> uuidFinal = new ArrayList<>();
        smallestDistance.add(-1.0);
        sorcerers.forEach((uuid,sorcery)->{
            if(sorcery instanceof Limitless && sorcery.innateOn){
                if(smallestDistance.getFirst()<sorcery.player.position().distanceTo(position)){
                    smallestDistance.set(0,sorcery.player.position().distanceTo(position));
                    uuidFinal.add(uuid);

                }
            }
        });
        if(uuidFinal.isEmpty()){
            return null;
        }
        return (Limitless) sorcerers.get(uuidFinal.getFirst());
    }




    //Does the action of the player running
    //These could in theory be replaced with statics because origianlly they were meant to get the attacker variable from inside the limitless class during initialization
    //two times as much cursed energy as blue

    @Override
    public void tick(MinecraftServer context){
        super.tick(context);
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
