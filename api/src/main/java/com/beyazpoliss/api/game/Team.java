package com.beyazpoliss.api.game;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public interface Team {

  DefaultLocation spawn();

  TeamType type();

  List<UUID> players();

  void removePlayer(@NotNull final UUID player);

  int teamSize();

  int maxSize();

  boolean isSuitable();

  boolean joinTeam(@NotNull final UUID player, @NotNull final String playerName);

}
