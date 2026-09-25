package com.qstorm.cca;

import com.qstorm.PracticeMod;
import com.qstorm.powers.combo.Combo;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;

import java.util.ArrayList;

public class CCAComboStorageClass implements CCAComboStorage{

    public static ComponentKey<CCAComboStorage> playerCombos =
            ComponentRegistry.getOrCreate(Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"player-combo-list"), CCAComboStorage.class);



    ArrayList<Combo> combos= new ArrayList<>();

    @Override
    public ArrayList<Combo> getCombos() {
        return combos;
    }

    @Override
    public void addCombo(Combo combo) {
        combos.add(combo);
    }

    @Override
    public void clearCombo() {
        combos.clear();
    }

    @Override
    public void removeCombo(String comboName) {
        for(Combo c: combos){
            if(c.name.equals(comboName)) combos.remove(c);
        }
    }

    @Override
    public void readData(ValueInput readView) {

    }

    @Override
    public void writeData(ValueOutput writeView) {

    }
}
