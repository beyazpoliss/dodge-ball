package com.beyazpoliss.common;

import com.beyazpoliss.api.config.Configuration;

import java.util.Arrays;

public class ConfigOptions implements com.beyazpoliss.api.config.ConfigOptions {

  public void setOptions(Configuration configuration){

    configuration.setIfNotExists("server.game-world.name", "minigame");
    configuration.setIfNotExists("server.game-world.red-team.x",-22);
    configuration.setIfNotExists("server.game-world.red-team.y",15);
    configuration.setIfNotExists("server.game-world.red-team.z",67);
    configuration.setIfNotExists("server.game-world.blue-team.x",22);
    configuration.setIfNotExists("server.game-world.blue-team.y", 15);
    configuration.setIfNotExists("server.game-world.blue-team.z", 38);

    configuration.setIfNotExists("server.database-options.redis.host", "localhost");
    configuration.setIfNotExists("server.database-options.redis.port", 6767);
    configuration.setIfNotExists("server.database-options.mongodb.url", "mongodb://localhost:27017");
    configuration.setIfNotExists("game.options.max-size", 20);
    configuration.setIfNotExists("game.options.win-round", 5);
    configuration.setIfNotExists("game.options.win-commands", Arrays.asList("/give %player% diamond_block",""));
    configuration.setIfNotExists("game.options.lose-commands", Arrays.asList("/kick %player%",""));
    configuration.setIfNotExists("game.options.per-server-room-count", 10);
    configuration.setIfNotExists("game.scheduler.start-time-second", 20);
    configuration.setIfNotExists("game.scheduler.spectate-time-second", 5);
  }

}
