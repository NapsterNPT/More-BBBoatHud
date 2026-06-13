package net.napsternpt.morebbboathud.mixin;

import net.minecraft.client.MinecraftClient;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaBoolean;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import uk.co.cablepost.bb_boat_hud.client.HudModule;

@Mixin(HudModule.class)
public class HudModuleMixin {
	@Shadow
	private Globals globals;

	@Inject(method = "runLua", at = @At("HEAD"))
	private void onRunLua(CallbackInfo ci) {
		var options = MinecraftClient.getInstance().options;
		globals.set("pressingSpace", LuaBoolean.valueOf(options.jumpKey.isPressed()));
		globals.set("pressingLeftClick", LuaBoolean.valueOf(options.attackKey.isPressed()));
		globals.set("pressingRightClick", LuaBoolean.valueOf(options.useKey.isPressed()));
	}
}
