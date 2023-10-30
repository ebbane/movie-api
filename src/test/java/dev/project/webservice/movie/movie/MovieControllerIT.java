package dev.project.webservice.movie.movie;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.project.webservice.movie.movie.model.front.GenreFront;
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

@WebMvcTest(MovieController.class)
@ExtendWith(InstancioExtension.class)
class MovieControllerIT {

  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private MovieService movieService;


  @Test
  void getGenres_controllerCalled_expectedJson() throws Exception {
    // GIVEN
    List<GenreFront> genres = Instancio.ofList(GenreFront.class).size(2).create();
    when(movieService.getGenres()).thenReturn(genres);

    // WHEN
    this.mockMvc.perform(get("/movie/genre").accept(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        // THEN
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$.[0].id").value(genres.get(0).id()))
        .andExpect(jsonPath("$.[0].name").value(genres.get(0).name()))
        .andExpect(jsonPath("$.[1].id").value(genres.get(1).id()))
        .andExpect(jsonPath("$.[1].name").value(genres.get(1).name()))
        .andReturn();
  }


}