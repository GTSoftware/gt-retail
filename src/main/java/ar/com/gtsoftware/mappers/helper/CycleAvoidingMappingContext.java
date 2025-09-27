/*
 * Copyright 2018 GT Software.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package ar.com.gtsoftware.mappers.helper;

import java.lang.reflect.Method;
import java.util.IdentityHashMap;
import java.util.Map;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Context;
import org.mapstruct.MappingTarget;
import org.mapstruct.TargetType;

/**
 * A type to be used as {@link Context} parameter to track cycles in graphs.
 *
 * <p>Note: When MapStruct generates code for Lombok @Builder targets, it may cache the builder
 * instance in this context to break cycles. That causes a class cast exception when MapStruct later
 * tries to retrieve the cached value as the DTO class. To handle this, getMappedInstance will
 * detect builder instances (objects that expose a no-arg build() method) and return the built
 * object instead, updating the cache.
 */
public class CycleAvoidingMappingContext {
  private final Map<Object, Object> knownInstances = new IdentityHashMap<>();

  @BeforeMapping
  @SuppressWarnings("unchecked")
  public <T> T getMappedInstance(Object source, @TargetType Class<T> targetType) {
    Object cached = knownInstances.get(source);
    if (cached == null) {
      return null;
    }
    if (targetType.isInstance(cached)) {
      return (T) cached;
    }
    // If the cached value is a builder, try to build the target instance on-the-fly
    try {
      Method buildMethod = cached.getClass().getMethod("build");
      Object built = buildMethod.invoke(cached);
      if (targetType.isInstance(built)) {
        knownInstances.put(source, built);
        return (T) built;
      }
    } catch (ReflectiveOperationException ignored) {
      // Not a builder or build() failed; fall through and return null
    }
    return null;
  }

  @BeforeMapping
  public void storeMappedInstance(Object source, @MappingTarget Object target) {
    knownInstances.put(source, target);
  }
}
