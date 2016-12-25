package de.ellpeck.game.tile;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import de.ellpeck.game.Registry;
import de.ellpeck.game.tile.activity.TileActivity;
import de.ellpeck.game.util.AABB;
import de.ellpeck.game.util.Direction;
import de.ellpeck.game.world.World;

public class Tile implements Disposable{

    public final String name;
    private final AABB boundingBox = new AABB(0F, 0F, 0F, 1F, 1F, 1F);

    public Tile(String name){
        this.name = name;
    }

    public Tile register(){
        Registry.TILE_REGISTRY.register(this.name, this);
        return this;
    }

    public TileActivity createActivity(World world, int x, int y, int z, int meta){
        return null;
    }

    public void onRemoved(World world, int x, int y, int z, int meta){

    }

    public void onAdded(World world, int x, int y, int z, int meta){

    }

    public boolean doesRender(){
        return true;
    }

    public boolean shouldShowFace(World world, int x, int y, int z, Direction face){
        Tile tileAdj = world.getTile(x+face.xOffset, y+face.yOffset, z+face.zOffset, false);
        return tileAdj == null;
    }

    public Vector2 getTextureCoordsForSide(World world, int x, int y, int z, Direction face){
        return Vector2.Zero;
    }

    public AABB getCollisionBox(World world, int x, int y, int z, int meta){
        return this.boundingBox.copy().offset(x, y, z);
    }

    @Override
    public void dispose(){

    }
}
