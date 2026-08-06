package com.qstorm.item.armor;


import com.qstorm.PracticeMod;
import com.qstorm.effects.CustomEffects;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LaserEyes extends Item {



    public LaserEyes(Properties properties) {
        super(properties);

    }


    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slot) {
        super.inventoryTick(stack,level,entity,slot);
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
