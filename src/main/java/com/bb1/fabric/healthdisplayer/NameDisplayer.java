package com.bb1.fabric.healthdisplayer;

import java.util.Set;

import com.bb1.fabric.bfapi.timings.timers.AbstractTimer;
import com.bb1.fabric.bfapi.timings.timers.SystemTimer;
import com.google.common.collect.Sets;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.text.LiteralText;

public class NameDisplayer extends ArmorStandEntity {
	
	public static final Set<NameDisplayer> DISPLAYS = Sets.newConcurrentHashSet();
	
	private final LivingEntity bound;
	private final AbstractTimer timer;

	public NameDisplayer(LivingEntity entityToBindTo) {
		super(entityToBindTo.getEntityWorld(), entityToBindTo.getX(), entityToBindTo.getY(), entityToBindTo.getZ());
		this.bound = entityToBindTo;
		this.timer = new SystemTimer(Loader.getConfig().displayTime);
		setCustomName(new LiteralText("FHD_HEALTH_DISPLAY"));
		setInvisible(true);
		setInvulnerable(true);
		setNoGravity(true);
		setCustomNameVisible(true);
		final int id = entityToBindTo.getId();
		DISPLAYS.forEach((display)->{ if (id==display.getBound().getId()) { display.kill(); } });
		DISPLAYS.add(this);
		entityToBindTo.getEntityWorld().spawnEntity(this);
	}
	
	public final LivingEntity getBound() { return this.bound; }
	
	@Override
	public void tick() {
		if (this.timer.hasEnded()||!this.bound.isAlive()) { kill(); }
		else {
			refreshPositionAfterTeleport(this.bound.getEyePos().subtract(0, 1.75, 0));
		}
		super.tick();
	}
	
	@Override
	public boolean canMoveVoluntarily() {
		return false;
	}
	
	@Override
	public boolean collides() {
		return false;
	}
	
	@Override
	public void kill() {
		DISPLAYS.remove(this);
		super.kill();
	}
	
}