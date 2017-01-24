package de.ellpeck.game;

import com.badlogic.gdx.graphics.Texture;
import de.ellpeck.game.tile.Tile;
import de.ellpeck.game.tile.TileLeaves;
import de.ellpeck.game.tile.TileLog;
import de.ellpeck.game.tile.TileSingleTextured;
import de.ellpeck.game.tile.activity.TileActivity;
import de.ellpeck.game.util.RegistryIndexed;

public final class Registry{

    public static final Texture TILES_TEXTURE = new Texture("game/textures/tiles.png");

    public static final RegistryIndexed<Tile> TILE_REGISTRY = new RegistryIndexed<>();
    public static final RegistryIndexed<Class<? extends TileActivity>> TILE_ACTIVITIES = new RegistryIndexed<>();

    public static final Tile TILE_DIRT = new TileSingleTextured(0).setTextureCoords(0, 0).register();
    public static final Tile TILE_GRASS = new TileSingleTextured(1).setTextureCoords(1, 0).register();
    public static final Tile TILE_ROCK = new TileSingleTextured(2).setTextureCoords(2, 0).register();
    public static final Tile TILE_LOG = new TileLog(3).register();
    public static final Tile TILE_LEAVES = new TileLeaves(4).register();

    public static void init(){

    }

    public static void dispose(){
        TILES_TEXTURE.dispose();
    }
}
