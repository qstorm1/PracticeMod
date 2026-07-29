package com.qstorm.block;

import com.qstorm.PracticeMod;
import com.qstorm.block.custom.MagicBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.util.math.intprovider.UniformIntProvider;

public class ModBlocks {
    public static final Block SOUL_SUMMONER = registerBlock("soul_summoner", AbstractBlock.Settings.create()
            .strength(6f).requiresTool().sounds(BlockSoundGroup.IRON));

    public static final Block SOUL_BLOCK = registerBlock("soul_block", AbstractBlock.Settings.create()
            .strength(4f).requiresTool().sounds(BlockSoundGroup.DRIPSTONE_BLOCK));

    public static final Block RAW_SOUL_ORE_BLOCK = registerExperienceBlock("raw_soul_ore_block",3,4,
            AbstractBlock.Settings.create().strength(4f).requiresTool().sounds(BlockSoundGroup.IRON));
    public static final Block RAW_DEEPSLATE_SOUL_ORE_BLOCK = registerExperienceBlock("raw_deepslate_soul_ore_block",4,5,
            AbstractBlock.Settings.create().strength(6f).requiresTool().sounds(BlockSoundGroup.DEEPSLATE));


    public static final Block MAGIC_BLOCK = registerMagicBlock("magic_block",AbstractBlock.Settings.create()
            .strength(5f).requiresTool().sounds(BlockSoundGroup.ANCIENT_DEBRIS)
    );


    private static void registerBlockItems(String name, Block block){
        RegistryKey<Item> key=RegistryKey.of(RegistryKeys.ITEM, Identifier.of(PracticeMod.MOD_ID,name));
        Registry.register(Registries.ITEM, key,
                new BlockItem(block,new Item.Settings().registryKey(key)));
    }


    private static Block registerMagicBlock(String name, AbstractBlock.Settings blockSettings){
        RegistryKey<Block> key = RegistryKey.of(RegistryKeys.BLOCK,Identifier.of(PracticeMod.MOD_ID,name));

        MagicBlock block = new MagicBlock(blockSettings.registryKey(key));
        registerBlockItems(name,block);
        return Registry.register(Registries.BLOCK,
                key,
                block);
    }

    private static Block registerBlock(String name, AbstractBlock.Settings blockSettings){
        RegistryKey<Block> key = RegistryKey.of(RegistryKeys.BLOCK,Identifier.of(PracticeMod.MOD_ID,name));

        Block block = new Block(blockSettings.registryKey(key));
        registerBlockItems(name,block);
        return Registry.register(Registries.BLOCK,
                key,
                block);
    }

    private static Block registerExperienceBlock(String name, int xpMin,int xpMax, AbstractBlock.Settings blockSettings){
        RegistryKey<Block> key = RegistryKey.of(RegistryKeys.BLOCK,Identifier.of(PracticeMod.MOD_ID,name));

        Block block = new ExperienceDroppingBlock(UniformIntProvider.create(xpMin,xpMax), blockSettings.registryKey(key));
        registerBlockItems(name,block);
        return Registry.register(Registries.BLOCK,
                key,
                block);
    }


    public static void registerModdedBlocks(){
        PracticeMod.LOGGER.info("registering blocks for "+PracticeMod.MOD_ID);


    }
}
