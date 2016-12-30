package de.ellpeck.game.entity;

import com.badlogic.gdx.utils.Disposable;
import de.ellpeck.game.util.AABB;
import de.ellpeck.game.world.World;

import java.util.List;

public class Entity implements Disposable{

    public World world;
    public double x;
    public double y;
    public double z;

    public double pitch;
    public double yaw;
    public boolean onGround;

    private final AABB boundingBox = new AABB(-0.5, -2, -0.5, 0.5, 1, 0.5);

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
        this.applyMotion();
    }

    protected void applyMotion(){
        this.motionY -= 0.05;

        this.motionX *= 0.5;
        this.motionY *= 0.98;
        this.motionZ *= 0.5;

        this.move(this.motionX, this.motionY, this.motionZ);

        if(this.onGround){
            this.motionY = 0;
        }
    }

    //TODO Maybe do the collision check for all sides independent of one another to avoid edge clipping?
    public void move(double motionX, double motionY, double motionZ){
        if(motionX != 0 || motionY != 0 || motionZ != 0){
            double motionYBefore = motionY;

            AABB ownBox = this.getBoundingBox();
            List<AABB> boxes = this.world.getCollisionBoxes(ownBox.copy().offset(this.x+motionX, this.y+motionY, this.z+motionZ), false);

            if(!boxes.isEmpty()){
                AABB ownBoxOffset = ownBox.copy().offset(this.x, this.y, this.z);

                for(AABB box : boxes){
                    if(motionX != 0){
                        motionX = box.calculateDistanceX(ownBoxOffset, motionX);
                    }

                    if(motionY != 0){
                        motionY = box.calculateDistanceY(ownBoxOffset, motionY);
                    }

                    if(motionZ != 0){
                        motionZ = box.calculateDistanceZ(ownBoxOffset, motionZ);
                    }
                }
            }

            this.onGround = motionY != motionYBefore && motionYBefore < 0;

            this.x += motionX;
            this.y += motionY;
            this.z += motionZ;
        }
    }

    public boolean shouldRemove(){
        return false;
    }

    public void onUnload(){

    }

    public void onRemove(){

    }

    public AABB getBoundingBox(){
        return this.boundingBox;
    }

    @Override
    public void dispose(){

    }

    public void moveRelative(double strafe, double forward, double friction){
        if(strafe != 0 || forward != 0){
            double length = friction/Math.sqrt(strafe*strafe+forward*forward);

            strafe = strafe*length;
            forward = forward*length;

            double yaw = Math.toRadians(this.yaw%360);
            double sin = Math.sin(yaw);
            double cos = Math.cos(yaw);

            this.motionX += strafe*cos-forward*sin;
            this.motionZ += forward*cos+strafe*sin;
        }
    }
}
