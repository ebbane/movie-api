package dev.project.webservice.movie.people.model.front;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.project.webservice.movie.people.model.mb.RoleMb;
import java.util.Date;
import java.util.List;

public class PeopleDetailFront {

  private Long id;
  private String firstname;
  private String lastname;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private Date birthday;
  private String nationality;
  private List<RoleMb> roles;
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private List<PeopleMovieFront> actorMovie;
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private List<PeopleMovieFront> directorMovie;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public Date getBirthday() {
    return birthday;
  }

  public void setBirthday(Date birthday) {
    this.birthday = birthday;
  }

  public String getNationality() {
    return nationality;
  }

  public void setNationality(String nationality) {
    this.nationality = nationality;
  }

  public List<RoleMb> getRoles() {
    return roles;
  }

  public void setRoles(List<RoleMb> roles) {
    this.roles = roles;
  }

  public List<PeopleMovieFront> getActorMovie() {
    return actorMovie;
  }

  public void setActorMovie(List<PeopleMovieFront> actorMovie) {
    this.actorMovie = actorMovie;
  }

  public List<PeopleMovieFront> getDirectorMovie() {
    return directorMovie;
  }

  public void setDirectorMovie(
      List<PeopleMovieFront> directorMovie) {
    this.directorMovie = directorMovie;
  }
}
