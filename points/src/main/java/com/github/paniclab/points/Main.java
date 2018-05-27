package com.github.paniclab.points;

import com.github.paniclab.points.utils.Point;
import com.github.paniclab.points.utils.PointContainer;

import java.util.*;

public class Main {

    public static void main(String[] args) {

        final PointContainer CONTAINER = PointContainer.newInstanceWithSimpleSymmetricContent();
        int max_X = CONTAINER.getContent().get(0).getX();
        int min_X = CONTAINER.getContent().get(0).getX();
        final float AXIS;
        Map<Integer, Set<Point>> scroll = new HashMap<>(CONTAINER.size() + 1, 1f);
        Map<Integer, Set<Point>> mutableScroll = new HashMap<>(CONTAINER.size() + 1, 1f);

        for (Point point : CONTAINER) {
            max_X = (point.getX() > max_X ? point.getX() : max_X);
            min_X = (point.getX() < min_X ? point.getX() : min_X);

            if(!scroll.containsKey(point.getX())) {
                Set<Point> payload = new HashSet<>();
                payload.add(point);
                scroll.put(point.getX(), payload);

                Set<Point> mutablePayload = new HashSet<>();
                mutablePayload.add(point);
                mutableScroll.put(point.getX(), mutablePayload);
            } else {
                scroll.get(point.getX()).add(point);
                mutableScroll.get(point.getX()).add(point);
            }
        }
        scroll = Collections.unmodifiableMap(scroll);
        assert (scroll.keySet().size() == mutableScroll.keySet().size());


        AXIS = Math.round(((float)max_X - min_X)/2 + min_X);
        assert AXIS - min_X == max_X - AXIS;

        float leftHalf = AXIS - min_X;
        float rightHalf = max_X - AXIS;
        assert leftHalf == rightHalf;


        try {
            for (int x : scroll.keySet()) {
                Set<Point> pointSet = scroll.get(x);
                if (pointSet == null) continue;

                for (Point point : pointSet) {
                    Point symmetricPoint = findSymmetricPoint(point, AXIS);
                    if(symmetricPoint == null) {
                        //TODO обработать случай, когда точка лежит на оси
                        throw new RuntimeException("Нет оси симметрии");
                    }
                    if(!scroll.get(symmetricPoint.getX()).contains(symmetricPoint)) {
                        throw new RuntimeException("Нет оси симметрии");
                    }

                    System.out.println(point);
                    mutableScroll.get(x).remove(point);
                    if(mutableScroll.get(x).size() == 0) {
                        System.out.println("Удаляем ключ: " + point.getX());
                        mutableScroll.remove(x);
                    }

                    mutableScroll.get(symmetricPoint.getX()).remove(symmetricPoint);
                    if(mutableScroll.get(symmetricPoint.getX()).size() == 0) {
                        System.out.println("Удаляем ключ: " + symmetricPoint.getX());
                        mutableScroll.remove(symmetricPoint.getX());
                    }

                    if(mutableScroll.keySet().size() == 0) {
                        throw new RuntimeException("Ось симметрии найдена!");
                    }
                }
            }
        } catch (RuntimeException e) {
            if(e.getMessage() != null && e.getMessage().equals("Ось симметрии найдена!")) {
                System.out.println("Ось симметрии найдена: " + AXIS);
            } else {
                throw e;
            }
        }

        if(mutableScroll.keySet().size() != 0) {
            throw new RuntimeException("Непредвиденная ошибка. Очевидно, какие-то факторы не учтены.");
        }
    }

    private static Point findSymmetricPoint(final Point POINT, final float AXIS) {
        float distanceFromAxis;
        float symmetricX;
        Point symmetricPoint = null;

        if(POINT.getX() == AXIS) {
            symmetricPoint = POINT;
        }
        if(POINT.getX() < AXIS) {
            distanceFromAxis = AXIS - POINT.getX();
            symmetricX = AXIS + distanceFromAxis;

            assert symmetricX - (int)symmetricX == 0;

            symmetricPoint = Point.newInstance((int)symmetricX, POINT.getY());
        }
        if (POINT.getX() > AXIS) {
            distanceFromAxis = POINT.getX() - AXIS;
            symmetricX = AXIS - distanceFromAxis;

            assert symmetricX - (int)symmetricX == 0;

            symmetricPoint = Point.newInstance((int)symmetricX, POINT.getY());
        }

        return symmetricPoint;
    }


}
