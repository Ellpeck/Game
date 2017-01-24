package de.ellpeck.game.entity.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.ellpeck.game.gui.GuiHandler;
import de.ellpeck.game.util.MathUtil;

import java.util.ArrayList;
import java.util.List;

public class PlayerController implements InputProcessor{

    private final Vector3 tempVec = new Vector3();
    private final Matrix4 tempMat = new Matrix4();

    private final EntityPlayer player;
    private final Viewport viewport;
    private final PerspectiveCamera camera;
    private final GuiHandler guiHandler;

    private final List<Integer> keys = new ArrayList<>();
    private final Matrix4 lookDirectionMatrix = new Matrix4();

    private boolean isMouseCaught;

    private static final int STRAFE_LEFT = Keys.A;
    private static final int STRAFE_RIGHT = Keys.D;
    private static final int FORWARD = Keys.W;
    private static final int BACKWARD = Keys.S;
    private static final int JUMP = Keys.SPACE;

    private static final float ROTATION_DEGREES_PER_PIXEL = 0.3F;
    private static final float PLAYER_HEIGHT_OFFSET = 1.75F;

    public PlayerController(EntityPlayer player, GuiHandler guiHandler){
        this.player = player;
        this.guiHandler = guiHandler;

        this.camera = new PerspectiveCamera();
        this.camera.near = 0.1F;
        this.camera.far = 1000F;
        this.camera.fieldOfView = 70F;
        this.viewport = new ScalingViewport(Scaling.fill, 1, 1, this.camera);
    }

    @Override
    public boolean keyDown(int keycode){
        if(keycode == Keys.ESCAPE){
            this.isMouseCaught = !this.isMouseCaught;
            Gdx.input.setCursorCatched(this.isMouseCaught);
            return true;
        }

        this.keys.add(keycode);
        this.guiHandler.keyDown(keycode);

        return true;
    }

    @Override
    public boolean keyUp(int keycode){
        this.keys.remove((Integer)keycode);
        this.guiHandler.keyUp(keycode);
        return true;
    }

    @Override
    public boolean keyTyped(char character){
        return this.guiHandler.keyTyped(character);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button){
        return this.guiHandler.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button){
        return this.guiHandler.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer){
        return this.doLooking() || this.guiHandler.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY){
        return this.doLooking() || this.guiHandler.mouseMoved(screenX, screenY);
    }

    public void onResize(int width, int height){
        this.viewport.update(width, height);
    }

    public Matrix4 getCamMatrix(){
        return this.camera.combined;
    }

    private boolean doLooking(){
        if(this.isMouseCaught && !this.guiHandler.isMovementStopped){
            float deltaX = -Gdx.input.getDeltaX()*ROTATION_DEGREES_PER_PIXEL;
            float deltaY = -Gdx.input.getDeltaY()*ROTATION_DEGREES_PER_PIXEL;

            this.player.yaw = (this.player.yaw-deltaX)%360;
            this.player.pitch = MathUtil.clamp(this.player.pitch+deltaY, -90, 90)%360;

            this.lookDirectionMatrix.setFromEulerAngles((float)-this.player.yaw, (float)this.player.pitch, 0F);

            return true;
        }
        return false;
    }

    @Override
    public boolean scrolled(int amount){
        return this.guiHandler.scrolled(amount);
    }

    public void update(){
        if(!this.guiHandler.isMovementStopped){
            this.doMovement();
        }
    }

    private void doMovement(){
        double strafe = 0;
        double forward = 0;

        if(this.keys.contains(STRAFE_LEFT)){
            strafe = -1;
        }
        else if(this.keys.contains(STRAFE_RIGHT)){
            strafe = 1;
        }

        if(this.keys.contains(FORWARD)){
            forward = -1;
        }
        if(this.keys.contains(BACKWARD)){
            forward = 1;
        }

        if(this.keys.contains(JUMP)){
            this.player.jump();
        }

        if(strafe != 0 || forward != 0){
            this.player.moveRelative(strafe, forward, 0.15);
        }

        float lerpDelta = Gdx.graphics.getDeltaTime()*10;

        this.camera.position.slerp(this.tempVec.set((float)this.player.x, (float)this.player.y+PLAYER_HEIGHT_OFFSET, (float)this.player.z), lerpDelta);

        this.camera.direction.set(0F, 0F, -1F);
        this.camera.up.set(0F, 1F, 0F);
        //this.camera.rotate(this.tempMat.lerp(this.lookDirectionMatrix, lerpDelta));
        this.camera.rotate(this.tempMat.set(this.lookDirectionMatrix));

        this.camera.update();
    }
}
