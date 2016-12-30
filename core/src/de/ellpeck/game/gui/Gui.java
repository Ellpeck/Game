package de.ellpeck.game.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

public class Gui implements Disposable{

    public int sizeX;
    public int sizeY;

    public Gui(int sizeX, int sizeY){
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public void render(SpriteBatch batch){

    }

    public void update(){

    }

    public void onOpened(){

    }

    public void onClosed(){

    }

    public void onScreenResize(int width, int height){

    }

    public boolean shouldBeClosed(){
        return false;
    }

    public boolean doesPauseMovement(){
        return true;
    }

    public boolean keyDown(int keycode){
        return false;
    }

    public boolean keyUp(int keycode){
        return false;
    }

    public boolean keyTyped(char character){
        return false;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button){
        return false;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button){
        return false;
    }

    public boolean touchDragged(int screenX, int screenY, int pointer){
        return false;
    }

    public boolean mouseMoved(int screenX, int screenY){
        return false;
    }

    public boolean scrolled(int amount){
        return false;
    }

    @Override
    public void dispose(){

    }
}
