package de.ellpeck.game.entity.player;

import de.ellpeck.game.entity.Entity;
import de.ellpeck.game.world.World;

//TODO Move controlling to a different class and use a custom controlling system?
public class EntityPlayer extends Entity{

    public EntityPlayer(World world, int x, int y, int z){
        super(world, x, y, z);
    }
}
