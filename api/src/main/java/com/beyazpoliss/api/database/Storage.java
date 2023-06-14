package com.beyazpoliss.api.database;

import com.beyazpoliss.api.profile.Profile;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface Storage {

  CompletableFuture<Void> connectAsync(@NotNull final String url);

  void close();

  CompletableFuture<Optional<Profile>> getOrEmptyAsync(@NotNull final UUID uuid);

  CompletableFuture<Optional<Profile>> saveAsync(@NotNull final UUID uuid);

  @NotNull
  CompletableFuture<Profile> getOrCreateAsync(@NotNull final UUID uuid);

  @NotNull
  CompletableFuture<Profile> saveOrCreateAsync(@NotNull final UUID uuid);

}