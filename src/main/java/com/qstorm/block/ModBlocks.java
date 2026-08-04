package com.qstorm.block;

import com.qstorm.PracticeMod;
import com.qstorm.block.custom.MagicBlock;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;

import java.util.function.Function;

public class ModBlocks {
    //the mass of a piece of cobble is treated as 1kg

    //9 cobble
    public static final Block COMPRESSED_COBBLESTONE = registerBlock("compressed_cobblestone",Block::new,AbstractBlock.Settings.create()
            .strength(4f,12f).requiresTool());

    //81 cobble
    public static final Block VERY_COMPRESSED_COBBLESTONE = registerBlock("very_compressed_cobblestone",Block::new,AbstractBlock.Settings.create()
            .strength(8f,24f).requiresTool());

    //729
    public static final Block EXTREMELY_COMPRESSED_COBBLESTONE = registerBlock("extremely_compressed_cobblestone",Block::new,AbstractBlock.Settings.create()
            .strength(16f,48f).requiresTool());

    //6561
    public static final Block SUPER_COMPRESSED_COBBLESTONE = registerBlock("super_compressed_cobblestone",Block::new,AbstractBlock.Settings.create()
            .strength(64f,96f).requiresTool());

    //59049
    public static final Block ULTRA_COMPRESSED_COBBLESTONE = registerBlock("ultra_compressed_cobblestone",Block::new,AbstractBlock.Settings.create()
            .strength(256f,192f).requiresTool());

    //531441
    public static final Block INCREDIBLY_COMPRESSED_COBBLESTONE = registerBlock("incredibly_compressed_cobblestone",Block::new,AbstractBlock.Settings.create()
            .strength(512f,384f).requiresTool());

    //4782969
    public static final Block RADICALLY_COMPRESSED_COBBLESTONE = registerBlock("radically_compressed_cobblestone",Block::new,AbstractBlock.Settings.create()
            .strength(1024f,768f).requiresTool());

    //43046721
    //387420489
    //3486784401
    //3.13810596e10 Has A gravitational Radius
    //2.82429536e11
    //2.54186582e12
    //2.28767924e13
    //2.05891132e14
    //1.85302019e15
    //1.66771817e16
    //1.50094635e17
    //1.35085172e18
    //1.21576655e19
    //1.0941899e20
    //9.8477091e20
    //8.86293819e21
    //7.97664437e22
    //7.17897993e23
    //6.46108194e24
    //5.81497375e25 incomprehensibility
    //5.23347638e26 black hole
    //4.71012874e27
    //4.23911587e28
    //3.81520428e29 super massive black hole


    //gravitational fields begin to form after 10 compresses


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


    public static void registerModdedBlocks(){
        PracticeMod.LOGGER.info("registering blocks for "+PracticeMod.MOD_ID);


    }
}
