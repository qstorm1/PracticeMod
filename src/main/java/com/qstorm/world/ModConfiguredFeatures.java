package com.qstorm.world;

import com.qstorm.PracticeMod;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;

//configures how a feature "looks like" like how many ores in a vein or what a house looks like
public class ModConfiguredFeatures {

    //bruh theres no documentation on configuredFeature

    //calls helper methods to help register custom features
    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> context) {


    }

    /**
     * From what I understand this creates a unique identifier of the feature to minecraft, specifying it is a configured featuer and has the identifier listed
     * this is like making a new Item really
     * @param name
     * @return
     */
    public static RegistryKey<ConfiguredFeature<?,?>> registerKey(String name){
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(PracticeMod.MOD_ID,name));
    }

    /**
     *
     *
     * makes sure that the configured feature is of type feature<CF>, or basically taht F is a feature that can be configured by FC
     * FC represents a custom configuration (IE ore height), it is assigned to the registry and given the key key
     * context represents the save, in the code the configureation is saved to context
     * it is saved to the idnetiifer of key as a new configured feature with the inputs feature and configuration
     *
     * @param context
     * @param key
     * @param feature
     * @param configuration
     * @param <FC>
     * @param <F>
     */
        public static <FC extends FeatureConfig,F extends Feature<FC>> void register(
            Registerable<ConfiguredFeature<?,?>> context,
        RegistryKey<ConfiguredFeature<?,?>> key,F feature,FC configuration){
        context.register(key,new ConfiguredFeature<>(feature,configuration));
    }

}
