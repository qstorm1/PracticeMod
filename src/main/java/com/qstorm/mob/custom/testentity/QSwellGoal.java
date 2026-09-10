package com.qstorm.mob.custom.testentity;

import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import org.jspecify.annotations.Nullable;


public class QSwellGoal
        extends Goal {
    private final Boom boom;
    private @Nullable LivingEntity target;

    public QSwellGoal(Boom monster) {
        this.boom = monster;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        LivingEntity livingEntity = this.boom.getTarget();
        return this.boom.getSwellDir() > 0 || livingEntity != null && this.boom.distanceToSqr(livingEntity) < 9.0;
    }

    @Override
    public void start() {
        this.boom.getNavigation().stop();
        this.target = this.boom.getTarget();
    }

    @Override
    public void stop() {
        this.target = null;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (this.target == null) {
            this.boom.setSwellDir(-1);
            return;
        }
        if (this.boom.distanceToSqr(this.target) > 49.0) {
            this.boom.setSwellDir(-1);
            return;
        }
        if (!this.boom.getSensing().hasLineOfSight(this.target)) {
            this.boom.setSwellDir(-1);
            return;
        }
        this.boom.setSwellDir(1);
    }

}
