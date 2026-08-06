package com.qstorm.datagen;

import com.qstorm.potion.ModPotions;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;

public class ModPotionModelGenerator {

    public void generate(){
        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
            builder.addMix(
                    //input
                    Potions.WATER,
                    //ingredient
                    Items.SOUL_SAND,
                    //output
                    ModPotions.SOUL_POTION

            );
        });
    }
}
