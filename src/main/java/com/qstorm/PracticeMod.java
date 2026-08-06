package com.qstorm;


import com.qstorm.effects.CustomEffects;
import com.qstorm.item.ModItems;
import com.qstorm.potion.ModPotions;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PracticeMod implements ModInitializer {
	public static final String MOD_ID = "practice-mod";


	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.initialize();
		CustomEffects.onInitialize();
		ModPotions.onInitialize();
	}



}
