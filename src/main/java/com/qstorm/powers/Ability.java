package com.qstorm.powers;

import com.qstorm.PAL.PALHandle;
import com.qstorm.powers.cursedtechnique.limitless.power.Blue;
import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranim.api.PlayerAnimationAccess;
import com.zigythebird.playeranim.api.PlayerAnimationFactory;
import com.zigythebird.playeranimcore.animation.Animation;
import com.zigythebird.playeranimcore.animation.RawAnimation;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.UUID;

public abstract class Ability {
    int id;
    Combo abilityCombo;
    public boolean isTicked=false;
    public UUID playerUUID;
    public ArrayList<Integer> times = new ArrayList<>();
    public final int textColor;

    //JJK Specific
    public PlayerInfo playerInfo;
    /**
     * the amount of energy needed for the ability to run,
     * ex: if your output is 1000 energy and this value is 1100 then the ability will not run and you will not lose any energy,
     * it will also trigger a NotEnoughOutputCall
     */
    protected int initial=0;
    /**
     * The how much the power increases as a result of the cost increasing
     * higher rate = higher cost per power
     * power=rate*cost+initial
     */
    protected double rate=0;
    /**
     * How much power this ability will have, each ability can handle this uniquely
     */
    public double power=0;
    protected double percentPower=0;

    public boolean isScroll=false;


    //the cursedEnergy system works linearly at the moment, power=increaseRate*energy+initial


    //PAL stuff
    protected Animation onRunAnimation;
    protected Identifier factoryAnimation;


    public Ability(UUID playerUUID, int id,int textColor){
        this.id=id;
        this.playerUUID = playerUUID;
        this.textColor = textColor;
    }
    public Ability(UUID playerUUID, int id){
        this(playerUUID,id,0xFF000000);
    }

    public Ability(UUID playerUUID, int id, int initial,int rate,int textColor){
        this(playerUUID,id,textColor);
        this.initial=initial;
        this.rate=rate;
    }

    public void Do(ServerPlayer player){
        playerInfo=PlayerInfo.playerInfoHashMap.get(playerUUID);
        power=(rate*playerInfo.cursedOutput);
        percentPower=power/rate*playerInfo.maxCursedOutput;
        if(power<initial){
            return;
        }
        playerInfo.useEnergy(player,playerInfo.cursedOutput);
        animate(player);
        if(times.isEmpty()) {
            run(player);
        }else{
            run(times, player);
        }

    }

    public void setCombo(Combo combo){
        this.abilityCombo=combo;
        //abilityCombo.setAction(()->Do());
    }


    /**
     * Run the ability
     * @param player the player running the ability
     */
    protected void run(ServerPlayer player) {

    }

    /**
     * Run the ability if this ability is a detection ability (an ability that tracks the time it took to click)
     * @param times the time it took to click the next comboKey
     * @param player the player running the ability
     */
    protected void run(ArrayList<Integer> times,ServerPlayer player) {

    }

    public void tick(Player contextPlayer){
    }


    protected void animate(Player player, Identifier factory, Animation animation){
        if(!player.level().isClientSide())
            return;

        PlayerAnimationController controller = (PlayerAnimationController) PlayerAnimationAccess.getPlayerAnimationLayer(
                player,factory
        );
        if (controller != null)
            controller.triggerAnimation(animation);

    }


    protected void animate(Player player){
        if(factoryAnimation!=null&&onRunAnimation!=null){
            animate(player,factoryAnimation,onRunAnimation);
        }
    }

    public static void initAnimate(Identifier factoryID, RawAnimation rawAnimation,int priority){
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(factoryID,priority,
                player ->new PlayerAnimationController(player,
                        (animationController, animationState, animationSetter) ->
                                animationSetter.setAnimation(rawAnimation))
        );
    }


    public void startTickLoop(){
        isTicked=true;
    }
    public void endTickLoop(){
        isTicked=false;
    }

    public void end(ServerLevel context){
        this.isTicked=false;
        disableScroll();
    };

    /**
     *
     * @param amount assume = 1
     */
    public void onScroll(double amount){

    }

    public void enableScroll(){
        this.isScroll=true;
    }
    public void disableScroll(){
        this.isScroll=false;

    }


    public void notEnoughEnergy(){

    }

    public void notEnoughOutput(){

    }


    protected void runOneTimeAnimation(){

    }






}
