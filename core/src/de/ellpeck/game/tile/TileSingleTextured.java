package de.ellpeck.game.tile;

import de.ellpeck.game.util.Direction;
import de.ellpeck.game.util.render.TextureCoord;
import de.ellpeck.game.world.World;

public class TileSingleTextured extends Tile{

    private TextureCoord texCoords;

    public TileSingleTextured(int id){
        super(id);
    }

    public Tile setTextureCoords(int x, int y){
        this.texCoords = new TextureCoord(x, y);
        return this;
    }

    @Override
    public TextureCoord getTextureCoordsForSide(World world, int x, int y, int z, Direction face){
        return this.texCoords;
    }
}
