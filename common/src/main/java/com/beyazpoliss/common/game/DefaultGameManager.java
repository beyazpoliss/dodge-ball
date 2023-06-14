package com.beyazpoliss.common.game;

import com.beyazpoliss.api.Provider;
import com.beyazpoliss.api.game.GameManager;
import com.beyazpoliss.api.game.GameRoom;
import com.beyazpoliss.api.game.Team;
import com.beyazpoliss.api.game.TeamType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DefaultGameManager implements GameManager {

  private final Map<String,GameRoom> rooms = new HashMap<>();

  @Override
  public Collection<GameRoom> rooms() {
    return rooms.values();
  }

  @Override
  public Team createTeam(@NotNull TeamType type) {
    return new DefaultTeam(type);
  }

  @Override
  public GameRoom createRoom(@NotNull Team... teams) {
    return new DefaultGameRoom();
  }

  @Override
  public GameRoom createEmptyRoom() {
    return new DefaultGameRoom();
  }

  @Override
  public void enable_process_of_rooms() {
    final var config = Provider.instance().configuration();
    final var roomCount = (int) config.get("game.options.per-server-room-count");
    for (int i = 0; i <= roomCount; i++) {
      final var emptyRoom = createEmptyRoom();
      rooms.put(emptyRoom.roomId(),emptyRoom);
    }
  }

  @Override
  public boolean join_room(@NotNull String roomId, @NotNull UUID player) {
    System.out.println(roomId);
    rooms.forEach((s, gameRoom) -> System.out.println(s));
    if (rooms.get(roomId) == null) return false;
    final var room = rooms.get(roomId);
    if (room.canJoin(player,room.getSuitableTeam())){
      return true;
    }

    return false;
  }
}
