package com.qstorm.cca;

import com.qstorm.PracticeMod;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

public class ComponentSorceryInfoStorage implements SorceryDataInterface, EntityComponentInitializer {
    //TODO: implement AutoSynced thing

    //data on weather the player can use things
    public boolean hasInnateTechnique = false;
    public boolean canUseInnateDomain=false;
    public boolean canUseDomain=false;

    //the power that this player is using, "null" = no power
    public static final String nullSorceryString="null";
    public String powerType=nullSorceryString;


    public static ComponentKey<SorceryDataInterface> sorceryInfoData =
            ComponentRegistry.getOrCreate(Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"sorcery-info-data"),SorceryDataInterface.class);



    @Override
    public void readData(ValueInput readView) {
        this.hasInnateTechnique=readView.getBooleanOr("HasInnateTechnique",false);
        this.canUseInnateDomain=readView.getBooleanOr("CanUseInnateDomain",false);
        this.canUseDomain=readView.getBooleanOr("CanUseDomain",false);
        this.powerType=readView.getStringOr("Power",nullSorceryString);

    }

    @Override
    public void writeData(ValueOutput writeView) {
        writeView.putBoolean("HasInnateTechnique",hasInnateTechnique);
        writeView.putBoolean("CanUseInnateDomain",canUseInnateDomain);
        writeView.putBoolean("CanUseDomain",canUseDomain);
        if(powerType!=null&&!powerType.equals(nullSorceryString)) writeView.putString("Power",powerType);
    }


    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(sorceryInfoData, player -> new ComponentSorceryInfoStorage(), RespawnCopyStrategy.ALWAYS_COPY);
    }


    @Override
    public boolean hasInnateTechnique() {
        return hasInnateTechnique;
    }

    @Override
    public boolean getInnateStatus() {
        return canUseInnateDomain;
    }

    @Override
    public boolean getDomainStatus() {
        return canUseDomain;
    }

    @Override
    public String getSorceryType() {
        return powerType;
    }

    @Override
    public void setSorceryType(String powerType) {
        this.powerType = powerType;
    }
}
