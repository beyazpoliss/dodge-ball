package com.beyazpoliss.common.game;

import com.beyazpoliss.api.Provider;
import com.beyazpoliss.api.game.*;
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
  public Team create_team(@NotNull final TeamType type, @NotNull final DefaultLocation location, int maxSize) {
    return new DefaultTeam(type,location,maxSize);
  }

  @Override
  public void send_all_players_to_room(@NotNull GameRoom room, @NotNull String message) {

    final var messenger = Provider.instance().messenger();
    room.red().players().forEach(player -> {
      messenger.sendMessageUuid(player,message);
    });


    room.blue().players().forEach(player -> {
      messenger.sendMessageUuid(player,message);
    });
  }

  @Override
  public GameRoom create_room(@NotNull Team... teams) {
    return new DefaultGameRoom(20);
  }

  @Override
  public void start_round(@NotNull GameRoom room) {
    final var manager = Provider.instance().playerManager();
    final var red = room.team(TeamType.RED);
    final var blue = room.team(TeamType.BLUE);

    red.players().forEach(manager::clear_inv);
    blue.players().forEach(manager::clear_inv);

    red.players().forEach(manager::give_ball_item);
    blue.players().forEach(manager::give_ball_item);

    Provider.instance().timer().runTaskSync(() -> {
      red.players().forEach(player-> {
        manager.teleport(player,red.spawn());
      });
      blue.players().forEach(player->{
        manager.teleport(player,blue.spawn());
      });
    });
  }

  @Override
  public GameRoom get_room_by_player(@NotNull final UUID uuid) {
    for (GameRoom room : rooms()){
      if (room.red().players().contains(uuid)){
        return room;
      }

      if (room.blue().players().contains(uuid)){
        return room;
      }
    }
    return null;
  }

  @Override
  public GameRoom create_empty_room() {
    return new DefaultGameRoom(20);
  }

  @Override
  public void change_team_player(@NotNull final String playerName, @NotNull final UUID player, @NotNull final TeamType type) {
    for (GameRoom room : rooms()){
      if (room.red().players().contains(player)){
        room.red().removePlayer(player);
        this.join_team_but_player_process(room,type,playerName,player);
        return;
      }

      if (room.blue().players().contains(player)){
        room.blue().removePlayer(player);
        this.join_team_but_player_process(room,type,playerName,player);
        return;
      }
      return;
    }

  }

  @Override
  public void enable_process_of_rooms() {
    final var config = Provider.instance().configuration();
    final var roomCount = (int) config.get("game.options.per-server-room-count");
    for (int i = 0; i <= roomCount; i++) {
      final var emptyRoom = this.create_empty_room();
      rooms.put(emptyRoom.roomId(),emptyRoom);
    }
  }

  @Override
  public boolean join_room(@NotNull String roomId, @NotNull final String playerName, @NotNull UUID player) {
    if (rooms.get(roomId) == null) return false;
    final var room = rooms.get(roomId);

    if (room.room_is_suitable_for_join()){
      Provider.instance().messenger().sendMessagePlayer(playerName,"&cThis room is full!");
      return false;
    }
    final var suitableTeam = room.getSuitableTeam();
    if (room.canJoin(player,suitableTeam)){
      if (this.join_team_but_player_process(room,suitableTeam,playerName,player)){
        return true;
      }
      return true;
    } else {
      Provider.instance().messenger().sendMessagePlayer(playerName,"&cThis room is not suitable!");
    }
    return false;
  }


  @Override
  public boolean join_team_but_player_process(@NotNull final GameRoom room, @NotNull final TeamType type,@NotNull final String playerName, @NotNull final UUID uuid){
    final var manager = Provider.instance().playerManager();
    final var team = room.team(type);
    team.joinTeam(uuid,playerName);
    this.send_all_players_to_room(room,playerName + " has joined the "+ type.getType() + " team!");
    manager.teleport(uuid,team.spawn());
    manager.give_team_select_item(uuid);
    room.scheduler().schedule_status();
    return true;
  }

}
