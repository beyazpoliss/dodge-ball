package com.beyazpoliss.api.game;

import java.util.List;
import java.util.UUID;

public interface Team {

  TeamType type();

  List<UUID> players();

  int teamSize();

}
