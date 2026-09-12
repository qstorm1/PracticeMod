package com.qstorm.mixin;

import com.qstorm.PracticeMod;
import com.qstorm.powers.cursedtechnique.limitless.power.LimitlessInnate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

/**
 * currently used for limitless innate technique
 */
@Mixin(Entity.class)
public abstract class EntityMovement {
//    @Shadow
//    public abstract Set<String> getTags();
//
//    @Inject(method = "move",at=@At("HEAD"))
//    public void move(MoverType type, Vec3 movement, CallbackInfo ci){
////        if(this.getTags().contains(LimitlessInnate.TAG)){
////            ci.cancel();
////        }
//    }
}
