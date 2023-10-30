package dev.project.webservice.movie.people;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.project.webservice.movie.people.model.front.RoleFront;
import java.util.List;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PeopleController.class)
@ExtendWith(InstancioExtension.class)
class PeopleControllerIT {

  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private PeopleService peopleService;


  @Test
  void getRoles_controllerCalled_expectedJson() throws Exception {
    //GIVEN
    List<RoleFront> roles = Instancio.ofList(RoleFront.class).size(2).create();
    when(peopleService.getRoles()).thenReturn(roles);

    // WHEN
    this.mockMvc.perform(get("/people/role").accept(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        // THEN
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$.[0].id").value(roles.get(0).id()))
        .andExpect(jsonPath("$.[0].name").value(roles.get(0).name()))
        .andExpect(jsonPath("$.[1].id").value(roles.get(1).id()))
        .andExpect(jsonPath("$.[1].name").value(roles.get(1).name()))
        .andReturn();
  }

}