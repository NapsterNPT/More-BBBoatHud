package net.napsternpt.morebbboathud.mixin;

import net.napsternpt.morebbboathud.MoreBBBoatHudClient;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import uk.co.cablepost.bb_boat_hud.client.BbBoatHudClient;
import uk.co.cablepost.bb_boat_hud.config.ModConfig;

@Mixin(BbBoatHudClient.class)
public class BbBoatHudClientMixin {
	@Inject(method = "runLua", at = @At("TAIL"))
	private static void clearClickAfterLua(CallbackInfo ci) {
		MoreBBBoatHudClient.CLICKED = false;
	}

	@Redirect(
			method = "renderHud",
			at = @At(
					value = "FIELD",
					target = "Luk/co/cablepost/bb_boat_hud/config/ModConfig;runLuaSkipFrames:I",
					opcode = Opcodes.GETFIELD
			)
	)
	private static int runEveryFrameWhileMenuOpen(ModConfig config) {
		return MoreBBBoatHudClient.BUTTON_KEY.isPressed() ? 1 : config.runLuaSkipFrames;
	}
}
