package net.sashakyotoz.wrathy_armament.networking;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.networking.packets.UpdateGroupedParticlePacket;
import net.sashakyotoz.wrathy_armament.networking.packets.UpdateParticlePacket;

public class WANetworkingManager {
    public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder.named(WrathyArmament.createWALocation("messages"))
            .networkProtocolVersion(() -> "1.0")
            .clientAcceptedVersions(s -> true)
            .serverAcceptedVersions(s -> true)
            .simpleChannel();
    public static int PACKET_ID = 0;

    public static void registerPackets(FMLCommonSetupEvent event) {
        registerC2SPackets();
        registerS2CPackets();
    }

    private static void registerC2SPackets() {
        INSTANCE.messageBuilder(UpdateParticlePacket.class, PACKET_ID++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(UpdateParticlePacket::new)
                .encoder(UpdateParticlePacket::toBytes)
                .consumerMainThread(UpdateParticlePacket::handle)
                .add();
    }

    private static void registerS2CPackets() {

        INSTANCE.messageBuilder(UpdateGroupedParticlePacket.class, PACKET_ID++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(UpdateGroupedParticlePacket::new)
                .encoder(UpdateGroupedParticlePacket::toBytes)
                .consumerMainThread(UpdateGroupedParticlePacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToPlayersNearby(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> player), message);
    }

    public static <MSG> void sendToPlayersNearbyAndSelf(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), message);
    }

    public static <MSG> void sendToAll(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
}