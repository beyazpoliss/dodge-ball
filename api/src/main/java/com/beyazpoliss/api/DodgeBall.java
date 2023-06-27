package com.beyazpoliss.api;

import com.beyazpoliss.api.config.Configuration;
import com.beyazpoliss.api.game.GameManager;
import com.beyazpoliss.api.game.PlayerManager;
import com.beyazpoliss.api.game.TimerWrapper;
import com.beyazpoliss.api.messenger.MessageSender;
import com.beyazpoliss.api.profile.ProfileManager;
import org.jetbrains.annotations.NotNull;
/**
 * the main core class of the Talent-Bank plugin.
 */
public interface DodgeBall {
  /**
   * method that starts and runs the plugin.
   */
  void enable();
  /**
   * The default build is loaded when the plugin is first opened.
   * if You want to change it immediately you can change all api layers here.
   */
  void structureChangeComponent();
  /**
   * Manages all api classes.
   *
   * @return plugin structure.
   */
  Structure structure();
  /**
   * common proxy to send messages to different places.
   *
   * @return common messenger.
   */
  @NotNull
  default MessageSender messenger(){
    return this.structure().provideOrThrow(MessageSender.class);
  }
  /**
   * It does operations like config.
   *
   * @see Configuration
   */
  @NotNull
  default Configuration configuration(){
    return this.structure().provideOrThrow(Configuration.class);
  }
  /**
   * It does operations like game.
   *
   * @see GameManager
   */
  @NotNull
  default GameManager gameManager(){
    return this.structure().provideOrThrow(GameManager.class);
  }
  /**
   * It does operations players.
   *
   * @see PlayerManager
   */
  @NotNull
  default PlayerManager playerManager(){
    return this.structure().provideOrThrow(PlayerManager.class);
  }
  /**
   * It does operations profiles.
   *
   * @see ProfileManager
   */
  @NotNull
  default ProfileManager profileManager(){
    return this.structure().provideOrThrow(ProfileManager.class);
  }
  /**
   * It does operations profiles.
   *
   * @see ProfileManager
   */
  @NotNull
  default TimerWrapper timer(){
    return this.structure().provideOrThrow(TimerWrapper.class);
  }
}
