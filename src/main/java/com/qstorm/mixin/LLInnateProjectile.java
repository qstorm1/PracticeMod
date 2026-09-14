package com.qstorm.mixin;

import com.qstorm.powers.cursedtechnique.limitless.Limitless;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Projectile.class)
public class LLInnateProjectile {

    @Inject(method="canHitEntity",at= @At("HEAD"),cancellable = true)
    public void canHitEntity(Entity target, CallbackInfoReturnable<Boolean> cir){
        if((Object)target instanceof Limitless ls &&ls.innateOn){
            ((Entity)(Object)this).setDeltaMovement(Vec3.ZERO);
            ((Entity)(Object)this).setNoGravity(true);
        }

    }


}
