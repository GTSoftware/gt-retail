package ar.com.gtsoftware.api.transformer;

import java.util.List;

public interface Transformer<T, R> {

  String DISPLAY_NAME_FMT = "[%d] %s";

  R transform(T from);

  default List<R> transform(List<T> from) {
    return from.stream().map(this::transform).toList();
  }
}
