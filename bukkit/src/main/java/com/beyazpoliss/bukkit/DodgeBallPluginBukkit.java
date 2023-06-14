package com.beyazpoliss.bukkit;

import com.beyazpoliss.api.Provider;
import org.bukkit.plugin.java.JavaPlugin;

public class DodgeBallPluginBukkit extends JavaPlugin {

  @Override
  public void onEnable() {
    final var bukkit =  new BukkitAdapter(this){};
    Provider.instance(bukkit);
    bukkit.enable();
  }

  @Override
  public void onDisable() {
    super.onDisable();
  }
}
