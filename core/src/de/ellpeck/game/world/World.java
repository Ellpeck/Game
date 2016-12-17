package de.ellpeck.game.world;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;
import de.ellpeck.game.entity.Entity;
import de.ellpeck.game.tile.Tile;
import de.ellpeck.game.tile.activity.TileActivity;
import de.ellpeck.game.util.CoordUtil;
import de.ellpeck.game.world.chunk.Chunk;

import java.util.HashMap;
import java.util.Map;

public class World implements Disposable{

    private final Map<Long, Chunk> loadedChunks = new HashMap<>();

    public boolean addTileActivity(TileActivity activity, boolean forceLoad){
        Chunk chunk = this.getChunkFromWorldCoords(activity.x, activity.z, forceLoad);
        if(chunk != null){
            chunk.addTileActivity(activity);
            return true;
        }
        else{
            return false;
        }
    }

    public boolean addEntity(Entity entity, boolean forceLoad){
        Chunk chunk = this.getChunkFromWorldCoords((int)entity.x, (int)entity.z, forceLoad);
        if(chunk != null){
            chunk.addEntity(entity);
            return true;
        }
        else{
            return false;
        }
    }

    public Tile getTile(int x, int y, int z, boolean forceLoad){
        Chunk chunk = this.getChunkFromWorldCoords(x, z, forceLoad);
        if(chunk != null){
            return chunk.getTile(CoordUtil.toChunkIntern(x), y, CoordUtil.toChunkIntern(z));
        }
        else{
            return null;
        }
    }

    public boolean setTile(int x, int y, int z, Tile tile, int meta, boolean forceLoad){
        Chunk chunk = this.getChunkFromWorldCoords(x, z, forceLoad);
        return chunk != null && chunk.setTile(CoordUtil.toChunkIntern(x), y, CoordUtil.toChunkIntern(z), tile, meta);
    }

    public int getMetadata(int x, int y, int z, boolean forceLoad){
        Chunk chunk = this.getChunkFromWorldCoords(x, z, forceLoad);
        if(chunk != null){
            return chunk.getMetadata(CoordUtil.toChunkIntern(x), y, CoordUtil.toChunkIntern(z));
        }
        else{
            return 0;
        }
    }

    public boolean setMetadata(int x, int y, int z, int meta, boolean forceLoad){
        Chunk chunk = this.getChunkFromWorldCoords(x, z, forceLoad);
        return chunk != null && chunk.setMetadata(CoordUtil.toChunkIntern(x), y, CoordUtil.toChunkIntern(z), meta);
    }

    public Chunk getChunkFromWorldCoords(int x, int z, boolean forceLoad){
        return this.getChunkFromChunkCoords(CoordUtil.toChunk(x), CoordUtil.toChunk(z), forceLoad);
    }

    public Chunk getChunkFromChunkCoords(int x, int z, boolean forceLoad){
        long id = CoordUtil.getChunkId(x, z);
        if(this.loadedChunks.containsKey(id)){
            return this.loadedChunks.get(id);
        }
        else{
            if(forceLoad){
                Chunk chunk = new Chunk(this, x, z);
                this.loadedChunks.put(id, chunk);
                this.populateChunk(chunk);

                return chunk;
            }
            else{
                return null;
            }
        }
    }

    public void update(){
        for(long id : this.loadedChunks.keySet()){
            Chunk chunk = this.loadedChunks.get(id);

            if(chunk.shouldUnload()){
                this.loadedChunks.remove(id);
                chunk.onUnload();

                this.saveChunkToDisk(chunk);
            }
            else{
                chunk.update();
            }
        }
    }

    public void render(ShaderProgram shader){
        for(Chunk chunk : this.loadedChunks.values()){
            chunk.render(shader);
        }
    }

    private void populateChunk(Chunk chunk){
        chunk.populate();
    }

    private void saveChunkToDisk(Chunk chunk){
        //TODO Save chunk
    }

    @Override
    public void dispose(){
        for(Chunk chunk : this.loadedChunks.values()){
            chunk.dispose();
        }
    }
}
