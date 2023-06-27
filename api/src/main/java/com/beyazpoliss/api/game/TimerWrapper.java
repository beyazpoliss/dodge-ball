package com.beyazpoliss.api.game;

import org.jetbrains.annotations.NotNull;

public interface TimerWrapper {

  int runTaskTimerAsync(@NotNull final Runnable runnable,int period, int late);

  void runTaskSync(@NotNull final Runnable runnable);

  void cancelTask(int id);

}
