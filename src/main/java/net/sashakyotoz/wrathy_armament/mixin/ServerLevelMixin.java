package net.sashakyotoz.wrathy_armament.mixin;

import net.minecraft.server.level.ServerLevel;
import net.sashakyotoz.wrathy_armament.entities.bosses.spawner.SashaKYotozSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {

    @Shadow public abstract ServerLevel getLevel();
    @Unique
    private final SashaKYotozSpawner sashaKYotozSpawner = new SashaKYotozSpawner();
    @Inject(method = "tickCustomSpawners", at = @At("RETURN"))
    private void tickSpawner(boolean p_8800_, boolean p_8801_, CallbackInfo ci){
        sashaKYotozSpawner.tick(getLevel(), p_8800_, p_8801_);
    }
}
