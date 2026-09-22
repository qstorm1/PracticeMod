package com.qstorm.cca;

import org.ladysnake.cca.api.v3.component.Component;

public interface SorceryDataInterface extends Component {
    boolean getInnateStatus();
    boolean hasInnateTechnique();
    boolean getDomainStatus();
    void setSorceryType(String powerType);
    String getSorceryType();
}
