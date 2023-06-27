package com.beyazpoliss.api.game;

import org.jetbrains.annotations.NotNull;

public interface RoomScheduler {

  GameStatus status();

  void status(@NotNull final GameStatus status);

  void schedule_room();

  void schedule_status();

  void start_round();

}
