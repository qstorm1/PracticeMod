package com.qstorm.powers.cursedtechnique;

import com.qstorm.PracticeMod;
import com.qstorm.key.HandleKeybinds;
import com.qstorm.packets.Packet;
import com.qstorm.powers.Ability;
import com.qstorm.powers.combo.Combo;
import com.qstorm.powers.PlayerInfo;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistryV3;
import org.ladysnake.cca.api.v3.component.ComponentV3;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

//player specific
//any players that can use cursed energy is added to the list of players
public class Sorcery implements EntityComponentInitializer, AutoSyncedComponent, Component {

    //data on weather the player can use things
    public boolean hasInnateTechnique = false;

    public boolean canUseInnateDomain=false;
    public boolean canUseDomain=false;

    //Sorcery specific data
    public int domainEnergyCost=0;//use for innate domain as well
    /**
     * the cursed energy used per tick innate techinque is active
     */
    int constantAbilityTickCost =0;



    public UUID storedPlayerUUID;
    public final Player storedPlayer;

    /**
     * Abilities of player
     * get(0) = Innate technique
     */
    public ArrayList<Ability> abilities = new ArrayList<>();
    public Ability innate;

    //The domain of this ability
    public Domain domain;




    //a global list of each player's sorcery data (All sorcery objects)
    //TODO: remove inplace of the AutoSyncedComponent thing
    public static HashMap<UUID,Sorcery> sorcerers = new HashMap<>();
    public static HashMap<UUID,Boolean> hasInitializedOnClient = new HashMap<>();



    //born luck is a percentage that represents how lucky are your initial stat growths
    //increase luck represents and increase in luck
    public Sorcery(Ability innate,Player player,int domainEnergyCost,int constantAbilityTickCost){
        this(player);

        //sets all the important values
        this.domainEnergyCost=domainEnergyCost;
        this.constantAbilityTickCost = constantAbilityTickCost;
        hasInnateTechnique=true;

        //set innate ability to be first
        if(!abilities.isEmpty()){
            abilities = new ArrayList<>();
        }
        abilities.add(innate);
    }



    private Sorcery(Player player){
        this.storedPlayer=player;

        if(!Minecraft.getInstance().isSingleplayer()||!player.level().isClientSide())
            sorcerers.put(player.getUUID(),this);
        hasInitializedOnClient.putIfAbsent(player.getUUID(),false);

        if(player instanceof ServerPlayer serverPlayer) {
            serverSideInitCode(serverPlayer);

        }
        else{
            clientSideInitCode(player);
        }

    }


    protected void serverSideInitCode(ServerPlayer serverPlayer){
        this.storedPlayer =serverPlayer;
        ServerPlayNetworking.send(serverPlayer, new Packet.ActivateSorceryRender(
                PlayerInfo.playerInfoHashMap.get(serverPlayer.getUUID()).cursedEnergy,
                PlayerInfo.getOutputAsPercent(serverPlayer.getUUID())));
        ServerTickEvents.END_SERVER_TICK.register(PracticeMod.USES_DATA,
                (server) -> {
                    this.tick(serverPlayer);
                });
    }


    protected void clientSideInitCode(Player player){
        hasInitializedOnClient.put(player.getUUID(),true);
        ClientTickEvents.END_CLIENT_TICK.register(
                client -> this.tick(player)
        );

    }




    /**
     * Checks if the sorcerer already exists
     * @return 0 if a sorcerer hasn't been initialized, 1 if client needs to be initialized on an integrated server, 2 if doesn't need to be initialized
     */
    protected static boolean checkIfAlreadyHasTechnique(Player player){
        //we are in an environment where the sorcerer hasn't been initialized
        if(Minecraft.getInstance().isSingleplayer()&&!(player instanceof ServerPlayer)) {
            if(hasInitializedOnClient.get(player.getUUID())==null)
                return true;

            return !hasInitializedOnClient.get(player.getUUID());
        }
        return !sorceryInMap(player.getUUID());
    }

    /**
     * Checks if the sorcerers map contains this element
     * @param playerUUID UUID of player checking
     * @return if the player input already has a sorcery object in memory
     */
    protected static boolean sorceryInMap(UUID playerUUID){
        return sorcerers.get(playerUUID)!=null;
    }


    protected void addAbility(Ability ability, Combo combo){
        ability.id = abilities.getLast().id+1;
        abilities.add(ability);
        if(combo.isActionNull()){
            combo.setAction(ability);
        }
        abilities.getLast().setCombo(combo);
    }


    public static void init(){
        registerServerNetworking();
    }


    public boolean innateOn =false;
    public boolean reversedOn=false;
    public boolean energyKeyOn=false;


