package com.beyazpoliss.common.game;

import com.beyazpoliss.api.Provider;
import com.beyazpoliss.api.game.*;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

@Getter
public class DefaultGameRoom implements GameRoom {

  private final Team red;
  private final Team blue;

  private final Spectators spectators;
  private final RoomScheduler scheduler;

  private final String roomId;

  public DefaultGameRoom(int maxRoomSize){
    this.roomId = this.generateId();

    this.red = Provider.instance().gameManager().create_team(TeamType.RED,this.location("red"),maxRoomSize);
    this.blue = Provider.instance().gameManager().create_team(TeamType.BLUE,this.location("blue"),maxRoomSize);

    this.spectators = new DefaultSpectators();
    this.scheduler = new DefaultRoomScheduler(this);
  }

  @Override
  public TeamType getSuitableTeam() {
    if (red.teamSize() > blue.teamSize()){
      return TeamType.BLUE;
    } else {
      if (blue.isSuitable()){
        return blue.type();
      } else {
        return red.type();
      }
    }
  }

  @Override
  public boolean room_is_suitable_for_join() {
    if (!(scheduler.status().equals(GameStatus.WAITING))) return false;
    if (red.teamSize() == maxTeamSize()) return false;
    return blue.teamSize() != maxTeamSize();
  }

  @Override
  public GameStatus getStatus() {
    return scheduler.status();
  }

  @Override
  public boolean players_is_enemy(@NotNull UUID playerOne, @NotNull UUID playerTwo) {
    if ((red().players().contains(playerOne) && blue().players().contains(playerTwo)) ||
      (blue().players().contains(playerOne) && red().players().contains(playerTwo))) {
      return true;
    }

    return false;
  }

  @Override
  public String roomId() {
    return roomId;
  }

  @Override
  public Collection<Team> teams() {
    return Arrays.asList(red,blue);
  }

  @Override
  public Team red() {
    return red;
  }

  @Override
  public Team blue() {
    return blue;
  }

  @Override
  public RoomScheduler scheduler() {
    return scheduler;
  }

  @Override
  public Team team(@NotNull final TeamType type) {
    return teams().stream()
      .filter(team -> team.type() == type)
      .findFirst()
      .orElse(null);
  }

  @Override
  public Spectators spectators() {
    return spectators;
  }

  @Override
  public boolean canJoin(@NotNull String name, @NotNull TeamType teamType) {
    return true;
  }

  @Override
  public boolean canJoin(@NotNull UUID uuid, @NotNull TeamType teamType) {
    final var team = this.team(teamType);
    if (team.isSuitable()){
      return true;
    }
    return false;
  }

  @Override
  public void join(@NotNull String name) {

  }

  @Override
  public void join(@NotNull UUID uuid) {

  }

  @Override
  public void leave(@NotNull String name) {

  }

  @Override
  public void leave(@NotNull UUID uuid) {

  }

  @Override
  public byte maxTeamSize() {
    return 0;
  }

  @Override
  public byte minTeamSize() {
    return 0;
  }

  private DefaultLocation location(@NotNull final String team){
    final var worldName = (String) Provider.instance().configuration().get("server.game-world.name");

    final var teamX = (int) Provider.instance().configuration().get("server.game-world." + team + "-team.x");
    final var teamY = (int) Provider.instance().configuration().get("server.game-world." + team + "-team.y");
    final var teamZ = (int) Provider.instance().configuration().get("server.game-world." + team + "-team.z");

    return new DefaultLocation(worldName,teamX,teamY,teamZ);
  }

}
