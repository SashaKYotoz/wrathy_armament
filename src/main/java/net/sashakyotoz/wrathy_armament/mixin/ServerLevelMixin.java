package net.sashakyotoz.wrathy_armament.mixin;

import net.minecraft.server.level.ServerLevel;
import net.sashakyotoz.wrathy_armament.entities.spawners.GuideSpawner;
import net.sashakyotoz.wrathy_armament.entities.spawners.SashaKYotozSpawner;
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
    private final SashaKYotozSpawner wrathy_armament$sashaKYotozSpawner = new SashaKYotozSpawner();

    @Unique
    private final GuideSpawner wrathy_armament$guideSpawner = new GuideSpawner();
    @Inject(method = "tickCustomSpawners", at = @At("HEAD"))
    private void tickSpawner(boolean pSpawnEnemies, boolean pSpawnFriendlies, CallbackInfo ci){
        wrathy_armament$sashaKYotozSpawner.tick(getLevel(), pSpawnEnemies, pSpawnFriendlies);
        wrathy_armament$guideSpawner.tick(getLevel(),pSpawnEnemies,pSpawnFriendlies);
    }
}
