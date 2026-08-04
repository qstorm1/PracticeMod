package com.qstorm.render.hud;


import com.qstorm.PracticeMod;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public class renderer {
    public static void renderBasicImage(){
        HudElementRegistry.addLast(Identifier.of(PracticeMod.MOD_ID,"practice_element"),hudElement());

    }
    private static HudElement hudElement(){
        return (context, tickCounter) -> {
            context.drawText(MinecraftClient.getInstance().textRenderer,"THIS IS COOL TEXT AHH",20,20,300,false);
        };
    }
}
