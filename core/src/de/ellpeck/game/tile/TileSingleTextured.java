package de.ellpeck.game.tile;

import com.badlogic.gdx.math.Vector2;
import de.ellpeck.game.util.Direction;
import de.ellpeck.game.world.World;

public class TileSingleTextured extends Tile{

    private Vector2 texCoords;

    public TileSingleTextured(String name){
        super(name);
    }

    public Tile setTextureCoords(int x, int y){
        this.texCoords = new Vector2(x, y);
        return this;
    }

    @Override
    public Vector2 getTextureCoordsForSide(World world, int x, int y, int z, Direction face){
        return this.texCoords;
    }
}
