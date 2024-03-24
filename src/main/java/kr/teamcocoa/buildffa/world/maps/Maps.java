package kr.teamcocoa.buildffa.world.maps;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum Maps {

    ARCHITECTURE("Architecture"),
    FLATLAND("Flatland"),
    SPRING("Spring"),
    CWBW("CWBW");

    private String name;

    public static Maps getByName(String name) {
        return switch (name.toLowerCase()) {
            case "architecture" -> ARCHITECTURE;
            case "flatland" -> FLATLAND;
            case "spring" -> SPRING;
            case "cwbw" -> CWBW;
            default -> null;
        };
    }
}
