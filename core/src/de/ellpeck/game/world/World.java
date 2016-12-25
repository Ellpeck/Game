package de.ellpeck.game.world;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;
import de.ellpeck.game.TheGame;
import de.ellpeck.game.entity.Entity;
import de.ellpeck.game.entity.player.EntityPlayer;
import de.ellpeck.game.tile.Tile;
import de.ellpeck.game.tile.activity.TileActivity;
import de.ellpeck.game.util.AABB;
import de.ellpeck.game.util.CoordUtil;
import de.ellpeck.game.util.MathUtil;
import de.ellpeck.game.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class World implements Disposable{

    private static final int MAX_CHUNK_VIEW = 2;

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

    public TileActivity getTileActivity(int x, int y, int z, boolean forceLoad){
        Chunk chunk = this.getChunkFromWorldCoords(x, z, forceLoad);
        if(chunk != null){
            return chunk.getTileActivity(CoordUtil.relativeToIncludingChunk(x), y, CoordUtil.relativeToIncludingChunk(z));
        }
        else{
            return null;
        }
    }

    public boolean addEntity(Entity entity, boolean forceLoad){
        Chunk chunk = this.getChunkFromWorldCoords(MathUtil.floor(entity.x), MathUtil.floor(entity.z), forceLoad);
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
        Chunk chunk = this.loadedChunks.get(id);
        if(chunk != null){
            return chunk;
        }
        else{
            if(forceLoad){
                chunk = new Chunk(this, x, z);
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
                        chunk.loadedByPlayerTimer = 30;
                    }
                }
            }
        }

        for(long id : this.loadedChunks.keySet()){
            Chunk chunk = this.loadedChunks.get(id);

            if(chunk.loadedByPlayerTimer <= 0){
                this.loadedChunks.remove(id);
                chunk.onUnload();

                this.saveChunkToDisk(chunk);

                TheGame.LOGGER.debug("Unloading chunk at "+chunk.x+", "+chunk.z+"!");
            }
            else{
                chunk.update();
                chunk.loadedByPlayerTimer--;
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

    public List<AABB> getCollisionBoxes(AABB area, boolean forceLoad){
        List<AABB> list = new ArrayList<>();

        for(int x = MathUtil.floor(area.x1); x <= MathUtil.floor(area.x2); x++){
            for(int y = MathUtil.floor(area.y1); y <= MathUtil.floor(area.y2); y++){
                for(int z = MathUtil.floor(area.z1); z <= MathUtil.floor(area.z2); z++){
                    Tile tile = this.getTile(x, y, z, forceLoad);
                    if(tile != null){
                        AABB collision = tile.getCollisionBox(this, x, y, z, this.getMetadata(x, y, z, forceLoad));
                        if(!collision.isEmpty()){
                            list.add(collision);
                        }
                    }
                }
            }
        }

        return list;
    }

    @Override
    public void dispose(){
        for(Chunk chunk : this.loadedChunks.values()){
            chunk.dispose();
        }
    }
}
