package com.beyazpoliss.common.database;

import com.beyazpoliss.api.Provider;
import com.beyazpoliss.api.database.Storage;
import com.beyazpoliss.api.profile.Profile;
import com.beyazpoliss.api.profile.ProfileImpl;
import com.beyazpoliss.api.profile.ProfileManager;
import com.mongodb.client.*;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class MongoStorage implements Storage {

  private MongoClient client;

  private MongoDatabase database;

  private final String collection_name = "Profiles";

  @Override
  public CompletableFuture<Void> connectAsync(@NotNull final String url) {
    return CompletableFuture.runAsync(() -> {
      try {
        client = MongoClients.create(url);
        database = client.getDatabase("dodge-ball");
      }catch (Exception e){
        e.printStackTrace();
      }
    });
  }

  @Override
  public void close() {
    client.close();
  }

  @Override
  public CompletableFuture<Optional<Profile>> getOrEmptyAsync(@NotNull final UUID uuid) {
    return CompletableFuture.supplyAsync(() -> {
      final var document = new Document("id", uuid.toString());
      final var profileDocument = database
        .getCollection(collection_name)
        .find(document)
        .first();
      if (profileDocument == null){
        return Optional.empty();
      }
      final var wins = profileDocument.getInteger("wins");
      return Optional.of(new ProfileImpl(uuid,wins));
    });
  }

  @NotNull
  public CompletableFuture<Optional<Profile>> saveAsync(@NotNull final UUID uuid) {
    return CompletableFuture.supplyAsync(() -> {
      final ProfileManager manager = Provider.instance().structure().provideOrThrow(ProfileManager.class);
      final Optional<Profile> estimatedProfile = manager.get(uuid);

      estimatedProfile.ifPresent(profile -> {
        final var document = new Document("id", uuid.toString());
        final var collection = database.getCollection(collection_name);
        final var first = collection.find(document).first();
        final var profileDocument = new Document("id", profile.getUuid().toString()).append("wins", profile.getWins());
        if (first != null) {
          collection.updateOne(first, profileDocument);
        } else {
          collection.insertOne(profileDocument);
        }
      });
      return estimatedProfile;
    });
  }

  @Override
  public @NotNull CompletableFuture<Profile> getOrCreateAsync(@NotNull final UUID uuid) {
    return CompletableFuture.supplyAsync(() -> {
      final ProfileManager manager = Provider.instance().structure().provideOrThrow(ProfileManager.class);
      final var estimated_profile = manager.get(uuid);
      if (estimated_profile.isPresent()) {
        return estimated_profile.get();
      } else {
        final var document = new Document("id", uuid.toString());
        final var collection = database.getCollection(collection_name);
        final var first = collection.find(document).first();
        if (first == null){
          return new ProfileImpl(uuid,0);
        } else {
          final var wins = first.getInteger("wins");
          return new ProfileImpl(uuid,wins);
        }
      }
    });
  }

  @Override
  public @NotNull CompletableFuture<Profile> saveOrCreateAsync(@NotNull final UUID uuid) {
    return CompletableFuture.supplyAsync(() -> {
      final ProfileManager manager = Provider.instance().structure().provideOrThrow(ProfileManager.class);
      final Optional<Profile> estimatedProfile = manager.get(uuid);

      estimatedProfile.ifPresent(profile -> {
        final var document = new Document("id", uuid.toString());
        final var collection = database.getCollection(collection_name);
        final var first = collection.find(document).first();
        final var profileDocument = new Document("id", profile.getUuid().toString()).append("wins", profile.getWins());
        if (first != null) {
          collection.updateOne(first, profileDocument);
        } else {
          collection.insertOne(profileDocument);
        }
      });

      if (estimatedProfile.isEmpty()) {
        final var profile = new ProfileImpl(uuid, 0);
        final var document = new Document("id", uuid.toString());
        final var collection = database.getCollection(collection_name);
        final var first = collection.find(document).first();
        final var profileDocument = new Document("id", profile.getUuid().toString()).append("wins", profile.getWins());
        if (first != null) {
          collection.updateOne(first, profileDocument);
        } else {
          collection.insertOne(profileDocument);
        }
        return profile;
      }

      return estimatedProfile.get();
    });
  }
}
