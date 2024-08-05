package minesweeper.enums;

public enum MoveType {
    FREE("free"),
    MINE("mine");

    private final String type;

    MoveType(String type) {
        this.type = type;
    }

    String type() {
        return type;
    }
}
