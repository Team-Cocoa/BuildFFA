package kr.teamcocoa.buildffa.enums;

public enum MessageEnum {

    JOIN_TITLE("join_sub_title"),
    MAP_CHANGE_MINUTES("map.change_minutes"),
    MAP_CHANGE_MINUTE("map.change_minute"),
    MAP_CHANGE_SECONDS("map.change_seconds"),
    MAP_CHANGE_SECOND("map.change_second"),
    SETTING_SAVED("setting.saved"),
    SETTING_RESET("setting.reset"),
    SETTING_ERROR("setting.error"),
    PLAYER_KILL("kill.player"),
    KILL_STREAK("kill.streak"),
    KILL_STREAK_BROKEN("kill.streak_broken"),
    VOTE_SUCCESS("vote.success"),
    VOTE_RESET("vote.reset"),
    VOTE_INVALID("vote.invalid"),
    VOTE_CANNOT_VOTE("vote.cannot"),
    VOTE_CURRENT_INFO("vote.current_info"),
    VOTE_NUMBER_OF_VOTE("vote.number_of_vote"),
    VOTE_ENDED("vote.ended"),
    VOTE_COMMAND("vote.command"),
    VOTE_MAP_SELECTED("vote.map_selected"),
    VOTE_CANNOT_VOTE_MAP("vote.cannot_vote_map"),
    VOTE_END_MINUTES("vote.end_minutes"),
    VOTE_END_MINUTE("vote.end_minute"),
    VOTE_END_SECONDS("vote.end_seconds"),
    VOTE_END_SECOND("vote.end_second"),
    SHOP_CANNOT_BUY("shop.cannot_buy"),
    SHOP_BOUGHT("shop.bought"),
    SHOP_ALREADY_BOUGHT("shop.already_bought"),
    CANNOT_USE_PLATFORM("other.cannot_use_platform"),
    USED_PLATFORM("other.used_platform"),
    GIVE_EXTRA_ITEM("other.receive_extra");
//    BOW_COUNT_SECONDS("message_bow_count_seconds"),
//    BOW_COUNT_SECOND("message_bow_count_second"),
//    BOW_CANNOT_USE("message_bow_cannot_use"),
//    BOW_CAN_USE("message_bow_can_use");





    private final String name;

    public String getName() {
        return name;
    }

    private MessageEnum(String name) {
        this.name = "message." + name;
    }


}
