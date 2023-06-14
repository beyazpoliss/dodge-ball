package com.beyazpoliss.common.game;

import com.beyazpoliss.api.game.Team;
import com.beyazpoliss.api.game.TeamType;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class DefaultTeam implements Team {

  private List<UUID> players;
  private final TeamType type;

  public DefaultTeam(@NotNull final TeamType type){
    this.type = type;
    this.players = new ArrayList<>();
  }

  @Override
  public TeamType type() {
    return type;
  }

  @Override
  public List<UUID> players() {
    return players;
  }

  @Override
  public int teamSize() {
    return players.size();
  }
}
