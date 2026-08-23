package net.napsternpt.morebbboathud.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.napsternpt.morebbboathud.MoreBBBoatHudClient;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {
	@Inject(method = "onMouseButton", at = @At("HEAD"), cancellable = true)
	private void freeMouseWhileButtonHeld(long window, int button, int action, int mods, CallbackInfo ci) {
		if (action != GLFW.GLFW_PRESS) {
			return;
		}
		MinecraftClient client = MinecraftClient.getInstance();
		if (client.currentScreen == null && MoreBBBoatHudClient.BUTTON_KEY.isPressed()) {
			ci.cancel();
			double scale = client.getWindow().getScaleFactor();
			MoreBBBoatHudClient.CLICKED = true;
			MoreBBBoatHudClient.CLICK_X = client.mouse.getX() / scale;
			MoreBBBoatHudClient.CLICK_Y = client.mouse.getY() / scale;
		}
	}
}
