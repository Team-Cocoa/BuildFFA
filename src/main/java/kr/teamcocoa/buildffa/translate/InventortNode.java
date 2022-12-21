package kr.teamcocoa.buildffa.translate;

public enum InventortNode {

    KIT_SELECT("kit_selection"),
    INVENTORY_SORTING("inventory_sorting"),
    VOTE("vote"),
    EXTRA_ITEM("shop");

    private final String name;

    public String getName() {
        return name;
    }

    private InventortNode(String name) {
        this.name = "inventory." + name;
    }
}
