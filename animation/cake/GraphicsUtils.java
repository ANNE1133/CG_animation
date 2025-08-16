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

    private void bresenhamLine(Graphics g, int x1, int y1, int x2, int y2) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);

        int sx = (x1 < x2) ? 1 : -1;
        int sy = (y1 < y2) ? 1 : -1;
        boolean isSwap = false;


        if (dy > dx) {
            int temp = dx;
            dx = dy;
            dy = temp;
            isSwap = true;
        }

        int D = 2 * dy - dx;

        int y = y1;
        int x = x1;

        for (int i = 0; i < dx; i++) {
            plot(g, x, y, 2);
            if (D >= 0) {
                if (isSwap) {
                    x+= sx;
                } else {
                    y += sy;
                }

                D -= 2 * dx;
            }
            if (isSwap) {
                y += sy;
            } else {
                x += sx;
            }
            D += 2 * dy;
        }
    }
        private void midpointCircle(Graphics g, int xc, int yc, int r) {
        int x = 0;
        int y = r;
        int Dx = 2 * x;
        int Dy = 2 * y;
        int D = 1 - r;

        while (x <= y) {
            plotCircle(g, xc, yc, x, y);

            x++;
            Dx = Dx + 2;
            D = D + Dx + 1;

            if (D >= 0){
                y = y - 1;
                Dy = Dy - 2;
                D = D - Dy;
            }
        }
    }

    private void midpointEllipse(Graphics g, int xc, int yc, int a, int b) {
        int a2 = a*a, b2 = b*b;
        int twoA2 = 2*a2, twoB2 = 2*b2;

        //Region1
        int x = 0;
        int y = b;
        int D = Math.round(b2 - (a2*b) + (a2/4));

        while (b2*x <= a2*y) {
            plotEllipse(g, xc, yc, x, y);

            x++;
            D = D + (twoB2*x) + b2;

            if (D >= 0){
                y = y - 1;
                D = D - (twoA2*y);
            }
        }

        //Region2
        x = a;
        y = 0;
        D = Math.round(a2 - (b2*a) + (b2/4));

        while (b2*x >= a2*y) {
            plotEllipse(g, xc, yc, x, y);

            y++;
            D = D + (twoA2*y) + a2;

            if (D >= 0){
                x = x - 1;
                D = D - (twoB2*x);
            }
        }
    }

    public void plotCircle(Graphics g, int xc, int yc, int x, int y) {
        plot(g, xc + x, yc + y,3);
        plot(g, xc + y, yc + x,3);
        plot(g, xc + y, yc - x,3);
        plot(g, xc + x, yc - y,3);
        plot(g, xc - x, yc - y,3);
        plot(g, xc - y, yc - x,3);
        plot(g, xc - y, yc + x,3);
        plot(g, xc - x, yc + y,3); 
    }

        public void plotEllipse(Graphics g, int xc, int yc, int x, int y) {
        plot(g, xc + x, yc + y,3);
        plot(g, xc + x, yc - y,3);
        plot(g, xc - x, yc - y,3);
        plot(g, xc - x, yc + y,3); 
    }
}