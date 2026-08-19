package com.qstorm;


import com.qstorm.effects.CustomEffects;
import com.qstorm.item.ModItems;
import com.qstorm.item.armor.LaserEyes;
import com.qstorm.key.HandleKeybinds;
import com.qstorm.potion.ModPotions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.EquipmentSlot;
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

		//configure a client to server payload
		//packets have two types of "phases", play and configure
		//playC2S() happens during gameplay while configureC2S() happens during initial connection
		PayloadTypeRegistry.playC2S().register(HandleKeybinds.LaserKeyServer.TYPE,HandleKeybinds.LaserKeyServer.CODEC);

		//tells the server that if it recieves packet data of the given type, run the following
		ServerPlayNetworking.registerGlobalReceiver(HandleKeybinds.LaserKeyServer.TYPE, (payload, context) -> {
			//says to run it on server
			context.server().execute(() -> {
				//if the player is wearing a laser eye
				if(context.player().getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof LaserEyes){
					LaserEyes.shootLaser(context.player());
				}
			});
        });

	}
	public static void test(){

	}






}
