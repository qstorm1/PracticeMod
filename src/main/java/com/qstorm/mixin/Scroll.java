package com.qstorm.mixin;

import com.qstorm.cca.sorceryStorage.SorceryInfoStorage;
import com.qstorm.key.HandleKeybinds;
import com.qstorm.powers.Ability;
import com.qstorm.powers.cursedtechnique.Sorcery;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
        assert Minecraft.getInstance().player != null;
        if(SorceryInfoStorage.sorceryData.get(Minecraft.getInstance().player).getSorcerer()==null||
                SorceryInfoStorage.sorceryData.get(Minecraft.getInstance().player).getSorceryType().equals(Sorcery.SORCERER_TAG))
            return;
        for(Ability ab: SorceryInfoStorage.sorceryData.get(Minecraft.getInstance().player).getSorcerer().abilities){
            if(ab.locksHotBarScroll&&ab.isScroll){
                ci.cancel();
            }
        }
        if(SorceryInfoStorage.sorceryData.get(Minecraft.getInstance().player).getSorcerer().innate.locksHotBarScroll&&
                SorceryInfoStorage.sorceryData.get(Minecraft.getInstance().player).getSorcerer().innate.isScroll){
            ci.cancel();
        }
    }
}
