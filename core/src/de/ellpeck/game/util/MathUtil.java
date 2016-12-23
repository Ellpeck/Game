package de.ellpeck.game.util;

public final class MathUtil{

    public static double distanceSq(double x1, double y1, double z1, double x2, double y2, double z2){
        double a = x2-x1;
        double b = y2-y1;
        double c = z2-z1;
        return a*a+b*b+c*c;
    }

    public static double distance(double x1, double y1, double z1, double x2, double y2, double z2){
        return Math.sqrt(distanceSq(x1, y1, z1, x2, y2, z2));
    }

    public static int round(double d){
        if(d > 0.5){
            return (int)d+1;
        }
        else{
            return (int)d;
        }
    }
}
