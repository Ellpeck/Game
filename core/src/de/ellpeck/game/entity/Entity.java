package de.ellpeck.game.entity;

import com.badlogic.gdx.utils.Disposable;
import de.ellpeck.game.util.AABB;
import de.ellpeck.game.world.World;

import java.util.List;

public class Entity implements Disposable{

    private final AABB tempBound = AABB.EMPTY.copy();

    public World world;
    public double x;
    public double y;
    public double z;

    public double pitch;
    public double yaw;
    public boolean onGround;
    public boolean isFlying;

    private final AABB boundingBox = new AABB(-1, -2, -1, 1, 1, 1);

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
        if(!this.onGround){
            this.motionY -= 0.05;
        }
        else if(this.motionY < 0){
            this.motionY = 0;
        }

        this.motionX *= 0.5;
        this.motionY *= 0.98;
        this.motionZ *= 0.5;

        this.move(this.motionX, this.motionY, this.motionZ);
    }

    public void move(double motionX, double motionY, double motionZ){
        double motionYBefore = motionY;

        AABB ownBox = this.getBoundingBox();
        List<AABB> boxes = this.world.getCollisionBoxes(this.tempBound.set(ownBox).offset(this.x+motionX, this.y+motionY, this.z+motionZ), false);

        if(!boxes.isEmpty()){
            this.tempBound.set(ownBox).offset(this.x, this.y, this.z);

            for(AABB box : boxes){
                if(motionX != 0){
                    motionX = box.calculateDistanceX(this.tempBound, motionX);
                }

                if(motionY != 0){
                    motionY = box.calculateDistanceY(this.tempBound, motionY);
                }

                if(motionZ != 0){
                    motionZ = box.calculateDistanceZ(this.tempBound, motionZ);
                }
            }
        }

        if(motionY != motionYBefore){
            this.onGround = motionYBefore < 0;
        }
        else if(motionYBefore > 0){
            this.onGround = false;
        }

        this.x += motionX;
        this.y += motionY;
        this.z += motionZ;
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

            double yaw = this.yaw%360;
            double sin = Math.sin(yaw*0.01);
            double cos = Math.cos(yaw*0.01);

            this.motionX += strafe*cos-forward*sin;
            this.motionZ += forward*cos+strafe*sin;
        }
    }
}
