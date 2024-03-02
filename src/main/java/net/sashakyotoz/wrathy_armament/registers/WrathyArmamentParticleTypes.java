package net.sashakyotoz.wrathy_armament.registers;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sashakyotoz.wrathy_armament.WrathyArmament;

public class WrathyArmamentParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, WrathyArmament.MODID);
    public static final RegistryObject<SimpleParticleType> PHANTOM_RAY = PARTICLE_TYPES.register("phantom_ray",()->new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> ZENITH_WAY = PARTICLE_TYPES.register("zenith_way",()->new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> FIRE_TRAIL = PARTICLE_TYPES.register("fire_trail",()->new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> FROST_SOUL_RAY = PARTICLE_TYPES.register("frostsoul_ray",()->new SimpleParticleType(true));
}
