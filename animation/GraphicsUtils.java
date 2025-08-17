package finalVer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;

public class GraphicsUtils {

    // --- Core plot method: draws a single pixel (1x1) ---
    private void plot(Graphics g, int x, int y) {
        g.fillRect(x, y, 1, 1);
    }

    // --- Overloaded plot method: draws with specified thickness ---
    private void plot(Graphics g, int x, int y, int thickness) {
        g.fillRect(x, y, thickness, thickness);
    }

    // ... (Your existing methods: quadraticBezier, quadraticThickBezier, drawBezier, drawThickBezier, floodFill) ...
    public void quadraticBezier(Graphics g, Point p1, Point p2, Point p3) {
        for (int i = 0; i <= 1000; i++) {
            double t = i / 1000.0;
            double x = Math.pow(1 - t, 2) * p1.x + 2 * (1 - t) * t * p2.x + t * t * p3.x;
            double y = Math.pow(1 - t, 2) * p1.y + 2 * (1 - t) * t * p2.y + t * t * p3.y;
            plot(g, (int) x, (int) y,3); // Uses default 1-pixel plot
        }
    }


    public void drawBezier(Graphics g, Point p0, Point p1, Point p2, Point p3) {
        for (int i = 0; i <= 1000; i++) {
            double t = i / 1000.0;
            double x = Math.pow(1 - t, 3) * p0.x + 3 * t * Math.pow(1 - t, 2) * p1.x + 3 * Math.pow(t, 2) * (1 - t) * p2.x + Math.pow(t, 3) * p3.x;
            double y = Math.pow(1 - t, 3) * p0.y + 3 * t * Math.pow(1 - t, 2) * p1.y + 3 * Math.pow(t, 2) * (1 - t) * p2.y + Math.pow(t, 3) * p3.y;
            plot(g, (int) x, (int) y,3); // Uses default 1-pixel plot
        }
    }

    public void quadraticThickBezier(Graphics g, Point p1, Point p2, Point p3, int thickness) {
    // คำนวณความยาวโดยประมาณของเส้นโค้ง
    double approxLength = Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2)) +
                         Math.sqrt(Math.pow(p3.x - p2.x, 2) + Math.pow(p3.y - p2.y, 2));
    
    // กำหนดจำนวน steps ตามความยาวและความหนา
    int steps = Math.max(20, Math.min(500, (int)(approxLength / Math.max(1, thickness / 2))));
    
    Point prevPoint = null;
    
    for (int i = 0; i <= steps; i++) {
        double t = (double) i / steps;
        
        // คำนวณจุดบนเส้นโค้ง
        double x = Math.pow(1 - t, 2) * p1.x + 2 * (1 - t) * t * p2.x + t * t * p3.x;
        double y = Math.pow(1 - t, 2) * p1.y + 2 * (1 - t) * t * p2.y + t * t * p3.y;
        
        Point currentPoint = new Point((int) x, (int) y);
        
        // วาดเส้นเชื่อมต่อระหว่างจุดแทนการวาดจุดเดี่ยว
        if (prevPoint != null) {
            drawThickLine(g, prevPoint.x, prevPoint.y, currentPoint.x, currentPoint.y, thickness);
        } else {
            // จุดแรก
            plot(g, currentPoint.x, currentPoint.y, thickness);
        }
        
        prevPoint = currentPoint;
    }
}

