package com.beyazpoliss.bukkit.commands;

import com.beyazpoliss.api.Provider;
import com.beyazpoliss.api.game.GameStatus;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@AllArgsConstructor
public class GamesCommand implements CommandExecutor {

  @NotNull
  private final Plugin plugin;

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    if (!(sender instanceof Player)) return true;

    final var player = ((Player) sender).getPlayer();

    final var gui = Bukkit.createInventory(null, 54, "Dodge-Ball Games.");

    ItemStack blackGlass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
    ItemMeta glassMeta = blackGlass.getItemMeta();
    assert glassMeta != null;
    glassMeta.setDisplayName(" ");
    blackGlass.setItemMeta(glassMeta);

    int rows = gui.getSize() / 9;
    int slotsPerRow = 9;

    for (int slot = 0; slot < slotsPerRow; slot++) {
      gui.setItem(slot, blackGlass);
      gui.setItem((rows - 1) * slotsPerRow + slot, blackGlass);
    }

    for (int row = 0; row < rows; row++) {
      gui.setItem(row * slotsPerRow, blackGlass);
      gui.setItem(row * slotsPerRow + slotsPerRow - 1, blackGlass);
    }

    assert player != null;

    final var manager = Provider.instance().gameManager();
    manager.rooms().forEach(gameRoom -> {
      if (gameRoom.getStatus().equals(GameStatus.STARTING)) {
        var starting = createItemStack(Material.GREEN_CANDLE, 1, "" + gameRoom.roomId(), "A game is starting");
        gui.addItem(starting);
      } else if (gameRoom.getStatus().equals(GameStatus.WAITING)) {
        var waiting = createItemStack(Material.WHITE_CANDLE, 1, "" + gameRoom.roomId(), "A game is waiting");
        gui.addItem(waiting);
      } else if (gameRoom.getStatus().equals(GameStatus.CLOSED)) {
        var closed = createItemStack(Material.RED_CANDLE, 1, "" + gameRoom.roomId(), "A game is closed");
        gui.addItem(closed);
      }
    });

    player.openInventory(gui);

    return true;
  }

  public ItemStack createItemStack(Material material, int amount, String displayName, String... lore) {
    ItemStack itemStack = new ItemStack(material, amount);
    ItemMeta itemMeta = itemStack.getItemMeta();

    if (displayName != null) {
      assert itemMeta != null;
      itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
    }

    if (lore != null && lore.length > 0) {
      for (int i = 0; i < lore.length; i++) {
        lore[i] = ChatColor.translateAlternateColorCodes('&', lore[i]);
      }
      assert itemMeta != null;
      itemMeta.setLore(Arrays.asList(lore));
    }

    itemStack.setItemMeta(itemMeta);
    return itemStack;
  }
}
