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
        switch (name.toLowerCase()) {
            case "architecture": return ARCHITECTURE;
            case "flatland": return FLATLAND;
            case "spring": return SPRING;
            case "cwbw": return CWBW;
            default: return null;
        }
    }
}
