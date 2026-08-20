package com.qstorm.key;

import com.qstorm.PracticeMod;
import com.qstorm.item.armor.LaserEyes;
import com.qstorm.powers.ComboKey;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;

public class HandleKeybinds {
    public static void handle(){
        //configure a client to server payload
        //packets have two types of "phases", play and configure
        //playC2S() happens during gameplay while configureC2S() happens during initial connection
        PayloadTypeRegistry.playC2S().register(HandleKeybinds.LaserKeyServer.TYPE,HandleKeybinds.LaserKeyServer.CODEC);

        PayloadTypeRegistry.playC2S().register(HandleKeybinds.AttackJJK1.TYPE,HandleKeybinds.AttackJJK1.CODEC);
        PayloadTypeRegistry.playC2S().register(HandleKeybinds.AttackJJK2.TYPE,HandleKeybinds.AttackJJK2.CODEC);
        PayloadTypeRegistry.playC2S().register(HandleKeybinds.AttackJJK3.TYPE,HandleKeybinds.AttackJJK3.CODEC);
        PayloadTypeRegistry.playC2S().register(HandleKeybinds.AttackJJK4.TYPE,HandleKeybinds.AttackJJK4.CODEC);









        //tells the server that if it recieves packet data of the given type, run the following
        ServerPlayNetworking.registerGlobalReceiver(HandleKeybinds.LaserKeyServer.TYPE, (payload, context) -> {
            //says to run it on server
            context.server().execute(() -> {
                //if the player is wearing a laser eye
                if(context.player().getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof LaserEyes){
                    LaserEyes.shootLaser(context.player());
                }
            });
        });




        ComboKey.setupServersideManagement();
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


    public record AttackJJK1() implements CustomPacketPayload{
        //just an identifier
        public static final Type<AttackJJK1> TYPE =
                new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"attack.jjk.1-packet"));

        public static final StreamCodec<Object,AttackJJK1> CODEC = StreamCodec.unit(new AttackJJK1());

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }


    public record AttackJJK2() implements CustomPacketPayload {
        //just an identifier
        public static final Type<AttackJJK2> TYPE =
                new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID, "attack.jjk.2-packet"));

        public static final StreamCodec<Object, AttackJJK2> CODEC = StreamCodec.unit(new AttackJJK2());

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record AttackJJK3() implements CustomPacketPayload {
        //just an identifier
        public static final Type<AttackJJK3> TYPE =
                new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID, "attack.jjk.3-packet"));

        public static final StreamCodec<Object, AttackJJK3> CODEC = StreamCodec.unit(new AttackJJK3());

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record AttackJJK4() implements CustomPacketPayload {
        //just an identifier
        public static final Type<AttackJJK4> TYPE =
                new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID, "attack.jjk.4-packet"));

        public static final StreamCodec<Object, AttackJJK4> CODEC = StreamCodec.unit(new AttackJJK4());

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record Domain() implements CustomPacketPayload {
        //just an identifier
        public static final Type<Domain> TYPE =
                new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID, "domain-packet"));

        public static final StreamCodec<Object, Domain> CODEC = StreamCodec.unit(new Domain());

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
