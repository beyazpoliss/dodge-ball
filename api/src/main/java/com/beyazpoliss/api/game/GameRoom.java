package com.beyazpoliss.api.game;

import java.util.Collection;
import java.util.Random;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

/**
 * Interface representing a game room.
 */
public interface GameRoom {

  TeamType getSuitableTeam();

  boolean room_is_suitable_for_join();

  GameStatus getStatus();

  boolean players_is_enemy(@NotNull final UUID playerOne,@NotNull final UUID playerTwo);

  String roomId();
  /**
   * Returns the collection of teams in the game room.
   *
   * @return the collection of teams in the game room
   */
  Collection<Team> teams();

  Team red();

  Team blue();

  /**
   * Returns the room scheduler for managing game room scheduling.
   *
   * @return the room scheduler for managing game room scheduling
   */
  RoomScheduler scheduler();

  /**
   * Returns the team of the specified type in the game room.
   *
   * @param type the type of the team
   * @return the team of the specified type in the game room, or null if not found
   */
  Team team(@NotNull final TeamType type);

  /**
   * Returns the spectators in the game room.
   *
   * @return the spectators in the game room
   */
  Spectators spectators();

  /**
   * Checks if a player with the specified name can join the game room for the given team type.
   *
   * @param name      the name of the player
   * @param teamType  the team type to join
   * @return true if the player can join, false otherwise
   */
  boolean canJoin(@NotNull final String name, @NotNull final TeamType teamType);

  /**
   * Checks if a player with the specified UUID can join the game room for the given team type.
   *
   * @param uuid      the UUID of the player
   * @param teamType  the team type to join
   * @return true if the player can join, false otherwise
   */
  boolean canJoin(@NotNull final UUID uuid, @NotNull final TeamType teamType);

  /**
   * Adds a player with the specified name to the game room.
   *
   * @param name the name of the player
   */
  void join(@NotNull final String name);

  /**
   * Adds a player with the specified UUID to the game room.
   *
   * @param uuid the UUID of the player
   */
  void join(@NotNull final UUID uuid);

  /**
   * Removes a player with the specified name from the game room.
   *
   * @param name the name of the player
   */
  void leave(@NotNull final String name);

  /**
   * Removes a player with the specified UUID from the game room.
   *
   * @param uuid the UUID of the player
   */
  void leave(@NotNull final UUID uuid);

  /**
   * Returns the maximum team size allowed in the game room.
   *
   * @return the maximum team size allowed in the game room
   */
  byte maxTeamSize();

  /**
   * Returns the minimum team size required in the game room.
   *
   * @return the minimum team size required in the game room
   */
  byte minTeamSize();

  default String generateId() {
    final var text = "MS-RANDOM";
    final var random = new Random();
    final var randomNumber = random.nextInt(9000) + 1000;
    final var randomNumberString = String.format("%04d", randomNumber);
    return text.replace("RANDOM", randomNumberString);
  }
}
