package dev.project.webservice.movie.movie;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.project.webservice.movie.movie.model.front.GenreFront;
import dev.project.webservice.movie.movie.model.front.MovieFront;
import dev.project.webservice.movie.utils.PageUtils;
import java.util.List;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.Seed;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@ExtendWith(InstancioExtension.class)
class MovieControllerTest {

  private MovieController movieController;
  private MovieService movieService;

  @BeforeEach
  void beforeEach() {
    movieService = Mockito.mock(MovieService.class);
    movieController = new MovieController(movieService);
  }

  @Test
  @Seed(3)
  void getGenres_nominal_callService() {
    //GIVEN
    List<GenreFront> genres = Instancio.ofList(GenreFront.class).size(10).create();
    when(movieService.getGenres()).thenReturn(genres);

    //WHEN
    List<GenreFront> actual = movieController.getGenres();

    //THEN
    assertThat(actual).containsExactlyElementsOf(genres);
    verify(movieService).getGenres();
  }

  @Test
  @Seed(3)
  void getMovies_noFilter_callService() {
    //GIVEN
    List<MovieFront> movies = Instancio.ofList(MovieFront.class).size(2).create();
    Pageable pageable = PageUtils.sortedPageable(0, 20);
    var expected = new PageImpl<>(movies, pageable, movies.size());
    when(movieService.getMovies(pageable, null, null)).thenReturn(expected);

    //WHEN
    Page<MovieFront> actual = movieController.getMovies(null, null, 0, 20);

    //THEN
    assertThat(actual).containsExactlyElementsOf(expected);
    verify(movieService).getMovies(pageable, null, null);
  }

  @Test
  @Seed(3)
  void getMovieById_nominal_callService() {
    //GIVEN
    MovieFront movie = Instancio.create(MovieFront.class);
    when(movieService.getMovieById(movie.id())).thenReturn(movie);

    //WHEN
    MovieFront actual = movieController.getMovieById(movie.id());

    //THEN
    assertThat(actual).isEqualTo(movie);
    verify(movieService).getMovieById(movie.id());
  }

  @Test
  @Seed(3)
  void deleteById_nominal_callService() {
    //GIVEN
    var movieId = 1L;
    doNothing().when(movieService).deleteMovie(movieId);

    //WHEN
    movieController.deleteById(movieId);

    //THEN
    verify(movieService).deleteMovie(movieId);
  }

  @Test
  @Seed(3)
  void updatePeople_nominal_callService() {
    //GIVEN
    var movieId = 1L;
    MovieFront movieFront = Instancio.create(MovieFront.class);
    doNothing().when(movieService).updateMovieById(movieId, movieFront);

    //WHEN
    movieController.updateMovie(movieId, movieFront);

    //THEN
    verify(movieService).updateMovieById(movieId, movieFront);
  }

  @Test
  @Seed(3)
  void createPeople_nominal_callService() {
    //GIVEN
    MovieFront movieFront = Instancio.create(MovieFront.class);
    when(movieService.createMovie(movieFront)).thenReturn(1L);
    var expected = ResponseEntity.ok()
        .body(1L);

    //WHEN
    var actual = movieController.createMovie(movieFront);

    //THEN
    assertThat(actual).isEqualTo(expected);
    verify(movieService).createMovie(movieFront);
  }


}