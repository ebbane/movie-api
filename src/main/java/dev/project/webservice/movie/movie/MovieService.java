package dev.project.webservice.movie.movie;

import dev.project.webservice.movie.exception.RecordNotFoundException;
import dev.project.webservice.movie.movie.model.front.GenreFront;
import dev.project.webservice.movie.movie.model.front.MovieFront;
import dev.project.webservice.movie.movie.model.mb.MovieMb;
import dev.project.webservice.movie.movie.model.mb.MoviePeopleMb;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

  private final MovieRepository movieRepository;
  private final MovieMapper movieMapper;


  public MovieService(MovieRepository movieRepository, MovieMapper movieMapper) {
    this.movieRepository = movieRepository;
    this.movieMapper = movieMapper;
  }

  public List<GenreFront> getGenres() {
    return movieMapper.genresMbToFront(movieRepository.getGenres());
  }

  public Long createMovie(MovieFront movieFront) {
    MovieMb movieMb = movieMapper.movieFrontToMb(movieFront);
    movieRepository.insertMovie(movieMb);
    manageInsertMovieMovieRelation(movieMb);
    return movieMb.getId();
  }

  public void updateMovieById(Long id, MovieFront movieFront) {
    var actual = movieRepository.getMovieById(id);
    if (actual.isEmpty()) {
      throw new RecordNotFoundException(String.format("Film (id=%d) introuvable", id));
    }
    MovieMb movieToUpdate = movieMapper.movieFrontToMb(movieFront);
    movieRepository.updateMovie(movieToUpdate);
    manageUpdateMovieRelation(movieToUpdate);
  }

  public Page<MovieFront> getMovies(Pageable pageable, String actors, String directors) {
    List<Long> actorIds = splitPeopleStringToList(actors);
    List<Long> directorIds = splitPeopleStringToList(directors);
    var movieMbs = movieRepository.getMovies();
    List<MovieMb> sortMovies = sortMoviesByActorsAndDirectors(movieMbs, actorIds, directorIds);
    return buildMoviePage(sortMovies, pageable);
  }

  private List<Long> splitPeopleStringToList(String peopleString) {

    if (peopleString == null) {
      return Collections.emptyList();
    }
    String[] stringArray = peopleString.split(",");
    return Arrays.stream(stringArray)
        .map(Long::valueOf).toList();
  }

  public MovieFront getMovieById(Long id) {
    return movieRepository.getMovieById(id)
        .map(movieMapper::movieMbToFront)
        .orElseThrow(
            () -> new RecordNotFoundException(String.format("Film (id=%d) introuvable", id)));
  }

  public void deleteMovie(Long id) {
    movieRepository.deleteMovieById(id);
  }

  private void manageUpdateMovieRelation(MovieMb movieToUpdate) {
    movieRepository.deleteMovieDirectors(movieToUpdate.getId());
    movieRepository.deleteMovieActors(movieToUpdate.getId());
    movieRepository.deleteMovieGenres(movieToUpdate.getId());
    manageInsertMovieMovieRelation(movieToUpdate);
  }

  private void manageInsertMovieMovieRelation(MovieMb movieMb) {
    movieRepository.insertMovieActors(movieMb.getActors(), movieMb.getId());
    movieRepository.insertMovieDirectors(movieMb.getDirectors(), movieMb.getId());
    movieRepository.insertMovieGenres(movieMb.getGenres(), movieMb.getId());
  }

  private List<MovieMb> sortMoviesByActorsAndDirectors(List<MovieMb> movies, List<Long> actors,
      List<Long> directors) {
    return movies.stream()
        .filter(movie -> actors.isEmpty() || actors.stream()
            .allMatch(actorId -> movieHasPerson(movie.getActors(), actorId)))
        .filter(movie -> directors.isEmpty() || directors.stream()
            .allMatch(directorId -> movieHasPerson(movie.getDirectors(), directorId)))
        .toList();
  }

  private boolean movieHasPerson(List<MoviePeopleMb> people, Long personId) {
    return people.stream().anyMatch(person -> person.id().equals(personId));
  }

  private Page<MovieFront> buildMoviePage(List<MovieMb> movies, Pageable pageable) {
    List<MovieFront> peopleFronts = movieMapper.movieMbToFront(movies);
    int endIndex = (int) Math.min(pageable.getOffset() + pageable.getPageSize(),
        peopleFronts.size());
    List<MovieFront> paginatedMovies = peopleFronts.subList((int) pageable.getOffset(), endIndex);
    return new PageImpl<>(paginatedMovies, pageable, paginatedMovies.size());
  }

}
