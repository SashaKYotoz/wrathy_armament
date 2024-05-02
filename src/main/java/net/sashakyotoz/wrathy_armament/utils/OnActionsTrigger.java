package net.sashakyotoz.wrathy_armament.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.blocks.MythrilAnvil;
import net.sashakyotoz.wrathy_armament.blocks.gui.MythrilAnvilMenu;
import net.sashakyotoz.wrathy_armament.entities.alive.LichMyrmidon;
import net.sashakyotoz.wrathy_armament.entities.bosses.LichKing;
import net.sashakyotoz.wrathy_armament.entities.technical.JohannesSpearEntity;
import net.sashakyotoz.wrathy_armament.entities.technical.ParticleLikeEntity;
import net.sashakyotoz.wrathy_armament.entities.technical.ZenithEntity;
import net.sashakyotoz.wrathy_armament.items.MasterSword;
import net.sashakyotoz.wrathy_armament.registers.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Supplier;

@Mod.EventBusSubscriber
public class OnActionsTrigger {
    private static int zenithIndex = 0;
    private static int timer;
    public static int shakingTime = 0;
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
    public static void onClickEvent(AttackEntityEvent event) {
        Player player = event.getEntity();
        if (player.getMainHandItem().is(WrathyArmamentItems.BLADE_OF_CHAOS.get()) && player.level() instanceof ServerLevel level) {
            ParticleLikeEntity particleEntity = new ParticleLikeEntity(WrathyArmamentEntities.PARTICLE_LIKE_ENTITY.get(), level, 0.6f, true, false, 13,
                    ParticleTypes.ASH, "cycle");
            particleEntity.moveTo(new Vec3(player.getX(), player.getY() + 1, player.getZ()));
            level.addFreshEntity(particleEntity);
        }
        if (player.getMainHandItem().is(WrathyArmamentItems.MASTER_SWORD.get()) && player.level() instanceof ServerLevel level) {
            ParticleLikeEntity particleEntity = new ParticleLikeEntity(WrathyArmamentEntities.PARTICLE_LIKE_ENTITY.get(), level, 0.6f, true, true, 3,
                    ParticleTypes.ELECTRIC_SPARK, "semicycle");
            particleEntity.setOwner(player);
            particleEntity.moveTo(new Vec3(player.getX(), player.getY() + 1, player.getZ()));
            level.addFreshEntity(particleEntity);
        }
        if (player.getMainHandItem().is(WrathyArmamentItems.MURASAMA.get()) && player.level() instanceof ServerLevel level) {
            ParticleLikeEntity particleEntity = new ParticleLikeEntity(WrathyArmamentEntities.PARTICLE_LIKE_ENTITY.get(), level, 0.6f, true, true, 4,
                    ParticleTypes.FLAME, "semicycle");
            particleEntity.setOwner(player);
            particleEntity.moveTo(new Vec3(player.getX(), player.getY() + 1, player.getZ()));
            level.addFreshEntity(particleEntity);
        }
    }

