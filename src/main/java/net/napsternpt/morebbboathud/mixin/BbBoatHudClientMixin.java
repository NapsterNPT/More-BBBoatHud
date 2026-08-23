package net.napsternpt.morebbboathud.mixin;

import net.napsternpt.morebbboathud.MoreBBBoatHudClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import uk.co.cablepost.bb_boat_hud.client.BbBoatHudClient;

@Mixin(BbBoatHudClient.class)
public class BbBoatHudClientMixin {
	@Inject(method = "runLua", at = @At("TAIL"))
	private static void clearClickAfterLua(CallbackInfo ci) {
		MoreBBBoatHudClient.CLICKED = false;
	}
}
