package dev.project.webservice.movie.people;

import dev.project.webservice.movie.people.model.mb.PeopleDetailMb;
import dev.project.webservice.movie.people.model.mb.PeopleMb;
import dev.project.webservice.movie.people.model.mb.PeopleMovieMb;
import dev.project.webservice.movie.people.model.mb.RoleMb;
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
public interface PeopleRepository {

  @Select(
      """
          SELECT id, name
          FROM role"""
  )
  List<RoleMb> getRoles();

  @Insert(
      """
          INSERT INTO people (firstname, lastname, birthday, nationality)
          VALUES (#{firstname}, #{lastname}, #{birthday}, #{nationality})
          """
  )
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertPeople(PeopleMb peopleMb);

  @Insert("""
      <script>
          INSERT INTO people_role(people_id, role_id)  values
          <foreach item='role' collection='roleMbs' separator=','>
              (#{peopleId}, CAST(#{role.id} AS bigint))
          </foreach>
      </script>""")
  void insertPeopleRoles(List<RoleMb> roleMbs, Long peopleId);

  @Update(
      """
          UPDATE people SET
          firstname = #{firstname},
          lastname = #{lastname},
          birthday = #{birthday},
          nationality = #{nationality}
          WHERE id = #{id}
          """
  )
  void updatePeople(PeopleMb peopleMb);

  @Select(
      """
          SELECT p.id, p.firstname, p.lastname, p.birthday, p.nationality
          FROM people p
          JOIN people_role ON p.id = people_role.people_id
          JOIN role ON people_role.role_id = role.id
          WHERE lower(role.name) = LOWER(#{role})
          LIMIT #{pageSize} OFFSET #{offset}
          """
  )
  @Results(value = {
      @Result(property = "id", column = "id"),
      @Result(property = "roles", column = "id", many = @Many(select = "selectRolesByPeople")),
  })
  List<PeopleMb> getPeoplesByRole(String role, int offset, int pageSize);

  @Select(
      """
          SELECT id, name
          FROM people_role pr
                   JOIN role r ON pr.role_id = r.id
          WHERE pr.people_id = #{peopleId}
          """
  )
  List<RoleMb> selectRolesByPeople(Long peopleId);


  @Select(
      """
          SELECT p.id, p.firstname, p.lastname, p.birthday, p.nationality
          FROM people p
          WHERE id = #{id}
          """
  )
  @Results(value = {
      @Result(property = "id", column = "id"),
      @Result(property = "roles", column = "id", many = @Many(select = "selectRolesByPeople")),
      @Result(property = "directorMovie", column = "id", many = @Many(select = "selectDirectorMovies")),
      @Result(property = "actorMovie", column = "id", many = @Many(select = "selectActorMovies")),
  })
  Optional<PeopleDetailMb> getPeopleById(Long id);


  @Select(
      """
          SELECT id, name, description, publication_date
          FROM movie_director md
          JOIN movie ON md.movie_id = id
          WHERE md.people_id = #{directorId}
          """
  )
  List<PeopleMovieMb> selectDirectorMovies(Long directorId);

  @Select(
      """
          SELECT id, name, description, publication_date
          FROM movie_actor ma
          JOIN movie ON ma.movie_id = id
          WHERE ma.people_id = #{actorId}
          """
  )
  List<PeopleMovieMb> selectActorMovies(Long actorId);


  @Delete(
      """
          DELETE FROM people WHERE id = #{id}
          """
  )
  void deletePeople(Long id);

  @Delete("""
          DELETE FROM people_role WHERE people_id = #{id}
      """)
  void deletePeopleRoles(Long id);

}
