package de.ellpeck.game.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.util.ArrayList;
import java.util.List;

public class GuiHandler implements Disposable{

    private final SpriteBatch batch = new SpriteBatch();

    private final List<Gui> activeGuis = new ArrayList<>();
    public boolean isMovementStopped;

    public void openGui(Gui gui){
        if(gui == null){
            throw new GdxRuntimeException("Tried to open a null Gui!");
        }
        else{
            if(gui.doesPauseMovement()){
                this.isMovementStopped = true;
            }

            this.activeGuis.add(gui);
            gui.onOpened();
        }
    }

    public void closeGui(int index){
        if(index < 0 || index >= this.activeGuis.size()){
            throw new GdxRuntimeException("Tried to close invalid Gui with index "+index+"!");
        }
        else{
            this.closeGui(this.activeGuis.get(index));
        }
    }

    public void closeGui(Gui gui){
        if(gui != null){
            if(this.activeGuis.contains(gui)){
                this.activeGuis.remove(gui);
                gui.onClosed();
                gui.dispose();
            }
            else{
                throw new GdxRuntimeException("Tried to close Gui "+gui+" that isn't currently open!");
            }
        }
        else{
            throw new GdxRuntimeException("Tried to close a null Gui!");
        }
    }

    public void render(){
        for(Gui gui : this.activeGuis){
            gui.render(this.batch);
        }
    }

    public void update(){
        for(int i = 0; i < this.activeGuis.size(); i++){
            Gui gui = this.activeGuis.get(i);

            if(gui.shouldBeClosed()){
                this.closeGui(gui);
                i--;
            }
            else{
                gui.update();
            }
        }
    }

    public void onScreenResize(int width, int height){
        for(Gui gui : this.activeGuis){
            gui.onScreenResize(width, height);
        }
    }

    public boolean keyDown(int keycode){
        for(Gui gui : this.activeGuis){
            if(gui.keyDown(keycode)){
                return true;
            }
        }
        return false;
    }

    public boolean keyUp(int keycode){
        for(Gui gui : this.activeGuis){
            if(gui.keyUp(keycode)){
                return true;
            }
        }
        return false;
    }

    public boolean keyTyped(char character){
        for(Gui gui : this.activeGuis){
            if(gui.keyTyped(character)){
                return true;
            }
        }
        return false;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button){
        for(Gui gui : this.activeGuis){
            if(gui.touchDown(screenX, screenY, pointer, button)){
                return true;
            }
        }
        return false;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button){
        for(Gui gui : this.activeGuis){
            if(gui.touchUp(screenX, screenY, pointer, button)){
                return true;
            }
        }
        return false;
    }

    public boolean touchDragged(int screenX, int screenY, int pointer){
        for(Gui gui : this.activeGuis){
            if(gui.touchDragged(screenX, screenY, pointer)){
                return true;
            }
        }
        return false;
    }

    public boolean mouseMoved(int screenX, int screenY){
        for(Gui gui : this.activeGuis){
            if(gui.mouseMoved(screenX, screenY)){
                return true;
            }
        }
        return false;
    }

    public boolean scrolled(int amount){
        for(Gui gui : this.activeGuis){
            if(gui.scrolled(amount)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void dispose(){
        this.batch.dispose();

        //If this isn't moved up here size will be decreased inside for loop and Guis will linger
        int size = this.activeGuis.size();
        for(int i = 0; i < size; i++){
            this.closeGui(0);
        }
    }
}
