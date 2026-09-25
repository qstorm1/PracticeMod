package com.qstorm.cca.playerInfo;

import org.ladysnake.cca.api.v3.component.Component;

public interface PlayerInfoContext extends Component {
    int getMaxEnergy();
    int getEnergy();
    int getMaxOutput();
    int getOutput();
    int getEfficiency();
    void setMaxEnergy(int energy);
    void setEnergy(int energy);
    void setMaxOutput(int output);
    void setOutput(int output);
    void setEfficiency(int increase);
}
