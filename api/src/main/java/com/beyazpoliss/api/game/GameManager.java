package com.beyazpoliss.api.game;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.UUID;

public interface GameManager {

  Collection<GameRoom> rooms();

  Team create_team(@NotNull final TeamType type, @NotNull final DefaultLocation location, int maxSize);

  void send_all_players_to_room(@NotNull final GameRoom room,@NotNull final String message);

  GameRoom create_room(@NotNull final Team... teams);

  void start_round(@NotNull final GameRoom room);

  GameRoom get_room_by_player(@NotNull final UUID uuid);

  GameRoom create_empty_room();

  void change_team_player(@NotNull final String playerName, @NotNull final UUID player,@NotNull final TeamType type);

  void enable_process_of_rooms();

  boolean join_room(@NotNull final String roomId, @NotNull final String playerName, @NotNull final UUID player);

  boolean join_team_but_player_process(@NotNull final GameRoom room, @NotNull final TeamType team,@NotNull final String playerName, @NotNull final UUID uuid);

}
