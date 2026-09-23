package com.qstorm.cca;

import com.qstorm.PracticeMod;
import com.qstorm.powers.cursedtechnique.Sorcery;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

public class SorceryInfoStorage implements SorceryDataContext, EntityComponentInitializer {
    //TODO: implement AutoSynced thing

    //data on weather the player can use things
    private boolean hasInnateTechnique = false;
    private boolean canUseInnateDomain=false;
    private boolean canUseDomain=false;


    //the power that this player is using, "null" = no power
    public static final String nullSorceryString="null";
    /**
     * The type of power the player is using
     */
    public String powerType=nullSorceryString;
    /**
     * weather the power has run through the new Sorcery stuff
     */
    private boolean initiated = false;

    /**
     * The data holder for each player
     */
    public static ComponentKey<SorceryDataContext> sorceryData =
            ComponentRegistry.getOrCreate(Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"sorcery-info-data"), SorceryDataContext.class);

    private Sorcery storedSorcerer = null;

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
        writeView.putString("Power",powerType);
    }


    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(sorceryData, player -> new SorceryInfoStorage(), RespawnCopyStrategy.ALWAYS_COPY);
    }


    @Override
    public boolean hasInnateTechnique() {
        return hasInnateTechnique;
    }

    @Override
    public void setHasInnateTechnique(boolean hasInnateTechnique) {
        this.hasInnateTechnique=hasInnateTechnique;
    }

    @Override
    public boolean getInnateStatus() {
        return canUseInnateDomain;
    }

    @Override
    public void setInnateStatus(boolean status) {
        this.canUseInnateDomain=status;
    }

    @Override
    public boolean getDomainStatus() {
        return canUseDomain;
    }

    @Override
    public void setDomainStatus(boolean status) {
        this.canUseDomain=status;
    }

    @Override
    public String getSorceryType() {
        return powerType;
    }

    @Override
    public void setSorceryType(String powerType) {
        this.powerType = powerType;
    }

    @Override
    public Sorcery getSorcerer() {
        return storedSorcerer;
    }

    @Override
    public void setSorcerer(Sorcery storedSorcerer) {
        this.storedSorcerer = storedSorcerer;
    }
}
