package com.qstorm.powers;

import com.qstorm.PracticeMod;
import com.qstorm.TestComponent;
import com.qstorm.packets.Packet;
import com.qstorm.powers.cursedtechnique.limitless.power.Blue;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

import java.util.HashMap;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class PlayerInfo implements EntityComponentInitializer, TestComponent {

    //you can only train up to this value of cursed efficiency (items and abilities like six eyes can further improve this value)
    public static final int maxCursedEfficiency = 50;

    //basic information
    static ComponentKey<TestComponent> playerInfoData =
            ComponentRegistry.getOrCreate(Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"player-data"),TestComponent.class);

    public int cursedEnergy = 0;
    public int cursedEnergyReserve=0;
    public int cursedOutput = 0;
    public int maxCursedOutput = 0;

    //concentration: decreases as you move around and do things (this decrease is removed if hit a black flash or a pet dies)
    public int concentration = 0;


    // Energy Used = Total Energy *(1-(cursedEfficiency+otherEfficiency)/100.0 )
    public int cursedEfficiency = 0;
    public int otherEfficiency=0;




    public static HashMap<UUID,PlayerInfo> playerInfoHashMap = new HashMap<>();

    Player playerAttached;

    public void useEnergy(ServerPlayer player, int energyUsed){
        this.cursedEnergy-=(int)(energyUsed*(1-(cursedEfficiency+otherEfficiency)/100.0));
        ServerPlayNetworking.send(player,new Packet.ActivateSorceryRender(
                PlayerInfo.playerInfoHashMap.get(player.getUUID()).cursedEnergy,
                PlayerInfo.getOutputAsPercent(player.getUUID())));
    }


    public static void initNewPlayer(UUID playerUUID){
        playerInfoHashMap.putIfAbsent(playerUUID,new PlayerInfo(0,0));
    }

    public static void initNewPlayer(UUID playerUUID,int bornLuck,int increaseLuck){
        playerInfoHashMap.putIfAbsent(playerUUID,new PlayerInfo(bornLuck,increaseLuck));
    }

    public static void regenPlayerInfo(UUID playerUUID){
        if(playerInfoHashMap.get(playerUUID)==null) return;
        playerInfoHashMap.put(playerUUID,new PlayerInfo(0,0));
    }

    public static void regenPlayerInfo(UUID playerUUID,int bornLuck, int increaseLuck){
        if(playerInfoHashMap.get(playerUUID)==null) return;
        playerInfoHashMap.put(playerUUID,new PlayerInfo(bornLuck,increaseLuck));
    }



    /**
     * for debug or aura farming
     */
    public void cheatMode(){
        this.cursedEnergyReserve=999999999;
        this.cursedEnergy= cursedEnergyReserve;
        this.maxCursedOutput=1000000;
        this.cursedOutput=maxCursedOutput/2;
    }


    @Override
    public String toString(){
        return  "Total Cursed Energy = "+cursedEnergyReserve + "Current Cursed Energy = " + cursedEnergy +
                "\nTotal Cursed Output = "+ maxCursedOutput +" Current Cursed Output = "+cursedOutput +
                "\nCurrent Cursed Efficiency = "+cursedEfficiency;
    }


    public static Double getOutputAsPercent(UUID player){
        return ((double)(playerInfoHashMap.get(player).cursedOutput)/(playerInfoHashMap.get(player).maxCursedOutput));
    }



    public PlayerInfo(int bornLuck, int increaseLuck){

        //a random number between 0-1 with a higher chance of being close to one based on bornLuck stat
        //tbh this is kinda more vibes based
        Random random = new Random();
        int i = 0;



        //how much cursed energy the player has
        this.cursedEnergyReserve = (int)(Math.abs(random.nextGaussian(500000+bornLuck,200000))*(1+increaseLuck/100.0));
        this.cursedEnergy=cursedEnergyReserve;

        //how much cursed energy the player can output (0-100%) default to 0.1% its realistically impossible to go past 5%
        // (you can run a thing that costs a default of 1/1000 of maxed cursed energy 1000 times)
        // I might change this from a percentage to an amount (ie you use scroll bar to output 1 energy-10000 energy)
        double tempMaxCursedOutput;
        do {
            tempMaxCursedOutput = Math.abs(random.nextGaussian(0.2,0.05));
            i++;
        }
        while(tempMaxCursedOutput>=1&&i<99);


        maxCursedOutput=(int)(cursedEnergyReserve*tempMaxCursedOutput);
        this.cursedOutput=maxCursedOutput/2;

    }

    public PlayerInfo(int efficiency, int maxEnergy, int maxOutput){
        this.cursedEfficiency=efficiency;
        this.cursedEnergyReserve=maxEnergy;
        this.cursedEnergy=maxEnergy;
        this.maxCursedOutput=maxOutput;
        this.cursedOutput=maxCursedOutput/2;
    }

    public void onPlayerJoin(ServerPlayer player){
        if()

    }

    public void onPlayerLeave(ServerPlayer player){

    }

    BiFunction<Integer,Integer,Integer> generateEnergy =
            (BornLuck,IncreasedLuck)->(int)(Math.abs(new Random().nextGaussian(500000+BornLuck,200000))*(1+IncreasedLuck/100.0));

    private Integer getOutput(){

    }


    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(playerInfoData,player -> new PlayerInfo(), RespawnCopyStrategy.ALWAYS_COPY);
    }





    public <T> T setValueOrMakeNew(Optional<T> value, Supplier<T> onNew){
        return value.orElseGet(onNew);
    }
    public <T> T setValueOrMakeNew(Optional<T> value, T valueElse){
        return value.orElse(valueElse);
    }

    @Override
    public void readData(ValueInput readView) {
        this.cursedEnergyReserve = setValueOrMakeNew(readView.getInt("Max Cursed Energy"),);
        this.cursedEnergy = setValueOrMakeNew(readView.getInt("Current Cursed Energy"),cursedEnergyReserve);
        this.maxCursedOutput = setValueOrMakeNew(readView.getInt("Max Cursed Output"),);
        this.cursedOutput = setValueOrMakeNew(readView.getInt("Current Cursed Output"),maxCursedOutput/2);
        this.cursedEfficiency = setValueOrMakeNew(readView.getInt("Cursed Efficiency"),0);
    }

    @Override
    public void writeData(ValueOutput writeView) {
        writeView.putInt("Max Cursed Energy",this.cursedEnergyReserve);
        writeView.putInt("Current Cursed Energy",this.cursedEnergy);
        writeView.putInt("Max Cursed Output",this.maxCursedOutput);
        writeView.putInt("Current Cursed Output",this.cursedOutput);
        writeView.putInt("Cursed Efficiency",this.cursedEfficiency);
    }


    //TODO: save playerInfo stuff to the player entity

}


