package com.beyazpoliss.bukkit.messenger;

import com.beyazpoliss.api.game.DefaultLocation;
import com.beyazpoliss.api.game.PlayerManager;
import com.beyazpoliss.bukkit.PlayerCacheProcessor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlayerManagerBukkitAdapter implements PlayerManager {

  @Override
  public void teleport(@NotNull UUID uuid, @NotNull DefaultLocation teamLocation) {
    final var player = Bukkit.getPlayer(uuid);
    if (player == null) return;
    if (!(player.isOnline())) return;
    final var world = Bukkit.getWorld(teamLocation.world_name());
    if (world == null) return;
    player.teleport(new Location(world,teamLocation.x(),teamLocation.y(),teamLocation.z()));
  }

  @Override
  public void give_team_select_item(@NotNull UUID uuid) {
    final var player = Bukkit.getPlayer(uuid);
    if (player == null) return;
    if (!(player.isOnline())) return;
    player.getInventory().addItem(createTeamCompass());
  }

  @Override
  public void give_ball_item(@NotNull UUID uuid) {
    final var player = Bukkit.getPlayer(uuid);
    if (player == null) return;
    if (!(player.isOnline())) return;
    final var item = PlayerCacheProcessor.createCustomSkull();
    player.getInventory().addItem(item);
  }

  @Override
  public void clear_inv(@NotNull UUID uuid) {
    final var player = Bukkit.getPlayer(uuid);
    if (player == null) return;
    if (!(player.isOnline())) return;
    player.getInventory().clear();
  }

  private ItemStack createTeamCompass() {
    ItemStack compass = new ItemStack(Material.COMPASS);
    ItemMeta compassMeta = compass.getItemMeta();
    compassMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&aSelect Team."));
    compass.setItemMeta(compassMeta);
    return compass;
  }
}
