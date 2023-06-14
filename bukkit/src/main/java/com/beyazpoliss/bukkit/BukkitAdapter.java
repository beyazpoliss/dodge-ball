package com.beyazpoliss.bukkit;

import com.beyazpoliss.api.StructureObject;
import com.beyazpoliss.api.config.Configuration;
import com.beyazpoliss.api.messenger.MessageSender;
import com.beyazpoliss.bukkit.commands.GamesCommand;
import com.beyazpoliss.bukkit.config.BukkitConfiguration;
import com.beyazpoliss.bukkit.messenger.BukkitMessenger;
import com.beyazpoliss.common.DefaultDodgeBall;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public abstract class BukkitAdapter extends DefaultDodgeBall {

  public BukkitAdapter(@NotNull final DodgeBallPluginBukkit plugin){
    final MessageSender messenger = new BukkitMessenger(plugin);
    final Configuration configuration = new BukkitConfiguration(plugin,"config.yml");
    this.structure().register(
      StructureObject.get(messenger, MessageSender.class),
      StructureObject.get(configuration, Configuration.class)
    );
    this.structureChangeComponent();
    Bukkit.getPluginManager().registerEvents(new PlayerCacheProcessor(plugin),plugin);
    plugin.getCommand("dodge-ball").setExecutor(new GamesCommand(plugin));
  }

  @Override
  public void structureChangeComponent() {
    //
  }
}
