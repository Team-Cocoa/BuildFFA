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
    USE_INVENTORY_SORTING("message_use_inventory_sorting"),
    VOTE_SUCCESS("message_vote_success"),
    VOTE_RESET("message_vote_reset"),
    VOTE_INVALID("message_vote_invalid"),
    VOTE_CANNOT_VOTE("message_vote_cannot"),
    VOTE_CURRENT_INFO("message_vote_current_info"),
    VOTE_NUMBER_OF_VOTE("message_vote_number_of_vote"),
    VOTE_ENDED("message_vote_ended"),
    VOTE_COMMAND("message_vote_command"),
    VOTE_MAP_SELECTED("message_vote_map_selected"),
    VOTE_CANNOT_VOTE_MAP("message_vote_cannot_vote_map"),
    VOTE_END_MINUTES("message_vote_end_minutes"),
    VOTE_END_MINUTE("message_vote_end_minute"),
    VOTE_END_SECONDS("message_vote_end_seconds"),
    VOTE_END_SECOND("message_vote_end_second");





    private final String name;

    public String getName() {
        return name;
    }

    private MessageEnum(String name) {
        this.name = name;
    }


}
