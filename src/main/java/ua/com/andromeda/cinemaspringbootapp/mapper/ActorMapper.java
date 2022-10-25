package ua.com.andromeda.cinemaspringbootapp.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.andromeda.cinemaspringbootapp.model.Actor;
import ua.com.andromeda.cinemaspringbootapp.repository.ActorRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ActorMapper {
    private final ActorRepository actorRepository;

    @Autowired
    public ActorMapper(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    public Set<Actor> mapFullNamesToActors(String actorsFullNamesDelimitedByComa) {
        List<String> fullNames = Arrays.stream(actorsFullNamesDelimitedByComa.split(",")).map(String::trim).toList();
        Set<Actor> foundedActors = fullNames.stream()
                .map(actorRepository::findByFullName)
                .collect(Collectors.toSet());

        Set<Actor> notFoundedActors = fullNames.stream()
                .filter(fullName -> actorRepository.findByFullName(fullName) == null)
                .map(Actor::new)
                .collect(Collectors.toSet());
        foundedActors.addAll(notFoundedActors);
        return foundedActors;
    }
}
