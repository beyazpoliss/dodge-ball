package com.beyazpoliss.api.game;

/**
 * Enum representing different team types.
 */
public enum TeamType {
  RED("red"),
  BLUE("blue");

  private final String type;

  /**
   * Constructs a TeamType with the given type.
   *
   * @param type the type of the team
   */
  TeamType(String type) {
    this.type = type;
  }

  /**
   * Returns the string representation of the team type.
   *
   * @return the string representation of the team type
   */
  public String getType() {
    return type;
  }

  /**
   * Returns the TeamType corresponding to the given type string.
   *
   * @param type the string representation of the team type
   * @return the TeamType enum constant corresponding to the given type, or null if no match is found
   */
  public static TeamType fromString(String type) {
    return java.util.Arrays.stream(TeamType.values())
      .filter(teamType -> teamType.getType().equalsIgnoreCase(type))
      .findFirst()
      .orElse(null);
  }
}