package com.qstorm.packets;

import com.qstorm.PracticeMod;
import com.qstorm.key.HandleKeybinds;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;

public class Packet {
    public record ComboRenderInfoS2C(ArrayList<String> comboNames, ArrayList<Integer> IDs) implements CustomPacketPayload {
        //just an identifier
        //TODO: six eyes or advance sorcery can automatically detect which combo's a player is doing so this info shared to them as well
        public static final Type<Packet.ComboRenderInfoS2C> TYPE =
                new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"render-info-packet"));

        public static final StreamCodec<RegistryFriendlyByteBuf, Packet.ComboRenderInfoS2C> CODEC =StreamCodec.composite(
                ByteBufCodecs.collection(ArrayList::new, ByteBufCodecs.STRING_UTF8),
                ComboRenderInfoS2C::comboNames,

                ByteBufCodecs.collection(ArrayList::new, ByteBufCodecs.INT),
                ComboRenderInfoS2C::IDs,

                ComboRenderInfoS2C::new
        );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
