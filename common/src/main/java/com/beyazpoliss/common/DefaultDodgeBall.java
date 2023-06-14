package com.beyazpoliss.common;

import com.beyazpoliss.api.DodgeBall;
import com.beyazpoliss.api.Provider;
import com.beyazpoliss.api.Structure;
import com.beyazpoliss.api.StructureObject;
import com.beyazpoliss.api.database.Storage;
import com.beyazpoliss.api.game.GameManager;
import com.beyazpoliss.api.profile.ProfileManager;
import com.beyazpoliss.common.database.MongoStorage;
import com.beyazpoliss.common.game.DefaultGameManager;
import com.beyazpoliss.common.manager.DefaultProfileManager;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public abstract class DefaultDodgeBall implements DodgeBall {

  @NotNull
  private final Structure structure;

  @NotNull
  private final ConfigOptions options;

  @NotNull
  private final MongoStorage mongoStorage;

  @NotNull
  private final ProfileManager profileManager;

  @NotNull
  private final GameManager gameManager;

  public DefaultDodgeBall(){
    this.options = new ConfigOptions();
    this.mongoStorage = new MongoStorage();
    this.profileManager = new DefaultProfileManager();
    this.gameManager = new DefaultGameManager();
    this.structure = Structure.create().register(
      StructureObject.get(this.options, com.beyazpoliss.api.config.ConfigOptions.class),
      StructureObject.get(this.mongoStorage, Storage.class),
      StructureObject.get(this.profileManager, ProfileManager.class),
      StructureObject.get(this.gameManager, GameManager.class)
    );
    this.structureChangeComponent();
  }

  @Override
  public Structure structure() {
    return structure;
  }

  @Override
  public void enable() {
    this.messenger().sendMessageConsole(Level.INFO,"Welcome the DodgeBall. This plugin dodgeball each features plugin.");
    this.configuration().load(options).thenAccept(unused -> {
      this.options.setOptions(configuration());
      this.configuration().save();
      this.mongoStorage.connectAsync((String) configuration().get("server.database-options.mongodb.url"))
        .thenAccept(u -> Provider.instance().gameManager().enable_process_of_rooms());
    });
  }
}
