package de.ellpeck.game.util.pos;

public class Pos2{

    public static final Pos2 ZERO = new Pos2(0, 0);

    public int x;
    public int y;

    public Pos2(int x, int y){
        this.x = x;
        this.y = y;
    }

    public static Pos2 zero(){
        return ZERO.copy();
    }

    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        else if(o instanceof Pos2){
            Pos2 pos3 = (Pos2)o;
            return this.x == pos3.x && this.y == pos3.y;
        }
        else{
            return false;
        }
    }

    @Override
    public int hashCode(){
        int result = this.x;
        result = 31*result+this.y;
        return result;
    }

    public Pos2 copy(){
        return new Pos2(this.x, this.y);
    }

    public Pos2 set(int x, int y){
        this.x = x;
        this.y = y;

        return this;
    }
}
