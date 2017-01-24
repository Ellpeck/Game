package de.ellpeck.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.ellpeck.game.entity.player.EntityPlayer;
import de.ellpeck.game.entity.player.PlayerController;
import de.ellpeck.game.gui.Gui;
import de.ellpeck.game.gui.GuiHandler;
import de.ellpeck.game.world.World;

public class TheGame implements ApplicationListener{

    private static TheGame instance;
    public static final Logger LOGGER = new Logger("TheGame", Logger.DEBUG);

    private GuiHandler guiHandler;

    //TODO Move all of this out to some sort of client handler
    private PlayerController playerController;
    private ShaderProgram shader;
    private int camProjLocation;
    //TODO Move this out to some world handler or something
    private World world;

    private static final float TIME_STEP = 1F/20F;
    private double accumulator;
    private long ups;
    private long lastMillis;

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
        this.camProjLocation = this.shader.getUniformLocation("u_camProjection");

        this.world = new World();
        this.world.setTile(0, 21, 5, Registry.TILE_LOG, 2, true);
        this.world.setTile(2, 21, 5, Registry.TILE_LOG, 1, true);
        this.world.setTile(2, 20, 8, Registry.TILE_LEAVES, 0, true);

        EntityPlayer player = new EntityPlayer(this.world, 0, 30, 0);
        this.world.addEntity(player, true);

        this.guiHandler = new GuiHandler();

        this.playerController = new PlayerController(player, this.guiHandler);
        Gdx.input.setInputProcessor(this.playerController);
    }

    @Override
    public void resize(int width, int height){
        this.playerController.onResize(width, height);
        this.guiHandler.onScreenResize(width, height);
    }

    @Override
    public void render(){
        this.doUpdate();
        this.doRender();

        Gdx.graphics.setTitle("FPS: "+Gdx.graphics.getFramesPerSecond()+", UPS: "+this.ups);
    }

    private void doRender(){
        Gdx.gl.glClearColor(0F, 0F, 0F, 1F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        Gdx.gl.glEnable(GL20.GL_CULL_FACE);
        Gdx.gl.glCullFace(GL20.GL_BACK);

        this.shader.begin();
        this.shader.setUniformMatrix(this.camProjLocation, this.playerController.getCamMatrix());

        Registry.TILES_TEXTURE.bind();
        this.world.render(this.shader);

        this.shader.end();

        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDisable(GL20.GL_CULL_FACE);

        this.guiHandler.render();
    }

    private void doUpdate(){
        float frameTime = Math.min(Gdx.graphics.getDeltaTime(), 0.25F);
        this.accumulator += frameTime;

        while(this.accumulator >= TIME_STEP){
            this.accumulator -= TIME_STEP;

            long millis = TimeUtils.millis();
            long timeSince = millis-this.lastMillis;
            this.lastMillis = millis;
            if(timeSince > 0){
                this.ups = 1000/timeSince;
            }

            //Update with fixed step
            this.world.update();
            this.guiHandler.update();
        }

        //Update with gdx delta
        this.playerController.update();
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
        this.guiHandler.dispose();
        Registry.dispose();
    }

    public static TheGame get(){
        return instance;
    }
}
