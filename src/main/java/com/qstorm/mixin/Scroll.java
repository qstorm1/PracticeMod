package com.qstorm.mixin;

import com.qstorm.key.HandleKeybinds;
import com.qstorm.key.InitializeBindings;
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
    private void onScroll(long windowPointer, double xOffset, double yOffset, CallbackInfo ci){
        if(yOffset!=0&&Minecraft.getInstance().screen==null){
            Minecraft.getInstance().execute(()->{
                ClientPlayNetworking.send(new HandleKeybinds.HandleScroll(yOffset));
            });
        }
    }
}
