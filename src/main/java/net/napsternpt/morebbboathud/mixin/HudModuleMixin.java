package net.napsternpt.morebbboathud.mixin;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.napsternpt.morebbboathud.MoreBBBoatHudClient;
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
import uk.co.cablepost.bb_boat_hud.client.AnchorType;
import uk.co.cablepost.bb_boat_hud.client.HudModule;
import uk.co.cablepost.bb_boat_hud.client.HudModulePlacement;
import uk.co.cablepost.bb_boat_hud.config.ModConfig;

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
		globals.set("menuOpen", LuaBoolean.valueOf(MoreBBBoatHudClient.BUTTON_KEY.isPressed()));
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void onInit(CallbackInfo ci) {
		globals.set("print", printFunction());
		globals.set("renderButton", renderButton());
		globals.set("getWidth", getSize(true));
		globals.set("getHeight", getSize(false));
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
				double[] localMouse = toLocalSpace(mouseX, mouseY);
				double mx = localMouse[0];
				double my = localMouse[1];

				int[] pos = anchoredPos(anchor, width, height);
				int x0 = pos[0];
				int y0 = pos[1];
				boolean hovered = canBePressed
						&& mx >= x0 && mx < x0 + width
						&& my >= y0 && my < y0 + height;

				if (canBePressed && MoreBBBoatHudClient.CLICKED) {
					double cx = MoreBBBoatHudClient.CLICK_X;
					double cy = MoreBBBoatHudClient.CLICK_Y;
					double[] localClick = toLocalSpace(cx, cy);
					if (localClick[0] >= x0 && localClick[0] < x0 + width
							&& localClick[1] >= y0 && localClick[1] < y0 + height) {
						MoreBBBoatHudClient.CLICKED = false;
						try {
							callback.call();
						} catch (LuaError e) {
							error = "renderButton: " + e.getMessage();
						}
					}
				}

				final int fx0 = x0;
				final int fy0 = y0;
				final int fw = width;
				final int fh = height;
				drawCalls.add(ctx -> {
					Identifier tex = !canBePressed ? BTN_DISABLED : hovered ? BTN_HIGHLIGHTED : BTN_NORMAL;
					ctx.drawGuiTexture(RenderLayer::getGuiTextured, tex, fx0, fy0, fw, fh);
				});
				return LuaValue.NIL;
			}
		};
	}

	private double[] toLocalSpace(double screenX, double screenY) {
		var window = MinecraftClient.getInstance().getWindow();
		float anchorX = 0;
		float anchorY = 0;
		HudModulePlacement placement = null;
		try {
			var config = (ModConfig) AutoConfig.getConfigHolder(ModConfig.class).getConfig();
			String id = getIdentifier().toString();
			for (var p : config.modulePlacements) {
				if (p.identifier.equals(id)) {
					placement = p;
					break;
				}
			}
		} catch (Exception ignored) {
		}

		if (placement != null) {
			if (placement.anchorType == AnchorType.TOP_CENTER || placement.anchorType == AnchorType.MIDDLE_CENTER
					|| placement.anchorType == AnchorType.BOTTOM_CENTER) {
				anchorX = window.getScaledWidth() / 2f;
			} else if (placement.anchorType == AnchorType.TOP_RIGHT || placement.anchorType == AnchorType.MIDDLE_RIGHT
					|| placement.anchorType == AnchorType.BOTTOM_RIGHT) {
				anchorX = window.getScaledWidth();
			}
			if (placement.anchorType == AnchorType.MIDDLE_LEFT || placement.anchorType == AnchorType.MIDDLE_CENTER
					|| placement.anchorType == AnchorType.MIDDLE_RIGHT) {
				anchorY = window.getScaledHeight() / 2f;
			} else if (placement.anchorType == AnchorType.BOTTOM_LEFT || placement.anchorType == AnchorType.BOTTOM_CENTER
					|| placement.anchorType == AnchorType.BOTTOM_RIGHT) {
				anchorY = window.getScaledHeight();
			}
		}

		double dx = screenX - anchorX - (placement != null ? placement.xOffset : 0);
		double dy = screenY - anchorY - (placement != null ? placement.yOffset : 0);
		float scale = placement != null && placement.scale != 0 ? placement.scale : 1;
		dx /= scale;
		dy /= scale;

		float angle = placement != null ? placement.angle : 0;
		if (Math.abs(angle) > 1.0E-5f) {
			double rad = Math.toRadians(angle);
			double cos = Math.cos(rad);
			double sin = Math.sin(rad);
			return new double[]{dx * cos + dy * sin, -dx * sin + dy * cos};
		}
		return new double[]{dx, dy};
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
