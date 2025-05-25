package net.sashakyotoz.wrathy_armament.utils;

import net.minecraft.ChatFormatting;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.sashakyotoz.wrathy_armament.client.renderer.layers.TransparentFireLayer;
import net.sashakyotoz.wrathy_armament.client.renderer.layers.WeaponsOnBackLayer;
import net.sashakyotoz.wrathy_armament.entities.alive.Guide;
import net.sashakyotoz.wrathy_armament.entities.alive.LichMyrmidon;
import net.sashakyotoz.wrathy_armament.entities.alive.TrueEyeOfCthulhu;
import net.sashakyotoz.wrathy_armament.entities.bosses.*;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentEntities;
import net.sashakyotoz.wrathy_armament.utils.capabilities.HalfZatoichiAbilityCapability;
import net.sashakyotoz.wrathy_armament.utils.capabilities.MistsplitterDefenseCapability;

import java.nio.file.Path;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class OnModActionsTrigger {

    @SubscribeEvent
    public static void registerPack(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            Path path = ModList.get().getModFileById("wrathy_armament").getFile().findResource("resourcepacks/pixel_wrathy_swords");
            Pack builtinDataPack = Pack.readMetaAndCreate(
                    "wrathy_armament:pixel_wrathy_swords",
                    Component.translatable("pack.wrathy_armament"),
                    false,
                    (a) -> new PathPackResources(a, path, false),
                    PackType.CLIENT_RESOURCES,
                    Pack.Position.TOP,
                    PackSource.create((arg) -> Component.translatable("pack.nameAndSource", arg,
                            Component.translatable("pack.source.builtin")).withStyle(ChatFormatting.GRAY), false)
            );

            event.addRepositorySource((packConsumer) -> packConsumer.accept(builtinDataPack));
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(HalfZatoichiAbilityCapability.class);
        event.register(MistsplitterDefenseCapability.class);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(WrathyArmamentEntities.SASHAKYOTOZ.get(), SashaKYotoz.createAttributes().build());
        event.put(WrathyArmamentEntities.HABCIAK.get(), Habciak.createAttributes().build());
        event.put(WrathyArmamentEntities.LICH_KING.get(), LichKing.createAttributes().build());
        event.put(WrathyArmamentEntities.JOHANNES_KNIGHT.get(), JohannesKnight.createAttributes().build());
        event.put(WrathyArmamentEntities.MOON_LORD.get(), MoonLord.createAttributes().build());
        event.put(WrathyArmamentEntities.TRUE_EYE_OF_CTHULHU.get(), TrueEyeOfCthulhu.createAttributes().build());
        event.put(WrathyArmamentEntities.LICH_MYRMIDON.get(), LichMyrmidon.createAttributes().build());
        event.put(WrathyArmamentEntities.THE_GUIDE.get(), Guide.createAttributes().build());
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.AddLayers event) {
        event.getSkins().forEach((s) -> {
            LivingEntityRenderer<? extends Player, ? extends EntityModel<? extends Player>> livingEntityRenderer = event.getSkin(s);
            if (livingEntityRenderer instanceof PlayerRenderer playerRenderer) {
                playerRenderer.addLayer(new TransparentFireLayer<>(playerRenderer, event.getEntityModels()));
                playerRenderer.addLayer(new WeaponsOnBackLayer<>(playerRenderer, event.getContext()));
            }
        });
    }
}