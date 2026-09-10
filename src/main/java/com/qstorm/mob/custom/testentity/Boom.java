package com.qstorm.mob.custom.testentity;

import java.util.Collection;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SwellGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.entity.animal.feline.Ocelot;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import org.jspecify.annotations.Nullable;

public class Boom
        extends Monster {
    private static final EntityDataAccessor<Integer> DATA_SWELL_DIR = SynchedEntityData.defineId(Creeper.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_IS_POWERED = SynchedEntityData.defineId(Creeper.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_IGNITED = SynchedEntityData.defineId(Creeper.class, EntityDataSerializers.BOOLEAN);
    private static final boolean DEFAULT_IGNITED = false;
    private static final boolean DEFAULT_POWERED = false;
    private static final short DEFAULT_MAX_SWELL = 30;
    private static final byte DEFAULT_EXPLOSION_RADIUS = 3;
    private int oldSwell;
    private int swell;
    private int maxSwell = 30;
    private int explosionRadius = 3;
    private boolean droppedSkulls;

    public Boom(EntityType<? extends Creeper> entityType, Level level) {
        super((EntityType<? extends Creeper>)entityType, level);
    }

    @Override
    protected void registerGoals() {
        //if in water, try to float up
        this.goalSelector.addGoal(1, new FloatGoal(this));

        //if near the player try to swell
        this.goalSelector.addGoal(2, new QSwellGoal(this));

        //avoid ocelots and cats
        this.goalSelector.addGoal(3, new AvoidEntityGoal<Ocelot>(this, Ocelot.class, 6.0f, 1.0, 1.2));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<Cat>(this, Cat.class, 6.0f, 1.0, 1.2));

        //try to pathfind to the target defined in the target selector, if within attack range, attack
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0, false));

        //avoid water if you have no goal
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));

        //look at player if 8 blocks away
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0f));

        //look around randomly
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<Player>((Mob)this, Player.class, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this, new Class[0]));
    }

    //set the attributes of this monster to 0.25 movement speed
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    //if the enemy has no target, don't allow the mob to fall down more then 3 blocks,
    //if it does have a target, don't let the entity die when falling otherwise normal
    @Override
    public int getMaxFallDistance() {
        if (this.getTarget() == null) {
            return this.getComfortableFallDistance(0.0f);
        }
        return this.getComfortableFallDistance(this.getHealth() - 1.0f);
    }

    //triggered when entity gets fall damage
    @Override
    public boolean causeFallDamage(double fallDistance, float damageMultiplier, DamageSource damageSource) {
        boolean bl = super.causeFallDamage(fallDistance, damageMultiplier, damageSource);
        //this is the behavior that handles the creeper exploding when falling
        this.swell += (int)(fallDistance * 1.5);
        if (this.swell > this.maxSwell - 5) {
            this.swell = this.maxSwell - 5;
        }
        return bl;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_SWELL_DIR, -1);
        builder.define(DATA_IS_POWERED, false);
        builder.define(DATA_IS_IGNITED, false);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("powered", this.isPowered());
        output.putShort("Fuse", (short)this.maxSwell);
        output.putByte("ExplosionRadius", (byte)this.explosionRadius);
        output.putBoolean("ignited", this.isIgnited());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.entityData.set(DATA_IS_POWERED, input.getBooleanOr("powered", false));
        this.maxSwell = input.getShortOr("Fuse", (short)30);
        this.explosionRadius = input.getByteOr("ExplosionRadius", (byte)3);
        if (input.getBooleanOr("ignited", false)) {
            this.ignite();
        }
    }

    @Override
    public void tick() {
        if (this.isAlive()) {
            int i;
            this.oldSwell = this.swell;
            //if the creeper is ignited
            if (this.isIgnited()) {
                //set the data to be ignited
                this.setSwellDir(1);
            }
            //if its a start tick
            if ((i = this.getSwellDir()) > 0 && this.swell == 0) {
                //play creeper sound
                this.playSound(SoundEvents.CREEPER_PRIMED, 1.0f, 0.5f);
                //do the things that happen when a fuse is primed
                this.gameEvent(GameEvent.PRIME_FUSE);
            }
            //add to te swell time
            this.swell += i;
            //idk why this would happen
            if (this.swell < 0) {
                this.swell = 0;
            }
            if (this.swell >= this.maxSwell) {
                this.swell = this.maxSwell;
                this.explodeCreeper();
            }
        }
        super.tick();
    }

    @Override
    public void setTarget(@Nullable LivingEntity livingEntity) {
        //ignore goat damage
        if (livingEntity instanceof Goat) {
            return;
        }
        super.setTarget(livingEntity);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.CREEPER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.CREEPER_DEATH;
    }

    @Override
    public boolean killedEntity(ServerLevel level, LivingEntity entity, DamageSource damageSource) {
        //the creeper should drop loot based on preset requirements
        if (this.shouldDropLoot(level) && this.isPowered() && !this.droppedSkulls) {
            entity.dropFromLootTable(level, damageSource, false, BuiltInLootTables.CHARGED_CREEPER, itemStack -> {
                entity.spawnAtLocation(level, (ItemStack)itemStack);
                this.droppedSkulls = true;
            });
        }
        return super.killedEntity(level, entity, damageSource);
    }

    @Override
    public boolean doHurtTarget(ServerLevel level, Entity source) {
        return true;
    }

    public boolean isPowered() {
        return this.entityData.get(DATA_IS_POWERED);
    }

    /**
     * Params: (Float)Render tick. Returns the intensity of the creeper's flash when it is ignited.
     */
    public float getSwelling(float partialTicks) {
        return Mth.lerp(partialTicks, this.oldSwell, this.swell) / (float)(this.maxSwell - 2);
    }

    /**
     * Returns the current state of creeper, -1 is idle, 1 is 'in fuse'
     */
    public int getSwellDir() {
        return this.entityData.get(DATA_SWELL_DIR);
    }

    /**
     * Sets the state of creeper, -1 to idle and 1 to be 'in fuse'
     */
    public void setSwellDir(int state) {
        this.entityData.set(DATA_SWELL_DIR, state);
    }

    @Override
    public void thunderHit(ServerLevel level, LightningBolt lightning) {
        super.thunderHit(level, lightning);
        this.entityData.set(DATA_IS_POWERED, true);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(ItemTags.CREEPER_IGNITERS)) {
            SoundEvent soundEvent = itemStack.is(Items.FIRE_CHARGE) ? SoundEvents.FIRECHARGE_USE : SoundEvents.FLINTANDSTEEL_USE;
            this.level().playSound((Entity)player, this.getX(), this.getY(), this.getZ(), soundEvent, this.getSoundSource(), 1.0f, this.random.nextFloat() * 0.4f + 0.8f);
            if (!this.level().isClientSide()) {
                this.ignite();
                if (!itemStack.isDamageableItem()) {
                    itemStack.shrink(1);
                } else {
                    itemStack.hurtAndBreak(1, (LivingEntity)player, hand.asEquipmentSlot());
                }
            }
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    /**
     * Creates an explosion as determined by this creeper's power and explosion radius.
     */
    private void explodeCreeper() {
        Level level = this.level();
        if (level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            float f = this.isPowered() ? 2.0f : 1.0f;
            this.dead = true;
            serverLevel.explode(this, this.getX(), this.getY(), this.getZ(), (float)this.explosionRadius * f, Level.ExplosionInteraction.MOB);
            this.spawnLingeringCloud();
            this.triggerOnDeathMobEffects(serverLevel, Entity.RemovalReason.KILLED);
            this.discard();
        }
    }

    private void spawnLingeringCloud() {
        Collection<MobEffectInstance> collection = this.getActiveEffects();
        if (!collection.isEmpty()) {
            AreaEffectCloud areaEffectCloud = new AreaEffectCloud(this.level(), this.getX(), this.getY(), this.getZ());
            areaEffectCloud.setRadius(2.5f);
            areaEffectCloud.setRadiusOnUse(-0.5f);
            areaEffectCloud.setWaitTime(10);
            areaEffectCloud.setDuration(300);
            areaEffectCloud.setPotionDurationScale(0.25f);
            areaEffectCloud.setRadiusPerTick(-areaEffectCloud.getRadius() / (float)areaEffectCloud.getDuration());
            for (MobEffectInstance mobEffectInstance : collection) {
                areaEffectCloud.addEffect(new MobEffectInstance(mobEffectInstance));
            }
            this.level().addFreshEntity(areaEffectCloud);
        }
    }

    public boolean isIgnited() {
        return this.entityData.get(DATA_IS_IGNITED);
    }

    public void ignite() {
        this.entityData.set(DATA_IS_IGNITED, true);
    }
}

