package com.qstorm.block.custom;

import com.qstorm.item.ModItems;
import net.minecraft.world.level.block.Block;

import java.util.Map;

public class MagicBlock extends Block {
    public MagicBlock(Properties properties) {
        super(properties);
    }

//    Map<Item, Item> translationChart = Map.of(
//            ModItems.RAW_SOUL_ORE,ModItems.SOUL_ORE
//
//    );
//
//    public MagicBlock(Settings settings) {
//        super(settings);
//    }
//
//    @Override
//    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
//        if(entity instanceof ItemEntity itemEntity){
//            if(translationChart.containsKey(itemEntity.getStack().getItem())){
//                itemEntity.setStack(new ItemStack(translationChart.get(itemEntity.getStack().getItem()),itemEntity.getStack().getCount()));
//            }
//        }
//
//
//        super.onSteppedOn(world, pos, state, entity);
//    }
//
//    @Override
//    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
//
//
//        world.playSound(player,pos,SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK, SoundCategory.BLOCKS,2f,1f);
//        //swings arm
//        return ActionResult.SUCCESS;
//    }



}
