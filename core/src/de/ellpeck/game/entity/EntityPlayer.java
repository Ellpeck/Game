package de.ellpeck.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import de.ellpeck.game.world.World;

//TODO Move controlling to a different class and use a custom controlling system?
public class EntityPlayer extends Entity{

    private final FirstPersonCameraController controller;

    public EntityPlayer(World world, int x, int y, int z, PerspectiveCamera camera){
        super(world, x, y, z);

        this.controller = new FirstPersonCameraController(camera){
            @Override
            public void update(float deltaTime){
                super.update(deltaTime);

                EntityPlayer.this.x = camera.position.x;
                EntityPlayer.this.y = camera.position.y-1;
                EntityPlayer.this.z = camera.position.z;
            }
        };
        Gdx.input.setInputProcessor(this.controller);
    }

    @Override
    public void update(){
        super.update();
        this.controller.update();
    }
}
