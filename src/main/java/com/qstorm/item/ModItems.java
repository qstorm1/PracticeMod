package com.qstorm.item;

import com.qstorm.PracticeMod;
import com.qstorm.effects.CustomEffects;
import com.qstorm.item.tools.SoulPickaxe;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ClearAllStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ConsumeEffect;

import java.util.function.Function;

public class ModItems {

    //States how the food is consumed as well as what effects the food has
    //basically treats component as something more than food
    public static final Consumable IN_SOUL_STATE_CONSUMABLE_COMPONENT = Consumable.builder()
            .animation(ItemUseAnimation.SPEAR)
            .onConsume(ClearAllStatusEffectsConsumeEffect.INSTANCE)
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(CustomEffects.SOUL_EFFECT,9600)))
            .build();
    //this is the normal settings when you create a food item which represents it's food related properties like
    //saturation, and nutrition
    public static final FoodProperties IN_SOUL_STATE_COMPONENT = new FoodProperties.Builder()
            .alwaysEdible()
            .build();

    public static final Item SOUL_ORE = register("soul_ore",Item::new,
            new Item.Properties().food(IN_SOUL_STATE_COMPONENT,IN_SOUL_STATE_CONSUMABLE_COMPONENT));

    public static final Item BLAZE_SWORD = register("blaze_sword", Item::new,
            new Item.Properties().sword(SoulPickaxe.BLAZE_TOOL_MATERIAL,10f,-2.4f));



    //A function is a class that takes an input and returns an output where <Input class, Output class>
    //In a function interface you can input a lambda that represents what the function does
    //Registries.ITEM is a resource key that basically points to the item registry
    //BuiltInRegistries.ITEM is the registry that holds the items itself
    public static <T extends Item> T register(String name, Function<Item.Properties,T> factory,Item.Properties properties){
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,name));

        return Registry.register(BuiltInRegistries.ITEM,key,factory.apply(properties.setId(key)));
    }

    public static void initialize(){
        //needs to be ran for all static methods in this class to load

        //modifies the CreativeModeTabs.INGREDIENTS entry as an event by registering ModItems.SOUL_ORE
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS)
                .register(entries -> {
                    entries.accept(ModItems.SOUL_ORE);
                    entries.accept(ModItems.BLAZE_SWORD);
                });
    }
}
