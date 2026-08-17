package com.qstorm;

import com.qstorm.key.InitializeBindings;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.item.Items;

public class PracticeModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        //initialize all keyboard stuff
        InitializeBindings.init();
    }

}
