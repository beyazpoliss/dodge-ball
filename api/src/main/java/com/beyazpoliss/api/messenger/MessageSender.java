package com.beyazpoliss.api.messenger;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.logging.Level;

/**
 * A class that provides methods for sending messages to players, UUIDs, and the console.
 */
public interface MessageSender {
  /**
   * Sends a message to the player with the specified name.
   *
   * @param name    the name of the player
   * @param message the message to send
   */
  void sendMessagePlayer(@NotNull final String name, @NotNull final String message);
  /**
   * Sends a message to the player with the specified UUID.
   *
   * @param uuid    the UUID of the player
   * @param message the message to send
   */
  void sendMessageUuid(@NotNull final UUID uuid, @NotNull final String message);
  /**
   * Sends a message to the console with the specified log level.
   *
   * @param level   the log level of the message
   * @param message the message to send
   */
  void sendMessageConsole(@NotNull final Level level, @NotNull final String message);
}