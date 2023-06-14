package com.beyazpoliss.api;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public interface Structure {
  /**
   * creates a simple structure.
   *
   * @return a newly created simple structure.
   */
  @NotNull
  static Structure create() {
    return new Impl();
  }
  /**
   * @return all implementations
   */
  Map<Class<?>, StructureObject> all();
  /**
   * provides the given class's implementation.
   *
   * @param cls the cls to provide.
   * @param <T> type of the provided class.
   *
   * @return provided implementation.
   */
  @NotNull
  <T> Optional<T> provide(@NotNull Class<? extends T> cls);
  /**
   * provides the given class's implementation.
   *
   * @param cls the cls to provide.
   * @param <T> type of the provided class.
   *
   * @return provided implementation.
   *
   * @throws NoSuchElementException if the provider not found.
   */
  @NotNull
  default <T> T provideOrThrow(@NotNull final Class<? extends T> cls) {
    System.out.println(cls);
    return this.provide(cls)
      .orElseThrow(() ->
        new IllegalStateException(
          "Provider for %s not found!".formatted(cls.toString())
        )
      );
  }
  /**
   * registers the object.
   *
   * @param object the object to register.
   *
   * @return {@code this} for builder chain.
   */
  @NotNull
  Structure register(@NotNull StructureObject object);
  /**
   * registers the object.
   *
   * @param object the object to register.
   *
   * @return {@code this} for builder chain.
   */
  @NotNull
  Structure register(@NotNull StructureObject... object);
  /**
   * a simple implementation for {@link Provider}.
   */
  @SuppressWarnings("unchecked")
  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  final class Impl implements Structure {
    /**
     * the implementations.
     */
    @NotNull
    private final Map<Class<?>, StructureObject> implementations = new HashMap<>();

    @Override
    public Map<Class<?>, StructureObject> all() {
      return implementations;
    }

    @NotNull
    @Override
    public <T> Optional<T> provide(@NotNull final Class<? extends T> cls) {
      return Optional.ofNullable(this.implementations.get(cls)).map(o -> (T) o.getObject());
    }

    @NotNull
    @Override
    public Structure register(@NotNull final StructureObject object) {
      this.implementations.put(object.getAClass(), object);
      return this;
    }

    @Override
    public @NotNull Structure register(@NotNull StructureObject... object) {
      Arrays.stream(object).forEach(o -> implementations.put(o.getAClass(),o));
      return this;
    }
  }
}
