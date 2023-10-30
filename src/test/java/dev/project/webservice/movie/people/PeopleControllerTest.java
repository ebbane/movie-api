package dev.project.webservice.movie.people;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.project.webservice.movie.people.model.front.PeopleDetailFront;
import dev.project.webservice.movie.people.model.front.PeopleFront;
import dev.project.webservice.movie.people.model.front.RoleFront;
import dev.project.webservice.movie.utils.PageUtils;
import java.util.List;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.Seed;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@ExtendWith(InstancioExtension.class)
class PeopleControllerTest {

  private PeopleController peopleController;
  private PeopleService peopleService;

  @BeforeEach
  void beforeEach() {
    peopleService = Mockito.mock(PeopleService.class);
    peopleController = new PeopleController(peopleService);
  }

  @Test
  @Seed(3)
  void getRoles_nominal_callService() {
    //GIVEN
    List<RoleFront> roles = Instancio.ofList(RoleFront.class).size(2).create();
    when(peopleService.getRoles()).thenReturn(roles);

    //WHEN
    List<RoleFront> actual = peopleController.getRoles();

    //THEN
    assertThat(actual).containsExactlyElementsOf(roles);
    verify(peopleService).getRoles();
  }

  @Test
  @Seed(3)
  void getPeoples_filterByRole_callService() {
    //GIVEN
    List<PeopleFront> peoples = Instancio.ofList(PeopleFront.class).size(2).create();
    Pageable pageable = PageUtils.sortedPageable(0, 20);
    var expected = new PageImpl<>(peoples, pageable, peoples.size());
    when(peopleService.getPeoples(pageable, "actor")).thenReturn(expected);

    //WHEN
    Page<PeopleFront> actual = peopleController.getPeoples("actor", 0, 20);

    //THEN
    assertThat(actual).containsExactlyElementsOf(expected);
    verify(peopleService).getPeoples(pageable, "actor");
  }

  @Test
  @Seed(3)
  void getPeopleById_nominal_callService() {
    //GIVEN
    PeopleDetailFront peopleDetailFront = Instancio.create(PeopleDetailFront.class);
    when(peopleService.getPeopleById(peopleDetailFront.getId())).thenReturn(peopleDetailFront);

    //WHEN
    PeopleDetailFront actual = peopleController.getPeopleById(peopleDetailFront.getId());

    //THEN
    assertThat(actual).isEqualTo(peopleDetailFront);
    verify(peopleService).getPeopleById(peopleDetailFront.getId());
  }

  @Test
  @Seed(3)
  void deleteById_nominal_callService() {
    //GIVEN
    var peopleId = 1L;
    doNothing().when(peopleService).deletePeople(peopleId);

    //WHEN
    peopleController.deleteById(peopleId);

    //THEN
    verify(peopleService).deletePeople(peopleId);
  }

  @Test
  @Seed(3)
  void updatePeople_nominal_callService() {
    //GIVEN
    var peopleId = 1L;
    PeopleFront peopleFront = Instancio.create(PeopleFront.class);
    doNothing().when(peopleService).updatePeopleById(peopleId, peopleFront);

    //WHEN
    peopleController.updatePeople(peopleId, peopleFront);

    //THEN
    verify(peopleService).updatePeopleById(peopleId, peopleFront);
  }

  @Test
  @Seed(3)
  void createPeople_nominal_callService() {
    //GIVEN
    PeopleFront peopleFront = Instancio.create(PeopleFront.class);
    when(peopleService.createPeople(peopleFront)).thenReturn(1L);
    var expected = ResponseEntity.ok()
        .body(1L);

    //WHEN
    var actual = peopleController.createPeople(peopleFront);

    //THEN
    assertThat(actual).isEqualTo(expected);
    verify(peopleService).createPeople(peopleFront);
  }


}