package com.qstorm.item;

import com.qstorm.PracticeMod;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class ModItems {
    public static Item SOUL_ORE = register("soul_ore",Item::new,new Item.Properties());

    //A function is a class that takes an input and returns an output where <Input class, Output class>
    //In a function interface you can input a llambda that represents what the function does
    public static <T extends Item> T register(String name, Function<Item.Properties,T> factory,Item.Properties properties){
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,name));

        return Registry.register(BuiltInRegistries.ITEM,key,factory.apply(properties.setId(key)));
    }

    public static void initialize(){
        //needs to be ran for all static methods in this class to load

        //modifies the CreativeModeTabs.INGREDIENTS entry as an event by registering ModItems.SOUL_ORE
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS)
                .register(entries -> entries.accept(ModItems.SOUL_ORE));
    }
}
