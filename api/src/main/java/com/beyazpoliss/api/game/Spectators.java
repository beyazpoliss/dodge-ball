package com.beyazpoliss.api.game;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface Spectators {

  void spectate(@NotNull final String name);

  void spectate(@NotNull final UUID uuid);
}
