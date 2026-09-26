package com.qstorm.powers.cursedtechnique;

import com.qstorm.PracticeMod;
import com.qstorm.cca.sorceryStorage.SorceryDataContext;
import com.qstorm.cca.sorceryStorage.SorceryInfoStorage;
import com.qstorm.cca.playerInfo.PlayerInfoContext;
import com.qstorm.key.HandleKeybinds;
import com.qstorm.packets.Packet;
import com.qstorm.powers.Ability;
import com.qstorm.powers.combo.Combo;
import com.qstorm.powers.PlayerInfo;
import com.qstorm.powers.cursedtechnique.limitless.Limitless;
import com.qstorm.powers.cursedtechnique.limitless.power.Blue;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Collection;

//player specific
//any players that can use cursed energy is added to the list of players

/**
 * A method that handles all the background stuff surrounding the power system.
 * Data stored in the ComponentSorceryInfoStorage class
 */
public class Sorcery {

    //Sorcery specific data
    private int domainEnergyCost=0;//use for innate domain as well

    /**
     * the cursed energy used per tick innate techinque is active
     */
    int constantAbilityTickCost =0;


    /**
     * All players who can use cursed energy have this tag
     */
    public static final String SORCERER_TAG = "Sorceror.TAG";


    /**
     * Abilities of player
     * get(0) = Innate technique
     * MUST BE INIT ON CLIENT SIDE CORRECTLY BECAUSE of SCROLL
     * -lockHotbar in Scroll Mixin class
     * TODO: all abilities delete themselves on un-initialized
     */
    public ArrayList<Ability> abilities = new ArrayList<>();
    public Ability innate;

    //The domain of this ability
    public Domain domain;

    private Player storedPlayer;



    //a global list of each player's sorcery data (All sorcery objects)



    //born luck is a percentage that represents how lucky are your initial stat growths
    //increase luck represents and increase in luck
    public Sorcery(Ability innate,Player player,int domainEnergyCost,int constantAbilityTickCost){
        this(player);


        //sets all the important values
        this.domainEnergyCost=domainEnergyCost;
        this.constantAbilityTickCost = constantAbilityTickCost;
        this.innate=innate;
    }



    private Sorcery(Player player){
        //if already a sorcerer ignore cause I haven't handled that yet
        if(!player.getTags().contains(SORCERER_TAG)) return;
        //register sorcerer to data
        SorceryInfoStorage.sorceryData.get(player).setSorcerer(this);
        this.storedPlayer=player;

        if(player instanceof ServerPlayer serverPlayer) {
            serverSideInitCode(serverPlayer);
        }
        else{
            clientSideInitCode((LocalPlayer)player);
        }

    }


    protected void serverSideInitCode(ServerPlayer serverPlayer){
        //initial loading of box in the bottom left
        updateEnergyOutputInfo(serverPlayer);

        //handle ticking
        ServerTickEvents.END_SERVER_TICK.register(PracticeMod.USES_DATA, (server) -> {
            this.tickServer(server,serverPlayer);
        });
    }


    protected void clientSideInitCode(LocalPlayer player){
        ClientTickEvents.END_CLIENT_TICK.register(
                client -> {
                    this.tickClient(client,player);
                }
        );

    }






    protected void addAbility(Ability ability, Combo combo){
        ability.id = abilities.isEmpty() ? 10 : abilities.getLast().id+1;
        abilities.add(ability);
        if(combo.isActionNull()){
            combo.setAction(ability);
        }
        abilities.getLast().setCombo(combo);
    }

    public Vec3 getPos(){
        return storedPlayer==null ? null:storedPlayer.position();
    }

