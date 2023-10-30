package dev.project.webservice.movie.movie.model.mb;

import java.util.Date;

public record MoviePeopleMb(
    Long id,
    String firstname,
    String lastname,
    Date birthday
) {

}
