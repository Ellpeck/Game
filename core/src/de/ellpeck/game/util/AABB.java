package de.ellpeck.game.util;

public class AABB{

    public static final AABB EMPTY = new AABB(0, 0, 0, 0, 0, 0);

    public double x1;
    public double y1;
    public double z1;

    public double x2;
    public double y2;
    public double z2;

    public AABB(double x1, double y1, double z1, double x2, double y2, double z2){
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;

        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
    }

    public boolean contains(double x, double y, double z){
        return this.x1 <= x && this.y1 <= y && this.z1 <= z && this.x2 >= x && this.y2 >= y && this.z2 >= z;
    }

    public AABB offset(double x, double y, double z){
        AABB box = this.copy();
        return box.offsetKeep(x, y, z);
    }

    public AABB offsetKeep(double x, double y, double z){
        this.x1 += x;
        this.y1 += y;
        this.z1 += z;

        this.x2 += x;
        this.y2 += y;
        this.z2 += z;

        return this;
    }

    public AABB set(AABB box){
        return this.set(box.x1, box.y1, box.z1, box.x2, box.y2, box.z2);
    }

    public AABB set(double x1, double y1, double z1, double x2, double y2, double z2){
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;

        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;

        return this;
    }

    public AABB copy(){
        return new AABB(this.x1, this.y1, this.z1, this.x2, this.y2, this.z2);
    }
}
