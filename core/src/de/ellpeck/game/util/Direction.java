package de.ellpeck.game.util;

public enum Direction{

    UP(0, 1, 0),
    DOWN(0, -1, 0),
    NORTH(0, 0, 1),
    EAST(1, 0, 0),
    SOUTH(0, 0, -1),
    WEST(-1, 0, 0);

    public final int xOffset;
    public final int yOffset;
    public final int zOffset;

    Direction(int xOffset, int yOffset, int zOffset){
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.zOffset = zOffset;
    }
}
