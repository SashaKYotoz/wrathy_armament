package net.sashakyotoz.wrathy_armament.entities.ai_goals.bosses.habciak;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedGoldenAppleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.sashakyotoz.anitexlib.client.particles.parents.options.ColorableParticleOption;
import net.sashakyotoz.wrathy_armament.entities.bosses.Habciak;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;

public class HabciakBypassGoal extends Goal {
    private final Habciak habciak;
    private int attackTime = 0;

    public HabciakBypassGoal(Habciak habciak) {
        this.habciak = habciak;
    }

    @Override
    public void start() {
        if (this.habciak.level().isClientSide())
            this.habciak.jump.start(this.habciak.tickCount);
    }

    @Override
    public boolean canUse() {
        return this.habciak.getTarget() != null && !this.habciak.isDeadOrDying();
    }

    public void tick() {
        LivingEntity target = this.habciak.getTarget();
        if (target != null && target.distanceToSqr(this.habciak) > 16 && this.habciak.hasLineOfSight(target)) {
            if (attackTime == 50) {
                attackTime = 0;
                this.habciak.spawnParticle(new ColorableParticleOption("cube", 1, 1, 1),
                        this.habciak.level(), this.habciak.getX(), this.habciak.getY() + 0.5f, this.habciak.getZ(), 1.5f);
                this.habciak.teleportTo(
                        target.getX() + OnActionsTrigger.getXVector(-2, target.getYRot()),
                        target.getY(),
                        target.getZ() + OnActionsTrigger.getZVector(-2, target.getYRot()));
                this.habciak.playSound(SoundEvents.ILLUSIONER_PREPARE_MIRROR);
                this.habciak.reassessAttackGoal(this.habciak.getRandom().nextInt(4));
            } else if (attackTime < 50) {
                attackTime++;
                if (this.habciak.getHealth() < 480){
                    if (!(this.habciak.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof EnchantedGoldenAppleItem))
                        this.habciak.setItemInHand(InteractionHand.MAIN_HAND,new ItemStack(Items.ENCHANTED_GOLDEN_APPLE));
                    else{
                        this.habciak.startUsingItem(InteractionHand.MAIN_HAND);
                        if (this.habciak.tickCount % 5 == 0){
                            this.habciak.heal(1);
                            if (target instanceof Player player)
                                this.habciak.level().playSound(player,this.habciak.getOnPos(), SoundEvents.GENERIC_EAT, SoundSource.HOSTILE);
                        }
                    }
                }
                this.habciak.getMoveControl().strafe(-2, 0);
            }
            this.habciak.getNavigation().stop();
        } else {
            if (target != null)
                this.habciak.getNavigation().moveTo(target, 0.5);
        }
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }
}
