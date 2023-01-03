package kr.teamcocoa.buildffa.translate;

public enum ItemNode implements Translatable {

    INVENTORY_SORTING("inventory_sorting"),
    LEAVE_ITEM("leave_item"),
    KIT("kit"),
    SAVE("save"),
    RESET("reset"),
    DEFAULT_KIT("kit_default"),
    ARCHER_KIT("kit_archer"),
    FISHER_KIT("kit_fisher"),
    CANCEL_VOTE("cancel_vote"),
    RESCUE_PLATFORM("rescue_platform"),
    GOLDEN_HEAD("golden_head"),
    SHOP("shop");

    private final String name;

    @Override
    public String getName() {
        return name;
    }

    private ItemNode(String name) {
        this.name = "item." + name;
    }
}
