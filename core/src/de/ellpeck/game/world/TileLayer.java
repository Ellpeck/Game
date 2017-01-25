package de.ellpeck.game.world;

import de.ellpeck.game.tile.Tile;
import de.ellpeck.game.util.CoordUtil;
import de.ellpeck.game.world.chunk.Chunk;

public class TileLayer{

    private final Tile[] tiles = new Tile[Chunk.SIZE*Chunk.SIZE*Chunk.SIZE];
    private final int[] metadata = new int[Chunk.SIZE*Chunk.SIZE*Chunk.SIZE];

    private final Chunk chunk;

    public TileLayer(Chunk chunk){
        this.chunk = chunk;
    }

    public Tile getTile(int x, int y, int z){
        return this.tiles[toInnerCoord(x, y, z)];
    }

    public boolean setTile(int x, int y, int z, Tile tile, int meta){
        Tile old = this.getTile(x, y, z);
        if(old != null){
            old.onRemoved(this.chunk.world, x, y, z, this.getMetadata(x, y, z));
        }

        this.tiles[toInnerCoord(x, y, z)] = tile;
        this.setMetadata(x, y, z, meta);

        if(tile != null){
            tile.onAdded(this.chunk.world, x, y, z, meta);
        }

        return old != tile;
    }

    public boolean setMetadata(int x, int y, int z, int meta){
        int old = this.getMetadata(x, y, z);

        this.metadata[toInnerCoord(x, y, z)] = meta;

        return old != meta;
    }

    public int getMetadata(int x, int y, int z){
        return this.metadata[toInnerCoord(x, y, z)];
    }

    private static int toInnerCoord(int x, int y, int z){
        int chunkX = CoordUtil.relativeToIncludingChunk(x);
        int chunkY = CoordUtil.relativeToIncludingChunk(y);
        int chunkZ = CoordUtil.relativeToIncludingChunk(z);
        return chunkX << 8 | chunkY << 4 | chunkZ;
    }
}
