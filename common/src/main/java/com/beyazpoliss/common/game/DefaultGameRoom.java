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

  private final GameStatus status;

  public DefaultGameRoom(){
    this.status = GameStatus.WAITING;
    this.roomId = generateId();

    this.red = Provider.instance().gameManager().createTeam(TeamType.RED);
    this.blue = Provider.instance().gameManager().createTeam(TeamType.BLUE);

    this.spectators = new DefaultSpectators();
    this.scheduler = new DefaultRoomScheduler();
  }

  @Override
  public TeamType getSuitableTeam() {
    if (red.teamSize() > blue.teamSize()){
      return TeamType.BLUE;
    } else {
      return TeamType.RED;
    }
  }

  @Override
  public GameStatus getStatus() {
    return status;
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
    return true;
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
}
