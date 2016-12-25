package de.ellpeck.game.entity.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;

import java.util.ArrayList;
import java.util.List;

public class PlayerController implements InputProcessor{

    private final EntityPlayer player;
    private final PerspectiveCamera camera;

    private final List<Integer> keys = new ArrayList<>();
    private final Matrix4 lookDirectionMatrix = new Matrix4();

    private static final int STRAFE_LEFT = Keys.A;
    private static final int STRAFE_RIGHT = Keys.D;
    private static final int FORWARD = Keys.W;
    private static final int BACKWARD = Keys.S;

    private static final float DEGREES_PER_PIXEL = 0.5F;

    public PlayerController(EntityPlayer player, PerspectiveCamera camera){
        this.player = player;
        this.camera = camera;
    }

    @Override
    public boolean keyDown(int keycode){
        this.keys.add(keycode);
        return true;
    }

    @Override
    public boolean keyUp(int keycode){
        this.keys.remove((Integer)keycode);
        return true;
    }

    @Override
    public boolean keyTyped(char character){
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button){
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button){
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer){
        float deltaX = -Gdx.input.getDeltaX()*DEGREES_PER_PIXEL;
        float deltaY = -Gdx.input.getDeltaY()*DEGREES_PER_PIXEL;

        this.player.yaw -= deltaX;
        this.player.pitch += deltaY;

        this.lookDirectionMatrix.setFromEulerAngles((float)-this.player.yaw%360, (float)this.player.pitch%360, 0F);

        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY){
        return false;
    }

    @Override
    public boolean scrolled(int amount){
        return false;
    }

    public void update(){
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

        if(strafe != 0 || forward != 0){
            this.player.moveRelative(strafe, forward, 0.15);
        }

        this.camera.position.set((float)this.player.x, (float)this.player.y, (float)this.player.z);

        this.camera.direction.set(0F, 0F, -1F);
        this.camera.up.set(0F, 1F, 0F);
        this.camera.rotate(this.lookDirectionMatrix);

        this.camera.update();
    }
}
