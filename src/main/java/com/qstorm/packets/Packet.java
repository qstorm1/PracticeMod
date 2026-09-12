package com.qstorm.packets;

import com.qstorm.PracticeMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;

public class Packet {
    public record ComboRenderInfoS2C(ArrayList<String> comboNames, ArrayList<Integer> IDs,ArrayList<Integer> colors) implements CustomPacketPayload {
        //TODO: six eyes or advance sorcery can automatically detect which combo's a player is doing so this info shared to them as well
        public static final Type<Packet.ComboRenderInfoS2C> TYPE =
                new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"render-info-packet"));

        public static final StreamCodec<RegistryFriendlyByteBuf, Packet.ComboRenderInfoS2C> CODEC =StreamCodec.composite(
                ByteBufCodecs.collection(ArrayList::new, ByteBufCodecs.STRING_UTF8),
                ComboRenderInfoS2C::comboNames,

                ByteBufCodecs.collection(ArrayList::new, ByteBufCodecs.INT),
                ComboRenderInfoS2C::IDs,

                ByteBufCodecs.collection(ArrayList::new, ByteBufCodecs.INT),
                ComboRenderInfoS2C::colors,



                ComboRenderInfoS2C::new
        );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }



    public record RenderBlueToClient(ArrayList<Double> position, Double radius) implements CustomPacketPayload {
        //TODO: six eyes or advance sorcery can automatically detect which combo's a player is doing so this info shared to them as well
        public static final Type<RenderBlueToClient> TYPE =
                new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID, "blue-render-packet"));

        public static final StreamCodec<RegistryFriendlyByteBuf, RenderBlueToClient> CODEC = StreamCodec.composite(
                ByteBufCodecs.collection(ArrayList::new,ByteBufCodecs.DOUBLE),
                RenderBlueToClient::position,

                ByteBufCodecs.DOUBLE,
                RenderBlueToClient::radius,

                RenderBlueToClient::new
        );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record ActivateSorceryRender(Integer energy, Double output) implements CustomPacketPayload {
        public static final Type<ActivateSorceryRender> TYPE =
                new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID, "client-render-packet"));

        public static final StreamCodec<RegistryFriendlyByteBuf, ActivateSorceryRender> CODEC = StreamCodec.composite(
                ByteBufCodecs.INT,
                ActivateSorceryRender::energy,

                ByteBufCodecs.DOUBLE,
                ActivateSorceryRender::output,

                ActivateSorceryRender::new
        );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }


    public record SendClientMessage(String message) implements CustomPacketPayload {
        public static final Type<SendClientMessage> TYPE =
                new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID, "send-client-message-packet"));

        public static final StreamCodec<RegistryFriendlyByteBuf, SendClientMessage> CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8,
                SendClientMessage::message,

                SendClientMessage::new
        );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }


}
