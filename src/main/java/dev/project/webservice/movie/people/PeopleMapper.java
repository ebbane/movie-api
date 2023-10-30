package dev.project.webservice.movie.people;

import dev.project.webservice.movie.people.model.front.PeopleDetailFront;
import dev.project.webservice.movie.people.model.front.PeopleFront;
import dev.project.webservice.movie.people.model.front.RoleFront;
import dev.project.webservice.movie.people.model.mb.PeopleDetailMb;
import dev.project.webservice.movie.people.model.mb.PeopleMb;
import dev.project.webservice.movie.people.model.mb.RoleMb;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PeopleMapper {

  List<RoleFront> roleMbToFront(List<RoleMb> rolesMb);
  PeopleDetailFront peopleDetaildMbToFront (PeopleDetailMb peopleDetailMb);
  PeopleMb peopleFrontToMb (PeopleFront peopleFront);
  List<PeopleFront> peopleMbsToFront (List<PeopleMb> peopleMbs);

}
