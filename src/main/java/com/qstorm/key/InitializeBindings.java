package com.qstorm.key;

import com.mojang.blaze3d.platform.InputConstants;
import com.qstorm.PracticeMod;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public class InitializeBindings {
    public static void init(){
        KeyMapping.Category CATEGORY = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"custom_mod_controls"));
        KeyMapping attack = KeyBindingHelper.registerKeyBinding(
                new KeyMapping(
                        "key.laser", InputConstants.KEY_L,CATEGORY
                )
        );


        KeyMapping domain = KeyBindingHelper.registerKeyBinding(
                new KeyMapping(
                        "key.domain", InputConstants.KEY_SEMICOLON,CATEGORY
                )
        );
    }
}
