package com.qstorm.mixin;

import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractArrow.class)
public class LLInnateArrowHandle {
    @Inject(
            method = "stepMoveAndHit",
            at=@At("HEAD")
    )
    private void stepMoveAndHit(BlockHitResult blockHitResult, CallbackInfo ci){

    }
}
