package cake;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;

public class GraphicsUtils {
    
    public void quadraticBezier(Graphics g, Point p1, Point p2, Point p3) {
        for (int i = 0; i <= 1000; i++) {
            double t = i / 1000.0;

            double x = Math.pow(1 - t, 2) * p1.x + 2 * (1-t) * t * p2.x + t * t * p3.x;
            double y = Math.pow(1 - t, 2) * p1.y + 2 * (1-t) * t * p2.y + t * t * p3.y;

            plot(g, (int)x, (int)y, 3);
        }
    }
    
    public void drawBezier(Graphics g, Point p0, Point p1, Point p2, Point p3) {
        for (int i = 0; i <= 1000; i++) {
            double t = i / 1000.0;

            double x = Math.pow(1 - t, 3) * p0.x
                     + 3 * t * Math.pow(1 - t, 2) * p1.x
                     + 3 * Math.pow(t, 2) * (1 - t) * p2.x
                     + Math.pow(t, 3) * p3.x;

            double y = Math.pow(1 - t, 3) * p0.y
                     + 3 * t * Math.pow(1 - t, 2) * p1.y
                     + 3 * Math.pow(t, 2) * (1 - t) * p2.y
                     + Math.pow(t, 3) * p3.y;

            plot(g, (int)x, (int)y, 3);
        }
    }
    
    private void plot(Graphics g, int x, int y, int size) {
        g.fillRect(x, y, size, size);
    }
    
    public BufferedImage floodFill(BufferedImage m, int x, int y, Color target, Color replacement) {
        int targetRGB = target.getRGB();
        int replacementRGB = replacement.getRGB();

        if (m.getRGB(x, y) != targetRGB || targetRGB == replacementRGB)
            return m;

        Queue<Point> q = new LinkedList<>();
        q.add(new Point(x, y));
        m.setRGB(x, y, replacementRGB);

        while (!q.isEmpty()) {
            Point p = q.poll();
            int px = p.x;
            int py = p.y;

            // North
            if (py > 0 && m.getRGB(px, py - 1) == targetRGB) {
                m.setRGB(px, py - 1, replacementRGB);
                q.add(new Point(px, py - 1));
            }

            // South
            if (py < m.getHeight() - 1 && m.getRGB(px, py + 1) == targetRGB) {
                m.setRGB(px, py + 1, replacementRGB);
                q.add(new Point(px, py + 1));
            }

            // East
            if (px < m.getWidth() - 1 && m.getRGB(px + 1, py) == targetRGB) {
                m.setRGB(px + 1, py, replacementRGB);
                q.add(new Point(px + 1, py));
            }

            // West
            if (px > 0 && m.getRGB(px - 1, py) == targetRGB) {
                m.setRGB(px - 1, py, replacementRGB);
                q.add(new Point(px - 1, py));
            }
        }
        return m;
    }
}