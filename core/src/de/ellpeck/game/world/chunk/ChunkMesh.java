package de.ellpeck.game.world.chunk;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;
import de.ellpeck.game.tile.Tile;
import de.ellpeck.game.util.Direction;
import de.ellpeck.game.util.render.MeshBuilder;

public class ChunkMesh implements Disposable{

    private static final float CUBE_SIZE = 1F;

    private final MeshBuilder mesh = new MeshBuilder(true, true);
    private final Chunk chunk;

    public ChunkMesh(Chunk chunk){
        this.chunk = chunk;
    }

    public void makeNewMesh(){
        this.mesh.init();

        for(int x = 0; x < Chunk.SIZE; x++){
            for(int z = 0; z < Chunk.SIZE; z++){
                for(int y = 0; y < Chunk.HEIGHT; y++){
                    Tile tile = this.chunk.getTile(x, y, z);
                    if(tile != null && tile.doesRender(this.chunk.world, x, y, z)){
                        this.addTile(tile, this.chunk.x*Chunk.SIZE+x, y, this.chunk.z*Chunk.SIZE+z, CUBE_SIZE);
                    }
                }
            }
        }

        this.mesh.create();
    }

    private void addTile(Tile tile, int x, int y, int z, float size){
        if(tile.shouldShowFace(this.chunk.world, x, y, z, Direction.NORTH)){
            this.mesh.addRectangle(
                    x, y, z+size,
                    x+size, y, z+size,
                    x+size, y+size, z+size,
                    x, y+size, z+size,
                    0, 0, 1,
                    tile.getTextureCoordsForSide(this.chunk.world, x, y, z, Direction.NORTH));
        }

        if(tile.shouldShowFace(this.chunk.world, x, y, z, Direction.SOUTH)){
            this.mesh.addRectangle(
                    x+size, y, z,
                    x, y, z,
                    x, y+size, z,
                    x+size, y+size, z,
                    0, 0, 1,
                    tile.getTextureCoordsForSide(this.chunk.world, x, y, z, Direction.SOUTH));
        }

        if(tile.shouldShowFace(this.chunk.world, x, y, z, Direction.DOWN)){
            this.mesh.addRectangle(
                    x, y, z,
                    x+size, y, z,
                    x+size, y, z+size,
                    x, y, z+size,
                    0, -1, 0,
                    tile.getTextureCoordsForSide(this.chunk.world, x, y, z, Direction.DOWN));
        }

        if(tile.shouldShowFace(this.chunk.world, x, y, z, Direction.UP)){
            this.mesh.addRectangle(
                    x+size, y+size, z,
                    x, y+size, z,
                    x, y+size, z+size,
                    x+size, y+size, z+size,
                    0, 1, 0,
                    tile.getTextureCoordsForSide(this.chunk.world, x, y, z, Direction.UP));
        }

        if(tile.shouldShowFace(this.chunk.world, x, y, z, Direction.WEST)){
            this.mesh.addRectangle(
                    x, y, z,
                    x, y, z+size,
                    x, y+size, z+size,
                    x, y+size, z,
                    -1, 0, 0,
                    tile.getTextureCoordsForSide(this.chunk.world, x, y, z, Direction.WEST));
        }

        if(tile.shouldShowFace(this.chunk.world, x, y, z, Direction.EAST)){
            this.mesh.addRectangle(
                    x+size, y, z+size,
                    x+size, y, z,
                    x+size, y+size, z,
                    x+size, y+size, z+size,
                    1, 0, 0,
                    tile.getTextureCoordsForSide(this.chunk.world, x, y, z, Direction.EAST));
        }
    }

    public void render(ShaderProgram shader){
        this.mesh.renderMesh(shader);
    }

    @Override
    public void dispose(){
        this.mesh.dispose();
    }
}
