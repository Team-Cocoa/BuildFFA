package kr.teamcocoa.buildffa.enums;

public enum MessageEnum {

    JOIN_TITLE("message_join_sub_title"),
    MAP_CHANGE_MINUTES("message_map_change_minutes"),
    MAP_CHANGE_MINUTE("message_map_change_minute"),
    MAP_CHANGE_SECONDS("message_map_change_seconds"),
    MAP_CHANGE_SECOND("message_map_change_second"),
    SETTING_SAVED("message_setting_saved"),
    SETTING_RESET("message_setting_reset"),
    SETTING_ERROR("message_setting_error"),
    PLAYER_KILL("message_player_kill"),
    KILL_STREAK("message_kill_streak"),
    KILL_STREAK_BROKEN("message_kill_streak_broken"),
    USE_INVENTORY_SORTING("message_use_inventory_sorting");





    private final String name;

    public String getName() {
        return name;
    }

    private MessageEnum(String name) {
        this.name = name;
    }


}
