package de.ellpeck.game.entity;

import com.badlogic.gdx.utils.Disposable;
import de.ellpeck.game.world.World;

public class Entity implements Disposable{

    public World world;
    public double x;
    public double y;
    public double z;
    public double height = 1;

    public double motionX;
    public double motionY;
    public double motionZ;

    public int chunkX;
    public int chunkZ;

    public Entity(World world, int x, int y, int z){
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void update(){
        this.doCollision();
        this.applyMotion();
    }

    protected void applyMotion(){
        //TODO
    }

    protected void doCollision(){
        //TODO
    }

    public boolean shouldRemove(){
        return false;
    }

    public void onUnload(){

    }

    public void onRemove(){

    }

    @Override
    public void dispose(){

    }
}
