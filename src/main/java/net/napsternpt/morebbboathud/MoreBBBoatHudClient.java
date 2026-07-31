package net.napsternpt.morebbboathud;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.AbstractBoatEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoreBBBoatHudClient implements ClientModInitializer {
	private static final Logger LOGGER = LoggerFactory.getLogger("morebbboathud");

	public static boolean BLOCK_COLLISION = false;
	public static boolean ENTITY_COLLISION = false;

	private static boolean lastBlockCollision;
	private static boolean lastEntityCollision;

	@Override
	public void onInitializeClient() {
		ClientTickEvents.END_WORLD_TICK.register(world -> {
			var player = MinecraftClient.getInstance().player;
			if (player != null && player.getVehicle() instanceof AbstractBoatEntity boat) {
				boolean hc = boat.horizontalCollision;
				boolean vc = boat.verticalCollision;
				ENTITY_COLLISION = isTouchingEntity(boat, player);
				BLOCK_COLLISION = hc && !ENTITY_COLLISION;
				if (BLOCK_COLLISION != lastBlockCollision) {
					LOGGER.info("BLOCK_COLLISION {} -> {} (boat horizontalCollision={} verticalCollision={}, boatPos={})",
							lastBlockCollision, BLOCK_COLLISION, hc, vc, boat.getBlockPos());
					lastBlockCollision = BLOCK_COLLISION;
				}
				if (ENTITY_COLLISION != lastEntityCollision) {
					LOGGER.info("ENTITY_COLLISION {} -> {}", lastEntityCollision, ENTITY_COLLISION);
					lastEntityCollision = ENTITY_COLLISION;
				}
			} else {
				BLOCK_COLLISION = false;
				ENTITY_COLLISION = false;
			}
		});
	}

	private static boolean isTouchingEntity(AbstractBoatEntity boat, Entity rider) {
		var world = MinecraftClient.getInstance().world;
		if (world == null) {
			return false;
		}

		var boatBox = boat.getBoundingBox();
		var searchBox = boatBox.expand(0.5);
		for (var other : world.getOtherEntities(boat, searchBox, Entity::isCollidable)) {
			if (other == rider) {
				continue;
			}
			if (boatBox.expand(0.1).intersects(other.getBoundingBox())) {
				return true;
			}
		}
		return false;
	}
}
