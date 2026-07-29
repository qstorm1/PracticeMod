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


                            }))

                    .build());

    public static void registerGroups(){
        PracticeMod.LOGGER.info("registering Item groups for "+PracticeMod.MOD_ID);
    }
}
