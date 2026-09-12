package com.qstorm.powers;

import com.qstorm.PracticeMod;

/**
 * When one can scroll on Innate Techniques to change them in some way
 */
public interface ScrollableInnate {
    default void changeInnateAmount(double amountIncrease){
        PracticeMod.LOGGER.info("Changed Innate Amount by {}", amountIncrease);
    }
}