    /**
     * Handle what happens on keybinds
     * Initializes sorcery when the data changes
     * initLimitlessPlayer happens on the server side in this function
     */
    public static void init(){
        registerServerNetworking();
        Blue.BlueLayers.registerLayers();

        ServerTickEvents.END_SERVER_TICK.register((server)->{
            Collection<ServerPlayer> players = PlayerLookup.all(server);
            for(ServerPlayer player:players){
                SorceryDataContext context = SorceryInfoStorage.sorceryData.get(player);

                //if the sorcery type is a limitless, the player is considered a sorcerer, and the sorcery of the data hasn't already been set
                if(context.getSorceryType().equals(Limitless.getSorcererID())&&Sorcery.isSorcerer(player)&&context.getSorcerer()==null){
                    Limitless.initLimitlessPlayer(player);
                }
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register((client -> {
             Player player = Minecraft.getInstance().player;
             if(player!=null) {
                 SorceryDataContext context = SorceryInfoStorage.sorceryData.get(player);
                 //if the sorcery type is a limitless, the player is considered a sorcerer, and the sorcery of the data hasn't already been set
                 if (context.getSorceryType().equals(Limitless.getSorcererID()) && Sorcery.isSorcerer(player) && context.getSorcerer() == null) {
                     Limitless.initLimitlessPlayer(player);
                 }
             }

        }));
    }


    /**
     * Checks if the sorcerer is considered official
     * @param player Player of Tag
     * @return if the player input already has a sorcery object in memory
     */
    public static boolean isSorcerer(Player player){
        return !SorceryInfoStorage.sorceryData.get(player).getSorceryType().equals(SorceryInfoStorage.nullSorceryString);
    }


    public boolean innateOn =false;
    protected boolean reversedOn=false;
    protected boolean energyKeyOn=false;


    /**
     * if a key is gotten from the client, it will activate the function associated with the key on the server
     */
    public static void registerServerNetworking(){

        //make all techniques reversed
        ServerPlayNetworking.registerGlobalReceiver(HandleKeybinds.ActivateReversed.TYPE, (payload, context) -> {
            //says to run it on server
            context.server().execute(() -> {
                if(isSorcerer(context.player()))
                    SorceryInfoStorage.sorceryData.get(context.player()).getSorcerer().changeReversed();
            });
        });


        //Enable Innate Technique
        ServerPlayNetworking.registerGlobalReceiver(HandleKeybinds.EnableInnateTechnique.TYPE, (payload, context) -> {
            //says to run it on server
            context.server().execute(() -> {
                //if the player changes the state of their innate technique
                if(isSorcerer(context.player()))
                    SorceryInfoStorage.sorceryData.get(context.player()).getSorcerer().activateInnate();
            });
        });

        //when a player scrolls
        ServerPlayNetworking.registerGlobalReceiver(HandleKeybinds.HandleScroll.TYPE,(payload, context)->{
            context.server().execute(()->{
                Sorcery sorcery = SorceryInfoStorage.sorceryData.get(context.player()).getSorcerer();
                if(!isSorcerer(context.player())) return;
                if(sorcery.energyKeyOn){
                    sorcery.changeOutput(context.player(),payload.scrollAmount());
                }

                if(sorcery.innateOn){
                    sorcery.changeInnateAmount(payload.scrollAmount());
                }
                for(Ability s:sorcery.abilities){
                    if(s.isScroll){
                        s.onScroll(payload.scrollAmount());
                    }
                }
                if(sorcery.innate.isScroll){
                    sorcery.innate.onScroll(payload.scrollAmount());
                }
            });
        });

        //when the energy key is pressed, handle that
        ServerPlayNetworking.registerGlobalReceiver(HandleKeybinds.EnergyKeyOn.TYPE,(payload, context)->{
            context.server().execute(()->{
                if(isSorcerer(context.player())) return;
                SorceryInfoStorage.sorceryData.get(context.player()).getSorcerer().energyKeyOn=true;
                PracticeMod.LOGGER.info("Energy Key: {}" ,SorceryInfoStorage.sorceryData.get(context.player()).getSorcerer().energyKeyOn);
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(HandleKeybinds.EnergyKeyOff.TYPE,((payload, context) -> {
            context.server().execute(()->{
                if(isSorcerer(context.player())) return;
                SorceryInfoStorage.sorceryData.get(context.player()).getSorcerer().energyKeyOn=false;
                PracticeMod.LOGGER.info("Energy Key: {}" , SorceryInfoStorage.sorceryData.get(context.player()).getSorcerer().energyKeyOn);
            });
        }));
    }

    public void serverTick(Player contextPlayer){

    }

    public void clientTick(Player contextPlayer){

    }

    public void tickServer(MinecraftServer context,ServerPlayer player){
        if(innate==null) return;
        innate.isTicked = innateOn;
        for (Ability ability:abilities){
            if(ability.isTicked){
                ability.tick(player);
            }
        }
    }
    public void tickClient(Minecraft context, LocalPlayer player){
        if(innate==null) return;
        innate.isTicked = innateOn;
        for (Ability ability:abilities){
            if(ability.isTicked){
                ability.tick(player);
            }
        }
    }
    public void tick(){

    }














    static void innateDomain(ServerPlayer attacker){
        PracticeMod.LOGGER.info("Used innate domain");
    }

    public void domain(ServerPlayer attacker){
        PracticeMod.LOGGER.info("Used domain");
    }








    public void changeOutput(ServerPlayer player,double percentAmount){
        changeOutput(player,(int)(percentAmount/20.0F*PlayerInfo.playerInfoData.get(player).getMaxOutput()));
    }




    public void activateInnate(){
        innateOn=!innateOn;
        PracticeMod.LOGGER.info("Activated innate {}", innateOn);
    }


    public void changeInnateAmount(double amountIncrease){
        PracticeMod.LOGGER.info("Changed Innate Amount by {}", amountIncrease);
    }



    public void changeReversed() {
        PracticeMod.LOGGER.info("Activated reversed");
        reversedOn=!reversedOn;
    }



    //TODO: remember that the cost of energy increases faster then power but both increase exponentially

    private void changeOutput(ServerPlayer player, int amount){
        PlayerInfoContext context = PlayerInfo.playerInfoData.get(player);
        if(context.getOutput()+amount>=context.getMaxOutput()){
            context.setOutput(context.getMaxOutput());
        }
        else if(context.getOutput()+amount<0){
            context.setOutput(0);
        }
        else {
            context.setOutput(context.getOutput() + amount);
        }

        PracticeMod.LOGGER.info("Changed amount by {}", context.getOutput());
        updateEnergyOutputInfo(player);

    }


    private static void updateEnergyOutputInfo(ServerPlayer player){
        ServerPlayNetworking.send(player,new Packet.ActivateSorceryRender(
                PlayerInfo.playerInfoData.get(player).getEnergy(),
                PlayerInfo.getOutputAsPercent(
                        PlayerInfo.playerInfoData.get(player).getMaxOutput(),
                        PlayerInfo.playerInfoData.get(player).getOutput())
        ));
    }


    // If the player starts getting emotional they increase in power
    // To be added next update
    public void onPetDeath(){

    }


    public static void onPlayerLeave(Player playerLeaving){
    }

}
