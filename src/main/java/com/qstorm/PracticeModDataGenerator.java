package com.qstorm;


import com.qstorm.datagen.ModItemModelGenerator;
import com.qstorm.datagen.ModPotionModelGenerator;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class PracticeModDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		//a datapack/resource pack
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		//adds a constructor to the code that creates data packs/resource packs
		pack.addProvider(ModItemModelGenerator::new);


		//GENERATE IN GRADLE runDatagen

	}
}
