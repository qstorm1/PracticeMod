package com.qstorm.datagen;

import com.qstorm.block.ModBlocks;
import com.qstorm.item.ModItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.Models;

public class ModModelProvider extends FabricModelProvider {

    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        BlockStateModelGenerator.BlockTexturePool soulBlockPool = blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.SOUL_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.SOUL_SUMMONER);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.RAW_SOUL_ORE_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.RAW_DEEPSLATE_SOUL_ORE_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.MAGIC_BLOCK);

        soulBlockPool.stairs(ModBlocks.SOUL_BLOCK_STAIR);
        soulBlockPool.slab(ModBlocks.SOUL_BLOCK_SLAB);
        soulBlockPool.button(ModBlocks.SOUL_BLOCK_BUTTON);
        soulBlockPool.pressurePlate(ModBlocks.SOUL_BLOCK_PRESSURE_PLATE);
        soulBlockPool.fence(ModBlocks.SOUL_BLOCK_FENCE);
        soulBlockPool.fenceGate(ModBlocks.SOUL_BLOCK_FENCE_GATE);
        soulBlockPool.wall(ModBlocks.SOUL_BLOCK_WALL);

        blockStateModelGenerator.registerDoor(ModBlocks.SOUL_BLOCK_DOOR);
        blockStateModelGenerator.registerTrapdoor(ModBlocks.SOUL_BLOCK_TRAP_DOOR);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.CHISEl, Models.HANDHELD);
        itemModelGenerator.register(ModItems.RAW_SOUL_ORE, Models.GENERATED);
        itemModelGenerator.register(ModItems.SOUL_ORE, Models.GENERATED);
    }
}
