package net.napsternpt.morebbboathud;

import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModuleConfigManager {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir().resolve("morebbboathud");

	public static String moduleIdFromIdentifier(String identifier) {
		String path = identifier;
		int colon = path.indexOf(':');
		if (colon >= 0) {
			path = path.substring(colon + 1);
		}
		if (path.endsWith(".lua")) {
			path = path.substring(0, path.length() - 4);
		}
		return path;
	}

	private static Path configPath(String moduleId) {
		return CONFIG_DIR.resolve(moduleId + ".json");
	}

	private static JsonObject loadJson(String moduleId) {
		Path path = configPath(moduleId);
		if (!Files.exists(path)) {
			return new JsonObject();
		}
		try (Reader reader = Files.newBufferedReader(path)) {
			JsonElement element = JsonParser.parseReader(reader);
			if (element.isJsonObject()) {
				return element.getAsJsonObject();
			}
		} catch (IOException | com.google.gson.JsonSyntaxException ignored) {
		}
		return new JsonObject();
	}

	private static void saveJson(String moduleId, JsonObject json) {
		Path path = configPath(moduleId);
		try {
			Files.createDirectories(path.getParent());
			try (Writer writer = Files.newBufferedWriter(path)) {
				GSON.toJson(json, writer);
			}
		} catch (IOException ignored) {
		}
	}

	public static Object get(String moduleId, String key) {
		JsonObject json = loadJson(moduleId);
		if (!json.has(key)) {
			return null;
		}
		JsonElement element = json.get(key);
		if (element.isJsonPrimitive()) {
			com.google.gson.JsonPrimitive prim = element.getAsJsonPrimitive();
			if (prim.isNumber()) {
				return prim.getAsDouble();
			} else if (prim.isBoolean()) {
				return prim.getAsBoolean();
			} else if (prim.isString()) {
				return prim.getAsString();
			}
		}
		return null;
	}

	public static void set(String moduleId, String key, Object value) {
		JsonObject json = loadJson(moduleId);
        switch (value) {
            case null -> json.remove(key);
            case Boolean b -> json.addProperty(key, b);
            case Number n -> json.addProperty(key, n.doubleValue());
            case String s -> json.addProperty(key, s);
            default -> {
                return;
            }
        }
		saveJson(moduleId, json);
	}
}
