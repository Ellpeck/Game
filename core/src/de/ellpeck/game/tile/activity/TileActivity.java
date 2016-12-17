package de.ellpeck.game.tile.activity;

import com.badlogic.gdx.utils.Disposable;

public class TileActivity implements Disposable{

    public int x;
    public int y;
    public int z;

    public void update(){

    }

    public boolean shouldRemove(){
        return false;
    }

    public void onUnload(){

    }

    @Override
    public void dispose(){

    }

    //TODO Add TileEntity-like updating and saving/loading system (using Darkhax' storage API?)

}