    @SubscribeEvent
    public static void onPlayerAttack(LivingAttackEvent event) {
        LivingEntity entity = event.getEntity();
        int damage = 0;
        if (event.getSource().getEntity() instanceof Player player && player.getItemInHand(InteractionHand.MAIN_HAND).getEnchantmentLevel(WrathyArmamentEnchants.NIGHTMARE_JUMPING.get()) > 0) {
            if (Math.random() > 0.75)
                damage = RandomSource.create().nextInt(3, 6);
            player.setPos(event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ());
            player.playSound(WrathyArmamentSounds.ITEM_LANCER_SHOT);
            entity.hurt(new DamageSource(event.getEntity().level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC)) {
                @Override
                public Component getLocalizedDeathMessage(@NotNull LivingEntity _msgEntity) {
                    return Component.translatable("death.attack.wrathy_armament.nightmare_jumping");
                }
            }, event.getAmount() + damage);
            if (entity.isAlive())
                entity.setDeltaMovement(0, RandomSource.create().nextInt(5, 15) / 10f, 0);
        }
        if (event.getSource().getEntity() instanceof LivingEntity livingEntity && livingEntity.getMainHandItem().is(WrathyArmamentItems.MASTER_SWORD.get())) {
            masterSwordAttack(livingEntity.level(), entity, event.getSource().getEntity().getYRot(), event.getSource().getEntity().getXRot());
        }
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
        Level level = event.getLevel();
        if (stack.is(WrathyArmamentItems.ZENITH.get()) && level instanceof ServerLevel serverLevel && !player.getCooldowns().isOnCooldown(stack.getItem())) {
            zenithAbility(player, serverLevel);
            if (zenithIndex > 3) {
                stack.getOrCreateTag().putDouble("CustomModelData", 0);
                player.getCooldowns().addCooldown(stack.getItem(), 60);
            } else {
                zenithIndex++;
                timer = 30;
                stack.getOrCreateTag().putDouble("CustomModelData", zenithIndex > 0 ? 1 : 0);
                player.getCooldowns().addCooldown(stack.getItem(), 10);
            }
        }
        if (player.isCrouching() && stack.is(WrathyArmamentItems.FROSTMOURNE.get())){
            if (player.getMainHandItem().getOrCreateTag().getInt("charge") > 1 && level instanceof ServerLevel serverLevel){
                player.getMainHandItem().getOrCreateTag().putInt("charge", player.getMainHandItem().getOrCreateTag().getInt("charge") - 1);
                LichMyrmidon lichMyrmidon = new LichMyrmidon(WrathyArmamentEntities.LICH_MYRMIDON.get(),serverLevel);
                lichMyrmidon.moveTo(player.getX(),player.getY()+1, player.getZ());
                lichMyrmidon.setHaveToFindOwner(true);
                level.addFreshEntity(lichMyrmidon);
            }
        }
    }
    @SubscribeEvent
    public static void tick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        TickEvent.Phase phase = event.phase;
        if (timer > 0)
            timer--;
        if (phase == TickEvent.Phase.END && player.getInventory().contains(new ItemStack(WrathyArmamentItems.ZENITH.get()))) {
            ItemStack stack = player.getInventory().getItem(player.getInventory().findSlotMatchingItem(new ItemStack(WrathyArmamentItems.ZENITH.get())));
            if (stack.getOrCreateTag().getDouble("CustomModelData") == 1 && timer <= 0)
                stack.getOrCreateTag().putDouble("CustomModelData", 0);
        }
    }
    @SubscribeEvent
    public static void onBlockLeftClick(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();
        Level level = event.getLevel();
        if (stack.is(WrathyArmamentItems.JOHANNES_SWORD.get()) && level instanceof ServerLevel serverLevel)
            johannesSwordAbility(player, stack, serverLevel);
        if (stack.getEnchantmentLevel(WrathyArmamentEnchants.PHANTOQUAKE.get()) > 0 && event.getHand() == InteractionHand.MAIN_HAND && event.getLevel().getBlockState(event.getPos().above(3)).isAir() && !event.getEntity().getCooldowns().isOnCooldown(event.getItemStack().getItem())) {
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
        if (minecraft.getCameraEntity() instanceof Player player && !player.isSpectator() && player.distanceToSqr(vec3) < 12){
            float delta = Minecraft.getInstance().getFrameTime();
            float ticksExistedDelta = player.tickCount + delta;
            float intensity = 0.025f;
            if (!minecraft.isPaused() && player.level().isClientSide() && shakingTime > 0) {
                event.setPitch((float) (event.getPitch() + intensity * Math.cos(ticksExistedDelta * 3 + 2) * 25));
                event.setYaw((float) (event.getYaw() + intensity * Math.cos(ticksExistedDelta * 5 + 1) * 25));
                event.setRoll((float) (event.getRoll() + intensity * Math.cos(ticksExistedDelta * 4) * 25));
            }
        }
    }
    public static double getXVector(double speed, double yaw) {
        return speed * Math.cos((yaw + 90) * (Math.PI / 180));
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



    private static void masterSwordAttack(Level level, LivingEntity target, double yaw, double xRot) {
        if (target.getRandom().nextBoolean()) {
            if (level instanceof ServerLevel serverLevel) {
                LightningBolt entityToSpawn = EntityType.LIGHTNING_BOLT.create(serverLevel);
                entityToSpawn.moveTo(Vec3.atBottomCenterOf(BlockPos.containing(target.getX(), target.getY(), target.getZ())));
                serverLevel.addFreshEntity(entityToSpawn);
                ParticleLikeEntity particleEntity = new ParticleLikeEntity(WrathyArmamentEntities.PARTICLE_LIKE_ENTITY.get(), level, 0.2f, true, false, 2,
                        ParticleTypes.ELECTRIC_SPARK, "semicycle");
                particleEntity.moveTo(new Vec3(target.getX(), target.getY() + 1, target.getZ()));
                level.addFreshEntity(particleEntity);
            }
        } else {
            if (!target.level().isClientSide()) {
                target.setSecondsOnFire(15);
            }
            if (target.level() instanceof ServerLevel serverLevel) {
                int tmp = RandomSource.create().nextInt(5, 11) + 3;
                for (int i = 0; i < tmp; i++) {
                    serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, target.getX() + getXVector(i, yaw), target.getY() + 1, target.getZ() + getZVector(i, yaw), 27, 1 + getXVector(i, yaw), 1, 1 + getZVector(i, yaw), 1);
                }
            }
        }
    }

    private static void zenithAbility(Player player, ServerLevel level) {
        if (zenithIndex > 3)
            zenithIndex = 0;
        else
            zenithIndex++;
        WrathyArmament.LOGGER.debug(zenithIndex + "\r");
        ZenithEntity zenith = new ZenithEntity(level, player, zenithIndex);
        WrathyArmament.LOGGER.debug(zenith.timer + "<- timer ticks");
        zenith.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3.0F, 1.0F);
        level.addFreshEntity(zenith);
    }

    private static void johannesSwordAbility(Player player, ItemStack stack, ServerLevel level) {
        double Yaw = player.getYRot();
        double d0 = getXVector(3, Yaw);
        double d1 = player.getXRot() * (-0.025);
        double d2 = getZVector(3, Yaw);
        for (int l = 0; l < 16; ++l) {
            createSpellEntity(level, player, d0, d1, d2, l);
        }
    }

    private static void createSpellEntity(ServerLevel level, Player player, double vx, double vy, double vz, int i) {
        level.addFreshEntity(new JohannesSpearEntity(level, player.getX() + vx * i, player.getY() + vy, player.getZ() + vz * i, 0, i, player));
        level.gameEvent(GameEvent.ENTITY_PLACE, new Vec3(player.getX() + vx * i, player.getY() + vy, player.getZ() + vz * i), GameEvent.Context.of(player));
    }
}

