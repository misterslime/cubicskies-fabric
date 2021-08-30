package net.misterslime.cubicskies.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ServerLevelData;
import net.misterslime.cubicskies.clouds.CloudHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerLevel.class)
public class MixinServerLevel {

    @Shadow private final ServerLevelData serverLevelData;

    public MixinServerLevel(ServerLevelData serverLevelData) {
        this.serverLevelData = serverLevelData;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tickClouds(BooleanSupplier booleanSupplier, CallbackInfo ci) {
        CloudHandler.speed = this.serverLevelData.getGameTime() * 0.03f;
    }
}
