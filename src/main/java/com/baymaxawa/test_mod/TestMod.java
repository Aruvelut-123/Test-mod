package com.baymaxawa.test_mod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestMod {
	public static final Logger LOGGER = LogManager.getLogger("Test Mod");
	private static final String CONFIG_FILE = "config/test_mod_config.txt";
	private static final int DEFAULT_X = 1024;
	private static final int DEFAULT_Y = 64;
	private static final int DEFAULT_Z = 1024;

	public static int sizeX;
	public static int sizeY;
	public static int sizeZ;

	/**
	 * Initializes the configuration:
	 * - Creates the file with defaults if it doesn't exist.
	 * - If it exists, attempts to load values; if loading fails (missing/corrupt),
	 *   writes defaults and logs a warning.
	 * - Resets the world every time game starts.
	 */
	public static void init() throws IOException {
		File worldFile = new File("level.dat");
		if (worldFile.exists()) {
			worldFile.delete();
		}
		Path configFolder = Paths.get("config");
		if (!Files.isDirectory(configFolder)) Files.createDirectories(configFolder);
		Path configPath = Paths.get(CONFIG_FILE);
		if (!Files.exists(configPath)) {
			LOGGER.info("Config file not found, creating default configuration.");
			saveDefaultConfig();
			return;
		}

		// Try to load existing config
		if (!loadConfig()) {
			LOGGER.warn("Config file is missing required integers or is corrupt. Resetting to defaults.");
			saveDefaultConfig();
		}
	}

	/**
	 * Reads three lines from the config file and parses them as integers.
	 * Updates sizeX, sizeY, sizeZ if successful.
	 *
	 * @return true if all three lines are valid integers, false otherwise
	 */
	private static boolean loadConfig() {
		Path configPath = Paths.get(CONFIG_FILE);
		try (BufferedReader reader = Files.newBufferedReader(configPath)) {
			String lineX = reader.readLine();
			String lineY = reader.readLine();
			String lineZ = reader.readLine();

			// If any line is null (file too short) or cannot be parsed, fail
			if (lineX == null || lineY == null || lineZ == null) {
				LOGGER.error("Config file has fewer than three lines.");
				return false;
			}

			int x = Integer.parseInt(lineX.trim());
			int y = Integer.parseInt(lineY.trim());
			int z = Integer.parseInt(lineZ.trim());

			sizeX = x;
			sizeY = y;
			sizeZ = z;
			LOGGER.info("Config loaded: ({}, {}, {})", sizeX, sizeY, sizeZ);
			return true;
		} catch (FileNotFoundException e) {
			LOGGER.error("Config file not found during load.");
			return false;
		} catch (IOException e) {
			LOGGER.error("Error reading config file: {}", e.getMessage());
			return false;
		} catch (NumberFormatException e) {
			LOGGER.error("Config file contains non-integer values: {}", e.getMessage());
			return false;
		}
	}

	/**
	 * Writes the given integers as three lines in the config file.
	 * After writing, updates the static fields.
	 */
	public static void saveConfig(int x, int y, int z) {
		Path configPath = Paths.get(CONFIG_FILE);
		try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(configPath))) {
			writer.println(x);
			writer.println(y);
			writer.println(z);
			sizeX = x;
			sizeY = y;
			sizeZ = z;
			LOGGER.info("Config saved: ({}, {}, {})", x, y, z);
		} catch (IOException e) {
			LOGGER.error("Failed to save config: {}", e.getMessage());
		}
	}

	/**
	 * Saves the default configuration values.
	 */
	public static void saveDefaultConfig() {
		saveConfig(DEFAULT_X, DEFAULT_Y, DEFAULT_Z);
	}
}
