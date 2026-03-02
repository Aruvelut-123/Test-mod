package com.baymaxawa.test_mod.mixin;

import com.baymaxawa.test_mod.TestMod;
import com.mojang.rubydung.RubyDung;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;

import java.io.IOException;

@Mixin(RubyDung.class)
public class RubyDungMixin {
	@ModifyArgs(method = "init()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;<init>(III)V"))
	private static void worldInitInjection(Args args) throws IOException {
		TestMod.init();
		args.set(0, TestMod.sizeX);
		args.set(1, TestMod.sizeZ);
		args.set(2, TestMod.sizeY);
		TestMod.LOGGER.info("Resizing world to {} x {} x {}.", TestMod.sizeX, TestMod.sizeY, TestMod.sizeZ);
	}
}
