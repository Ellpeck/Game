package de.ellpeck.game.util;

import de.ellpeck.game.world.chunk.Chunk;

public final class CoordUtil{

    public static int coordsOfIncludingChunk(double pos){
        return MathUtil.floor(pos/(double)Chunk.SIZE);
    }

    public static int relativeToIncludingChunk(int pos){
        return pos-coordsOfIncludingChunk(pos)*Chunk.SIZE;
    }
}
