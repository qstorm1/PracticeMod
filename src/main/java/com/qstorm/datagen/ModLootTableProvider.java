package com.qstorm.datagen;


public class ModLootTableProvider {
//    public ModLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
//        super(dataOutput, registryLookup);
//    }
//
//    @Override
//    public void generate() {
//        addDrop(ModBlocks.MAGIC_BLOCK);
//        addDrop(ModBlocks.SOUL_BLOCK);
//        addDrop(ModBlocks.RAW_SOUL_ORE_BLOCK, oreDrops(ModBlocks.RAW_SOUL_ORE_BLOCK, ModItems.RAW_SOUL_ORE));
//        addDrop(ModBlocks.RAW_DEEPSLATE_SOUL_ORE_BLOCK, oreDrops(ModBlocks.RAW_DEEPSLATE_SOUL_ORE_BLOCK, ModItems.RAW_SOUL_ORE));
//        addDrop(ModBlocks.SOUL_SUMMONER);
//
//        addDrop(ModBlocks.COMPRESSED_COBBLESTONE);
//        addDrop(ModBlocks.VERY_COMPRESSED_COBBLESTONE);
//        addDrop(ModBlocks.EXTREMELY_COMPRESSED_COBBLESTONE);
//        addDrop(ModBlocks.SUPER_COMPRESSED_COBBLESTONE);
//        addDrop(ModBlocks.ULTRA_COMPRESSED_COBBLESTONE);
//        addDrop(ModBlocks.INCREDIBLY_COMPRESSED_COBBLESTONE);
//        addDrop(ModBlocks.RADICALLY_COMPRESSED_COBBLESTONE);
//
//
//        addDrop(ModBlocks.SOUL_BLOCK_FENCE);
//        addDrop(ModBlocks.SOUL_BLOCK_FENCE_GATE);
//        addDrop(ModBlocks.SOUL_BLOCK_WALL);
//        addDrop(ModBlocks.SOUL_BLOCK_BUTTON);
//        addDrop(ModBlocks.SOUL_BLOCK_STAIR);
//        //2 blocks in one sometimes for slab and door so we have 2 outputs
//        addDrop(ModBlocks.SOUL_BLOCK_SLAB,slabDrops(ModBlocks.SOUL_BLOCK_SLAB));
//        addDrop(ModBlocks.SOUL_BLOCK_DOOR,doorDrops(ModBlocks.SOUL_BLOCK_DOOR));
//        addDrop(ModBlocks.SOUL_BLOCK_TRAP_DOOR);
//        addDrop(ModBlocks.SOUL_BLOCK_PRESSURE_PLATE);
//
//    }
//
//    //Im not even gonna try to understand this
//    public LootTable.Builder oreWithSilkTouch(Block drop,Item item,int min, int max) {
//        RegistryWrapper.Impl<Enchantment> impl = this.registries.getOrThrow(RegistryKeys.ENCHANTMENT);
//        return this.dropsWithSilkTouch(
//                drop,
//                (LootPoolEntry.Builder<?>)this.applyExplosionDecay(
//                        drop,
//                        ItemEntry.builder(item)
//                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(min, max)))
//                                .apply(ApplyBonusLootFunction.oreDrops(impl.getOrThrow(Enchantments.FORTUNE)))
//                )
//        );
//    }
}
