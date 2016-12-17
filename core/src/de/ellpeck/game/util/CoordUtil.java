package de.ellpeck.game.util;

import de.ellpeck.game.world.chunk.Chunk;

public final class CoordUtil{

    public static int toChunk(double pos){
        return (int)Math.floor(pos/(double)Chunk.SIZE);
    }

    public static int toChunkIntern(int pos){
        return pos-toChunk(pos)*Chunk.SIZE;
    }

    public static long getChunkId(int x, int z){
        return (long)x << 32 | ((long)z & 0xFFFFFFFFL);
    }
}
