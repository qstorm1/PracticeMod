package com.qstorm.cca;


import com.qstorm.powers.combo.Combo;
import org.ladysnake.cca.api.v3.component.Component;

import java.util.ArrayList;

public interface CCAComboStorage extends Component {
    ArrayList<Combo> getCombos();
    void addCombo(Combo combo);
    void clearCombo();
    /**
     * Remove first combo of comboName
     */
    void removeCombo(String comboName);
}