    /**
     * if a key is gotten from the client, it will activate the function associated with the key on the server
     */
    public static void registerServerNetworking(){

        //make all techniques reversed
        ServerPlayNetworking.registerGlobalReceiver(HandleKeybinds.ActivateReversed.TYPE, (payload, context) -> {
            //says to run it on server
            context.server().execute(() -> {
                if(sorcerers.get(context.player().getUUID())!=null)
                    sorcerers.get(context.player().getUUID()).changeReversed();
            });
        });


        //Enable Innate Technique
        ServerPlayNetworking.registerGlobalReceiver(HandleKeybinds.EnableInnateTechnique.TYPE, (payload, context) -> {
            //says to run it on server
            context.server().execute(() -> {
                //if the player changes the state of their innate technique
                if(sorcerers.get(context.player().getUUID()) !=null)
                    sorcerers.get(context.player().getUUID()).activateInnate();
            });
        });

        //when a player scrolls
        ServerPlayNetworking.registerGlobalReceiver(HandleKeybinds.HandleScroll.TYPE,(payload, context)->{
            context.server().execute(()->{
                Sorcery sorcery =sorcerers.get(context.player().getUUID());
                if(sorcery==null) return;
                if(sorcery.energyKeyOn){
                    sorcery.changeOutput(payload.scrollAmount());
                }

                if(sorcery.innateOn){
                    sorcery.changeInnateAmount(payload.scrollAmount());
                }
                for(Ability s:sorcerers.get(context.player().getUUID()).abilities){
                    if(s.isScroll){
                        s.onScroll(payload.scrollAmount());
                    }
                }
            });
        });

        //when the energy key is pressed, handle that
        ServerPlayNetworking.registerGlobalReceiver(HandleKeybinds.EnergyKeyOn.TYPE,(payload, context)->{
            context.server().execute(()->{
                if(sorcerers.get(context.player().getUUID())==null) return;
                sorcerers.get(context.player().getUUID()).energyKeyOn=true;
                PracticeMod.LOGGER.info("Energy Key: {}" ,sorcerers.get(context.player().getUUID()).energyKeyOn);
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(HandleKeybinds.EnergyKeyOff.TYPE,((payload, context) -> {
            context.server().execute(()->{
                if(sorcerers.get(context.player().getUUID())==null) return;
                sorcerers.get(context.player().getUUID()).energyKeyOn=false;
                PracticeMod.LOGGER.info("Energy Key: {}" ,sorcerers.get(context.player().getUUID()).energyKeyOn);
            });
        }));
    }



    public void tick(Player contextPlayer){
        if(abilities.isEmpty()) return;
        abilities.getFirst().isTicked = innateOn;
        for (Ability ability:abilities){
            if(ability.isTicked){
                ability.tick(contextPlayer);
            }
        }
    }














    static void innateDomain(ServerPlayer attacker){
        PracticeMod.LOGGER.info("Used innate domain");
    }

    public void domain(ServerPlayer attacker){
        PracticeMod.LOGGER.info("Used domain");
    }

    public void changeOutputPercent(double percentAmount){
        changeOutput((int)(percentAmount*PlayerInfo.playerInfoHashMap.get(storedPlayer.getUUID()).maxCursedOutput));
    }

    public void changeOutput(double percentAmount){
        changeOutput((int)((percentAmount/20.0F)*PlayerInfo.playerInfoHashMap.get(storedPlayer.getUUID()).maxCursedOutput));
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
    //TODO: remember that the cost of energy increases faster then the power

    private void changeOutput(int amount){
        PlayerInfo playerInfo = PlayerInfo.playerInfoHashMap.get(storedPlayer.getUUID());

        if(playerInfo.cursedOutput+amount>=playerInfo.maxCursedOutput){
            playerInfo.cursedOutput= playerInfo.maxCursedOutput;
        }
        else if(playerInfo.cursedOutput+amount<0){
            playerInfo.cursedOutput=0;
        }
        else {
            playerInfo.cursedOutput += amount;
        }
        PracticeMod.LOGGER.info("Amount {}", playerInfo.cursedOutput);
        ServerPlayNetworking.send((ServerPlayer) storedPlayer,new Packet.ActivateSorceryRender(
                PlayerInfo.playerInfoHashMap.get(storedPlayer.getUUID()).cursedEnergy,
                PlayerInfo.getOutputAsPercent(storedPlayer.getUUID())));

    }





    // If the player starts getting emotional they increase in power
    // To be added next update
    public void onPetDeath(){

    }




    public static final ComponentKey<ComponentV3> HAS_INNATE_TECHNIQUE =
            ComponentRegistryV3.INSTANCE.getOrCreate(Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"string"),ComponentV3.class);


    @Override
    public void readData(ValueInput readView) {
        this.hasInnateTechnique=readView.getBooleanOr("HasInnateTechnique",false);
        this.canUseInnateDomain=readView.getBooleanOr("CanUseInnateDomain",false);
        this.canUseDomain=readView.getBooleanOr("CanUseDomain",false);
    }

    @Override
    public void writeData(ValueOutput writeView) {
        writeView.putBoolean("HasInnateTechnique",hasInnateTechnique);
        writeView.putBoolean("CanUseInnateDomain",canUseInnateDomain);
        writeView.putBoolean("CanUseDomain",canUseDomain);
        writeView.putString("Class",this.getClass().toString());
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(HAS_INNATE_TECHNIQUE,ComponentV3::new);
    }
}
