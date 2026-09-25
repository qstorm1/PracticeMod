package com.qstorm.cca.sorceryStorage;

import com.qstorm.powers.cursedtechnique.Sorcery;
import org.ladysnake.cca.api.v3.component.Component;

/**
 * Sorcery Data that all players have
 */
public interface SorceryDataContext extends Component {
    boolean hasInnateTechnique();
    void setHasInnateTechnique(boolean has);
    boolean getInnateStatus();
    void setInnateStatus(boolean status);
    boolean getDomainStatus();
    void setDomainStatus(boolean status);
    String getSorceryType();
    void setSorceryType(String powerType);
    Sorcery getSorcerer();
    void setSorcerer(Sorcery storedSorcerer);
}
