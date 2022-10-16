package ua.com.andromeda.cinemaspringbootapp.mapper;

import ua.com.andromeda.cinemaspringbootapp.model.Actor;

import java.util.Arrays;
import java.util.List;

public class ActorsMapper {

    private ActorsMapper() {

    }

    public static List<Actor> mapStringToList(String string) {
        String[] actorsAsStrings = string.split(",");
        return Arrays.stream(actorsAsStrings).peek(String::trim).map(Actor::new).toList();
    }
}
