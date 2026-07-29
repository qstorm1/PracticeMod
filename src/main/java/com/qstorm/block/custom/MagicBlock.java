package com.qstorm.block.custom;

import com.qstorm.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Map;

public class MagicBlock extends Block {

    Map<Item, Item> translationChart = Map.of(
            ModItems.RAW_SOUL_ORE,ModItems.SOUL_ORE

    );

    public MagicBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if(entity instanceof ItemEntity itemEntity){
            if(translationChart.containsKey(itemEntity.getStack().getItem())){
                itemEntity.setStack(new ItemStack(translationChart.get(itemEntity.getStack().getItem()),itemEntity.getStack().getCount()));
            }
        }


        super.onSteppedOn(world, pos, state, entity);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {


        world.playSound(player,pos,SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK, SoundCategory.BLOCKS,2f,1f);
        //swings arm
        return ActionResult.SUCCESS;
    }



}
