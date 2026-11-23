package uk.co.dotcode.coin.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.co.dotcode.coin.DCM;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

public class ConfigHandler {

	public static File file;

	private static final Gson GSON =
			new GsonBuilder().setPrettyPrinting().create();

	private static final Logger LOGGER =
			LogManager.getLogger(DCM.MOD_ID);

	public static void load() {
		if (!file.exists()) {
			try (FileWriter writer = new FileWriter(file)) {
				GSON.toJson(new ModConfig(), writer);
			} catch (IOException e) {
				LOGGER.error("Failed to create DCM config: {}", file.getPath(), e);
			}
		}

		if (isJsonFile(file)) {
			loadFile(file);
		}
	}

	public static void save() {
		try (FileWriter writer = new FileWriter(file)) {
			GSON.toJson(DCM.modConfig, writer);
		} catch (IOException e) {
			LOGGER.error("Failed to save DCM config: {}", file.getPath(), e);
		}
	}

	private static void loadFile(File file) {
		try (Reader reader = new FileReader(file)) {
			LOGGER.info("Loading DCM config");

			DCM.modConfig = GSON.fromJson(reader, ModConfig.class);

		} catch (FileNotFoundException e) {
			LOGGER.error(
					"Failed to load DCM config (file not found): {}",
					file.getPath(),
					e
			);
		} catch (IOException e) {
			LOGGER.error(
					"Failed to load DCM config (IOException): {}",
					file.getPath(),
					e
			);
		}
	}

	private static boolean isJsonFile(File file) {
		String name = file.getName();
		int extensionIndex = name.lastIndexOf('.');

		return extensionIndex >= 0
				&& name.substring(extensionIndex).equalsIgnoreCase(".json");
	}
}