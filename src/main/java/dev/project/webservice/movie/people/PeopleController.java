package dev.project.webservice.movie.people;


import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;

import dev.project.webservice.movie.people.model.front.PeopleDetailFront;
import dev.project.webservice.movie.people.model.front.PeopleFront;
import dev.project.webservice.movie.people.model.front.RoleFront;
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
@RequestMapping(value = "/people")
public class PeopleController {

  private final PeopleService peopleService;

  public PeopleController(PeopleService peopleService) {
    this.peopleService = peopleService;
  }

  @GetMapping("role")
  public List<RoleFront> getRoles() {
    return peopleService.getRoles();
  }

  @PostMapping
  public ResponseEntity<Long> createPeople(@RequestBody @Valid PeopleFront peopleFront) {
    return ResponseEntity.ok()
        .body(peopleService.createPeople(peopleFront));
  }


  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updatePeople(
      @PathVariable Long id,
      @RequestBody @Valid PeopleFront peopleFront) {
    peopleService.updatePeopleById(id, peopleFront);

  }

  @GetMapping()
  @Operation(summary = "Page de toute les personnes en fonction de leurs roles")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Succès",
          content = {
              @Content(mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = PeopleFront.class)))})
  })
  public Page<PeopleFront> getPeoples(
      @Parameter(description = "Le nom du role") @RequestParam(required = true) String role,
      @Parameter(description = "Numéro de la page à afficher.") @RequestParam(required = false, defaultValue = "0") int page,
      @Parameter(description = "Nombre d'éléments par page.") @RequestParam(required = false, defaultValue = "20") int pageSize
  ) {
    Pageable pageable = PageUtils.sortedPageable(page, pageSize);
    return peopleService.getPeoples(pageable, role);
  }


  @Operation(summary = "Recherche d'une personne par son id")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Succès",
          content = {
              @Content(mediaType = "application/json",
                  schema = @Schema(implementation = PeopleDetailFront.class))}),
      @ApiResponse(
          responseCode = "404",
          description = "La personne avec l'id spécifié n'existe pas"),
      @ApiResponse(
          responseCode = "400",
          description = "L'id spécifié n'a pas un format valide")
  })
  @GetMapping("/{id}")
  public PeopleDetailFront getPeopleById(
      @Parameter(required = true, in = PATH) @PathVariable Long id) {
    return peopleService.getPeopleById(id);
  }

  @Operation(summary = "Suppression d'une personne")
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
    peopleService.deletePeople(id);
  }


}
