package de.ellpeck.game.world.chunk;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import de.ellpeck.game.entity.Entity;
import de.ellpeck.game.tile.Tile;
import de.ellpeck.game.tile.activity.TileActivity;
import de.ellpeck.game.util.CoordUtil;
import de.ellpeck.game.world.ChunkMesh;
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

    public boolean shouldReformMesh;
    public final ChunkMesh mesh = new ChunkMesh(this);

    public Chunk(World world, int x, int z){
        this.world = world;
        this.x = x;
        this.z = z;

        for(int i = 0; i < this.tileLayers.length; i++){
            this.tileLayers[i] = new TileLayer(this, i);
        }
    }

    public void addTileActivity(TileActivity activity){
        this.tileActivities.add(activity);
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
                this.shouldReformMesh = true;
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
                this.shouldReformMesh = true;
                return true;
            }
        }
        return false;
    }

    private TileLayer getTileLayer(int layer){
        if(layer >= 0 && layer < this.tileLayers.length){
            return this.tileLayers[layer];
        }
        else{
            return null;
        }
    }

    public void update(){
        if(this.shouldReformMesh){
            this.mesh.makeNewMesh();
            this.shouldReformMesh = false;
        }

        for(int i = 0; i < this.entities.size(); i++){
            Entity entity = this.entities.get(i);

            boolean shouldRemove = entity.shouldRemove();

            if(!shouldRemove){
                int estChunkX = CoordUtil.toChunk(entity.x);
                int estChunkZ = CoordUtil.toChunk(entity.z);

                if(entity.chunkX != estChunkX || entity.chunkZ != estChunkZ){
                    shouldRemove = true;

                    Chunk newChunk = this.world.getChunkFromChunkCoords(estChunkX, estChunkZ, false);
                    if(newChunk != null){
                        newChunk.addEntity(entity);
                    }
                    else{
                        throw new GdxRuntimeException("Tried to move an entity to a non-existant chunk!");
                    }
                }

                entity.update();
            }

            if(shouldRemove){
                if(entity.shouldRemove()){
                    this.entities.remove(i);
                    i--;
                }
            }
        }

        for(int i = 0; i < this.tileActivities.size(); i++){
            TileActivity activity = this.tileActivities.get(i);

            if(activity.shouldRemove()){
                this.tileActivities.remove(i);
                i--;
            }
            else{
                activity.update();
            }
        }
    }

    public boolean shouldUnload(){
        return false; //TODO unload chunks
    }

    public void onUnload(){
        for(Entity entity : this.entities){
            entity.onUnload();
        }
        for(TileActivity activity : this.tileActivities){
            activity.onUnload();
        }

        this.dispose();
    }

    public void render(ShaderProgram shader){
        this.mesh.render(shader);
    }

    public void populate(){
        //TODO World gen
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
