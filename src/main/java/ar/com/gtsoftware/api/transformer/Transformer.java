package ar.com.gtsoftware.api.transformer;

import java.util.List;

public interface Transformer<T, R> {

  String DISPLAY_NAME_FMT = "[%d] %s";

  R transform(T from);

  List<R> transform(List<T> from);
}
