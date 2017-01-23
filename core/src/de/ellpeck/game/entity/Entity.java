package de.ellpeck.game.entity;

import com.badlogic.gdx.utils.Disposable;
import de.ellpeck.game.util.AABB;
import de.ellpeck.game.world.World;

import java.util.List;

public class Entity implements Disposable{

    private final AABB tempCollisionCalc = AABB.empty();

    public World world;
    public double x;
    public double y;
    public double z;

    public double pitch;
    public double yaw;

    public boolean onGround;
    public boolean collidedHor;
    public boolean collidedVert;

    private final AABB boundingBox = new AABB(-0.4, 0, -0.4, 0.4, 1.8, 0.4);

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

    public void move(double motionX, double motionY, double motionZ){
        if(motionX != 0 || motionY != 0 || motionZ != 0){
            double motionXBefore = motionX;
            double motionYBefore = motionY;
            double motionZBefore = motionZ;

            AABB ownBox = this.getBoundingBox();
            if(!ownBox.isEmpty()){
                AABB searchBox = this.tempCollisionCalc.set(ownBox).offset(this.x+motionX, this.y+motionY, this.z+motionZ);
                List<AABB> boxes = this.world.getCollisionBoxes(searchBox, false);

                if(motionY != 0){
                    if(!boxes.isEmpty()){
                        AABB yBox = this.tempCollisionCalc.set(ownBox).offset(this.x, this.y, this.z);
                        for(AABB box : boxes){
                            if(motionY != 0){
                                if(!box.isEmpty()){
                                    motionY = box.getCollisionMotionY(yBox, motionY);
                                }
                            }
                            else{
                                break;
                            }
                        }
                    }

                    this.y += motionY;
                }

                if(motionX != 0){
                    if(!boxes.isEmpty()){
                        AABB xBox = this.tempCollisionCalc.set(ownBox).offset(this.x, this.y, this.z);
                        for(AABB box : boxes){
                            if(motionX != 0){
                                if(!box.isEmpty()){
                                    motionX = box.getCollisionMotionX(xBox, motionX);
                                }
                            }
                            else{
                                break;
                            }
                        }
                    }

                    this.x += motionX;
                }

                if(motionZ != 0){
                    if(!boxes.isEmpty()){
                        AABB zBox = this.tempCollisionCalc.set(ownBox).offset(this.x, this.y, this.z);
                        for(AABB box : boxes){
                            if(motionZ != 0){
                                if(!box.isEmpty()){
                                    motionZ = box.getCollisionMotionZ(zBox, motionZ);
                                }
                            }
                            else{
                                break;
                            }
                        }
                    }

                    this.z += motionZ;
                }
            }

            this.collidedHor = motionX != motionXBefore || motionZ != motionZBefore;
            this.collidedVert = motionY != motionYBefore;
            this.onGround = this.collidedVert && motionYBefore < 0;
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
