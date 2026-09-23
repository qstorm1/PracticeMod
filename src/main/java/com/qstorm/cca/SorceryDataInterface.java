package com.qstorm.cca;

import org.ladysnake.cca.api.v3.component.Component;

public interface SorceryDataInterface extends Component {
    boolean hasInnateTechnique();
    void setHasInnateTechnique(boolean has);
    boolean getInnateStatus();
    void setInnateStatus(boolean status);
    boolean getDomainStatus();
    void setDomainStatus(boolean status);
    String getSorceryType();
    void setSorceryType(String powerType);
}
