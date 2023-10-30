package dev.project.webservice.movie.movie.model.front;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MovieFront(
    Long id,
    @NotBlank
    @Size(max = 128)
    String name,
    @NotBlank
    @Size(max = 2048)
    String description,
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date publicationDate,
    String duration,
    String distributor,
    List<GenreFront> genres,
    @NotEmpty
    List<MoviePeopleFront> actors,
    @NotEmpty
    List<MoviePeopleFront> directors
) {

}
