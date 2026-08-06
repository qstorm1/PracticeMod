package com.qstorm.item.tools;

import com.qstorm.PracticeMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

public class SoulPickaxe {

    //makes a new tag called repairs_blaze_item
    public static final TagKey<Item> REPAIRS_BLAZE_ITEM = TagKey.create(BuiltInRegistries.ITEM.key(), Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"repairs_blaze_items"));


    //this certifies what type of material the tool is made of
    //the block tags section represents the name of the data folder that describes if a block can be mined by a pickaxe
    //the 2nd value is durability, the third is mining speed, the fourth is attack damage increase
    //the j idk something to do with enchanting
    //and the repair items represents a list of items that can repair this tool
    public static final ToolMaterial BLAZE_TOOL_MATERIAL = new ToolMaterial(
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            999999,
            11f,
            5f,
            22,
            REPAIRS_BLAZE_ITEM
    );


}
