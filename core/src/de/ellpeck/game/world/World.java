package de.ellpeck.game.world;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;
import de.ellpeck.game.TheGame;
import de.ellpeck.game.entity.Entity;
import de.ellpeck.game.entity.EntityPlayer;
import de.ellpeck.game.tile.Tile;
import de.ellpeck.game.tile.activity.TileActivity;
import de.ellpeck.game.util.CoordUtil;
import de.ellpeck.game.util.MathUtil;
import de.ellpeck.game.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class World implements Disposable{

    private static final int MAX_CHUNK_VIEW = 2;
    private static final int MAX_CHUNK_VIEW_SQ = MAX_CHUNK_VIEW*MAX_CHUNK_VIEW;

    private final Map<Long, Chunk> loadedChunks = new ConcurrentHashMap<>();
    public final List<EntityPlayer> players = new ArrayList<>();

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

            if(entity instanceof EntityPlayer){
                this.players.add((EntityPlayer)entity);
            }

            return true;
        }
        else{
            return false;
        }
    }

    public Tile getTile(int x, int y, int z, boolean forceLoad){
        Chunk chunk = this.getChunkFromWorldCoords(x, z, forceLoad);
        if(chunk != null){
            return chunk.getTile(CoordUtil.relativeToIncludingChunk(x), y, CoordUtil.relativeToIncludingChunk(z));
        }
        else{
            return null;
        }
    }

    public boolean setTile(int x, int y, int z, Tile tile, int meta, boolean forceLoad){
        Chunk chunk = this.getChunkFromWorldCoords(x, z, forceLoad);
        return chunk != null && chunk.setTile(CoordUtil.relativeToIncludingChunk(x), y, CoordUtil.relativeToIncludingChunk(z), tile, meta);
    }

    public int getMetadata(int x, int y, int z, boolean forceLoad){
        Chunk chunk = this.getChunkFromWorldCoords(x, z, forceLoad);
        if(chunk != null){
            return chunk.getMetadata(CoordUtil.relativeToIncludingChunk(x), y, CoordUtil.relativeToIncludingChunk(z));
        }
        else{
            return 0;
        }
    }

    public boolean setMetadata(int x, int y, int z, int meta, boolean forceLoad){
        Chunk chunk = this.getChunkFromWorldCoords(x, z, forceLoad);
        return chunk != null && chunk.setMetadata(CoordUtil.relativeToIncludingChunk(x), y, CoordUtil.relativeToIncludingChunk(z), meta);
    }

    public Chunk getChunkFromWorldCoords(int x, int z, boolean forceLoad){
        return this.getChunkFromChunkCoords(CoordUtil.coordsOfIncludingChunk(x), CoordUtil.coordsOfIncludingChunk(z), forceLoad);
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
        for(EntityPlayer player : this.players){
            for(int x = -MAX_CHUNK_VIEW; x <= MAX_CHUNK_VIEW; x++){
                for(int z = -MAX_CHUNK_VIEW; z <= MAX_CHUNK_VIEW; z++){
                    Chunk chunk = this.getChunkFromChunkCoords(player.chunkX+x, player.chunkZ+z, true);
                    if(chunk != null){
                        chunk.loadedByPlayer = true;
                    }
                }
            }
        }

        for(long id : this.loadedChunks.keySet()){
            Chunk chunk = this.loadedChunks.get(id);

            if(!chunk.loadedByPlayer){
                this.loadedChunks.remove(id);
                chunk.onUnload();

                this.saveChunkToDisk(chunk);

                TheGame.LOGGER.debug("Unloading chunk at "+chunk.x+", "+chunk.z+"!");
            }
            else{
                chunk.update();
                chunk.loadedByPlayer = false;
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
