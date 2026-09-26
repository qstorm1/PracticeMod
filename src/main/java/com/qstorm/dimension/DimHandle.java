package com.qstorm.dimension;

import com.qstorm.PracticeMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;


public class DimHandle {
    public static final ResourceKey<Level> LIMITLESS_VOID = ResourceKey.create(Registries.DIMENSION,Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"custom_dim"));
    public static void init(){

    }
}
