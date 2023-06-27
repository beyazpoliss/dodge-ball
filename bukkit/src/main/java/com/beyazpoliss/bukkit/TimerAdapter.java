package com.beyazpoliss.bukkit;

import com.beyazpoliss.api.game.TimerWrapper;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public record TimerAdapter(@NotNull Plugin plugin) implements TimerWrapper {

  @Override
  public int runTaskTimerAsync(@NotNull Runnable runnable, int delay, int period) {
   return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, period).getTaskId();
  }

  @Override
  public void runTaskSync(@NotNull Runnable runnable) {
    Bukkit.getScheduler().runTask(plugin,runnable);
  }

  @Override
  public void cancelTask(int id) {
    Bukkit.getScheduler().cancelTask(id);
  }
}
