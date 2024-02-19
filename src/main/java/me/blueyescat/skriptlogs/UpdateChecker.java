package me.blueyescat.skriptlogs;


import me.blueyescat.skriptlogs.SkriptLogs;
import org.bukkit.plugin.java.JavaPlugin;
import ch.njol.skript.util.Version;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class UpdateChecker implements Listener {

	private static Version UPDATE_VERSION;

	public static void checkForUpdate(String pluginVersion) {
		SkriptLogs.getInstance().getLogger().info("Checking for updates.");
		getLatestReleaseVersion(version -> {
			Version plugVer = new Version(pluginVersion);
			Version curVer = new Version(version);
			if (curVer.compareTo(plugVer) <= 0) {
				SkriptLogs.getInstance().getLogger().info("SkLogs is up to date!");
			} else {
				SkriptLogs.getInstance().getLogger().info("SkLogs is NOT up to date!");
				SkriptLogs.getInstance().getLogger().info("> Current Version: "+pluginVersion);
				SkriptLogs.getInstance().getLogger().info("> Latest Version: "+version);
				SkriptLogs.getInstance().getLogger().info("> Download it at: https://github.com/EquipableMC/SkLogs/releases");
				UPDATE_VERSION = curVer;
			}
		});
	}

	private static void getLatestReleaseVersion(final Consumer<String> consumer) {
		try {
			URL url = new URL("https://api.github.com/repos/EquipableMC/SkLogs/releases/latest");
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			JsonObject jsonObject = new Gson().fromJson(reader, JsonObject.class);
			String tag_name = jsonObject.get("tag_name").getAsString();
			consumer.accept(tag_name);
		} catch (IOException e) {
			SkriptLogs.getInstance().getLogger().info("Update checker has failed...");
			e.printStackTrace();
		}
	}

	private final SkriptLogs plugin;

	public UpdateChecker(SkriptLogs plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	private void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (!player.hasPermission("sklogs.update.check") || player.isOp()) return;

		String currentVersion = this.plugin.getDescription().getVersion();
		CompletableFuture<Version> updateVersion = getUpdateVersion(currentVersion);

		Bukkit.getScheduler().runTaskLater(this.plugin, () -> updateVersion.thenApply(version -> {
			player.sendRichMessage(" ");
			player.sendRichMessage("<red>[<white>SkLogs<red>] <white>SkLogs is <red><bold>OUTDATED<white>!");
			player.sendRichMessage("<red>[<white>SkLogs<red>] <white>New version: "+version);
			player.sendRichMessage("<red>[<white>SkLogs<red>] <white>Download at: <link>https://github.com/EquipableMC/SkLogs/releases");
			player.sendRichMessage(" ");
			return true;
		}), 30);
	}

	private CompletableFuture<Version> getUpdateVersion(String currentVersion) {
		CompletableFuture<Version> future = new CompletableFuture<>();
		if (UPDATE_VERSION != null) {
			future.complete(UPDATE_VERSION);
		} else {
			Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> getLatestReleaseVersion(version -> {
				Version plugVer = new Version(currentVersion);
				Version curVer = new Version(version);
				if (curVer.compareTo(plugVer) <= 0) {
					future.cancel(true);
				} else {
					UPDATE_VERSION = curVer;
					future.complete(UPDATE_VERSION);
				}
			}));
		}
		return future;
	}

}