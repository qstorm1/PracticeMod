package com.qstorm.powers.cursedtechnique;

import com.qstorm.powers.cursedtechnique.limitless.Limitless;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.world.WorldComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.world.WorldComponentInitializer;

import java.util.HashMap;
import java.util.UUID;

public class SorcererInfoWorldSave implements WorldComponentInitializer, Component {
    //player info to integer where integer is the stored class ID
    public static HashMap<UUID, Integer> playerSorceryInfo= new HashMap<>();

    public Class<? extends Sorcery> getClassFromID(Integer ID){
        switch (ID){
            case Limitless.CLASS_ID -> {return Limitless.class;}
            default -> {return Sorcery.class;}
        }
    }

    public Integer getIDFromClass(Class<Limitless> classID){
        if(classID.isInstance(Limitless.class)) return Limitless.CLASS_ID;
        else return Sorcery.CLASS_ID;

    }

    @Override
    public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {

    }

    @Override
    public void readData(ValueInput readView) {
        playerSorceryInfo.clear();
        HashMap<UUID,Class<? extends Sorcery>> updated = new HashMap<>();
    }

    @Override
    public void writeData(ValueOutput writeView) {
        int[] playerSorceryIntArray = new int[playerSorceryInfo.size()];
        Integer[] test =playerSorceryInfo.values().toArray(Integer[]::new);
        for(int i = 0; i<playerSorceryInfo.size();i++){
            playerSorceryIntArray[i]=test[i];
        }
        writeView.putIntArray("player-sorcery-types",playerSorceryIntArray);
        writeView.putIntArray("player-sorcery-uuids",playerSorceryIntArray);
    }
}
