package com.baymaxawa.test_mod.mixin;

import com.baymaxawa.test_mod.TestMod;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(World.class)
public class WorldMixin {
	@Inject(method = "load", at = @At("HEAD"), cancellable = true)
	private void loadInjection(CallbackInfo info) {
		TestMod.LOGGER.info("world load injected.");
		File file = new File("level.dat");
		if (!file.exists()) {
			TestMod.LOGGER.warn("level.dat not found!");
			info.cancel();
		}
	}
}
