package de.ellpeck.game.world.chunk;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import de.ellpeck.game.Registry;
import de.ellpeck.game.TheGame;
import de.ellpeck.game.entity.Entity;
import de.ellpeck.game.entity.EntityPlayer;
import de.ellpeck.game.tile.Tile;
import de.ellpeck.game.tile.activity.TileActivity;
import de.ellpeck.game.util.CoordUtil;
import de.ellpeck.game.util.Direction;
import de.ellpeck.game.world.TileLayer;
import de.ellpeck.game.world.World;

import java.util.ArrayList;
import java.util.List;

public class Chunk implements Disposable{

    public static final int SIZE = 16;
    public static final int HEIGHT = 64;

    private final TileLayer[] tileLayers = new TileLayer[HEIGHT];
    private final List<Entity> entities = new ArrayList<>();
    private final List<TileActivity> tileActivities = new ArrayList<>();

    public final World world;
    public final int x;
    public final int z;

    public int loadedByPlayerTimer;
    public boolean shouldReformMesh;
    public final ChunkMesh mesh = new ChunkMesh(this);

    public Chunk(World world, int x, int z){
        this.world = world;
        this.x = x;
        this.z = z;
    }

    public void addTileActivity(TileActivity activity){
        TileActivity there = this.getTileActivity(activity.x, activity.y, activity.z);
        if(there != null){
            this.tileActivities.remove(there);
            there.onRemove();
            there.dispose();
        }

        this.tileActivities.add(activity);
        activity.onAdded();
    }

    public TileActivity getTileActivity(int x, int y, int z){
        for(TileActivity activity : this.tileActivities){
            if(activity.x == x && activity.y == y && activity.z == z){
                return activity;
            }
        }
        return null;
    }

    public void addEntity(Entity entity){
        this.entities.add(entity);

        entity.chunkX = this.x;
        entity.chunkZ = this.z;
    }

    public Tile getTile(int x, int y, int z){
        TileLayer layer = this.getTileLayer(y);
        if(layer != null){
            return layer.getTile(x, z);
        }
        else{
            return null;
        }
    }

    public boolean setTile(int x, int y, int z, Tile tile, int meta){
        TileLayer layer = this.getTileLayer(y);

        if(layer != null){
            if(layer.setTile(x, z, tile, meta)){
                this.reformMeshesAround(x, z);
                return true;
            }
        }
        return false;
    }

    public int getMetadata(int x, int y, int z){
        TileLayer layer = this.getTileLayer(y);
        if(layer != null){
            return layer.getMetadata(x, z);
        }
        else{
            return 0;
        }
    }

    public boolean setMetadata(int x, int y, int z, int meta){
        TileLayer layer = this.getTileLayer(y);
        if(layer != null){
            if(layer.setMetadata(x, z, meta)){
                this.reformMeshesAround(x, z);
                return true;
            }
        }
        return false;
    }

    private TileLayer getTileLayer(int layer){
        if(layer >= 0 && layer < this.tileLayers.length){
            TileLayer theLayer = this.tileLayers[layer];

            if(theLayer == null){
                theLayer = new TileLayer(this, layer);
                this.tileLayers[layer] = theLayer;
            }

            return theLayer;
        }
        else{
            return null;
        }
    }

    private void reformMeshesAround(int xChanged, int zChanged){
        this.shouldReformMesh = true;

        if(xChanged == 0 || xChanged == SIZE-1 || zChanged == 0 || zChanged == SIZE-1){
            for(Direction dir : Direction.HORIZONTAL){
                Chunk chunk = this.world.getChunkFromChunkCoords(this.x+dir.xOffset, this.z+dir.zOffset, false);
                if(chunk != null){
                    chunk.shouldReformMesh = true;
                }
            }
        }
    }

    public void update(){
        if(this.shouldReformMesh){
            this.mesh.makeNewMesh();
            this.shouldReformMesh = false;
        }

        for(int i = 0; i < this.entities.size(); i++){
            Entity entity = this.entities.get(i);

            boolean removeFromWorld = entity.shouldRemove();
            boolean removeFromChunk = removeFromWorld;

            if(!removeFromChunk){
                int estChunkX = CoordUtil.coordsOfIncludingChunk(entity.x);
                int estChunkZ = CoordUtil.coordsOfIncludingChunk(entity.z);

                if(entity.chunkX != estChunkX || entity.chunkZ != estChunkZ){
                    removeFromChunk = true;

                    Chunk newChunk = this.world.getChunkFromChunkCoords(estChunkX, estChunkZ, entity instanceof EntityPlayer);
                    if(newChunk != null){
                        newChunk.addEntity(entity);
                        TheGame.LOGGER.debug("Moved entity "+entity+" from chunk at "+this.x+", "+this.z+" to chunk at "+newChunk.x+", "+newChunk.z+".");
                    }
                    else{
                        throw new GdxRuntimeException("Tried to move an entity to a non-existant chunk!");
                    }
                }

                entity.update();
            }

            if(removeFromChunk){
                TheGame.LOGGER.debug("Removed entity "+entity+" from chunk at "+this.x+", "+this.z+".");

                this.entities.remove(i);
                i--;

                if(removeFromWorld){
                    entity.onRemove();
                    entity.dispose();

                    if(entity instanceof EntityPlayer){
                        this.world.players.remove(entity);
                    }

                    TheGame.LOGGER.debug("Removed entity "+entity+" from world entirely!");
                }
            }
        }

        for(int i = 0; i < this.tileActivities.size(); i++){
            TileActivity activity = this.tileActivities.get(i);

            if(activity.shouldRemove()){
                this.tileActivities.remove(i);
                i--;

                activity.onRemove();
                activity.dispose();
            }
            else{
                activity.update();
            }
        }
    }

    public void onUnload(){
        for(Entity entity : this.entities){
            entity.onUnload();
        }
        for(TileActivity activity : this.tileActivities){
            activity.onUnload();
        }

        this.reformMeshesAround(0, 0);
        this.dispose();
    }

    public void render(ShaderProgram shader){
        this.mesh.render(shader);
    }

    public void populate(){
        for(int x = 0; x < SIZE; x++){
            for(int z = 0; z < SIZE; z++){
                for(int y = 0; y < 20; y++){
                    this.setTile(x, y, z, Registry.TILE_ROCK, 0);
                }
            }
        }
    }

    @Override
    public void dispose(){
        this.mesh.dispose();

        for(Entity entity : this.entities){
            entity.dispose();
        }
        for(TileActivity activity : this.tileActivities){
            activity.dispose();
        }
    }
}
