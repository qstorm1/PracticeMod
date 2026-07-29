package com.qstorm.item.custom;

import com.qstorm.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.consume.UseAction;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

import java.util.Map;
import java.util.function.Consumer;

public class Chizel extends Item {

    public static final Map<Block, Block> CHIZEL_MAP = Map.of(
            Blocks.STONE,Blocks.STONE_BRICKS,
            Blocks.END_STONE,Blocks.END_STONE_BRICKS,
            Blocks.DIAMOND_BLOCK, ModBlocks.SOUL_BLOCK
    );

    public Chizel(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        //the world the action is being done in
        World world = context.getWorld();
        //getBlockState requires inputing some position
        //the getBlockState gives a blockstate object that can be converted into an actual block with the getblock() command;
        Block clickedBlock= world.getBlockState(context.getBlockPos()).getBlock();
        if(CHIZEL_MAP.containsKey(clickedBlock)){
            //I think this is how it works
            //we want to run this on the server rather then the client
            //both client and server code go through this class when it is instantiated, so we need to make sure it only runs on server
            if(!world.isClient()){
                //i understand the rest but .getDefaultState() basically returns the default block
                world.setBlockState(context.getBlockPos(),CHIZEL_MAP.get(clickedBlock).getDefaultState());

                context.getStack().damage(1, (ServerWorld) world, (ServerPlayerEntity) context.getPlayer(),
                        item -> context.getPlayer().sendEquipmentBreakStatus(item, EquipmentSlot.MAINHAND));

                world.addBlockBreakParticles(context.getBlockPos(),world.getBlockState(context.getBlockPos()));
            }
        }




        return ActionResult.SUCCESS;
    }

    @Override
    public UseAction getUseAction(ItemStack stack){
        return UseAction.DRINK;
    }
}
