package com.beyazpoliss.api;

import com.google.common.base.Preconditions;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
/**
 * @author BeyazPolis
 */
@UtilityClass
public class Provider {
  /**
   * the plugin instance.
   */
  @Nullable
  private DodgeBall instance;
  /**
   * obtains the instance.
   *
   * @return instance.
   */
  @NotNull
  public DodgeBall instance() {
    return Objects.requireNonNull(Provider.instance, "The instance not set yet!");
  }
  /**
   * sets the instance.
   *
   * @param instance the instance to set.
   * @throws IllegalStateException if the instance is already set.
   */
  public void instance(@NotNull final DodgeBall instance) {
    Preconditions.checkState(
        Provider.instance == null,
        "The instance has been set already!",
        Provider.instance
    );
    Provider.instance = instance;
  }
}
