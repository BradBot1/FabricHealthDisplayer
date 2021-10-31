package com.bb1.fabric.healthdisplayer.mixins;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.bb1.fabric.healthdisplayer.Loader;
import com.bb1.fabric.healthdisplayer.NameDisplayer;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker.Entry;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

@Mixin(ServerPlayNetworkHandler.class)
public class EntityNameChangerMixin {
	
	@Inject(method = "sendPacket(Lnet/minecraft/network/Packet;Lio/netty/util/concurrent/GenericFutureListener;)V", at = @At("HEAD"))
	public void onSendPacketCheckIfAllowed(Packet<?> packet, @Nullable GenericFutureListener<? extends Future<? super Void>> listener, CallbackInfo callbackInfo) {
		if (packet instanceof EntityTrackerUpdateS2CPacket entityTrackerPacket) {
			for (NameDisplayer displayer : NameDisplayer.DISPLAYS) {
				if (displayer.getId()==entityTrackerPacket.id()) {
					final float health = displayer.getBound().getHealth(),
								maxHealth = displayer.getBound().getMaxHealth();
					final Style borderStyle = Style.EMPTY.withColor(Formatting.GREEN),
								dataStyle = Style.EMPTY.withColor(Formatting.WHITE);
					Text displayText;
					switch (Loader.getDisplayMode(((ServerPlayNetworkHandler)(Object)this).getPlayer().getUuid())) {
						case 1: // hearts ♥
							displayText = new LiteralText("[").setStyle(borderStyle).append(new LiteralText(Float.toString(health/2)+"♥").setStyle(dataStyle).append(new LiteralText("]").setStyle(borderStyle)));
							break;
						case 2: // percent %
							displayText = new LiteralText("[").setStyle(borderStyle).append(new LiteralText(new DecimalFormat("0.00").format((health/maxHealth)*100)+"%").setStyle(dataStyle).append(new LiteralText("]").setStyle(borderStyle)));
							break;
						case 3: // fraction /
							displayText = new LiteralText("[").setStyle(borderStyle).append(new LiteralText(new DecimalFormat("0.00").format(health)).setStyle(dataStyle).append(new LiteralText("/").setStyle(borderStyle).append(new LiteralText(new DecimalFormat("0.00").format(maxHealth)).setStyle(dataStyle).append(new LiteralText("]").setStyle(borderStyle)))));
							break;
						default: 
							List<Entry<?>> updatedTrackedValues = new ArrayList<Entry<?>>();
							for (Entry<?> entry : entityTrackerPacket.trackedValues) {
								if (entry.getData()!=Entity.NAME_VISIBLE) {
									updatedTrackedValues.add(entry);
								} else {
									updatedTrackedValues.add(new Entry<Boolean>(Entity.NAME_VISIBLE, false));
								}
							}
							entityTrackerPacket.trackedValues = updatedTrackedValues;
							return;
					}
					// Here we are going to clone the tracked values array and move it to a new array since the array could be immutable
					List<Entry<?>> updatedTrackedValues = new ArrayList<Entry<?>>();
					for (Entry<?> entry : entityTrackerPacket.trackedValues) {
						if (entry.getData()!=Entity.CUSTOM_NAME) {
							updatedTrackedValues.add(entry);
						} else {
							updatedTrackedValues.add(new Entry<Optional<Text>>(Entity.CUSTOM_NAME, Optional.of(displayText)));
						}
					}
					entityTrackerPacket.trackedValues = updatedTrackedValues;
				}
			}
		}
	}
	
}
