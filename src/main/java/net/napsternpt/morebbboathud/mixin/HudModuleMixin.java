package net.napsternpt.morebbboathud.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.napsternpt.morebbboathud.MoreBBBoatHudClient;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaBoolean;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import uk.co.cablepost.bb_boat_hud.client.HudModule;

import java.io.IOException;

@Mixin(HudModule.class)
public class HudModuleMixin {
	@Shadow
	private Globals globals;

	@Shadow
	private Identifier identifier;

	@Inject(method = "runLua", at = @At("HEAD"))
	private void onRunLua(CallbackInfo ci) {
		var options = MinecraftClient.getInstance().options;
		globals.set("pressingSpace", LuaBoolean.valueOf(options.jumpKey.isPressed()));
		globals.set("pressingLeftClick", LuaBoolean.valueOf(options.attackKey.isPressed()));
		globals.set("pressingRightClick", LuaBoolean.valueOf(options.useKey.isPressed()));
		globals.set("touchingBlock", LuaBoolean.valueOf(MoreBBBoatHudClient.BLOCK_COLLISION));
		globals.set("touchingEntity", LuaBoolean.valueOf(MoreBBBoatHudClient.ENTITY_COLLISION));
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void onInit(CallbackInfo ci) {
		Identifier id = identifier;
		globals.set("print", createPrintFunction(id));
		globals.set("getWidth", createGetSizeFunction(id, true));
		globals.set("getHeight", createGetSizeFunction(id, false));
	}

	private static LuaValue createPrintFunction(Identifier id) {
		return new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs v) {
				String message = v.checkjstring(1);

				String[] parts = id.toString().split("/");
				String filename = parts[parts.length - 1];
				String nameWithoutExt = filename.replace(".lua", "");
				String[] words = nameWithoutExt.split("_");
				String moduleName = Character.toUpperCase(words[0].charAt(0)) + words[0].substring(1);

				Text text = Text.literal("[MoreBBBoatHud] ")
						.append(Text.literal(message).styled(style ->
								style
										.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
												Text.literal("Message sent by module: ")
														.append(Text.literal(moduleName))
										))
										.withFormatting(Formatting.BLUE)
										.withFormatting(Formatting.UNDERLINE)
						));
				assert MinecraftClient.getInstance().player != null;
				MinecraftClient.getInstance().player.sendMessage(text, false);
				return LuaValue.NIL;
			}
		};
	}

	private static LuaValue createGetSizeFunction(Identifier id, boolean returnWidth) {
		return new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs v) {
				String path = v.checkjstring(1);
				Identifier texId = Identifier.of(id.getNamespace(), "textures/bb_boat_hud_modules/" + path);
				var optResource = MinecraftClient.getInstance().getResourceManager().getResource(texId);
				if (optResource.isEmpty()) {
					return LuaValue.valueOf(0);
				}
				try (var in = optResource.get().getInputStream()) {
					NativeImage image = NativeImage.read(in);
					int size = returnWidth ? image.getWidth() : image.getHeight();
					image.close();
					return LuaValue.valueOf(size);
				} catch (IOException e) {
					return LuaValue.valueOf(0);
				}
			}
		};
	}
}
