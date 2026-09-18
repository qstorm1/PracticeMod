package com.qstorm.mixin;

import com.qstorm.key.HandleKeybinds;
import com.qstorm.key.InitializeBindings;
import com.qstorm.powers.Ability;
import com.qstorm.powers.cursedtechnique.Sorcery;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MouseHandler.class)
public class Scroll {
    @Inject(method = "onScroll",at=@At("HEAD"))
    private void onScroll1(long windowPointer, double xOffset, double yOffset, CallbackInfo ci){
        if(yOffset!=0&&Minecraft.getInstance().screen==null){
            Minecraft.getInstance().execute(()->{
                ClientPlayNetworking.send(new HandleKeybinds.HandleScroll(yOffset));
            });
        }
    }

    @Inject(method = "onScroll",at= @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Inventory;setSelectedSlot(I)V"
    ),cancellable = true)
    private void lockHotbar(long windowPointer, double xOffset, double yOffset, CallbackInfo ci){
        for(Ability ab: Sorcery.sorcerers.get(Minecraft.getInstance().player.getUUID()).abilities){
            if(ab.locksHotBarScroll&&ab.isScroll){
                ci.cancel();
            }
        }
    }
}
