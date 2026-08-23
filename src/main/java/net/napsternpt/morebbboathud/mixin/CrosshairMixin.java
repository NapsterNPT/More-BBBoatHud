package net.napsternpt.morebbboathud.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import net.napsternpt.morebbboathud.MoreBBBoatHudClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class CrosshairMixin {
	@Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
	private void morebbboathud$hideCrosshair(CallbackInfo ci) {
		if (MoreBBBoatHudClient.BUTTON_KEY.isPressed()) {
			ci.cancel();
		}
	}
}
