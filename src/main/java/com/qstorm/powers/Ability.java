package com.qstorm.powers;

import com.qstorm.cca.PlayerInfoContext;
import com.qstorm.packets.Packet;
import com.qstorm.powers.combo.Combo;
import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranim.api.PlayerAnimationAccess;
import com.zigythebird.playeranim.api.PlayerAnimationFactory;
import com.zigythebird.playeranimcore.animation.Animation;
import com.zigythebird.playeranimcore.animation.RawAnimation;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.UUID;

public abstract class Ability {
    /**
     * The identifier of this ability
     */
    public int id;
    /**
     * The combo attached to this player
     */
    Combo abilityCombo;

    /**
     * If minecraft runs this program every tick
     */
    public boolean isTicked=false;
    /**
     * how many ticks minecraft runs this program before ending
     * MUST BE SET EVERY RUN
     */
    public int ticksEnabled;

    /**
     * The player with this ability
     */
    public UUID playerUUID;

    /**
     * The time it takes for each key to be pressed
     */
    public ArrayList<Integer> times = new ArrayList<>();
    /**
     * The color of the name on the client renderer (hexadecimal)
     */
    public final int textColor;

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

    /**
     * If this ability detects when the scroll wheel happens
     */
    public boolean isScroll=false;

    /**
     * If this ability prevents the hotBar from moving
     */
    public boolean locksHotBarScroll=false;


    //the cursedEnergy system works linearly at the moment, power=increaseRate*energy+initial


    //PAL stuff
    /**
     * weather this ability runs an animation on run
     */
    public boolean animationOnRun=false;
    /**
     * The animation this animation runs
     */
    protected Animation onRunAnimation;
    /**
     * The factory registry attached to this animation
     */
    protected Identifier IS_JJK_ANIMATION;







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







    public void DoSinglePlayerLocal(LocalPlayer player){
        runClient(player);
    }


    public void Do(Player player){
        PlayerInfoContext context = PlayerInfo.playerInfoData.get(player);
        power=(rate*context.getOutput());
        percentPower=power/rate*context.getMaxOutput();

        //if the power isn't enough to run the abilities initial cost
        if(power<initial){
            return;
        }//if this is records times
        if (times.isEmpty()) {
            run(player);
        } else {
            run(times, player);
        }

    }


    /**
     * Run the ability
     * @param player the player running the ability
     */
    protected void run(Player player) {
        runServerClient(player);
        if(player instanceof ServerPlayer serverPlayer)
            runServer(serverPlayer);
        else if(player instanceof LocalPlayer localPlayer)
            runClient(localPlayer);
        runServerClientPost(player);
    }

    /**
     * Run the ability if this ability is a detection ability (an ability that tracks the time it took to click)
     * @param times the time it took to click the next comboKey
     * @param player the player running the ability
     */
    protected void run(ArrayList<Integer> times,Player player) {
        this.times=times;
        run(player);
    }

    protected void runServerClient(Player player){

    }

    /**
     * Runs after the runServer/runClient method
     */
    protected void runServerClientPost(Player player){

    }

    protected void runClient(LocalPlayer player){

    }

    protected void runServer(ServerPlayer player){
        PlayerInfoContext context=PlayerInfo.playerInfoData.get(player);
        runServer(context,player);
    }

    protected void runServer(PlayerInfoContext context,ServerPlayer player){
        context.setEnergy(context.getEnergy()-context.getOutput());
        ServerPlayNetworking.send(player,new Packet.ActivateSorceryRender(
                context.getEnergy(),
                PlayerInfo.getOutputAsPercent(context.getMaxOutput(),context.getOutput())
        ));
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
        if(IS_JJK_ANIMATION !=null&&onRunAnimation!=null){
            animate(player, IS_JJK_ANIMATION,onRunAnimation);
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

    public void setCombo(Combo combo){
        this.abilityCombo=combo;
        //abilityCombo.setAction(()->Do());
    }





}
