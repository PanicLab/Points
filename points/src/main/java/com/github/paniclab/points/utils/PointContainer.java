package com.github.paniclab.points.utils;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.*;

public class PointContainer extends Container<Point> {
    private Comparator<Point> comparator;
    private final Random RANDOM_GENERATOR = new SecureRandom(ByteBuffer.allocate(4).putInt(Point.TOTAL).array());

    public PointContainer(Set<Point> content) {
        super(content);
        this.comparator = Point.ORDER_COMPARATOR;
    }

    public PointContainer(Set<Point> content, Comparator<Point> c) {
        super(content);
        this.comparator = c;
    }

    public static PointContainer newInstanceWithRandomContent() {
        Set<Point> content = new HashSet<>(Point.TOTAL + 1, 1f);
        while (content.size() < Point.TOTAL) {
            content.add(Point.newRandomInstance());
        }

        PointContainer container = new PointContainer(content);
        container.shuffle();

        return container;
    }

    public static PointContainer newInstanceWithSimpleSymmetricContent() {
        Set<Point> content = new HashSet<>(Point.TOTAL + 1, 1f);
        Set<Point> leftHalf = new HashSet<>(Point.TOTAL/2 + 1, 1f);
        Set<Point> rightHalf = new HashSet<>(Point.TOTAL/2 + 1, 1f);

        final int AXIS = Math.round((float)Point.MAX_X/2);

        while (leftHalf.size() < (Point.TOTAL/2)) {
            Point point = Point.newRandomInstance();
            if(point.getX() == 0) continue;

            int distanceFromAxis = AXIS - point.getX();

            if(point.getX() < Point.MAX_X/2) {
                leftHalf.add(point);

                int symmetricX = AXIS + distanceFromAxis;
                Point symmetricPoint = Point.newInstance(symmetricX, point.getY());
                rightHalf.add(symmetricPoint);

                assert symmetricPoint.getX() - point.getX() == distanceFromAxis*2;
                assert Point.MAX_X - symmetricPoint.getX() == point.getX();
            }
        }

        content.addAll(leftHalf);
        content.addAll(rightHalf);

        PointContainer container = new PointContainer(content);
        container.shuffle();

        return container;
    }


    @Override
    public void shuffle() {
        List<Point> newContent = new ArrayList<>(this.content().size());

        this.content().forEach(point -> newContent.add(Point.copy(point)));
        this.content().clear();
        this.content().addAll(newContent);

        this.content().sort(comparator);
    }
}
