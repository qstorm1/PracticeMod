package com.qstorm.cca;

import com.qstorm.PracticeMod;
import com.qstorm.TestComponent;
import com.qstorm.powers.cursedtechnique.Sorcery;
import com.qstorm.powers.cursedtechnique.limitless.Limitless;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ComponentSorceryInfoStorage implements TestComponent, EntityComponentInitializer {

//    public static HashMap<UUID,ComponentSorceryInfoStorage> infoList= new HashMap<>();

    //data on weather the player can use things
    public boolean hasInnateTechnique = false;

    public boolean canUseInnateDomain=false;
    public boolean canUseDomain=false;


    public String powerType="null";

    public static ComponentKey<TestComponent> sorceryInfoData =
            ComponentRegistry.getOrCreate(Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"sorcery-info-data"),TestComponent.class);



    public static void initPowerType(Player player){

//        if(infoList.get(player.getUUID()).powerType.equals("null")){
//
//        }
//        else if (infoList.get(player.getUUID()).powerType.equals(Limitless.SORCERER_TAG)){
//            Limitless.initLimitlessPlayer(player);
//        }

    }

    @Override
    public void readData(ValueInput readView) {
        this.hasInnateTechnique=readView.getBooleanOr("HasInnateTechnique",false);
        this.canUseInnateDomain=readView.getBooleanOr("CanUseInnateDomain",false);
        this.canUseDomain=readView.getBooleanOr("CanUseDomain",false);
        this.powerType=readView.getStringOr("Power","null");
    }

    @Override
    public void writeData(ValueOutput writeView) {
        writeView.putBoolean("HasInnateTechnique",hasInnateTechnique);
        writeView.putBoolean("CanUseInnateDomain",canUseInnateDomain);
        writeView.putBoolean("CanUseDomain",canUseDomain);
        if(powerType!=null&&!powerType.equals("null")) writeView.putString("Power",powerType);
    }


    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(sorceryInfoData, player -> new ComponentSorceryInfoStorage(), RespawnCopyStrategy.ALWAYS_COPY);
    }
}
