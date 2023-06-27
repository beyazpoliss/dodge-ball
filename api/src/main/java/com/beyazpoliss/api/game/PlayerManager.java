package com.beyazpoliss.api.game;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface PlayerManager {

  void teleport(@NotNull final UUID uuid,@NotNull final DefaultLocation teamLocation);

  void give_team_select_item(@NotNull final UUID uuid);

  void give_ball_item(@NotNull final UUID uuid);

  void clear_inv(@NotNull final UUID uuid);


}
