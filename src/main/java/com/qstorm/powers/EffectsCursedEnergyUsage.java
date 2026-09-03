package com.qstorm.powers;

public interface EffectsCursedEnergyUsage {
    default int getEnergyAdd(){
        return(0);
    }
    default int getEnergyMultiply(){
        return (1);
    }
}
