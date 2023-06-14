package com.beyazpoliss.common.manager;

import com.beyazpoliss.api.profile.Profile;
import com.beyazpoliss.api.profile.ProfileManager;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class DefaultProfileManager implements ProfileManager {

  @NotNull
  private final Map<UUID,Profile> profiles = new HashMap<>();

  @Override
  public Collection<Profile> profiles() {
    return profiles.values();
  }

  @Override
  public Optional<Profile> get(@NotNull final UUID uuid) {
    return Optional.ofNullable(profiles.get(uuid));
  }

  @Override
  public void put(@NotNull final UUID uuid, @NotNull final Profile profile) {
    this.profiles.put(uuid,profile);
  }

  @Override
  public void removeKey(@NotNull final UUID uuid) {
    this.profiles.remove(uuid);
  }
}
