package com.qstorm.item.armor;


import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.qstorm.PracticeMod;
import com.qstorm.effects.CustomEffects;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.particles.*;
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
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.*;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
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


        //partialTick represents the position at the end of the vector
        Vec3 eyePosition = player.getEyePosition();
        Vec3 direction = player.getViewVector(1F);
        //new array representing max distance
        Vec3 endTarget = eyePosition.add(direction.scale(max));

        //make a raycast to the closest block
        //the 3 settings at the end tell when the ray should stop,
        //The block one says it should stop
        BlockHitResult hitResult= player.level().clip(new ClipContext(
                eyePosition,
                endTarget,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                player
        ));
        double currentMinBlock = max;
        if(hitResult.getType()== HitResult.Type.BLOCK){
            currentMinBlock=hitResult.getLocation().distanceTo(eyePosition);
        }


        //the 5th element represents the broad phase search, a narrow search is then done after using the vectors
        //I really wish this had documentation bruh (aka i wish my documentation freaking worked)
        EntityHitResult entityHitResult = getRaycastedHitresult(
                player.level(),
                player,
                eyePosition,
                eyePosition.add(direction.scale(currentMinBlock)),
                player.getBoundingBox().expandTowards(direction.scale(currentMinBlock)).inflate(1.0),
                entity -> entity != player,
                0F

        );


        if(entityHitResult==null) {

            PracticeMod.LOGGER.info(player.getBoundingBox().expandTowards(direction.scale(currentMinBlock)).inflate(1.0).toString());
        }else{
            PracticeMod.LOGGER.info("HIT");
        }






        if(entityHitResult!=null&&entityHitResult.getType()== HitResult.Type.ENTITY){
            currentMinBlock=entityHitResult.getEntity().getPosition(1F).distanceTo(eyePosition);




            entityHitResult.getEntity().hurtServer((ServerLevel) player.level(), new DamageSource(
                        player.level().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE)
                                .get(DamageTypes.EXPLOSION.identifier()).get()
                ), 1);
        }else if(currentMinBlock!=max){
            //if hit a block
            player.level().setBlockAndUpdate(hitResult.getBlockPos(),Blocks.REDSTONE_BLOCK.defaultBlockState());
        }
        //do things





        PracticeMod.LOGGER.info("min: "+ currentMinBlock);
        DustParticleOptions redParticle = new DustParticleOptions(16711680,1F);

        Vec3 targetPosition;
        double step=0.1;
        for(double i = 0; i<currentMinBlock;i+=step) {
            targetPosition = direction.scale(i);
            ((ServerLevel) player.level()).sendParticles(
                    redParticle,
                    targetPosition.x, targetPosition.y, targetPosition.z, 1,
                    0.0, 0.0, 0.0, 0.0
            );
        }




    }

    public static EntityHitResult getRaycastedHitresult(Level level, Entity projectile,Vec3 startVec,Vec3 endVec,AABB boundingBox, Predicate<Entity> filter,float inflationAmount){
        double d = Double.MAX_VALUE;
        Optional<Vec3> optional = Optional.empty();
        Entity entity = null;

        for(Entity entity2 : level.getEntities(projectile, boundingBox, filter)) {
            PracticeMod.LOGGER.info("entities exist");
            AABB aABB = entity2.getBoundingBox().inflate((double)inflationAmount);
            Optional<Vec3> optional2 = aABB.clip(startVec, endVec);
            PracticeMod.LOGGER.info("clip result"+optional2.toString()+"\n vStart " + startVec +"vEnd "+endVec);
            if (optional2.isPresent()) {
                double e = startVec.distanceToSqr((Vec3)optional2.get());
                if (e < d) {
                    entity = entity2;
                    d = e;
                    optional = optional2;
                }
            }
        }

        if (entity == null) {
            return null;
        } else {
            return new EntityHitResult(entity, (Vec3)optional.get());
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
