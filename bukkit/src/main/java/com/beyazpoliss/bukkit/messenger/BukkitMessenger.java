package com.beyazpoliss.bukkit.messenger;

import com.beyazpoliss.api.messenger.MessageSender;
import lombok.AllArgsConstructor;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;

@AllArgsConstructor
public class BukkitMessenger implements MessageSender {

  private final Plugin plugin;

  @Override
  public void sendMessagePlayer(@NotNull final String name, @NotNull final String message) {
    Optional
      .ofNullable(this.plugin.getServer().getPlayer(name))
      .ifPresent(player -> player.sendMessage(message)
    );
  }

  @Override
  public void sendMessageUuid(@NotNull final UUID uuid, @NotNull final String message) {
    Optional
      .ofNullable(this.plugin.getServer().getPlayer(uuid))
      .ifPresent(player -> player.sendMessage(message)
    );
  }

  @Override
  public void sendMessageConsole(@NotNull final Level level, @NotNull final String message) {
    plugin
      .getServer()
      .getLogger()
      .log(level,message);
  }
}
