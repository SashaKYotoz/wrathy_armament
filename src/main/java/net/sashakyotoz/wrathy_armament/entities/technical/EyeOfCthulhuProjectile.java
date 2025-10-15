package net.sashakyotoz.wrathy_armament.entities.technical;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import net.sashakyotoz.anitexlib.client.particles.parents.options.ColorableParticleOption;

@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class EyeOfCthulhuProjectile extends AbstractArrow implements ItemSupplier {
    private static final EntityDataAccessor<Integer> TIME_TO_BOOM = SynchedEntityData.defineId(EyeOfCthulhuProjectile.class, EntityDataSerializers.INT);

    public EyeOfCthulhuProjectile(EntityType<EyeOfCthulhuProjectile> type, Level level) {
        super(type, level);
    }

    public EyeOfCthulhuProjectile(EntityType<EyeOfCthulhuProjectile> type, LivingEntity entity, Level level) {
        super(type, entity, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TIME_TO_BOOM, 30);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public void tick() {
        super.tick();
        this.entityData.set(TIME_TO_BOOM, this.entityData.get(TIME_TO_BOOM) - 1);
        if (this.entityData.get(TIME_TO_BOOM) < 0) {
            if (this.getOwner() instanceof LivingEntity entity) {
                this.level().explode(this, this.getX(), this.getY(), this.getZ(), 1.5f, Level.ExplosionInteraction.NONE);
                FireworkRocketEntity firework = new FireworkRocketEntity(this.level(), getFirework(), entity);
                firework.moveTo(this.getOnPos().getCenter());
                this.level().addFreshEntity(firework);
            }
            this.discard();
        }
        if (this.inGround)
            this.discard();
        else if (this.tickCount % 2 == 0)
            this.level().addParticle(new ColorableParticleOption("sparkle", 0f, 0.35f, 1f), this.getX(), this.getY(), this.getZ(), 0, 0, 0);
    }

    private ItemStack getFirework() {
        ItemStack stack = Items.FIREWORK_ROCKET.getDefaultInstance();
        CompoundTag fireworkTag = new CompoundTag();
        CompoundTag fireworksTag = new CompoundTag();
        int[] colors = new int[]{6085589};
        ListTag explosionsTag = new ListTag();
        CompoundTag explosionTag = new CompoundTag();
        explosionTag.putInt("Type", 1);
        explosionTag.putIntArray("Colors", colors);
        explosionTag.putIntArray("FadeColors", new int[]{6724056});
        explosionTag.putBoolean("Flicker", true);
        explosionTag.putBoolean("Trail", true);
        explosionsTag.add(explosionTag);
        fireworksTag.put("Explosions", explosionsTag);
        fireworksTag.putInt("Flight", -2);
        fireworkTag.put("Fireworks", fireworksTag);
        stack.setTag(fireworkTag);
        return stack;
    }

    @Override
    public ItemStack getItem() {
        return Items.GHAST_TEAR.getDefaultInstance();
    }
}
