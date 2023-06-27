package com.beyazpoliss.common.game;

import com.beyazpoliss.api.game.DefaultLocation;
import com.beyazpoliss.api.game.Team;
import com.beyazpoliss.api.game.TeamType;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@AllArgsConstructor
public class DefaultTeam implements Team {

  private Map<UUID,String> players;
  private final TeamType type;
  private final int maxSize;

  private final DefaultLocation location;

  public DefaultTeam(@NotNull final TeamType type,@NotNull final DefaultLocation location, final int maxSize){
    this.type = type;
    this.players = new HashMap<>();
    this.maxSize = maxSize;
    this.location = location;
  }

  @Override
  public DefaultLocation spawn() {
    return location;
  }

  @Override
  public TeamType type() {
    return type;
  }

  @Override
  public List<UUID> players() {
    return new ArrayList<>(players.keySet());
  }

  @Override
  public void removePlayer(@NotNull UUID player) {
    players.remove(player);
  }

  @Override
  public int teamSize() {
    return players.size();
  }

  @Override
  public int maxSize() {
    return maxSize;
  }


  @Override
  public boolean isSuitable() {
    return maxSize != players.size();
  }

  @Override
  public boolean joinTeam(@NotNull UUID player, @NotNull String playerName) {
    this.players.put(player,playerName);
    return true;
  }

}
