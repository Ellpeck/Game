package de.ellpeck.game.world;

import de.ellpeck.game.tile.Tile;
import de.ellpeck.game.world.chunk.Chunk;

public class TileLayer{

    private final Tile[][] tiles = new Tile[Chunk.SIZE][Chunk.SIZE];
    private final int[][] metadata = new int[Chunk.SIZE][Chunk.SIZE];

    private final Chunk chunk;
    private final int layer;

    public TileLayer(Chunk chunk, int layer){
        this.chunk = chunk;
        this.layer = layer;
    }

    public Tile getTile(int x, int z){
        return this.tiles[x][z];
    }

    public boolean setTile(int x, int z, Tile tile, int meta){
        Tile old = this.getTile(x, z);
        if(old != null){
            old.onRemoved(this.chunk.world, x, this.layer, z, this.getMetadata(x, z));
        }

        this.tiles[x][z] = tile;
        this.metadata[x][z] = meta;

        if(tile != null){
            tile.onAdded(this.chunk.world, x, this.layer, z, meta);
        }

        return old != tile;
    }

    public boolean setMetadata(int x, int z, int meta){
        int old = this.metadata[x][z];

        this.metadata[x][z] = meta;

        return old != meta;
    }

    public int getMetadata(int x, int z){
        return this.metadata[x][z];
    }
}
