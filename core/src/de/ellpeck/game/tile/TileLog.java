package de.ellpeck.game.tile;

import com.badlogic.gdx.math.Vector2;
import de.ellpeck.game.util.Direction;
import de.ellpeck.game.world.World;

public class TileLog extends Tile{

    private final Vector2 coordsTop = new Vector2(3, 0);
    private final Vector2 coordsSide = new Vector2(4, 0);

    public TileLog(){
        super("log");
    }

    @Override
    public Vector2 getTextureCoordsForSide(World world, int x, int y, int z, Direction face){
        int meta = world.getMetadata(x, y, z, false);

        if(face == Direction.UP || face == Direction.DOWN){
            return meta == 0 ? this.coordsTop : this.coordsSide;
        }
        else if(face == Direction.NORTH || face == Direction.SOUTH){
            return meta == 1 ? this.coordsTop : this.coordsSide;
        }
        else{
            return meta == 2 ? this.coordsTop : this.coordsSide;
        }
    }
}
