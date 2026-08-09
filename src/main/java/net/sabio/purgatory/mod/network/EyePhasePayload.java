package net.sabio.purgatory.mod.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.sabio.purgatory.Purgatory;

public record EyePhasePayload(int phase) implements CustomPacketPayload {
    public static final Identifier EYE_PHASE_ID = Identifier.fromNamespaceAndPath(Purgatory.MOD_ID, "eye_phase");
    public static final CustomPacketPayload.Type<EyePhasePayload> TYPE = new CustomPacketPayload.Type<>(EYE_PHASE_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, EyePhasePayload> CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, EyePhasePayload::phase, EyePhasePayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
