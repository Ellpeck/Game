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
        this.set(x1, y1, z1, x2, y2, z2);
    }

    public boolean contains(double x, double y, double z){
        return this.x1 <= x && this.y1 <= y && this.z1 <= z && this.x2 >= x && this.y2 >= y && this.z2 >= z;
    }

    public boolean intersects(AABB other){
        return this.intersects(other.x1, other.y1, other.z1, other.x2, other.y2, other.z2);
    }

    public boolean intersects(double x1, double y1, double z1, double x2, double y2, double z2){
        return this.x1 < x2 && this.x2 > x1 && this.y1 < y2 && this.y2 > y1 && this.z1 < z2 && this.z2 > z1;
    }

    public AABB offset(double x, double y, double z){
        this.x1 += x;
        this.y1 += y;
        this.z1 += z;

        this.x2 += x;
        this.y2 += y;
        this.z2 += z;

        return this;
    }

    public AABB expand(double x, double y, double z){
        this.x1 -= x;
        this.y1 -= y;
        this.z1 -= z;

        this.x2 += x;
        this.y2 += y;
        this.z2 += z;

        return this;
    }

    public double getSizeX(){
        return this.x2-this.x1;
    }

    public double getSizeY(){
        return this.y2-this.y1;
    }

    public double getSizeZ(){
        return this.z2-this.z1;
    }

    public AABB set(AABB box){
        return this.set(box.x1, box.y1, box.z1, box.x2, box.y2, box.z2);
    }

    public AABB set(double x1, double y1, double z1, double x2, double y2, double z2){
        this.x1 = Math.min(x1, x2);
        this.y1 = Math.min(y1, y2);
        this.z1 = Math.min(z1, z2);

        this.x2 = Math.max(x1, x2);
        this.y2 = Math.max(y1, y2);
        this.z2 = Math.max(z1, z2);

        return this;
    }

    public AABB copy(){
        return new AABB(this.x1, this.y1, this.z1, this.x2, this.y2, this.z2);
    }

    public boolean isEmpty(){
        return EMPTY.equals(this) || (this.x1 == this.x2 && this.y1 == this.y2 && this.z1 == this.z2);
    }

    public double calculateDistanceX(AABB other, double offsetX){
        if(other.y2 > this.y1 && other.y1 < this.y2 && other.z2 > this.z1 && other.z1 < this.z2){
            if(offsetX > 0 && other.x2 <= this.x1){
                double diff = this.x1-other.x2;
                if(diff < offsetX){
                    offsetX = diff;
                }
            }
            else if(offsetX < 0 && other.x1 >= this.x2){
                double diff = this.x2-other.x1;
                if(diff > offsetX){
                    offsetX = diff;
                }
            }
        }
        return offsetX;
    }

    public double calculateDistanceY(AABB other, double offsetY){
        if(other.x2 > this.x1 && other.x1 < this.x2 && other.z2 > this.z1 && other.z1 < this.z2){
            if(offsetY > 0 && other.y2 <= this.y1){
                double diff = this.y1-other.y2;
                if(diff < offsetY){
                    offsetY = diff;
                }
            }
            else if(offsetY < 0 && other.y1 >= this.y2){
                double diff = this.y2-other.y1;
                if(diff > offsetY){
                    offsetY = diff;
                }
            }
        }
        return offsetY;
    }

    public double calculateDistanceZ(AABB other, double offsetZ){
        if(other.x2 > this.x1 && other.x1 < this.x2 && other.y2 > this.y1 && other.y1 < this.y2){
            if(offsetZ > 0 && other.z2 <= this.z1){
                double diff = this.z1-other.z2;
                if(diff < offsetZ){
                    offsetZ = diff;
                }
            }
            else if(offsetZ < 0 && other.z1 >= this.z2){
                double diff = this.z2-other.z1;
                if(diff > offsetZ){
                    offsetZ = diff;
                }
            }
        }
        return offsetZ;
    }

    @Override
    public boolean equals(Object o){
        if(o == this){
            return true;
        }
        else if(o instanceof AABB){
            AABB box = (AABB)o;
            return box.x1 == this.x1 && box.y1 == this.y1 && box.z1 == this.z1 && box.x2 == this.x2 && box.y2 == this.y2 && box.z2 == this.z2;
        }
        else{
            return false;
        }
    }

    @Override
    public int hashCode(){
        int result;
        long temp;
        temp = Double.doubleToLongBits(this.x1);
        result = (int)(temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.y1);
        result = 31*result+(int)(temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.z1);
        result = 31*result+(int)(temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.x2);
        result = 31*result+(int)(temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.y2);
        result = 31*result+(int)(temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.z2);
        result = 31*result+(int)(temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString(){
        return "AABB{"+"x1="+this.x1+", y1="+this.y1+", z1="+this.z1+", x2="+this.x2+", y2="+this.y2+", z2="+this.z2+"}";
    }
}
