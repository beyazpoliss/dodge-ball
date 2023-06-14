package com.beyazpoliss.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@Getter
public class StructureObject {

  private final Object object;
  private final Class<?> aClass;

  @Contract("_, _ -> new")
  public static @NotNull StructureObject get(@NotNull final Object object, @NotNull final Class<?> aClass){
    return new StructureObject(object,aClass);
  }
}
