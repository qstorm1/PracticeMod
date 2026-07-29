package com.qstorm.block;

import com.qstorm.PracticeMod;
import com.qstorm.block.custom.MagicBlock;
import net.minecraft.block.*;
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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ModBlocks {
    public static final Block SOUL_SUMMONER = registerBlock("soul_summoner",Block::new, AbstractBlock.Settings.create()
            .strength(6f).requiresTool().sounds(BlockSoundGroup.IRON));

    public static final Block SOUL_BLOCK = registerBlock("soul_block", Block::new,AbstractBlock.Settings.create()
            .strength(4f).requiresTool().sounds(BlockSoundGroup.DRIPSTONE_BLOCK));


    public static final Block SOUL_BLOCK_STAIR = registerBlock("soul_block_stair",
            settings -> new StairsBlock(ModBlocks.SOUL_BLOCK.getDefaultState(),settings),
            AbstractBlock.Settings.create().strength(4f).requiresTool().sounds(BlockSoundGroup.DRIPSTONE_BLOCK));

    public static final Block SOUL_BLOCK_SLAB = registerBlock("soul_block_slab",
            SlabBlock::new,
            AbstractBlock.Settings.create().strength(4f).requiresTool().sounds(BlockSoundGroup.DRIPSTONE_BLOCK));


    public static final Block SOUL_BLOCK_BUTTON = registerBlock("soul_block_button",
            settings -> new ButtonBlock(BlockSetType.OAK,5,settings),
            AbstractBlock.Settings.create().strength(4f).requiresTool().sounds(BlockSoundGroup.DRIPSTONE_BLOCK));

    public static final Block SOUL_BLOCK_PRESSURE_PLATE = registerBlock("soul_block_pressure_plate",
            settings -> new PressurePlateBlock(BlockSetType.IRON,settings),
            AbstractBlock.Settings.create().strength(4f).requiresTool().sounds(BlockSoundGroup.DRIPSTONE_BLOCK));


    public static final Block SOUL_BLOCK_FENCE = registerBlock("soul_block_fence",
            FenceBlock::new,
            AbstractBlock.Settings.create().strength(4f).requiresTool().sounds(BlockSoundGroup.DRIPSTONE_BLOCK));

    public static final Block SOUL_BLOCK_FENCE_GATE = registerBlock("soul_block_fence_gate",
            settings -> new FenceGateBlock(WoodType.ACACIA,settings),
            AbstractBlock.Settings.create().strength(4f).requiresTool().sounds(BlockSoundGroup.DRIPSTONE_BLOCK));

    public static final Block SOUL_BLOCK_WALL = registerBlock("soul_block_wall",
            WallBlock::new,
            AbstractBlock.Settings.create().strength(4f).requiresTool().sounds(BlockSoundGroup.DRIPSTONE_BLOCK));

//call non-opaque
    public static final Block SOUL_BLOCK_DOOR = registerBlock("soul_block_door",
            settings -> new DoorBlock(BlockSetType.IRON,settings),
            AbstractBlock.Settings.create().strength(4f).requiresTool().sounds(BlockSoundGroup.DRIPSTONE_BLOCK));

    public static final Block SOUL_BLOCK_TRAP_DOOR = registerBlock("soul_block_trap_door",
            settings -> new TrapdoorBlock(BlockSetType.IRON,settings),
            AbstractBlock.Settings.create().strength(4f).requiresTool().sounds(BlockSoundGroup.DRIPSTONE_BLOCK));



    public static final Block RAW_SOUL_ORE_BLOCK = registerBlock("raw_soul_ore_block",
            settings -> new ExperienceDroppingBlock(UniformIntProvider.create(3,4),settings),
            AbstractBlock.Settings.create().strength(4f).requiresTool().sounds(BlockSoundGroup.IRON));

    public static final Block RAW_DEEPSLATE_SOUL_ORE_BLOCK = registerBlock(
            "raw_deepslate_soul_ore_block",
            settings -> new ExperienceDroppingBlock(UniformIntProvider.create(5,6),settings),
            AbstractBlock.Settings.create().strength(6f).requiresTool().sounds(BlockSoundGroup.DEEPSLATE));



    public static final Block MAGIC_BLOCK = registerBlock("magic_block",MagicBlock::new,AbstractBlock.Settings.create()
            .strength(5f).requiresTool().sounds(BlockSoundGroup.ANCIENT_DEBRIS)
    );


    private static void registerBlockItems(String name, Block block){
        RegistryKey<Item> key=RegistryKey.of(RegistryKeys.ITEM, Identifier.of(PracticeMod.MOD_ID,name));
        Registry.register(Registries.ITEM, key,
                new BlockItem(block,new Item.Settings().registryKey(key)));
    }


//    /**
//     * Use when registering a normal Block
//     * @param name name of an object
//     * @param settings settings for the object
//     * @return the registered block
//     */
//    private static Block registerBlock(String name,AbstractBlock.Settings settings){
//        RegistryKey<Block> key = RegistryKey.of(RegistryKeys.BLOCK,Identifier.of(PracticeMod.MOD_ID,name));
//        settings.registryKey(key);
//        Block block = new Block(settings);
//        registerBlockItems(name,block);
//        return Registry.register(Registries.BLOCK,
//                key,
//                block);
//    }




    //AI GENERATED
    //AI because IDK what the Function class is and I was trying to do this with generics and Constructor things and it just wasn't working
    //if I ever learn the Function class, I'll remake this code
    public static <T extends Block> T registerBlock(String name, Function<AbstractBlock.Settings, T> factory, AbstractBlock.Settings settings) {
        // 1. Create the RegistryKey
        RegistryKey<Block> key = RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(PracticeMod.MOD_ID, name));

        // 2. Assign the RegistryKey to the settings (Required in 1.21+ for loot tables/models)
        settings.registryKey(key);

        // 3. Create the block instance using the factory
        T block = factory.apply(settings);

        // 4. Register the corresponding BlockItem (your existing helper)
        registerBlockItems(name, block);

        // 5. Register and return the block
        return Registry.register(Registries.BLOCK, key, block);
    }

//    /**
//     * Used when child class has the same number of constructor parameters as Block constructor
//     * @param name the name of the block
//     * @param settings the settings for the block
//     * @param classT the class that the block is in
//     * @return the registered block
//     * @param <T> the type of the class the block is in
//     */
//    private static<T extends Block> Block registerBlock(String name, AbstractBlock.Settings settings,Class<T> classT){
//        RegistryKey<Block> key = RegistryKey.of(RegistryKeys.BLOCK,Identifier.of(PracticeMod.MOD_ID,name));
//        settings.registryKey(key);
//
//        T block = null;
//        try {
//            block = classT.getConstructor(AbstractBlock.Settings.class).newInstance(settings);
//        } catch (Exception e) {
//            PracticeMod.LOGGER.info("ERROR WITH GENERATING CLASS");
//        }
//
//        registerBlockItems(name,block);
//        return Registry.register(Registries.BLOCK,
//                key,
//                block);
//    }

    public static void registerModdedBlocks(){
        PracticeMod.LOGGER.info("registering blocks for "+PracticeMod.MOD_ID);


    }
}
