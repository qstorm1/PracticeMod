package com.qstorm.packets;

import com.qstorm.PracticeMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public class AnimationPacket {
    public record SendAnimationUpdate(int ID) implements CustomPacketPayload {

        public static final int blueInit=2050;

        public static final Type<AnimationPacket.SendAnimationUpdate> TYPE =
                new AnimationPacket.SendAnimationUpdate.Type<>(Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"animation-update-packet"));

        public static final StreamCodec<RegistryFriendlyByteBuf, AnimationPacket.SendAnimationUpdate> CODEC =StreamCodec.composite(
                ByteBufCodecs.VAR_INT,
                SendAnimationUpdate::ID,

                SendAnimationUpdate::new
        );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
