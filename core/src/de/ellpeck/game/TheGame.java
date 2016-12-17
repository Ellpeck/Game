package de.ellpeck.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.ellpeck.game.world.World;

public class TheGame implements ApplicationListener{

    private static TheGame instance;
    public static final Logger LOGGER = new Logger("TheGame", Logger.DEBUG);

    //TODO Move all of this out to some sort of client handler
    private PerspectiveCamera camera;
    private Viewport viewport;
    private FirstPersonCameraController controller;
    private ShaderProgram shader;
    //TODO Move this out to some world handler or something
    private World world;

    public TheGame(){
        if(instance == null){
            instance = this;
        }
        else{
            throw new GdxRuntimeException("Tried to create the game while it was already running!");
        }
    }

    @Override
    public void create(){
        Registry.init();

        this.camera = new PerspectiveCamera();
        this.camera.near = 0.1F;
        this.camera.position.set(5, 5, 10);
        this.viewport = new ScalingViewport(Scaling.fill, 1, 1, this.camera);
        this.controller = new FirstPersonCameraController(this.camera);
        Gdx.input.setInputProcessor(this.controller);

        this.shader = new ShaderProgram(Gdx.files.local("game/shaders/world_vertex.glsl"), Gdx.files.local("game/shaders/world_fragment.glsl"));
        if(!this.shader.isCompiled()){
            String s = "Unable to compile World Shader! \nLog: "+this.shader.getLog();
            LOGGER.error(s);
            throw new GdxRuntimeException(s);
        }
        else{
            LOGGER.info("World Shader compiled successfully.");
            LOGGER.info("Shader Log: "+this.shader.getLog());
        }

        this.world = new World();
        this.world.setTile(0, 0, 0, Registry.TILE_LOG, 0, true);
        this.world.setTile(0, 0, 2, Registry.TILE_LOG, 1, true);
        this.world.setTile(0, 0, 4, Registry.TILE_LOG, 2, true);
        this.world.setTile(0, 0, 6, Registry.TILE_LOG, 3, true);

        this.world.setTile(0, 2, 1, Registry.TILE_DIRT, 0, true);
        this.world.setTile(0, 2, 3, Registry.TILE_GRASS, 0, true);
        this.world.setTile(0, 2, 5, Registry.TILE_ROCK, 0, true);
    }

    @Override
    public void resize(int width, int height){
        this.viewport.update(width, height);
    }

    @Override
    public void render(){
        Gdx.gl.glClearColor(1F, 0F, 1F, 1F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        Gdx.gl.glEnable(GL20.GL_CULL_FACE);
        Gdx.gl.glCullFace(GL20.GL_BACK);

        this.shader.begin();
        this.shader.setUniformMatrix("u_camProjection", this.camera.combined);

        Registry.TILES_TEXTURE.bind();
        this.world.render(this.shader);

        this.shader.end();

        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDisable(GL20.GL_CULL_FACE);

        this.controller.update();
        this.world.update();
    }

    @Override
    public void pause(){

    }

    @Override
    public void resume(){

    }

    @Override
    public void dispose(){
        LOGGER.info("Closing game and disposing of objects...");

        this.shader.dispose();
        this.world.dispose();
        Registry.dispose();
    }

    public static TheGame get(){
        return instance;
    }
}
