package com.qstorm.datagen;

import com.qstorm.block.ModBlocks;
import com.qstorm.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.data.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registries, RecipeExporter exporter) {
        List<ItemConvertible> SMELT_TO_SOUL_ORE= List.of(ModItems.RAW_SOUL_ORE);


        return new RecipeGenerator(registries,exporter) {
            @Override
            public void generate() {
                offerSmelting(SMELT_TO_SOUL_ORE, RecipeCategory.MISC,ModItems.SOUL_ORE,5f,400,"soul_ore_smelting");
                offerBlasting(SMELT_TO_SOUL_ORE, RecipeCategory.MISC,ModItems.SOUL_ORE,5f,100,"soul_ore_blasting");

                offerReversibleCompactingRecipes(RecipeCategory.BUILDING_BLOCKS,ModItems.SOUL_ORE,RecipeCategory.DECORATIONS,ModBlocks.SOUL_BLOCK);

                offerReversibleCompactingRecipes(RecipeCategory.BUILDING_BLOCKS,Blocks.COBBLESTONE,RecipeCategory.BUILDING_BLOCKS,ModBlocks.COMPRESSED_COBBLESTONE);
                offerReversibleCompactingRecipes(RecipeCategory.BUILDING_BLOCKS,ModBlocks.VERY_COMPRESSED_COBBLESTONE,RecipeCategory.BUILDING_BLOCKS,ModBlocks.EXTREMELY_COMPRESSED_COBBLESTONE);
                offerReversibleCompactingRecipes(RecipeCategory.BUILDING_BLOCKS,ModBlocks.SUPER_COMPRESSED_COBBLESTONE,RecipeCategory.BUILDING_BLOCKS,ModBlocks.ULTRA_COMPRESSED_COBBLESTONE);
                offerReversibleCompactingRecipes(RecipeCategory.BUILDING_BLOCKS,ModBlocks.INCREDIBLY_COMPRESSED_COBBLESTONE,RecipeCategory.BUILDING_BLOCKS,ModBlocks.RADICALLY_COMPRESSED_COBBLESTONE);

//im too lazy to do this rn but I have to do this manually because the recipe json files are already created for the other blocks
//                offerReversibleCompactingRecipes(RecipeCategory.BUILDING_BLOCKS,ModBlocks.COMPRESSED_COBBLESTONE,RecipeCategory.BUILDING_BLOCKS,ModBlocks.VERY_COMPRESSED_COBBLESTONE);
//                offerReversibleCompactingRecipes(RecipeCategory.BUILDING_BLOCKS,ModBlocks.EXTREMELY_COMPRESSED_COBBLESTONE,RecipeCategory.BUILDING_BLOCKS,ModBlocks.SUPER_COMPRESSED_COBBLESTONE);
//                offerReversibleCompactingRecipes(RecipeCategory.BUILDING_BLOCKS,ModBlocks.ULTRA_COMPRESSED_COBBLESTONE,RecipeCategory.BUILDING_BLOCKS,ModBlocks.INCREDIBLY_COMPRESSED_COBBLESTONE);
//


                //I understand everything except this cause the documentation is so bad bruh
                ShapedRecipeJsonBuilder.create(new RegistryEntryLookup<Item>() {
                    @Override
                    public Optional<RegistryEntry.Reference<Item>> getOptional(RegistryKey<Item> key) {
                        return Optional.empty();
                    }

                    @Override
                    public Optional<RegistryEntryList.Named<Item>> getOptional(TagKey<Item> tag) {
                        return Optional.empty();
                    }
                },RecipeCategory.MISC,ModBlocks.SOUL_SUMMONER)
                        .pattern("#F#")
                        .pattern("DEC")
                        .pattern("#B#")
                        .input('#', ModBlocks.SOUL_BLOCK)
                        .input('F', Blocks.NETHERITE_BLOCK)
                        .input('D', Blocks.HEAVY_CORE)
                        .input('C', Blocks.CONDUIT)
                        .input('B', Items.ENCHANTED_GOLDEN_APPLE)
                        .input('E', Items.TOTEM_OF_UNDYING)
                        .criterion(hasItem(ModItems.SOUL_ORE),conditionsFromItem(ModItems.SOUL_ORE))
                        .offerTo(exporter);
            }
        };
    }


    @Override
    public String getName() {
        return "";
    }
}
