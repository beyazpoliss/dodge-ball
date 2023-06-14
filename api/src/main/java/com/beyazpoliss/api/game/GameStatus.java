package com.beyazpoliss.api.game;

/**
 * Enum class representing the game status.
 */
public enum GameStatus {
  /**
   * Represents the waiting status.
   */
  WAITING("waiting"),

  /**
   * Represents the starting status.
   */
  STARTING("starting"),

  /**
   * Represents the closed status.
   */
  CLOSED("closed");

  private final String status;

  /**
   * Constructs a GameStatus enum with the specified status.
   *
   * @param status the status of the game
   */
  GameStatus(String status) {
    this.status = status;
  }

  /**
   * Returns the string representation of the game status.
   *
   * @return the string representation of the game status
   */
  @Override
  public String toString() {
    return status;
  }
}
