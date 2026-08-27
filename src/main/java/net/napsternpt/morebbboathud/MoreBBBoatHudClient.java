package net.napsternpt.morebbboathud;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.luaj.vm2.LuaValue;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MoreBBBoatHudClient implements ClientModInitializer {
	private static final Logger LOGGER = LoggerFactory.getLogger("morebbboathud");

	public static final KeyBinding BUTTON_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"key.morebbboathud.button",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_UNKNOWN,
			"key.category.morebbboathud"
	));

	public static boolean CLICKED = false;
	public static double CLICK_X;
	public static double CLICK_Y;
	public static boolean MOUSE_DOWN = false;
	public static final List<LuaValue> PENDING_CALLBACKS = new ArrayList<>();

	@Override
	public void onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.currentScreen != null || !client.isWindowFocused()) {
				return;
			}
			boolean held = BUTTON_KEY.isPressed();
			if (held && client.mouse.isCursorLocked()) {
				client.mouse.unlockCursor();
			} else if (!held && !client.mouse.isCursorLocked()) {
				client.mouse.lockCursor();
			}
		});
	}
}
