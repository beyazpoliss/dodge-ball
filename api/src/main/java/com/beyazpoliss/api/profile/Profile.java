package com.beyazpoliss.api.profile;

import java.util.UUID;

public interface Profile {

  UUID getUuid();

  int getWins();

  int setWins(int number);

  int addWin();

  int addLose();

}

