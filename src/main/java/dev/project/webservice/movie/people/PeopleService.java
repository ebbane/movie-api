package dev.project.webservice.movie.people;

import dev.project.webservice.movie.exception.RecordNotFoundException;
import dev.project.webservice.movie.people.model.front.PeopleDetailFront;
import dev.project.webservice.movie.people.model.front.PeopleFront;
import dev.project.webservice.movie.people.model.front.RoleFront;
import dev.project.webservice.movie.people.model.mb.PeopleMb;
import dev.project.webservice.movie.people.model.mb.RoleMb;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PeopleService {

  private final PeopleRepository peopleRepository;
  private final PeopleMapper peopleMapper;

  public PeopleService(PeopleRepository peopleRepository, PeopleMapper peopleMapper) {
    this.peopleRepository = peopleRepository;
    this.peopleMapper = peopleMapper;
  }

  public List<RoleFront> getRoles() {
    return peopleMapper.roleMbToFront(peopleRepository.getRoles());
  }

  public Long createPeople(PeopleFront peopleFront) {
    PeopleMb peopleMb = peopleMapper.peopleFrontToMb(peopleFront);
    peopleRepository.insertPeople(peopleMb);
    peopleRepository.insertPeopleRoles(peopleMb.getRoles(), peopleMb.getId());
    return peopleMb.getId();
  }

  public void updatePeopleById(Long id, PeopleFront peopleFront) {
    var actual = peopleRepository.getPeopleById(id);
    if (actual.isEmpty()) {
      throw new RecordNotFoundException(String.format("Personne (id=%d) introuvable", id));
    }
    PeopleMb peopleToUpdate = peopleMapper.peopleFrontToMb(peopleFront);
    peopleRepository.updatePeople(peopleToUpdate);
    managePeopleRoleUpdate(peopleToUpdate.getRoles(), peopleToUpdate.getId());
  }

  public Page<PeopleFront> getPeoples(Pageable pageable, String role) {
    var peopleMbs = peopleRepository.getPeoplesByRole(role, (int) pageable.getOffset(),
        pageable.getPageSize());
    List<PeopleFront> peopleFronts = peopleMapper.peopleMbsToFront(peopleMbs);
    return new PageImpl<>(peopleFronts, pageable, peopleMbs.size());
  }

  public PeopleDetailFront getPeopleById(Long id) {
    return peopleRepository.getPeopleById(id)
        .map(peopleMapper::peopleDetaildMbToFront)
        .orElseThrow(
            () -> new RecordNotFoundException(String.format("Personne (id=%d) introuvable", id)));
  }

  public void deletePeople(Long id) {
    peopleRepository.deletePeople(id);
  }

  private void managePeopleRoleUpdate(List<RoleMb> roleMbs, Long peopleId) {
    peopleRepository.deletePeopleRoles(peopleId);
    peopleRepository.insertPeopleRoles(roleMbs, peopleId);
  }
}
