package net.sashakyotoz.wrathy_armament.client.particles.options;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.registries.ForgeRegistries;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentMiscRegistries;

import java.util.Locale;

public record FireSphereParticleOption(float maxScale, boolean toScale, boolean isReversed, float redColor, float greenColor, float blueColor) implements ParticleOptions {
    public static final Codec<FireSphereParticleOption> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("maxScale").forGetter(FireSphereParticleOption::maxScale),
            Codec.BOOL.fieldOf("toScale").forGetter(FireSphereParticleOption::toScale),
            Codec.BOOL.fieldOf("isReversed").forGetter(FireSphereParticleOption::isReversed),
            Codec.FLOAT.fieldOf("redColor").forGetter(FireSphereParticleOption::redColor),
            Codec.FLOAT.fieldOf("greenColor").forGetter(FireSphereParticleOption::greenColor),
            Codec.FLOAT.fieldOf("blueColor").forGetter(FireSphereParticleOption::blueColor)
    ).apply(instance, FireSphereParticleOption::new));
    public static final ParticleOptions.Deserializer<FireSphereParticleOption> DESERIALIZER = new ParticleOptions.Deserializer<>() {
        public FireSphereParticleOption fromCommand(ParticleType<FireSphereParticleOption> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float m = reader.readFloat();
            reader.expect(' ');
            boolean t = reader.readBoolean();
            reader.expect(' ');
            boolean i = reader.readBoolean();
            reader.expect(' ');
            float f = reader.readFloat();
            reader.expect(' ');
            float f1 = reader.readFloat();
            reader.expect(' ');
            float f2 = reader.readFloat();
            return new FireSphereParticleOption(m,t,i,f, f1, f2);
        }

        public FireSphereParticleOption fromNetwork(ParticleType<FireSphereParticleOption> type, FriendlyByteBuf byteBuf) {
            CompoundTag tag = byteBuf.readNbt();
            float maxScale = tag.getFloat("maxScale");
            boolean toScale = tag.getBoolean("toScale");
            boolean isReversed = tag.getBoolean("isReversed");
            float r = tag.getFloat("red");
            float g = tag.getFloat("green");
            float b = tag.getFloat("blue");
            return new FireSphereParticleOption(maxScale,toScale,isReversed,r, g, b);
        }
    };
    @Override
    public ParticleType<?> getType() {
        return WrathyArmamentMiscRegistries.FIRE_SPHERE.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf pBuffer) {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("maxScale", this.maxScale);
        tag.putBoolean("toScale", this.toScale);
        tag.putBoolean("isReversed", this.isReversed);
        tag.putFloat("red", this.redColor);
        tag.putFloat("green", this.greenColor);
        tag.putFloat("blue", this.blueColor);
        pBuffer.writeNbt(tag);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %.2f %b %b %.2f %.2f %.2f", ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()),maxScale,toScale,isReversed,redColor,greenColor,blueColor);
    }
}