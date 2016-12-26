package de.ellpeck.game.tile;

import de.ellpeck.game.util.Direction;
import de.ellpeck.game.world.World;

public class TileLeaves extends TileSingleTextured{

    public TileLeaves(){
        super("leaves");
        this.setTextureCoords(5, 0);
    }

    @Override
    public boolean isFaceTransparent(World world, int x, int y, int z, Direction face){
        return true;
    }
}
