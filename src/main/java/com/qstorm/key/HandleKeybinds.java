package com.qstorm.key;

import com.qstorm.PracticeMod;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public class HandleKeybinds {
    public static void handle(){

    }

    //This is a packet that is sent to the server when the keybind is pressed
    public record LaserKeyServer() implements CustomPacketPayload{
        //just an identifier
        public static final Type<LaserKeyServer> TYPE =
                new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"laser-packet"));

        public static final StreamCodec<Object,LaserKeyServer> CODEC = StreamCodec.unit(new LaserKeyServer());

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
