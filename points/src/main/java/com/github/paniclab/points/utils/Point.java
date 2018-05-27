package com.github.paniclab.points.utils;

import java.security.SecureRandom;
import java.util.Comparator;
import java.util.Objects;
import java.util.Random;

public class Point {
/*    public static final int TOTAL = 10;
    public static final int MAX_X = TOTAL*10 + 1;
    public static final int MAX_Y = TOTAL*10 + 1;*/
    public static final int TOTAL = 10;
    public static final int MAX_X = 101;
    public static final int MAX_Y = 101;
    public static final Comparator<Point> ORDER_COMPARATOR = Comparator.comparingInt(Point::getSortingOrder);

    private static final byte[] SEED = new SecureRandom().generateSeed(32);
    private static final Random RANDOM_GENERATOR = new SecureRandom(SEED);

    private int axisX;
    private int axisY;
    private int sortingOrder;

    private Point(int x, int y) {
        this.axisX = x;
        this.axisY = y;
    }


    public static Point newInstance(int x, int y) {
        Point point = new Point(x, y);
        point.sortingOrder = RANDOM_GENERATOR.nextInt(TOTAL*TOTAL);
        return point;
    }

    public static Point newRandomInstance() {
        return newInstance(RANDOM_GENERATOR.nextInt(MAX_X), RANDOM_GENERATOR.nextInt(MAX_Y));
    }

    public static Point copy(Point other) {
        return newInstance(other.axisX, other.axisY);
    }

    public int getX() {
        return axisX;
    }

    public int getY() {
        return axisY;
    }

    private int getSortingOrder() {
        return sortingOrder;
    }

    @Override
    public int hashCode() {
        return Objects.hash(axisX, axisY);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;

        if (this.getClass() != obj.getClass()) return false;
        Point other = Point.class.cast(obj);

        return this.axisX == other.axisX &&
                this.axisY == other.axisY;
    }

    @Override
    public String toString() {
        return "Point: x = " + this.axisX + ", y = " + this.axisY + ", order = " + this.sortingOrder;
    }
}
