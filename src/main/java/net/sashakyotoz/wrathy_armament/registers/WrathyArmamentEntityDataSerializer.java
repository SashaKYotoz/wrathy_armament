package net.sashakyotoz.wrathy_armament.registers;

import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.entities.bosses.JohannesKnight;

public class WrathyArmamentEntityDataSerializer {
    public static final DeferredRegister<EntityDataSerializer<?>> SERIALIZER = DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, WrathyArmament.MODID);
    public static final RegistryObject<EntityDataSerializer<JohannesKnight.KnightPose>> KNIGHT_POSE = SERIALIZER.register("knight_pose",()-> EntityDataSerializer.simpleEnum(JohannesKnight.KnightPose.class));
}
