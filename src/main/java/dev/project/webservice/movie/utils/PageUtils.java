package dev.project.webservice.movie.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public final class PageUtils {

  private PageUtils() {
  }

  public static Pageable sortedPageable(
      int page,
      int pageSize
  ) {
    return PageRequest.of(page, pageSize);
  }
}
