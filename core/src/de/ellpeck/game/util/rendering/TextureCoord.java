package de.ellpeck.game.util.rendering;

import de.ellpeck.game.util.pos.Pos2;

public class TextureCoord extends Pos2{

    private static final float TEXTURE_SHEET_SIZE = 256F;
    private static final float TEXTURE_TILE_SIZE = 16F;
    private static final float TEX_TILE_SIZE = 1F/TEXTURE_SHEET_SIZE*TEXTURE_TILE_SIZE;

    public static final TextureCoord NONE = new TextureCoord(0, 0);

    public float sizePercentage = TEX_TILE_SIZE;

    public TextureCoord(int x, int y){
        super(x, y);
    }
}
