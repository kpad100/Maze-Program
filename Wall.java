import java.awt.*;

public class Wall {

    private int distance;
    private boolean leftWall, endWall;

    public Wall(int distance, boolean leftWall) {
        this.distance = distance;
        this.leftWall = leftWall;
        endWall = false;
    }

    public Wall(int distance) {
        this.distance = distance;
        endWall = true;
    }

    public Polygon getPolygon() {
        if (endWall) {
            switch (distance) {
                case 1:
                    return new Polygon(new int[] { 300, 800, 800, 300 }, new int[] { 350, 350, 750, 750 }, 4);
                case 2:
                    return new Polygon(new int[] { 400, 700, 700, 400 }, new int[] { 400, 400, 700, 700 }, 4);
                case 3:
                    return new Polygon(new int[] { 450, 650, 650, 450 }, new int[] { 450, 450, 650, 650 }, 4);
            }
        } else if (leftWall) {
            switch (distance) {
                case 0:
                    return new Polygon(new int[] { 150, 300, 300, 150 }, new int[] { 300, 350, 750, 800 }, 4);
                case 1:
                    return new Polygon(new int[] { 300, 300, 400, 400 }, new int[] { 350, 750, 700, 400 }, 4);
                case 2:
                    return new Polygon(new int[] { 400, 400, 450, 450 }, new int[] { 700, 400, 450, 650 }, 4);
            }
        } else {
            switch (distance) {
                case 0:
                    return new Polygon(new int[] { 800, 800, 950, 950 }, new int[] { 350, 750, 800, 300 }, 4);
                case 1:
                    return new Polygon(new int[] { 700, 700, 800, 800 }, new int[] { 700, 400, 350, 750 }, 4);
                case 2:
                    return new Polygon(new int[] { 650, 650, 700, 700 }, new int[] { 450, 650, 700, 400 }, 4);
            }
        }
        return new Polygon();
    }
}