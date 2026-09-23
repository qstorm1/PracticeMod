package com.qstorm.powers;

import com.qstorm.PracticeMod;
import com.qstorm.cca.PlayerInfoContext;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;

import java.util.HashMap;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.function.Supplier;

public class PlayerInfo implements PlayerInfoContext {

    //you can only train up to this value of cursed efficiency (items and abilities like six eyes can further improve this value)
    public static final int maxCursedEfficiency = 50;

    //basic information
    public static ComponentKey<PlayerInfoContext> playerInfoData =
            ComponentRegistry.getOrCreate(Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"player-data"), PlayerInfoContext.class);

    public int cursedEnergy = 0;
    public int cursedEnergyReserve=0;
    public int cursedOutput = 0;
    public int maxCursedOutput = 0;

    //concentration: decreases as you move around and do things (this decrease is removed if hit a black flash or a pet dies)
    public int concentration = 0;


    // Energy Used = Total Energy *(1-(cursedEfficiency+otherEfficiency)/100.0 )
    public int cursedEfficiency = 0;
    public int otherEfficiency=0;


    //MUST ONLY BE RUN ON SERVER SIDE
    public PlayerInfo(Player player,int bornLuck, int increaseLuck){
         this(player);
         if(player instanceof LocalPlayer) return;
        //a random number between 0-1 with a higher chance of being close to one based on bornLuck stat
        //tbh this is kinda more vibes based

        //how much cursed energy the player has
        this.cursedEnergyReserve = generateCursedEnergyStat(bornLuck,increaseLuck);
        this.cursedEnergy=cursedEnergyReserve;

        maxCursedOutput= generateCursedOutputStat(cursedEnergyReserve);
        this.cursedOutput=maxCursedOutput/2;

    }

    public PlayerInfo(Player player,int efficiency, int maxEnergy, int maxOutput){
        this(player);
        this.cursedEfficiency=efficiency;
        this.cursedEnergyReserve=maxEnergy;
        this.cursedEnergy=maxEnergy;
        this.maxCursedOutput=maxOutput;
        this.cursedOutput=maxCursedOutput/2;
    }

    private PlayerInfo(Player player){
        if(player instanceof LocalPlayer) return;
    }




    public static void regenPlayerInfo(Player player){
        regenPlayerInfo(player,0,0);
    }

    public static void regenPlayerInfo(Player player,int bornLuck, int increaseLuck){
        PlayerInfo newValues = new PlayerInfo(player,0,0);
        playerInfoData.get(player).setMaxEnergy(newValues.cursedEnergyReserve);
        playerInfoData.get(player).setEnergy(newValues.cursedEnergy);
        playerInfoData.get(player).setOutput(newValues.cursedOutput);
        playerInfoData.get(player).setMaxOutput(newValues.maxCursedOutput);
        playerInfoData.get(player).setEfficiency(newValues.cursedEfficiency);
    }



    /**
     * for debug or aura farming
     */
    public static void setCheatMode(Player player){
        PlayerInfoContext pIC = playerInfoData.get(player);
        pIC.setMaxEnergy(999999999);
        pIC.setEnergy(999999999);
        pIC.setMaxOutput(1000000);
        pIC.setOutput(1000000/2);
    }






    private Integer generateCursedEnergyStat(int bornLuck, int increasedLuck){
        return (int)(Math.abs(new Random().nextGaussian(500000+bornLuck,200000))*(1+increasedLuck/100.0));
    }

    private Integer generateCursedOutputStat(int cursedEnergyReserve){
        //how much cursed energy the player can output (0-100%) default to 0.1% its realistically impossible to go past 5%
        // (you can run a thing that costs a default of 1/1000 of maxed cursed energy 1000 times)
        // I might change this from a percentage to an amount (ie you use scroll bar to output 1 energy-10000 energy)

        int i = 0;
        double tempMaxCursedOutput;
        do {
            tempMaxCursedOutput = Math.abs(new Random().nextGaussian(0.2,0.05));
            i++;
        }
        while(tempMaxCursedOutput>=1&&i<99);

        return (int)(cursedEnergyReserve*tempMaxCursedOutput);
    }



    @Deprecated
    public void useEnergy(ServerPlayer player, int energyUsed){
        this.cursedEnergy-=(int)(energyUsed*(1-(cursedEfficiency+otherEfficiency)/100.0));

    }


    @Override
    public String toString(){
        return  "Total Cursed Energy = "+cursedEnergyReserve + "Current Cursed Energy = " + cursedEnergy +
                "\nTotal Cursed Output = "+ maxCursedOutput +" Current Cursed Output = "+cursedOutput +
                "\nCurrent Cursed Efficiency = "+cursedEfficiency;
    }




//    public static Double getOutputAsPercent(UUID player){
//        //return ((double)(playerInfoHashMap.get(player).cursedOutput)/(playerInfoHashMap.get(player).maxCursedOutput));
//    }

    public static Double getOutputAsPercent(int max, int output){
        return ((double)(output)/(max));
    }








    public <T> T setValueOrMakeNew(Optional<T> value, Supplier<T> onNew){
        return value.orElseGet(onNew);
    }
    public <T> T setValueOrMakeNew(Optional<T> value, T valueElse){
        return value.orElse(valueElse);
    }

    //TODO: Check if I need the suppliers to regen cursed energy info because it happens on player initialization
    @Override
    public void readData(ValueInput readView) {
        this.cursedEnergyReserve = setValueOrMakeNew(readView.getInt("Max Cursed Energy"),
                (Supplier<Integer>) () -> generateCursedEnergyStat(0,1));
        this.cursedEnergy = setValueOrMakeNew(readView.getInt("Current Cursed Energy"),cursedEnergyReserve);


        this.maxCursedOutput = setValueOrMakeNew(readView.getInt("Max Cursed Output"),
                (Supplier<Integer>) ()-> generateCursedOutputStat(cursedEnergyReserve));
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

    @Override
    public int getMaxEnergy() {
        return this.cursedEnergyReserve;
    }

    @Override
    public int getEnergy() {
        return this.cursedEnergy;
    }

    @Override
    public int getMaxOutput() {
        return this.maxCursedOutput;
    }

    @Override
    public int getOutput() {
        return this.cursedOutput;
    }

    @Override
    public int getEfficiency() {
        return this.cursedEfficiency;
    }

    @Override
    public void setMaxEnergy(int energy) {
        this.cursedEnergyReserve=energy;
    }

    @Override
    public void setEnergy(int energy) {
        this.cursedEnergy=energy;
    }

    @Override
    public void setMaxOutput(int output) {
        this.maxCursedOutput=output;
    }

    @Override
    public void setOutput(int output) {
        this.cursedOutput=output;
    }

    @Override
    public void setEfficiency(int increase) {
        this.cursedEfficiency=increase;
    }
}


