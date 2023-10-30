package dev.project.webservice.movie.people.model.mb;

import java.util.Date;
import java.util.List;

public class PeopleMb {
  private Long id;
  private String firstname;
  private String lastname;
  private Date birthday;
  private String nationality;
  private List<RoleMb> roles;

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
}
