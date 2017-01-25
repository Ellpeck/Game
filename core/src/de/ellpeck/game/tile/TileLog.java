package de.ellpeck.game.tile;

import de.ellpeck.game.util.Direction;
import de.ellpeck.game.util.render.TextureCoord;
import de.ellpeck.game.world.World;

public class TileLog extends Tile{

    private final TextureCoord coordsTop = new TextureCoord(3, 0);
    private final TextureCoord coordsSide = new TextureCoord(4, 0);

    public TileLog(int id){
        super(id);
    }

    @Override
    public TextureCoord getTextureCoordsForSide(World world, int x, int y, int z, Direction face){
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
