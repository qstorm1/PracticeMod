package com.qstorm.item.armor;


import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.qstorm.PracticeMod;
import com.qstorm.effects.CustomEffects;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class LaserEyes extends Item {



    public LaserEyes(Properties properties) {
        super(properties);

    }


    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slot) {
        super.inventoryTick(stack,level,entity,slot);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        super.use(level, player, hand);
        if(!level.isClientSide()){
            shootLaser(player);

        }

        return InteractionResult.SUCCESS;
    }

    public static void shootLaser(Player player){
        int max=20;//max distance in blocks the laser can shoot
        boolean laserHit=false;

        //partialTick represents the position at the end of the vector
        Vec3 eyePosition = player.getEyePosition();
        Vec3 direction = player.getViewVector(1F);
        Vec3 endTarget = eyePosition.add(direction.scale(max));

        //make a raycast to the closest block
        //the 3 settings at the end tell when the ray should stop,
        //The block one says it should stop
        HitResult hitResult= player.level().clip(new ClipContext(
                eyePosition,
                endTarget,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                player
        ));

        if(hitResult.getType()== HitResult.Type.BLOCK){
            BlockHitResult blockHitResult = (BlockHitResult) hitResult;
            player.level().setBlockAndUpdate(blockHitResult.getBlockPos(),Blocks.REDSTONE_BLOCK.defaultBlockState());
        }
        else if(hitResult.getType()==HitResult.Type.ENTITY){
            EntityHitResult entityHitResult=(EntityHitResult) hitResult;
            if(player.level() instanceof ServerLevel) {
                entityHitResult.getEntity().hurtServer((ServerLevel) player.level(), new DamageSource(
                        player.level().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE)
                                .get(DamageTypes.EXPLOSION.identifier()).get()
                ), 5);
            }else{
                PracticeMod.LOGGER.info("error i think");
            }
        }


    }

    static {
        //because the client tick starts at the very beginning of when minecraft loads,
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(client.player==null){
                return;
            }
            if (client.options.keyAttack.isDown()){
                if(client.player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof LaserEyes ){
                    activateLaser(client.player);
                }
                else{
                    deactivateLaser(client.player);
                }

            }else{
                deactivateLaser(client.player);
            }

        });

    }

    public static void activateLaser(Player player){
        player.addEffect(new MobEffectInstance(CustomEffects.SOUL_EFFECT,5));
    }

    public static void deactivateLaser(Player player){
        player.removeAllEffects();
    }




}
