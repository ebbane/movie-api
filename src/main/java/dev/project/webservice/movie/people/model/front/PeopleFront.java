package dev.project.webservice.movie.people.model.front;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.project.webservice.movie.people.model.mb.RoleMb;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record PeopleFront(
    Long id,
    @NotBlank
    String firstname,
    @NotBlank
    String lastname,
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date birthday,
    String nationality,
    @NotEmpty
    List<RoleMb> roles
) {

}
