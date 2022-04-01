package kr.teamcocoa.buildffa.enums;

public enum OtherEnum {

    BAR_MAP("bar.map"),
    BAR_KIT("bar.kit"),
    BAR_TIME_LEFT("bar.time_left"),
    SCOREBOARD_KILLS("scoreboard.kills"),
    SCOREBOARD_BEST_KILL_STREAK("scoreboard.best_kill_streak"),
    SCOREBOARD_TEAMING_ALLOW("scoreboard.teaming_allow"),
    SCOREBOARD_TEAMING_PROHIBIT("scoreboard.teaming_prohibit"),
    LORE_VOTE_COUNT("lore.vote_count"),
    LORE_PRICE("lore.price");

    private final String name;

    public String getName() {
        return name;
    }

    private OtherEnum(String name) {
        this.name = name;
    }
}
