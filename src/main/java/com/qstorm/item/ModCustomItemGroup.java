package com.qstorm.item;

import com.qstorm.PracticeMod;
import com.qstorm.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModCustomItemGroup {

    public static final ItemGroup JJK_Group = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(PracticeMod.MOD_ID,"jjk_group")
            , FabricItemGroup.builder().displayName(Text.translatable("itemgroup.practice-mod.jjk_group"))
                            .entries(((displayContext, entries) -> {
                                entries.add(ModItems.RAW_SOUL_ORE);
                                entries.add(ModItems.SOUL_ORE);
                                entries.add(ModBlocks.SOUL_BLOCK);
                                entries.add(ModBlocks.SOUL_SUMMONER);

                                entries.add(ModItems.CHISEl);
                                entries.add(ModBlocks.MAGIC_BLOCK);

                                entries.add(ModBlocks.RAW_SOUL_ORE_BLOCK);
                                entries.add(ModBlocks.RAW_DEEPSLATE_SOUL_ORE_BLOCK);


                                entries.add(ModBlocks.SOUL_BLOCK_FENCE);
                                entries.add(ModBlocks.SOUL_BLOCK_FENCE_GATE);
                                entries.add(ModBlocks.SOUL_BLOCK_WALL);
                                entries.add(ModBlocks.SOUL_BLOCK_STAIR);
                                entries.add(ModBlocks.SOUL_BLOCK_SLAB);
                                entries.add(ModBlocks.SOUL_BLOCK_PRESSURE_PLATE);
                                entries.add(ModBlocks.SOUL_BLOCK_BUTTON);
                                entries.add(ModBlocks.SOUL_BLOCK_TRAP_DOOR);
                                entries.add(ModBlocks.SOUL_BLOCK_DOOR);

                                entries.add(ModBlocks.COMPRESSED_COBBLESTONE);
                                entries.add(ModBlocks.VERY_COMPRESSED_COBBLESTONE);
                                entries.add(ModBlocks.EXTREMELY_COMPRESSED_COBBLESTONE);
                                entries.add(ModBlocks.SUPER_COMPRESSED_COBBLESTONE);
                                entries.add(ModBlocks.ULTRA_COMPRESSED_COBBLESTONE);
                                entries.add(ModBlocks.RADICALLY_COMPRESSED_COBBLESTONE);
                                entries.add(ModBlocks.INCREDIBLY_COMPRESSED_COBBLESTONE);



                            }))

                    .build());

    public static void registerGroups(){
        PracticeMod.LOGGER.info("registering Item groups for "+PracticeMod.MOD_ID);
    }
}
