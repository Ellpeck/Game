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

    public static int floor(float value){
        int i = (int)value;
        return value < (float)i ? i-1 : i;
    }

    public static int floor(double value){
        int i = (int)value;
        return value < (double)i ? i-1 : i;
    }

    public static int ceil(float value){
        int i = (int)value;
        return value > (float)i ? i+1 : i;
    }

    public static int ceil(double value){
        int i = (int)value;
        return value > (double)i ? i+1 : i;
    }

    public static int clamp(int num, int min, int max){
        return num < min ? min : (num > max ? max : num);
    }

    public static float clamp(float num, float min, float max){
        return num < min ? min : (num > max ? max : num);
    }

    public static double clamp(double num, double min, double max){
        return num < min ? min : (num > max ? max : num);
    }
}
