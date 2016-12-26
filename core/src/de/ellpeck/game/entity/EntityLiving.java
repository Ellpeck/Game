package de.ellpeck.game.entity;

import de.ellpeck.game.world.World;

public class EntityLiving extends Entity{

    private int jumpTimer;

    public EntityLiving(World world, int x, int y, int z){
        super(world, x, y, z);
    }

    @Override
    public void update(){
        super.update();

        if(this.jumpTimer > 0){
            this.jumpTimer--;
        }
    }

    public void jump(){
        if(this.jumpTimer <= 0 && this.onGround){
            this.jumpTimer = 5;

            this.motionY += 0.4;
        }
    }
}
