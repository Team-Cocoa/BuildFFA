package kr.teamcocoa.buildffa.enums;

public enum OtherEnum {

    BAR_MAP("bar_map"),
    BAR_KIT("bar_kit"),
    BAR_TIME_LEFT("bar_time_left"),
    SCOREBOARD_KILLS("scoreboard_kills"),
    SCOREBOARD_BEST_KILL_STREAK("scoreboard_best_kill_streak"),
    SCOREBOARD_TEAMING_ALLOW("scoreboard_teaming_allow"),
    SCOREBOARD_TEAMING_PROHIBIT("scoreboard_teaming_prohibit");

    private final String name;

    public String getName() {
        return name;
    }

    private OtherEnum(String name) {
        this.name = name;
    }
}
