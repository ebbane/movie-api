package dev.project.webservice.movie.people.model.front;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

public record PeopleMovieFront(
    Long id,
    String name,
    String description,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date publicationDate
    ) {

}
