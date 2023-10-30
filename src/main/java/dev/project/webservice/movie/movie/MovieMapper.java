package dev.project.webservice.movie.movie;

import dev.project.webservice.movie.movie.model.front.GenreFront;
import dev.project.webservice.movie.movie.model.front.MovieFront;
import dev.project.webservice.movie.movie.model.mb.GenreMb;
import dev.project.webservice.movie.movie.model.mb.MovieMb;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface MovieMapper {

  List<GenreFront> genresMbToFront(List<GenreMb> genresMb);

  MovieMb movieFrontToMb(MovieFront movieFront);

  MovieFront movieMbToFront(MovieMb movieFront);

  List<MovieFront> movieMbToFront(List<MovieMb> moviesFront);

}
