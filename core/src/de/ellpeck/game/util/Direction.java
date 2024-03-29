package de.ellpeck.game.util;

public enum Direction{

    UP(0, 1, 0),
    DOWN(0, -1, 0),
    NORTH(0, 0, 1),
    EAST(1, 0, 0),
    SOUTH(0, 0, -1),
    WEST(-1, 0, 0);

    public static final Direction[] HORIZONTAL = new Direction[]{NORTH, EAST, SOUTH, WEST};
    public static final Direction[] VERTICAL = new Direction[]{UP, DOWN};
    public static final Direction[] ALL = new Direction[]{UP, DOWN, NORTH, EAST, SOUTH, WEST};

    public final int xOffset;
    public final int yOffset;
    public final int zOffset;

    Direction(int xOffset, int yOffset, int zOffset){
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.zOffset = zOffset;
    }

    public Direction getOpposite(){
        switch(this){
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case NORTH:
                return SOUTH;
            case EAST:
                return WEST;
            case SOUTH:
                return NORTH;
            default:
                return EAST;
        }
    }
}
