package com.beyazpoliss.api.game;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.UUID;

public interface GameManager {

  Collection<GameRoom> rooms();

  Team createTeam(@NotNull final TeamType type);

  GameRoom createRoom(@NotNull final Team... teams);

  GameRoom createEmptyRoom();

  void enable_process_of_rooms();

  boolean join_room(@NotNull final String roomId, @NotNull final UUID player);

}
