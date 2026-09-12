package com.qstorm.mixin;

import com.qstorm.powers.cursedtechnique.Sorcery;
import com.qstorm.powers.cursedtechnique.limitless.Limitless;
import com.qstorm.powers.cursedtechnique.limitless.power.LimitlessInnate;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class FallingBlockEntityMixin {

    @Shadow
    private Vec3 position;

    @Shadow
    public boolean needsSync;

    @ModifyVariable(
            method="applyGravity",
            at= @At(value="STORE"),
            ordinal=0
    )
    public double modifyVar(double d){
        Sorcery closest = Limitless.getClosestLimitlessPosition(this.position);
        this.needsSync=true;
        if (closest!=null&&closest.abilities.getFirst() instanceof LimitlessInnate li) {
            Vec3 closestPos = closest.player.getPosition(0);

            double distanceFromPlayer = closestPos.distanceTo(position);
            if(li.startDistance < distanceFromPlayer) {
                //if out of range
                return d;
            } else if (li.endDistance < distanceFromPlayer) {
                //if between 0 and start
                return d* (distanceFromPlayer*li.startDistance);
            } else{
                //if in 0 area
                return 0;
            }
//            return li.startDistance < closestPos.distanceTo(position) ? li.endDistance< closestPos.distanceTo(position) ?
//                    //case 1: velocity is 0 cause too close to player
//                    d*0 :
//                    //case 2: velocity is in endzone
//                    d * (li.startDistance) :
//                    //case 3: too far away from player
//                    d; 3/4
        }
        return d;
    }

}
