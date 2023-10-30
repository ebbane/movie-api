package dev.project.webservice.movie.movie;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;

import dev.project.webservice.movie.movie.model.front.GenreFront;
import dev.project.webservice.movie.movie.model.front.MovieFront;
import dev.project.webservice.movie.utils.PageUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/movie")
public class MovieController {

  private final MovieService movieService;

  public MovieController(MovieService movieService) {
    this.movieService = movieService;
  }

  @Operation(summary = "List des genres de film")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Succès",
          content = {
              @Content(mediaType = "application/json",
                  schema = @Schema(implementation = MovieFront.class))}),
  })
  @GetMapping("genre")
  public List<GenreFront> getGenres() {
    return movieService.getGenres();
  }


  @Operation(summary = "Création d'un film")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201",
          description = "Succès",
          content = {
              @Content(mediaType = "application/json",
                  schema = @Schema(implementation = Long.class))}),
      @ApiResponse(
          responseCode = "400",
          description = "Non respect des champs",
          content = {
              @Content(mediaType = "application/json")})
  })
  @PostMapping
  public ResponseEntity<Long> createMovie(
      @RequestBody(required = true) @Valid MovieFront movieFront) {
    return ResponseEntity.ok()
        .body(movieService.createMovie(movieFront));
  }

  @Operation(summary = "Mise à jour d'un film")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Succès"),
      @ApiResponse(
          responseCode = "404",
          description = "Le film l'id spécifié n'existe pas"
      ),
      @ApiResponse(
          responseCode = "400",
          description = "L'id spécifié n'a pas un format valide"
      ),
      @ApiResponse(
          responseCode = "204",
          description = "Succès"
      )
  })
  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateMovie(
      @PathVariable Long id,
      @RequestBody @Valid MovieFront movieFront) {
    movieService.updateMovieById(id, movieFront);

  }

  @GetMapping()
  @Operation(summary = "Page de tous les films")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Succès",
          content = {
              @Content(mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = MovieFront.class)))})
  })
  public Page<MovieFront> getMovies(
      @Parameter(description = "Filtrer en fonction d’un ou plusieurs acteurs") @RequestParam(required = false) String actors,
      @Parameter(description = "Filtrer en fonction d’un ou plusieurs réalisateurs") @RequestParam(required = false) String directors,
      @Parameter(description = "Numéro de la page à afficher.") @RequestParam(required = false, defaultValue = "0") int page,
      @Parameter(description = "Nombre d'éléments par page.") @RequestParam(required = false, defaultValue = "20") int pageSize
  ) {
    Pageable pageable = PageUtils.sortedPageable(page, pageSize);
    return movieService.getMovies(pageable, actors, directors);
  }


  @Operation(summary = "Recherche d'un film par son id")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Succès",
          content = {
              @Content(mediaType = "application/json",
                  schema = @Schema(implementation = MovieFront.class))}),
      @ApiResponse(
          responseCode = "404",
          description = "Le film avec l'id spécifié n'existe pas",
          content = {}),

      @ApiResponse(
          responseCode = "400",
          description = "L'id spécifié n'a pas un format valide",
          content = {}
      )
  })
  @GetMapping("/{id}")
  public MovieFront getMovieById(
      @Parameter(required = true, in = PATH) @PathVariable Long id) {
    return movieService.getMovieById(id);
  }

  @Operation(summary = "Suppression d'un film")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Succès"),
      @ApiResponse(
          responseCode = "400",
          description = "L'id spécifié n'a pas un format valide",
          content = {
              @Content(mediaType = "application/json")})
  })
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteById(@Parameter(required = true, in = PATH) @PathVariable Long id) {
    movieService.deleteMovie(id);
  }
}
