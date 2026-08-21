package com.qstorm;


import com.qstorm.effects.CustomEffects;
import com.qstorm.item.ModItems;
import com.qstorm.item.armor.LaserEyes;
import com.qstorm.key.HandleKeybinds;
import com.qstorm.key.InitializeBindings;
import com.qstorm.potion.ModPotions;
import com.qstorm.powers.ComboKey;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
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

		//if player joins/leaves checks
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			onPlayerJoin(handler.player);

		});
		ServerPlayConnectionEvents.DISCONNECT.register((handler,server) -> {
			onPlayerLeave(handler.player);
		});



		//handle serverside keybinding management
		HandleKeybinds.handle();

	}
	public static void test(){

	}

	public static void onPlayerJoin(Player player){
		InitializeBindings.onPlayerJoin(player);
		ComboKey.registerPlayer(player.getUUID());
	}

	public static void onPlayerLeave(Player player){
		InitializeBindings.onPlayerLeave(player);
		ComboKey.deregisterPlayer(player.getUUID());

	}






}
