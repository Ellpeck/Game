package de.ellpeck.game.util.pos;

public class Pos3{

    public static final Pos3 ZERO = new Pos3(0, 0, 0);

    public int x;
    public int y;
    public int z;

    public Pos3(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Pos3 zero(){
        return ZERO.copy();
    }

    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        else if(o instanceof Pos3){
            Pos3 pos3 = (Pos3)o;
            return this.x == pos3.x && this.y == pos3.y && this.z == pos3.z;
        }
        else{
            return false;
        }
    }

    @Override
    public int hashCode(){
        int result = this.x;
        result = 31*result+this.y;
        result = 31*result+this.z;
        return result;
    }

    public Pos3 copy(){
        return new Pos3(this.x, this.y, this.z);
    }

    public Pos3 set(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;

        return this;
    }
}
