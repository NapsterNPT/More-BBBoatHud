package net.napsternpt.morebbboathud.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.napsternpt.morebbboathud.MoreBBBoatHudClient;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaBoolean;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import uk.co.cablepost.bb_boat_hud.client.HudModule;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

@Mixin(HudModule.class)
public abstract class HudModuleMixin {
	private static final Identifier BTN_NORMAL = Identifier.ofVanilla("widget/button");
	private static final Identifier BTN_HIGHLIGHTED = Identifier.ofVanilla("widget/button_highlighted");
	private static final Identifier BTN_DISABLED = Identifier.ofVanilla("widget/button_disabled");

	@Shadow
	private Globals globals;

	@Shadow
	public String error;

	@Shadow
	private List<Consumer<DrawContext>> drawCalls;

    @Shadow
	public abstract Identifier getIdentifier();

	@Inject(method = "runLua", at = @At("HEAD"))
	private void onRunLua(CallbackInfo ci) {
		var options = MinecraftClient.getInstance().options;
		globals.set("addon", LuaBoolean.valueOf(true));
		globals.set("pressingSpace", LuaBoolean.valueOf(options.jumpKey.isPressed()));
		globals.set("pressingLeftClick", LuaBoolean.valueOf(options.attackKey.isPressed()));
		globals.set("pressingRightClick", LuaBoolean.valueOf(options.useKey.isPressed()));
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void onInit(CallbackInfo ci) {
		globals.set("print", printFunction());
		globals.set("renderButton", renderButton());
		globals.set("onMenuOpen", onMenuOpen());
		globals.set("getWidth", getSize(true));
		globals.set("getHeight", getSize(false));
	}

	@Inject(method = "render", at = @At("RETURN"))
	private void onRenderReturn(DrawContext ctx, CallbackInfoReturnable<String> cir) {
		while (!MoreBBBoatHudClient.PENDING_CALLBACKS.isEmpty()) {
			LuaValue callback = MoreBBBoatHudClient.PENDING_CALLBACKS.removeFirst();
			try {
				callback.call();
			} catch (LuaError e) {
				error = "renderButton: " + e.getMessage();
			}
		}
	}

	private LuaValue printFunction() {
		return new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs v) {
				String message = v.checkjstring(1);

				String[] parts = getIdentifier().toString().split("/");
				String filename = parts[parts.length - 1];
				String nameWithoutExt = filename.replace(".lua", "");
				String[] words = nameWithoutExt.split("_");
				String moduleName = Character.toUpperCase(words[0].charAt(0)) + words[0].substring(1);

				Text text = Text.literal("[MoreBBBoatHud] ")
						.append(Text.literal(message).styled(style -> style
										.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
												Text.literal("Message sent by module: ").append(Text.literal(moduleName)))
										)
										.withFormatting(Formatting.BLUE).withFormatting(Formatting.UNDERLINE))
						);
				assert MinecraftClient.getInstance().player != null;
				MinecraftClient.getInstance().player.sendMessage(text, false);
				return LuaValue.NIL;
			}
		};
	}

	private LuaValue renderButton() {
		return new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs v) {
				int width = v.checkint(1);
				int height = v.checkint(2);
				boolean canBePressed = v.optboolean(3, true);
				String anchor = v.optjstring(4, "MIDDLE_CENTER");
				LuaValue callback = v.checkfunction(5);

				var client = MinecraftClient.getInstance();
				double scale = client.getWindow().getScaleFactor();
				double mouseX = client.mouse.getX() / scale;
				double mouseY = client.mouse.getY() / scale;

				int[] pos = anchoredPos(anchor, width, height);
				final int fx0 = pos[0];
				final int fy0 = pos[1];
				final int fw = width;
				final int fh = height;
				final double fmx = mouseX;
				final double fmy = mouseY;

				drawCalls.add(ctx -> {
					Matrix4f inv = new Matrix4f(ctx.getMatrices().peek().getPositionMatrix());
					inv.invert();

					Vector3f mouseLocal = new Vector3f((float) fmx, (float) fmy, 0f).mulPosition(inv);
					boolean hovered = canBePressed
							&& mouseLocal.x >= fx0 && mouseLocal.x < fx0 + fw
							&& mouseLocal.y >= fy0 && mouseLocal.y < fy0 + fh;

					if (canBePressed && MoreBBBoatHudClient.CLICKED) {
						Vector3f clickLocal = new Vector3f((float) MoreBBBoatHudClient.CLICK_X, (float) MoreBBBoatHudClient.CLICK_Y, 0f).mulPosition(inv);
						if (clickLocal.x >= fx0 && clickLocal.x < fx0 + fw
								&& clickLocal.y >= fy0 && clickLocal.y < fy0 + fh) {
							MoreBBBoatHudClient.CLICKED = false;
							MoreBBBoatHudClient.PENDING_CALLBACKS.add(callback);
						}
					}

					Identifier tex = !canBePressed ? BTN_DISABLED : hovered ? BTN_HIGHLIGHTED : BTN_NORMAL;
					ctx.drawGuiTexture(RenderLayer::getGuiTextured, tex, fx0, fy0, fw, fh);
				});
				return LuaValue.NIL;
			}
		};
	}

	private LuaValue onMenuOpen() {
		return new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs v) {
				String anchor = v.optjstring(1, "MIDDLE_CENTER");
				LuaValue fn = v.checkfunction(2);

				if (!MoreBBBoatHudClient.BUTTON_KEY.isPressed()) {
					return LuaValue.NIL;
				}

				var window = MinecraftClient.getInstance().getWindow();
				int[] base = screenAnchorPos(anchor, window.getScaledWidth(), window.getScaledHeight());
				final float cx = base[0];
				final float cy = base[1];

				int idx = drawCalls.size();
				fn.call();

				drawCalls.add(idx, ctx -> {
					ctx.getMatrices().push();
					ctx.getMatrices().loadIdentity();
					ctx.getMatrices().translate(cx, cy, 0f);
				});
				drawCalls.add(ctx -> ctx.getMatrices().pop());
				return LuaValue.NIL;
			}
		};
	}

	private static int[] screenAnchorPos(String anchor, int w, int h) {
		String[] parts = anchor.split("_");
		String vertical = parts.length > 0 ? parts[0] : "MIDDLE";
		String horizontal = parts.length > 1 ? parts[1] : "CENTER";
		int x = switch (horizontal.toUpperCase()) {
			case "LEFT" -> 0;
			case "RIGHT" -> w;
			default -> w / 2;
		};
		int y = switch (vertical.toUpperCase()) {
			case "TOP" -> 0;
			case "BOTTOM" -> h;
			default -> h / 2;
		};
		return new int[]{x, y};
	}

	private static int[] anchoredPos(String anchor, int width, int height) {
		String[] parts = anchor.split("_");
		String vertical = parts.length > 0 ? parts[0] : "MIDDLE";
		String horizontal = parts.length > 1 ? parts[1] : "CENTER";
		int x = switch (horizontal.toUpperCase()) {
			case "CENTER" -> -width / 2;
			case "RIGHT" -> -width;
			default -> 0;
		};
		int y = switch (vertical.toUpperCase()) {
			case "MIDDLE" -> -height / 2;
			case "BOTTOM" -> -height;
			default -> 0;
		};
		return new int[]{x, y};
	}

	private LuaValue getSize(boolean returnWidth) {
		String namespace = getIdentifier().getNamespace();
		return new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs v) {
				String path = v.checkjstring(1);
				Identifier texId = Identifier.of(namespace, "textures/bb_boat_hud_modules/" + path);
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
