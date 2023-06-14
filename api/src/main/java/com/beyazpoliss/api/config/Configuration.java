package com.beyazpoliss.api.config;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface Configuration {

  /**
   * Asynchronously loads an object.
   *
   * @return a CompletableFuture that completes when the object is loaded
   */
  CompletableFuture<Void> load(ConfigOptions options);

  /**
   * Asynchronously saves an object.
   *
   * @return a CompletableFuture that completes when the object is saved
   */
  CompletableFuture<Void> save();

  /**
   * Sets the value of an object if it does not already exist at the specified path.
   *
   * @param path   the path where the object should be set
   * @param object the object to set
   */
  void setIfNotExists(@NotNull final String path, Object object);

  /**
   * Retrieves the keys at the specified path.
   *
   * @param path the path to retrieve keys from
   * @param deep specifies whether to perform a deep search for keys
   * @return a list of keys at the specified path
   */
  List<String> getKeys(@NotNull final String path, final boolean deep);

  /**
   * Checks if an object is not set at the specified path.
   *
   * @param path the path to check
   * @return true if the object is not set, false otherwise
   */
  boolean isNotSet(@NotNull final String path);

  /**
   * Retrieves the object at the specified path.
   *
   * @param path the path to retrieve the object from
   * @return the object at the specified path
   */
  Object get(@NotNull final String path);

  /**
   * Creates a file in the specified plugin folder if it does not already exist.
   *
   * @param pluginFolder the folder where the file should be created
   */
  default void createFile(@NotNull final File pluginFolder) {
    if (!pluginFolder.exists()) {
      try {
        Files.createDirectories(pluginFolder.toPath());
        pluginFolder.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}