package dev.project.webservice.movie.people.model.mb;

import dev.project.webservice.movie.movie.model.mb.MovieMb;
import java.util.Date;
import java.util.List;

public class PeopleDetailMb {
  private Long id;
  private String firstname;
  private String lastname;
  private Date birthday;
  private String nationality;
  private List<RoleMb> roles;
  private List<MovieMb> actorMovie;
  private List<MovieMb> directorMovie;

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

  public List<MovieMb> getActorMovie() {
    return actorMovie;
  }

  public void setActorMovie(List<MovieMb> actorMovie) {
    this.actorMovie = actorMovie;
  }

  public List<MovieMb> getDirectorMovie() {
    return directorMovie;
  }

  public void setDirectorMovie(
      List<MovieMb> directorMovie) {
    this.directorMovie = directorMovie;
  }
}
