package dev.project.webservice.movie.movie.model.front;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

public record MoviePeopleFront(
    Long id,
    String firstname,
    String lastname,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date birthday

) {

}
