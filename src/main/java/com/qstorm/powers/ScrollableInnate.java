package com.qstorm.powers;

import com.qstorm.PracticeMod;

public interface ScrollableInnate {
    default void changeInnateAmount(double amountIncrease){
        PracticeMod.LOGGER.info("Changed Innate Amount by {}", amountIncrease);
    }
}
