package com.qstorm.powers;

import net.minecraft.client.KeyMapping;

/**
 * A key that can be used in a combo
 */
public class ComboKey{
    double ticksSincePrevKey =0;//ticks since the previous button was pressed, if it reaches max combo value then it ends
    double minimumTimeRequired;
    KeyMapping value;

    /**
     *
     * @param value key needed to press for combo to continue
     * @param minimumTickRequired the ticks it takes for the key to count to the combo
     */
    public ComboKey(KeyMapping value,double minimumTickRequired){
        this.value=value;
        this.minimumTimeRequired=minimumTickRequired;
    }

    /**
     * checks if combo has moved forward or ended
     * @return 0 if the combo ends, 1 if the combo moves next, and 2 if the combo stays how it is
     */
    public int check(){
        if(ticksSincePrevKey >=minimumTimeRequired){
            return 0;
        }

        ticksSincePrevKey++;
        if(value.isDown()){
            return 1;
        }
        return 2;
    }

}
