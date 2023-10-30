package dev.project.webservice.movie.movie;

import dev.project.webservice.movie.movie.model.mb.GenreMb;
import dev.project.webservice.movie.movie.model.mb.MovieMb;
import dev.project.webservice.movie.movie.model.mb.MoviePeopleMb;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MovieRepository {

  @Select(
      """
              SELECT id, name
              FROM genre
          """
  )
  List<GenreMb> getGenres();

  @Insert(
      """
          INSERT INTO movie (name, description, publication_date, duration, distributor)
          VALUES (#{name}, #{description}, #{publicationDate}, #{duration}, #{distributor})
          """
  )
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertMovie(MovieMb movieMb);

  @Insert("""
      <script>
          INSERT INTO movie_genre(movie_id, genre_id)  values
          <foreach item='genre' collection='genres' separator=','>
              (#{movieId}, CAST(#{genre.id} AS bigint))
          </foreach>
      </script>""")
  void insertMovieGenres(List<GenreMb> genres, Long movieId);

  @Insert("""
      <script>
          INSERT INTO movie_actor(movie_id, people_id)  values
          <foreach item='people' collection='peoples' separator=','>
              (#{movieId}, CAST(#{people.id} AS bigint))
          </foreach>
      </script>""")
  void insertMovieActors(List<MoviePeopleMb> peoples, Long movieId);

  @Insert("""
      <script>
          INSERT INTO movie_director(movie_id, people_id)  values
          <foreach item='people' collection='peoples' separator=','>
              (#{movieId}, CAST(#{people.id} AS bigint))
          </foreach>
      </script>""")
  void insertMovieDirectors(List<MoviePeopleMb> peoples, Long movieId);


  @Select(
      """
      SELECT m.id, m.name, m.description, m.publication_date, m.duration, m.distributor
      FROM movie m
      LEFT JOIN movie_actor ma ON m.id = ma.movie_id
      LEFT JOIN people_role ra ON ma.people_id = ra.people_id
      LEFT JOIN people pa ON ma.people_id = pa.id
      LEFT JOIN movie_director md ON m.id = md.movie_id
      LEFT JOIN people_role rd ON md.people_id = rd.people_id
      LEFT JOIN people pd ON md.people_id = pd.id
      WHERE
        (#{actors} IS NULL OR (
          ra.role_id = 'ACTOR' AND pa.lastname IN (#{actors})
        ))
        AND (#{directors} IS NULL OR (
          rd.role_id = 'DIRECTOR' AND pd.lastname IN (#{directors})
        ))
      LIMIT #{pageSize} OFFSET #{offset}
      """
  )
  @Results(value = {
      @Result(property = "id", column = "id"),
      @Result(property = "actors", column = "id", many = @Many(select = "selectMovieActors")),
      @Result(property = "directors", column = "id", many = @Many(select = "selectMovieDirectors")),
      @Result(property = "genre", column = "id", many = @Many(select = "selectMovieGenres"))
  })
  List<MovieMb> getMoviesByPeoplesLastName(int offset, int pageSize, List<String> actors, List<String> directors);

  @Select(
      """
      SELECT DISTINCT m.id, m.name, m.description, m.publication_date, m.duration, m.distributor
      FROM movie m
      LEFT JOIN movie_actor ma ON m.id = ma.movie_id
      LEFT JOIN movie_director md ON m.id = md.movie_id
      WHERE
        (
          (ARRAY[#{actors}]::BIGINT[] IS NULL OR ma.people_id = ANY(ARRAY[#{actors}]::BIGINT[]))
          AND
          (ARRAY[#{directors}]::BIGINT[] IS NULL OR md.people_id = ANY(ARRAY[#{directors}]::BIGINT[]))
        )
      LIMIT #{pageSize} OFFSET #{offset}
      """
  )
  @Results(value = {
      @Result(property = "id", column = "id"),
      @Result(property = "actors", column = "id", many = @Many(select = "selectMovieActors")),
      @Result(property = "directors", column = "id", many = @Many(select = "selectMovieDirectors")),
      @Result(property = "genres", column = "id", many = @Many(select = "selectMovieGenres"))
  })
  List<MovieMb> getMoviesByPeoples(int offset, int pageSize, Long[] actors, Long[] directors);

  @Select(
      """
      SELECT DISTINCT m.id, m.name, m.description, m.publication_date, m.duration, m.distributor
      FROM movie m
      """
  )
  @Results(value = {
      @Result(property = "id", column = "id"),
      @Result(property = "actors", column = "id", many = @Many(select = "selectMovieActors")),
      @Result(property = "directors", column = "id", many = @Many(select = "selectMovieDirectors")),
      @Result(property = "genres", column = "id", many = @Many(select = "selectMovieGenres"))
  })
  List<MovieMb> getMovies();

  @Select(
      """
          SELECT id, name, description, publication_date, duration, distributor
          FROM movie
          WHERE id = #{id}
          """
  )
  @Results(value = {
      @Result(property = "id", column = "id"),
      @Result(property = "actors", column = "id", many = @Many(select = "selectMovieActors")),
      @Result(property = "directors", column = "id", many = @Many(select = "selectMovieDirectors")),
      @Result(property = "genres", column = "id", many = @Many(select = "selectMovieGenres"))
  })
  Optional<MovieMb> getMovieById(Long id);

  @Select(
      """
          SELECT p.id, p.firstname, p.lastname, p.birthday
          FROM movie_actor ma
                   JOIN people p ON ma.people_id = p.id
          WHERE ma.movie_id = #{movieId}
          """
  )
  List<MoviePeopleMb> selectMovieActors(Long movieId);

  @Select(
      """
          SELECT p.id, p.firstname, p.lastname, p.birthday
          FROM movie_director mv
                   JOIN people p ON mv.people_id = p.id
          WHERE mv.movie_id = #{movieId}
          """
  )
  List<MoviePeopleMb> selectMovieDirectors(Long movieId);

  @Select(
      """
          SELECT id, name
          FROM movie_genre mg
                   JOIN genre g ON mg.genre_id = g.id
          WHERE mg.movie_id = #{movieId}
          """
  )
  List<GenreMb> selectMovieGenres(Long movieId);


  @Delete(
      """
          DELETE FROM movie WHERE id = #{id}
          """
  )
  void deleteMovieById(Long id);

  @Update(
      """
          UPDATE movie SET
          name = #{name},
          description = #{description},
          publication_date = #{publicationDate},
          duration = #{duration},
          distributor = #{distributor}
          WHERE id = #{id}
          """
  )
  void updateMovie(MovieMb movieMb);

  @Delete("""
          DELETE FROM movie_actor WHERE movie_id = #{id}
      """)
  void deleteMovieActors(Long id);

  @Delete("""
          DELETE FROM movie_director WHERE movie_id = #{id}
      """)
  void deleteMovieDirectors(Long id);

  @Delete("""
          DELETE FROM movie_genre WHERE movie_id = #{id}
      """)
  void deleteMovieGenres(Long id);

}
