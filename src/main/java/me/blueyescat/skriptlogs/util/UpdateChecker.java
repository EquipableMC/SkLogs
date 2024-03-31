package me.blueyescat.skriptlogs.util;

import ch.njol.skript.util.Version;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.blueyescat.skriptlogs.SkriptLogs;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;

/**
 * @author ShaneBeee, ImNotStable
 */

public class UpdateChecker {
  
  public static void check(SkriptLogs plugin) {
    String latestVersion = getLatestVersion();
    if (latestVersion == null)
      return;
    String currentVersion = plugin.getDescription().getVersion();
    if (new Version(currentVersion).isSmallerThan(new Version(latestVersion))) {
      SkriptLogs.getInstance().getLogger().info("SkLogs is NOT up to date!");
      SkriptLogs.getInstance().getLogger().info("> Current Version: " + currentVersion);
      SkriptLogs.getInstance().getLogger().info("> Latest Version: " + latestVersion);
      SkriptLogs.getInstance().getLogger().info("> Download it at: https://github.com/EquipableMC/SkLogs/releases");
      Bukkit.getPluginManager().registerEvents(new Listener() {
        @EventHandler
        public void on(PlayerJoinEvent event) {
          Player player = event.getPlayer();
          if (!player.hasPermission("sklogs.update.check") && !player.isOp()) return;
          
          player.sendMessage(" ");
          player.sendMessage(MiniMessage.miniMessage().deserialize("<red>[<white>SkLogs<red>] <white>SkLogs is <red><bold>OUTDATED<white>!"));
          player.sendMessage(MiniMessage.miniMessage().deserialize("<red>[<white>SkLogs<red>] <white>New version: " + latestVersion));
          player.sendMessage(MiniMessage.miniMessage().deserialize("<red>[<white>SkLogs<red>] <white>Download at: <link>https://github.com/EquipableMC/SkLogs/releases"));
          player.sendMessage(" ");
        }
      }, plugin);
    } else
      SkriptLogs.getInstance().getLogger().info("SkLogs is up to date!");
  }
  
  private static String getLatestVersion() {
    String url = "https://api.github.com/repos/EquipableMC/SkLogs/releases/latest";
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
      JsonObject jsonObject = new Gson().fromJson(reader, JsonObject.class);
      return jsonObject.get("tag_name").getAsString();
    } catch (Exception exception) {
      SkriptLogs.getInstance().getLogger().log(Level.WARNING, "Failed to check for latest version.");
      exception.printStackTrace();
    }
    return null;
  }
  
}
