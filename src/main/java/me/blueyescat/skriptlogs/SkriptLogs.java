package me.blueyescat.skriptlogs;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import me.blueyescat.skriptlogs.util.LogAppender;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class SkriptLogs extends JavaPlugin {
  
  private static SkriptLogs instance;
  private static SkriptAddon addonInstance;
  public String lastMessage;
  
  public SkriptLogs() {
    if (instance == null) {
      instance = this;
    } else {
      throw new IllegalStateException();
    }
  }
  
  public static SkriptLogs getInstance() {
    if (instance == null) {
      throw new IllegalStateException();
    }
    return instance;
  }
  
  public static SkriptAddon getAddonInstance() {
    if (addonInstance == null) {
      addonInstance = Skript.registerAddon(getInstance());
    }
    return addonInstance;
  }
  
  @Override
  public void onEnable() {
    if (!Skript.isAcceptRegistrations()) {
      getServer().getPluginManager().disablePlugin(this);
      getLogger().severe("SkLogs can't be loaded when the server is already loaded! Plugin is disabled.");
      return;
    }
    
    try {
      SkriptAddon addonInstance = Skript.registerAddon(this).setLanguageFileDirectory("lang");
      addonInstance.loadClasses("me.blueyescat.skriptlogs", "skript");
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    new LogAppender().start();
    
    Metrics metrics = new Metrics(this, 20924);
    metrics.addCustomChart(new SimplePie("skript_version", () ->
      Skript.getInstance().getDescription().getVersion()));
    getLogger().info("Started metrics!");
    getLogger().info("Finished loading!");
  }
  
}
