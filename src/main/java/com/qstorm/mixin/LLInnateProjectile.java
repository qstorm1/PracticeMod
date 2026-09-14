package com.qstorm.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.qstorm.powers.cursedtechnique.Sorcery;
import com.qstorm.powers.cursedtechnique.limitless.Limitless;
import com.qstorm.powers.cursedtechnique.limitless.power.LimitlessInnate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Predicate;

@Mixin(ProjectileUtil.class)
public class LLInnateProjectile {


    @Inject(
        method = "getEntityHitResult(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;F)Lnet/minecraft/world/phys/EntityHitResult;",
        at=@At(value="RETURN",ordinal=1),cancellable = true)
    private static void getEntityHitResult(Level level, Entity projectile, Vec3 startVec, Vec3 endVec, AABB boundingBox, Predicate<Entity> filter, float inflationAmount,
                                       CallbackInfoReturnable<EntityHitResult> cir, @Local(ordinal = 0, argsOnly = true) Entity entity){
        if(Sorcery.sorcerers.get(entity.getUUID()) != null){
            //if the target is a sorcerer
            Sorcery.sorcerers.get(entity.getUUID()).abilities.forEach((ability -> {
                if (ability instanceof LimitlessInnate li && li.isTicked) {
                    cir.setReturnValue(null);
//                    if(!projectile.getTags().contains(LimitlessInnate.TAG))
//                        projectile.addTag(LimitlessInnate.TAG);
                }
            }));

        }

    }


}
