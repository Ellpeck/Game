package de.ellpeck.game.tile;

import com.badlogic.gdx.utils.Disposable;
import de.ellpeck.game.Registry;
import de.ellpeck.game.tile.activity.TileActivity;
import de.ellpeck.game.util.AABB;
import de.ellpeck.game.util.Direction;
import de.ellpeck.game.util.render.TextureCoord;
import de.ellpeck.game.world.World;

public class Tile implements Disposable{

    public final int id;
    private final AABB boundingBox = new AABB(0F, 0F, 0F, 1F, 1F, 1F);

    public Tile(int id){
        this.id = id;
    }

    public Tile register(){
        Registry.TILE_REGISTRY.register(this.id, this);
        return this;
    }

    public TileActivity createActivity(World world, int x, int y, int z, int meta){
        return null;
    }

    public void onRemoved(World world, int x, int y, int z, int meta){

    }

    public void onAdded(World world, int x, int y, int z, int meta){

    }

    public boolean doesRender(World world, int x, int y, int z){
        return true;
    }

    public boolean isFaceTransparent(World world, int x, int y, int z, Direction face){
        return false;
    }

    public boolean shouldShowFace(World world, int x, int y, int z, Direction face){
        Tile tileAdj = world.getTile(x+face.xOffset, y+face.yOffset, z+face.zOffset, false);
        return tileAdj == null || tileAdj.isFaceTransparent(world, x+face.xOffset, y+face.yOffset, z+face.zOffset, face.getOpposite());
    }

    public TextureCoord getTextureCoordsForSide(World world, int x, int y, int z, Direction face){
        return TextureCoord.NONE;
    }

    public AABB getCollisionBox(World world, int x, int y, int z, int meta){
        return this.boundingBox.copy().offset(x, y, z);
    }

    @Override
    public void dispose(){

    }
}
