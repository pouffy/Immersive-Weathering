package io.github.pouffy.immersive_weathering.network;

import io.github.pouffy.immersive_weathering.ImmersiveWeathering;
import io.github.pouffy.immersive_weathering.configs.ClientConfigs;
import net.mehvahdjukaar.moonlight.api.platform.network.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class SendCustomParticlesMessage implements Message {
    public static final CustomPacketPayload.TypeAndCodec<RegistryFriendlyByteBuf, SendCustomParticlesMessage> TYPE = Message.makeType(ImmersiveWeathering.res("s2c_custom_particles"), SendCustomParticlesMessage::new);

    private final EventType type;
    private final int extraData;
    private final BlockPos pos;

    public SendCustomParticlesMessage(FriendlyByteBuf buffer) {
        this.extraData = buffer.readInt();
        this.type = EventType.values()[buffer.readByte()];
        this.pos = buffer.readBlockPos();
    }

    public SendCustomParticlesMessage(EventType type, BlockPos pos, int extraData) {
        this.type = type;
        this.pos = pos;
        this.extraData = extraData;
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeInt(this.extraData);
        buf.writeByte(type.ordinal());
        buf.writeBlockPos(pos);
    }

    @Override
    public void handle(Context context) {
        clientStuff(type, pos, extraData);
    }

    @OnlyIn(Dist.CLIENT)
    public void clientStuff( EventType type, BlockPos pos, int extraData) {
        Player player = Minecraft.getInstance().player;
        var level = player.level();
        if (type == EventType.DECAY_LEAVES) {
            if (ClientConfigs.LEAF_DECAY_PARTICLES.get()) {
                BlockState state = Block.stateById(extraData);
                var leafParticle = new BlockParticleOption(ParticleTypes.BLOCK, state);
                int color = Minecraft.getInstance().getBlockColors().getColor(state, level, pos, 0);

                //add more than one?
                for (int i = 0; i < 20; i++) {
                    double d = pos.getX() + level.random.nextDouble();
                    double e = pos.getY() - 0.05;
                    double f = pos.getZ() + level.random.nextDouble();
                    level.addParticle(leafParticle, d, e, f, 0.0, color, 0.0);
                }
            }

            if (ClientConfigs.LEAF_DECAY_SOUND.get()) {
                level.playSound(player, pos, SoundEvents.AZALEA_LEAVES_BREAK, SoundSource.BLOCKS, 1.0f, 1.0f);
            }
        }
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE.type();
    }

    public enum EventType {
        DECAY_LEAVES
    }
}
