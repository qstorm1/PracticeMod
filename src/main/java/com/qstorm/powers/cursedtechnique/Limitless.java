package com.qstorm.powers.cursedtechnique;

import com.qstorm.key.InitializeBindings;
import com.qstorm.powers.Combo;
import com.qstorm.powers.ComboKey;
import com.qstorm.powers.Sorcery;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class Limitless implements Sorcery {
    Combo ability1=Combo.createCombo(
            new ComboKey(InitializeBindings.combo1,80),
            new ComboKey(InitializeBindings.combo2,80),
            new ComboKey(InitializeBindings.combo3,80));
    Combo ability2=Combo.createCombo(
            new ComboKey(InitializeBindings.combo3,80),
            new ComboKey(InitializeBindings.combo2,80),
            new ComboKey(InitializeBindings.combo3,80));;
    Combo ability3=Combo.createCombo(
            new ComboKey(InitializeBindings.combo2,80),
            new ComboKey(InitializeBindings.combo2,80),
            new ComboKey(InitializeBindings.combo1,80));;
    Combo ability4=Combo.createCombo(
            new ComboKey(InitializeBindings.combo3,80),
            new ComboKey(InitializeBindings.combo3,80),
            new ComboKey(InitializeBindings.combo1,80));;

    public static void initializeCombos(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {

        });
    }

    //two times as much cursed energy as blue
    public static void triggerRed(){

    }

    public static void triggerBlue(){

    }

    public static void triggerPurple(){

    }

    //requires constant cursed energy output
    public static void infinityOn(){

    }

    public static void infinityOff(){

    }

    public Limitless(){

    }
}
