package net.sashakyotoz.wrathy_armament.utils;

import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.items.Frostmourne;
import net.sashakyotoz.wrathy_armament.items.SwordLikeItem;
import net.sashakyotoz.wrathy_armament.registers.*;
import net.sashakyotoz.wrathy_armament.utils.capabilities.HalfZatoichiAbilityCapability;
import net.sashakyotoz.wrathy_armament.utils.capabilities.MistsplitterDefenseCapability;
import net.sashakyotoz.wrathy_armament.utils.capabilities.ModCapabilities;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@Mod.EventBusSubscriber
public class OnActionsTrigger {
    public static int shakingTime = 0;
    public static int rollingTime = 0;
    public static Vec3 vec3 = new Vec3(0,-256,0);
    private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

    public static void queueServerWork(int tick, Runnable action) {
        workQueue.add(new AbstractMap.SimpleEntry<>(action, tick));
    }
    @SubscribeEvent
    public static void tick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            List<AbstractMap.SimpleEntry<Runnable, Integer>> actions = new ArrayList<>();
            workQueue.forEach(work -> {
                work.setValue(work.getValue() - 1);
                if (work.getValue() == 0)
                    actions.add(work);
            });
            actions.forEach(e -> e.getKey().run());
            workQueue.removeAll(actions);
        }
    }
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event){
        if (event.phase == TickEvent.Phase.END){
            if (shakingTime > 0)
                shakingTime--;
            if (rollingTime > 0)
                rollingTime-=10;
        }
    }
    @SubscribeEvent
    public static void onPlayerAttack(LivingAttackEvent event) {
        if (event.getEntity() instanceof Player player && player.getMainHandItem().getItem() instanceof SwordLikeItem swordLikeItem)
            swordLikeItem.leftClickAttack(player,player.getMainHandItem());
    }
    @SubscribeEvent
    public static void onPlayerLeftClick(PlayerInteractEvent.LeftClickEmpty event){
        if (event.getItemStack().getItem() instanceof SwordLikeItem item)
            item.leftClickAttack(event.getEntity(),event.getItemStack());
    }
    @SubscribeEvent
    public static void onEntityDiesEvent(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            if (player.getMainHandItem().is(WrathyArmamentItems.HALF_ZATOICHI.get())) {
                if (player.getMainHandItem().getOrCreateTag().getInt("charge") < 3)
                    player.getMainHandItem().getOrCreateTag().putInt("charge", player.getMainHandItem().getOrCreateTag().getInt("charge") + 1);
                player.addEffect(new MobEffectInstance(MobEffects.HEAL, 10, 1 + player.getMainHandItem().getOrCreateTag().getInt("charge")));
            }
            if (player.getMainHandItem().is(WrathyArmamentItems.FROSTMOURNE.get())){
                if (player.getMainHandItem().getOrCreateTag().getInt("charge") < 5)
                    player.getMainHandItem().getOrCreateTag().putInt("charge", player.getMainHandItem().getOrCreateTag().getInt("charge") + 1);
            }
        }
    }

    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();
        if (stack.getItem() instanceof SwordLikeItem swordLikeItem){
            if (!event.getEntity().isCrouching())
                swordLikeItem.rightClick(player,stack);
            else
                swordLikeItem.rightClickOnShiftClick(player,stack);
        }
    }
    @SubscribeEvent
    public static void onBlockLeftClick(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();
        Level level = event.getLevel();
        if (stack.getItem() instanceof SwordLikeItem swordLikeItem)
            swordLikeItem.rightClickBlock(player,stack);
        if (stack.getEnchantmentLevel(WrathyArmamentMiscRegistries.PHANTOQUAKE.get()) > 0 && event.getHand() == InteractionHand.MAIN_HAND && event.getLevel().getBlockState(event.getPos().above(3)).isAir() && !event.getEntity().getCooldowns().isOnCooldown(event.getItemStack().getItem())) {
            player.setDeltaMovement(0, 0.5, 0);
            player.playSound(SoundEvents.PHANTOM_BITE);
            player.getCooldowns().addCooldown(event.getItemStack().getItem(), Mth.randomBetweenInclusive(RandomSource.create(), 20, 80));
            addParticles(ParticleTypes.EXPLOSION, level, player.getX(), player.getY(), player.getZ(), 3);
            final Vec3 center = new Vec3(event.getPos().getX(), event.getPos().getY() + 1, event.getPos().getZ());
            List<Entity> entities = event.getLevel().getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(8 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(center))).toList();
            for (Entity entityIterator : entities) {
                if (!(entityIterator == event.getEntity())) {
                    if (entityIterator instanceof LivingEntity entity) {
                        entity.setSecondsOnFire(20);
                        entity.setDeltaMovement(RandomSource.create().nextInt(0, 2) - 1, 0.75f, RandomSource.create().nextInt(0, 2) - 1);
                        event.getEntity().getCooldowns().addCooldown(event.getItemStack().getItem(), Mth.randomBetweenInclusive(RandomSource.create(), 60, 120));
                    }
                }
            }
        }
    }
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onCameraSetup(ViewportEvent.ComputeCameraAngles event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.getCameraEntity() instanceof Player player && !player.isSpectator()){
            if (player.distanceToSqr(vec3) < 12){
                float delta = Minecraft.getInstance().getFrameTime();
                float ticksExistedDelta = player.tickCount + delta;
                float intensity = 0.025f;
                if (!minecraft.isPaused() && player.level().isClientSide() && shakingTime > 0) {
                    event.setPitch((float) (event.getPitch() + intensity * Math.cos(ticksExistedDelta * 3 + 2) * 25));
                    event.setYaw((float) (event.getYaw() + intensity * Math.cos(ticksExistedDelta * 5 + 1) * 25));
                    event.setRoll((float) (event.getRoll() + intensity * Math.cos(ticksExistedDelta * 4) * 25));
                }
            }
            if (!minecraft.isPaused() && player.level().isClientSide() && rollingTime > 0 && player.getMainHandItem().getItem() instanceof Frostmourne) {
                if (minecraft.options.getCameraType().isFirstPerson())
                    event.setPitch(-(event.getPitch() + rollingTime));
            }
        }
    }
    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(HalfZatoichiAbilityCapability.class);
        event.register(MistsplitterDefenseCapability.class);
    }
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            if (!player.getCapability(ModCapabilities.MISTSPLITTER_DEFENCE).isPresent())
                event.addCapability(new ResourceLocation(WrathyArmament.MODID, "properties"), new ModCapabilities());
        }
    }
    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().getCapability(ModCapabilities.MISTSPLITTER_DEFENCE).ifPresent(oldStore -> event.getOriginal().getCapability(ModCapabilities.MISTSPLITTER_DEFENCE).ifPresent(newStore -> newStore.copyFrom(oldStore)));
            event.getOriginal().getCapability(ModCapabilities.HALF_ZATOICHI_ABILITIES).ifPresent(oldStore -> event.getOriginal().getCapability(ModCapabilities.HALF_ZATOICHI_ABILITIES).ifPresent(newStore -> newStore.copyFrom(oldStore)));
        }
    }
    public static double getXVector(double speed, double yaw) {
        return speed * Math.cos((yaw + 90) * (Math.PI / 180));
    }
    public static double getYVector(double speed,double pitch){
        return pitch * (-0.025) * speed;
    }

    public static double getZVector(double speed, double yaw) {
        return speed * Math.sin((yaw + 90) * (Math.PI / 180));
    }

    public static void addParticles(ParticleOptions type, Level world, double x, double y, double z, float modifier) {
        for (int i = 0; i < 360; i++) {
            if (i % 20 == 0)
                world.addParticle(type, x + 0.25, y, z + 0.25, Math.cos(i) * 0.25d * modifier, 0.2d, Math.sin(i) * 0.25d * modifier);
        }
    }
    public static void addParticlesWithDelay(ParticleOptions type, Level level, double x, double y, double z, float modifier,int delay) {
        for (int i = 0; i < 360; i++) {
            if (i % 20 == 0) {
                int finalI = i;
                queueServerWork(finalI * 5 + delay,()-> level.addParticle(type, x + 0.25, y, z + 0.25, Math.cos(finalI) * 0.25d * modifier, 0.2d, Math.sin(finalI) * 0.25d * modifier));
            }
        }
    }
    public static void playPlayerAnimation(Player player,String path){
        var animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) player).get(new ResourceLocation(WrathyArmament.MODID, "animation"));
        if (animation != null)
            animation.setAnimation(new KeyframeAnimationPlayer(PlayerAnimationRegistry.getAnimation(new ResourceLocation(WrathyArmament.MODID, path))));
    }
}