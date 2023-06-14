package com.beyazpoliss.api.profile;

import com.beyazpoliss.api.Provider;
import com.beyazpoliss.api.database.Storage;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface ProfileManager {

  Collection<Profile> profiles();

  Optional<Profile> get(UUID uuid);

  void put(@NotNull final UUID uuid, @NotNull final Profile profile);

  void removeKey(@NotNull final UUID uuid);

  default void process_of_join(@NotNull final UUID uuid){
    final var storage = Provider.instance().structure().provideOrThrow(Storage.class);
    storage.getOrCreateAsync(uuid).thenApply(profile -> {
      put(uuid,profile);
      return profile;
    });
  }

  default void process_of_quit(@NotNull final UUID uuid){
    final var storage = Provider.instance().structure().provideOrThrow(Storage.class);
    storage.saveOrCreateAsync(uuid).thenApply(profile -> {
      removeKey(uuid);
      return profile;
    });
  }

}
