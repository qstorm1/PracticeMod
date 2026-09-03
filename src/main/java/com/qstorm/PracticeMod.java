package com.qstorm;


import com.qstorm.commands.JJKCommands;
import com.qstorm.effects.CustomEffects;
import com.qstorm.item.ModItems;
import com.qstorm.key.HandleKeybinds;
import com.qstorm.key.InitializeBindings;
import com.qstorm.potion.ModPotions;
import com.qstorm.powers.ComboKey;
import com.qstorm.powers.PlayerInfo;
import com.qstorm.powers.cursedtechnique.Sorcery;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PracticeMod implements ModInitializer {
	public static final String MOD_ID = "practice-mod";


	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);



	public static final Identifier USES_DATA = Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"use_data");
	public static final Identifier RESET = Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"reset");



	@Override
	public void onInitialize() {

		//all actions that reset happen after the data is used
		ServerTickEvents.END_SERVER_TICK.addPhaseOrdering(USES_DATA,RESET);


		ModItems.initialize();
		CustomEffects.onInitialize();
		ModPotions.onInitialize();


		ComboKey.init();

		JJKCommands.init();

		//if player joins/leaves checks
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			onPlayerJoin(handler.player);
		});
		ServerPlayConnectionEvents.DISCONNECT.register((handler,server) -> {
			onPlayerLeave(handler.player);
		});




		//handle serverside keybinding management
		HandleKeybinds.handle();

		//init (mostly the keybind stuff) for sorcery users
		Sorcery.init();

	}
	public static void test(){

	}







	public static void onPlayerJoin(Player player){
		InitializeBindings.onPlayerJoin(player);
		ComboKey.registerPlayer(player.getUUID());
		PlayerInfo.initNewPlayer(player.getUUID());
	}

	public static void onPlayerLeave(Player player){
		PracticeMod.LOGGER.info(player.getDisplayName().getString() + " has left");

		InitializeBindings.onPlayerLeave(player);
		ComboKey.deregisterPlayer(player.getUUID());


	}






}
