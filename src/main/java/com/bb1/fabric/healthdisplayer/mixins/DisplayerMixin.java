package com.bb1.fabric.healthdisplayer.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.bb1.fabric.healthdisplayer.NameDisplayer;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

@Mixin(LivingEntity.class)
public class DisplayerMixin {
	
	@Inject(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", at = @At("HEAD"))
	public void onDamageShowHealth(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
		if (((Object)this) instanceof LivingEntity mob) { new NameDisplayer(mob); }
	}
	
}
