package com.beyazpoliss.api.profile;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class ProfileImpl implements Profile {

  private final UUID uuid;

  private int wins;

  @Override
  public int setWins(int number) {
    this.wins = number;
    return wins;
  }

  @Override
  public int addWin() {
    this.wins += wins;
    return this.wins;
  }

  @Override
  public int addLose() {
    this.wins -= wins;
    return this.wins;
  }
}
