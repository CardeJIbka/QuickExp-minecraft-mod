package cardejibka.quickexp.packets;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public class OptOutPacket implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<OptOutPacket> ID =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath("quickexp", "optout"));

    public static final StreamCodec<RegistryFriendlyByteBuf, OptOutPacket> CODEC =
            StreamCodec.of(
                    (buf, packet) -> {},
                    buf -> new OptOutPacket()
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}