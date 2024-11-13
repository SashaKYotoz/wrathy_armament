package net.sashakyotoz.wrathy_armament.client.particles.options;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.PositionSourceType;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentMiscRegistries;

import java.util.Locale;

public record CapturedSoulParticleOption(PositionSource source, int arrivalInTicks, float red, float green,
                                         float blue) implements ParticleOptions {
    public static final Codec<CapturedSoulParticleOption> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PositionSource.CODEC.fieldOf("source").forGetter(CapturedSoulParticleOption::source),
            Codec.INT.fieldOf("arrivalInTicks").forGetter(CapturedSoulParticleOption::arrivalInTicks),
            Codec.FLOAT.fieldOf("red").forGetter(CapturedSoulParticleOption::red),
            Codec.FLOAT.fieldOf("green").forGetter(CapturedSoulParticleOption::green),
            Codec.FLOAT.fieldOf("blue").forGetter(CapturedSoulParticleOption::blue)
    ).apply(instance, CapturedSoulParticleOption::new));
    public static final ParticleOptions.Deserializer<CapturedSoulParticleOption> DESERIALIZER = new ParticleOptions.Deserializer<>() {
        public CapturedSoulParticleOption fromCommand(ParticleType<CapturedSoulParticleOption> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float f = (float) reader.readDouble();
            reader.expect(' ');
            float f1 = (float) reader.readDouble();
            reader.expect(' ');
            float f2 = (float) reader.readDouble();
            reader.expect(' ');
            int i = reader.readInt();
            BlockPos blockpos = BlockPos.containing(f, f1, f2);
            reader.expect(' ');
            float r = reader.readFloat();
            reader.expect(' ');
            float g = reader.readFloat();
            reader.expect(' ');
            float b = reader.readFloat();
            return new CapturedSoulParticleOption(new BlockPositionSource(blockpos), i, r, g, b);
        }

        public CapturedSoulParticleOption fromNetwork(ParticleType<CapturedSoulParticleOption> type, FriendlyByteBuf byteBuf) {
            CompoundTag tag = byteBuf.readNbt();
            PositionSource positionsource = PositionSourceType.fromNetwork(byteBuf);
            int i = byteBuf.readVarInt();
            float r = tag.getFloat("red");
            float g = tag.getFloat("green");
            float b = tag.getFloat("blue");
            return new CapturedSoulParticleOption(positionsource, i, r, g, b);
        }
    };

    @Override
    public ParticleType<?> getType() {
        return WrathyArmamentMiscRegistries.CAPTURED_SOUL.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf pBuffer) {
        CompoundTag tag = new CompoundTag();
        PositionSourceType.toNetwork(this.source, pBuffer);
        pBuffer.writeVarInt(this.arrivalInTicks);
        tag.putFloat("red", this.red);
        tag.putFloat("green", this.green);
        tag.putFloat("blue", this.blue);
        pBuffer.writeNbt(tag);
    }

    @Override
    public String writeToString() {
        Vec3 vec3 = this.source.getPosition(null).get();
        double d0 = vec3.x();
        double d1 = vec3.y();
        double d2 = vec3.z();
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %d %.2f %.2f %.2f", BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()), d0, d1, d2, this.arrivalInTicks, red, green, blue);
    }
}
