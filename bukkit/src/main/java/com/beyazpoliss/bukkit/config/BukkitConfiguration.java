package com.beyazpoliss.bukkit.config;

import com.beyazpoliss.api.config.ConfigOptions;
import com.beyazpoliss.api.config.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class BukkitConfiguration implements Configuration {

  @NotNull
  private final Plugin plugin;

  private final File pluginFolder;
  private final File ymlFile;

  private FileConfiguration configuration;

  public BukkitConfiguration(final Plugin plugin, final String ymlName) {
    this.plugin = plugin;
    this.pluginFolder = new File(plugin.getDataFolder() + File.separator);
    this.ymlFile = new File(this.plugin.getDataFolder().getPath(), ymlName);
  }

  @Override
  public CompletableFuture<Void> load(ConfigOptions options) {
    return CompletableFuture.supplyAsync(() -> {
      createFile(pluginFolder);
      if (!pluginFolder.exists()){
        try {
          pluginFolder.createNewFile();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      this.configuration = YamlConfiguration.loadConfiguration(ymlFile);
      return null;
    });
  }

  @Override
  public CompletableFuture<Void> save() {
    return CompletableFuture.supplyAsync(() -> {
      try {
        this.configuration.save(ymlFile);
      } catch (IOException e) {
        e.printStackTrace();
      }
      return null;
    });
  }

  @Override
  public void setIfNotExists(final @NotNull String path, final Object o) {
    if (isNotSet(path)){
      this.configuration.set(path, o);
    }
  }

  @Override
  public List<String> getKeys(final @NotNull String path, final boolean deep){
    if (isNotSet(path)) return new ArrayList<>();
    return new ArrayList<>(Objects.requireNonNull(configuration.getConfigurationSection(path)).getKeys(deep));
  }

  @Override
  public boolean isNotSet(final @NotNull String path){
    return !this.configuration.isSet(path);
  }

  @Override
  @NotNull
  public Object get(final @NotNull String path) {
    return Objects.requireNonNull(configuration.get(path));
  }
}
