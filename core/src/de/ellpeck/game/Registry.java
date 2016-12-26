package de.ellpeck.game;

import com.badlogic.gdx.graphics.Texture;
import de.ellpeck.game.tile.Tile;
import de.ellpeck.game.tile.TileLeaves;
import de.ellpeck.game.tile.TileLog;
import de.ellpeck.game.tile.TileSingleTextured;
import de.ellpeck.game.tile.activity.TileActivity;
import de.ellpeck.game.util.NameRegistry;

public final class Registry{

    public static final Texture TILES_TEXTURE = new Texture("game/textures/tiles.png");

    public static final NameRegistry<Tile> TILE_REGISTRY = new NameRegistry<>();
    public static final NameRegistry<Class<? extends TileActivity>> TILE_ACTIVITIES = new NameRegistry<>();

    public static final Tile TILE_DIRT = new TileSingleTextured("dirt").setTextureCoords(0, 0).register();
    public static final Tile TILE_GRASS = new TileSingleTextured("grass").setTextureCoords(1, 0).register();
    public static final Tile TILE_ROCK = new TileSingleTextured("rock").setTextureCoords(2, 0).register();
    public static final Tile TILE_LOG = new TileLog().register();
    public static final Tile TILE_LEAVES = new TileLeaves().register();

    public static void init(){

    }

    public static void dispose(){
        TILES_TEXTURE.dispose();
    }
}