public void drawThickBezier(Graphics g, Point p0, Point p1, Point p2, Point p3, int thickness) {
    // คำนวณความยาวโดยประมาณ
    double approxLength = Math.sqrt(Math.pow(p1.x - p0.x, 2) + Math.pow(p1.y - p0.y, 2)) +
                         Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2)) +
                         Math.sqrt(Math.pow(p3.x - p2.x, 2) + Math.pow(p3.y - p2.y, 2));
    
    // กำหนดจำนวน steps แบบ adaptive
    int steps = Math.max(20, Math.min(500, (int)(approxLength / Math.max(1, thickness / 2))));
    
    Point prevPoint = null;
    
    for (int i = 0; i <= steps; i++) {
        double t = (double) i / steps;
        
        // คำนวณจุดบน cubic bezier
        double x = Math.pow(1 - t, 3) * p0.x + 
                   3 * t * Math.pow(1 - t, 2) * p1.x + 
                   3 * Math.pow(t, 2) * (1 - t) * p2.x + 
                   Math.pow(t, 3) * p3.x;
        double y = Math.pow(1 - t, 3) * p0.y + 
                   3 * t * Math.pow(1 - t, 2) * p1.y + 
                   3 * Math.pow(t, 2) * (1 - t) * p2.y + 
                   Math.pow(t, 3) * p3.y;
        
        Point currentPoint = new Point((int) x, (int) y);
        
        // วาดเส้นเชื่อมต่อ
        if (prevPoint != null) {
            drawThickLine(g, prevPoint.x, prevPoint.y, currentPoint.x, currentPoint.y, thickness);
        } else {
            // จุดแรก
            plot(g, currentPoint.x, currentPoint.y, thickness);
        }
        
        prevPoint = currentPoint;
    }
}

    // Flood fill method is unchanged
    public BufferedImage floodFill(BufferedImage m, int x, int y, Color target, Color replacement) {
        int targetRGB = target.getRGB();
        int replacementRGB = replacement.getRGB();

        if (x < 0 || x >= m.getWidth() || y < 0 || y >= m.getHeight() || m.getRGB(x, y) != targetRGB || targetRGB == replacementRGB)
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
		// Make sure this method has access to your `utils` object
    public void bresenhamLine(Graphics g, int x1, int y1, int x2, int y2) {
        // bresenhamLine should typically draw 1-pixel lines unless it's a "thick line" function itself.
        // If this is meant to be a fundamental 1-pixel line, use plot(g,x,y)
        // If this is meant to *always* draw with thickness 3, then leave plot(g,x,y,3)
        // For the purposes of making fillEllipse work correctly, this should ideally be 1-pixel lines.
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

        for (int i = 0; i <= dx; i++) {
            plot(g, x, y); // Calls the 1-pixel plot for basic line segments
            if (D >= 0) {
                if (isSwap) {
                    x += sx;
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

    // midpointCircle outline methods (unchanged, will now use 1-pixel plot)
    void midpointCircle(Graphics g, int xc, int yc, int r) {
        midpointCircle(g, xc, yc, r, Color.BLACK);
    }

    public void midpointCircle(Graphics g, int xc, int yc, int r, Color circleColor) {
        g.setColor(circleColor);
        int x = 0;
        int y = r;
        int D = 1 - r;

        while (x <= y) {
            plotCircle(g, xc, yc, x, y, circleColor);
            x++;
            if (D < 0) {
                D = D + 2 * x + 1;
            } else {
                y--;
                D = D + 2 * x + 1 - 2 * y;
            }
        }
    }

    // --- MODIFIED: midpointEllipse to accept thickness ---
    void midpointEllipse(Graphics g, int xc, int yc, int a, int b) {
        midpointEllipse(g, xc, yc, a, b, Color.black, 1); // Default to black and 1-pixel thickness
    }

    // New overloaded midpointEllipse with thickness
    void midpointEllipse(Graphics g, int xc, int yc, int a, int b, Color ellipseColor) {
        midpointEllipse(g, xc, yc, a, b, ellipseColor, 1); // Default to 1-pixel thickness
    }

    // The main midpointEllipse method that accepts thickness
    void midpointEllipse(Graphics g, int xc, int yc, int a, int b, Color ellipseColor, int thickness) {
        int a2 = a * a, b2 = b * b;
        int twoA2 = 2 * a2, twoB2 = 2 * b2;

        // Region1
        int x = 0;
        int y = b;
        float D1 = b2 - (a2 * b) + (a2 / 4.0f);

        while (b2 * x <= a2 * y) {
            plotEllipse(g, xc, yc, x, y, ellipseColor, thickness); // Pass thickness here

            x++;
            if (D1 < 0) {
                D1 = D1 + (twoB2 * x) + b2;
            } else {
                y = y - 1;
                D1 = D1 + (twoB2 * x) + b2 - (twoA2 * y);
            }
        }

        // Region2
        x = a;
        y = 0;
        float D2 = a2 - (b2 * a) + (b2 / 4.0f);

        while (b2 * x >= a2 * y) {
            plotEllipse(g, xc, yc, x, y, ellipseColor, thickness); // Pass thickness here

            y++;
            if (D2 < 0) {
                D2 = D2 + (twoA2 * y) + a2;
            } else {
                x = x - 1;
                D2 = D2 + (twoA2 * y) + a2 - (twoB2 * x);
            }
        }
    }

    // New overloaded plotEllipse with thickness
    private void plotEllipse(Graphics g, int xc, int yc, int x, int y, Color ellipseColor, int thickness) {
        g.setColor(ellipseColor);
        plot(g, xc + x, yc + y, thickness); // Use specified thickness
        plot(g, xc + x, yc - y, thickness); // Use specified thickness
        plot(g, xc - x, yc - y, thickness); // Use specified thickness
        plot(g, xc - x, yc + y, thickness); // Use specified thickness
    }

    // Old plotEllipse (calls overloaded with default black and default thickness 1)
    public void plotEllipse(Graphics g, int xc, int yc, int x, int y) {
        plotEllipse(g, xc, yc, x, y, Color.black, 3);
    }

    // plotCircle methods (now use 1-pixel plot by default unless you want thick circles specifically)
    public void plotCircle(Graphics g, int xc, int yc, int x, int y) {
        plotCircle(g, xc, yc, x, y, Color.BLACK, 3); // Default to black and 1-pixel thickness
    }

    public void plotCircle(Graphics g, int xc, int yc, int x, int y, Color circle) {
        plotCircle(g, xc, yc, x, y, circle, 1); // Default to 1-pixel thickness
    }

    // New overloaded plotCircle with thickness
    public void plotCircle(Graphics g, int xc, int yc, int x, int y, Color circle, int thickness) {
        g.setColor(circle);
        plot(g, xc + x, yc + y, thickness);
        plot(g, xc + y, yc + x, thickness);
        plot(g, xc + y, yc - x, thickness);
        plot(g, xc + x, yc - y, thickness);
        plot(g, xc - x, yc - y, thickness);
        plot(g, xc - y, yc - x, thickness);
        plot(g, xc - y, yc + x, thickness);
        plot(g, xc - x, yc + y, thickness);
    }

    // ... (drawThickPolygonOutline, drawThickLine, myFillRect, myDrawRect - should be fine as they use bresenhamLine or drawThickLine which now use the appropriate plot calls) ...
    public void drawThickPolygonOutline(Graphics g, Polygon polygon, int thickness) {
        int[] xPoints = polygon.xpoints;
        int[] yPoints = polygon.ypoints;
        int nPoints = polygon.npoints;

        for (int i = 0; i < nPoints; i++) {
            int x1 = xPoints[i];
            int y1 = yPoints[i];
            int x2 = xPoints[(i + 1) % nPoints];
            int y2 = yPoints[(i + 1) % nPoints];

            drawThickLine(g, x1, y1, x2, y2, thickness);
        }
    }

    public void drawThickLine(Graphics g, int x1, int y1, int x2, int y2, int thickness) {
        int dx = x2 - x1;
        int dy = y2 - y1;
        double length = Math.sqrt(dx * dx + dy * dy);

        if (length == 0) return;

        double perpX = -dy / length;
        double perpY = dx / length;

        int halfThickness = thickness / 2;
        for (int i = -halfThickness; i <= halfThickness; i++) {
            int offsetX = (int) (i * perpX);
            int offsetY = (int) (i * perpY);
            bresenhamLine(g, x1 + offsetX, y1 + offsetY, x2 + offsetX, y2 + offsetY);
        }
    }

    public void myFillRect(Graphics g, int x, int y, int width, int height) {
        for (int i = y; i < y + height; i++)
            bresenhamLine(g, x, i, x + width, i);
    }

    public void myDrawRect(Graphics g, int x, int y, int width, int height, int thickness) {
        drawThickLine(g, x, y, x + width, y, thickness);
        drawThickLine(g, x, y + height, x + width, y + height, thickness);
        drawThickLine(g, x, y, x, y + height, thickness);
        drawThickLine(g, x + width, y, x + width, y + height, thickness);
    }

    // fillEllipse remains largely the same, relying on bresenhamLine drawing 1-pixel lines
    public void fillEllipse(Graphics g, int xc, int yc, int a, int b, Color c) {
        g.setColor(c);

        int a2 = a * a;
        int b2 = b * b;
        int twoA2 = 2 * a2;
        int twoB2 = 2 * b2;

        int x = 0;
        int y = b;
        float D1 = b2 - (a2 * b) + (0.25f * a2);

        while ((b2 * x) <= (a2 * y)) {
            bresenhamLine(g, xc - x, yc + y, xc + x, yc + y);
            bresenhamLine(g, xc - x, yc - y, xc + x, yc - y);

            x++;
            if (D1 < 0) {
                D1 = D1 + (twoB2 * x) + b2;
            } else {
                y--;
                D1 = D1 + (twoB2 * x) + b2 - (twoA2 * y);
            }
        }

        x = a;
        y = 0;
        float D2 = a2 - (b2 * a) + (0.25f * b2);

        while ((b2 * x) >= (a2 * y)) {
            bresenhamLine(g, xc - x, yc + y, xc + x, yc + y);
            bresenhamLine(g, xc - x, yc - y, xc + x, yc - y);

            y++;
            if (D2 < 0) {
                D2 = D2 + (twoA2 * y) + a2;
            } else {
                x--;
                D2 = D2 + (twoA2 * y) + a2 - (twoB2 * x);
            }
        }
    }

    public void fillEllipse(Graphics g, int xc, int yc, int a, int b) {
        fillEllipse(g, xc, yc, a, b, Color.black);
    }
}