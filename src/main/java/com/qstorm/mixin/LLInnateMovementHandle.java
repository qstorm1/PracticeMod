package com.qstorm.mixin;

import com.qstorm.powers.cursedtechnique.Sorcery;
import com.qstorm.powers.cursedtechnique.limitless.Limitless;
import com.qstorm.powers.cursedtechnique.limitless.power.LimitlessInnate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

@Mixin(Entity.class)
public abstract class LLInnateMovementHandle{
    //TODO: make the explosion line things in the 16x16 thing stop at the start of limitless domains

    //Used to calculate things that don't tick their velocity (ex: rockets)
    @Unique
    private Vec3 prevVelocity;

    @Shadow
    private Vec3 position;

    @Shadow
    public boolean needsSync;

    @Shadow
    public abstract Set<String> getTags();

    @Shadow
    public abstract boolean removeTag(String tag);

    @Shadow
    public abstract boolean addTag(String tag);


    @Shadow
    public abstract UUID getUUID();

    @Shadow
    public abstract void setDeltaMovement(Vec3 deltaMovement);

    @Shadow
    public abstract Vec3 getDeltaMovement();

    @ModifyVariable(
            method="move",
            at= @At("HEAD"),
            argsOnly = true
    )
    public Vec3 updateMovement(Vec3 movement) {
        if (!((Object) this instanceof Player)) {
            double increaseAmount;
            if((Object)this instanceof FallingBlockEntity){
                increaseAmount=1.5;

            }
            else{
                increaseAmount=1;
            }
            Sorcery closest = Limitless.getClosestLimitlessPosition(this.position);
            this.needsSync = true;
            if (closest != null && closest.abilities.getFirst() instanceof LimitlessInnate li) {
                Vec3 closestPos = closest.player.getPosition(0);

                double distanceFromPlayer = closestPos.distanceTo(position);


                //if inside sphere
                if (li.startDistance*increaseAmount > distanceFromPlayer) {
                    if (!this.getTags().contains(LimitlessInnate.TAG)) {
                        this.addTag(LimitlessInnate.TAG);
                    }
                }


                if (li.startDistance*increaseAmount < distanceFromPlayer) {
                    //outside sphere
                    if (this.getTags().contains(LimitlessInnate.TAG)) {
                        this.removeTag(LimitlessInnate.TAG);
                    }
                } else if (li.endDistance*increaseAmount < distanceFromPlayer) {
                    //if between 0 and start
                    //TODO: item entities currently stop all forward velocity after setting it to 0, fix by giving small push
                    movement = movement.scale(Math.pow((distanceFromPlayer/(li.startDistance*increaseAmount)),2));
                } else {
                    //if in 0 area
                    movement = movement.scale(0);
                }
            }
        }
        return movement;
    }


    @Inject(method = "canBeHitByProjectile",at=@At("HEAD"),cancellable = true)
    public void canBeHitByProjectile(CallbackInfoReturnable<Boolean> cir){
        if(Sorcery.sorcerers.get(this.getUUID()) != null){
            //if the target is a sorcerer
            Sorcery.sorcerers.get(this.getUUID()).abilities.forEach((ability -> {
                if (ability instanceof LimitlessInnate li && li.isTicked) {
                    cir.setReturnValue(false);
                }
            }));

        }
    }



}
