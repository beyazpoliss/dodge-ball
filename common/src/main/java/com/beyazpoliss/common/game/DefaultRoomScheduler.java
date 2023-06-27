package com.beyazpoliss.common.game;

import com.beyazpoliss.api.Provider;
import com.beyazpoliss.api.game.GameRoom;
import com.beyazpoliss.api.game.GameStatus;
import com.beyazpoliss.api.game.RoomScheduler;
import com.beyazpoliss.api.game.TeamType;
import org.jetbrains.annotations.NotNull;

public class DefaultRoomScheduler implements RoomScheduler {

  @NotNull
  private GameStatus status;

  private int round;

  //Round Start time
  private int waitingTime;

  //Lobby start time
  private int startTime;

  @NotNull
  private final GameRoom room;

  private int taskId;

  public DefaultRoomScheduler(@NotNull final GameRoom room){
    this.status = GameStatus.WAITING;
    this.room = room;
    this.round = 0;
    this.startTime = 20;
    this.waitingTime = 20;
  }

  @Override
  public GameStatus status() {
    return status;
  }

  @Override
  public void status(@NotNull GameStatus status) {
    this.status = status;
  }

  @Override
  public void schedule_room(){
  }

  @Override
  public void schedule_status() {
    final var timer = Provider.instance().timer();
    final var gameManager = Provider.instance().gameManager();
    if (room.team(TeamType.RED).players().size() == room.team(TeamType.BLUE).teamSize()){
      taskId = timer.runTaskTimerAsync(() -> {
        if (room.team(TeamType.RED).players().size() == room.team(TeamType.BLUE).teamSize()){
          if (startTime == 0){
            start_round();
            timer.cancelTask(taskId);
          }
          if (startTime > 0 && startTime <= 20){
            gameManager.send_all_players_to_room(room,"&7DODGE-BALL >> This game start is " + startTime + "  seconds.");
          }
          startTime--;
        } else {
          startTime = 20;
        }
      },20,20);
    }
  }

  @Override
  public void start_round() {
    final var manager = Provider.instance().gameManager();
    round++;
    if (round == 1){
      manager.send_all_players_to_room(room,"The first round has started!");
      manager.start_round(room);
    }
  }

}
