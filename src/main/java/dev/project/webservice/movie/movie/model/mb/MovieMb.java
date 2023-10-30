package dev.project.webservice.movie.movie.model.mb;

import java.util.Date;
import java.util.List;

public class MovieMb {

  private Long id;
  private String name;
  private String description;
  private Date publicationDate;
  private String duration;
  private String distributor;
  private List<GenreMb> genres;
  private List<MoviePeopleMb> actors;
  private List<MoviePeopleMb> directors;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Date getPublicationDate() {
    return publicationDate;
  }

  public void setPublicationDate(Date publicationDate) {
    this.publicationDate = publicationDate;
  }

  public String getDuration() {
    return duration;
  }

  public void setDuration(String duration) {
    this.duration = duration;
  }

  public String getDistributor() {
    return distributor;
  }

  public void setDistributor(String distributor) {
    this.distributor = distributor;
  }

  public List<MoviePeopleMb> getActors() {
    return actors;
  }

  public void setActors(List<MoviePeopleMb> actors) {
    this.actors = actors;
  }

  public List<MoviePeopleMb> getDirectors() {
    return directors;
  }

  public void setDirectors(
      List<MoviePeopleMb> directors) {
    this.directors = directors;
  }

  public List<GenreMb> getGenres() {
    return genres;
  }

  public void setGenres(List<GenreMb> genres) {
    this.genres = genres;
  }
}
