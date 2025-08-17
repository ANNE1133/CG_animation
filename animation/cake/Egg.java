package cake;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


public class Egg extends JPanel {
    // Colors
    private final Color eggOutline = new Color(91, 33, 20);
    private final Color eggFill = new Color(253, 217, 146);
    private final Color eggInside = new Color(251, 211, 140);
    private final Color eggDetail = new Color(235, 165, 76);
    private final Color eggBack = new Color(203, 156, 102);
    private final Color eggLine = new Color(255, 232, 159);
    
    // Animation
    private final long startTime;
    private final List<Point[]> crackSegments;
    private Cat cat;
    private final GraphicsUtils graphicsUtils;
    
    public Egg() {
        setPreferredSize(new Dimension(600, 600));
        setOpaque(false);
        this.startTime = System.currentTimeMillis();
        this.crackSegments = initCrackSegments();
        this.cat = new Cat();
        this.graphicsUtils = new GraphicsUtils();
        
        Timer timer = new Timer(30, e -> repaint());
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        BufferedImage buffer = new BufferedImage(601, 601, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = buffer.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        long elapsed = System.currentTimeMillis() - startTime;
        AnimationPhase phase = getCurrentPhase(elapsed);
        
        switch (phase) {
            case SWINGING:
                drawWholeEgg(g2, buffer, getSwingAngle(elapsed));
                break;
            case CRACKING:
                drawCrackedEgg(g2, elapsed, buffer, getSwingAngle(elapsed));
                break;
            case OPENING:
                drawBackEgg(g2, buffer);
                cat.drawCatEmerging(g2, elapsed, buffer);
                drawBottomHalfEggOverlay(g2, buffer);
                drawOpenedEgg(g2, elapsed, buffer);
                break;
            case CAT_EMERGED:
                drawBackEgg(g2, buffer);
                cat.drawCatEmerging(g2, elapsed, buffer);
                drawBottomHalfEggOverlay(g2, buffer);
                cat.drawHands(g2, buffer);
                break;
        }
        
        g2.dispose();
        g2d.drawImage(buffer, 0, 0, null);
    }
    
    private AnimationPhase getCurrentPhase(long elapsed) {
        if (elapsed < 1000) return AnimationPhase.SWINGING;
        if (elapsed < 5000) return AnimationPhase.CRACKING;
        if (elapsed < 8000) return AnimationPhase.OPENING;
        return AnimationPhase.CAT_EMERGED;
    }
    
    private double getSwingAngle(long elapsed) {
        if (elapsed < 3900) {
            return Math.sin(elapsed * 0.003) * Math.PI / 20;
        }
        return 0;
    }

    public void drawWholeEgg(Graphics2D g, BufferedImage buffer, double angle) {
        int pivotX = 305;
        int pivotY = 490;
        
        AffineTransform originalTransform = g.getTransform();
        g.rotate(angle, pivotX, pivotY);
        
        // Create egg shape using Polygon
        Polygon eggPolygon = createEggPolygon();
        
        // Fill egg using Polygon
        g.setColor(eggFill);
        g.fillPolygon(eggPolygon);

        shadowEggCustom(g, buffer);
        
        // Draw outline using Polygon
        g.setColor(eggOutline);
        // g.drawPolygon(eggPolygon);
        graphicsUtils.drawThickPolygonOutline(g, eggPolygon, 2);
        
        g.setTransform(originalTransform);
    }
        
    private Polygon createEggPolygon() {
        Polygon polygon = new Polygon();
        
        // Create egg shape using bezier curves and convert to polygon points
        List<Point> points = new ArrayList<>();
        
        // Add points from bezier curves
        addBezierPointsToList(points, new Point(237, 214), new Point(299, 151), new Point(372, 213));
        addBezierPointsToList(points, new Point(372, 213), new Point(415, 250), new Point(430, 304));
        addBezierPointsToList(points, new Point(430, 304), new Point(446, 356), new Point(439, 396));
        addBezierPointsToList(points, new Point(439, 396), new Point(436, 447), new Point(386, 489));
        addBezierPointsToList(points, new Point(386, 489), new Point(305, 545), new Point(229, 493));
        addBezierPointsToList(points, new Point(229, 493), new Point(173, 455), new Point(168, 395));
        addBezierPointsToList(points, new Point(168, 395), new Point(154, 283), new Point(237, 214));
        
        for (Point p : points) {
            polygon.addPoint(p.x, p.y);
        }
        
        return polygon;
    }

    private void addBezierPointsToList(List<Point> points, Point p0, Point p1, Point p2) {
        for (double t = 0; t <= 1; t += 0.02) {
            double x = (1-t)*(1-t)*p0.x + 2*(1-t)*t*p1.x + t*t*p2.x;
            double y = (1-t)*(1-t)*p0.y + 2*(1-t)*t*p1.y + t*t*p2.y;
            points.add(new Point((int)Math.round(x), (int)Math.round(y)));
        }
    }
    
    private void shadowEggCustom(Graphics2D g, BufferedImage buffer) {
        // Create shadow area using Polygon
        Polygon shadowPolygon = new Polygon();
        List<Point> shadowPoints = new ArrayList<>();
        
        addBezierPointsToList(shadowPoints, new Point(240, 265), new Point(228, 261), new Point(240, 240));
        addBezierPointsToList(shadowPoints, new Point(240, 240), new Point(253, 218), new Point(271, 227));
        addBezierPointsToList(shadowPoints, new Point(271, 227), new Point(265, 242), new Point(260, 256));
        addBezierPointsToList(shadowPoints, new Point(260, 256), new Point(250, 267), new Point(240, 265));
        
        for (Point p : shadowPoints) {
            shadowPolygon.addPoint(p.x, p.y);
        }
        
        // Fill shadow area using Polygon
        g.setColor(eggLine);
        g.fillPolygon(shadowPolygon);
        
        // Draw small shadow circle using midpoint algorithm
        g.setColor(eggLine);
        graphicsUtils.plotCircle(g, 231, 273, 4, 0); // Using existing midpoint circle method
    }

    private void drawCrackedEgg(Graphics2D g, long elapsed, BufferedImage buffer, double angle) {
        int pivotX = 305;
        int pivotY = 490;
        
        AffineTransform originalTransform = g.getTransform();
        g.rotate(angle, pivotX, pivotY);
        
                // Draw egg shape using Polygon
        Polygon eggPolygon = createEggPolygon();
        g.setColor(eggFill);
        g.fillPolygon(eggPolygon);
        
        shadowEggCustom(g, buffer);
        
        g.setColor(eggOutline);
        // g.drawPolygon(eggPolygon);
        graphicsUtils.drawThickPolygonOutline(g, eggPolygon, 2);

        drawCracks(g, elapsed);
        
        g.setTransform(originalTransform);
    }
    
    private void drawOpenedEgg(Graphics2D g, long elapsed, BufferedImage buffer) {
        int moveAmount = (int) Math.min((elapsed - 5000) / 5.0, 400);

        BufferedImage topBuffer = new BufferedImage(601, 601, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gTop = topBuffer.createGraphics();
        gTop.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawTopHalfEgg(gTop, topBuffer);
        gTop.dispose();
        
        g.drawImage(topBuffer, 0, -moveAmount, null);
    }
    
    private void drawBottomHalfEggOverlay(Graphics2D g, BufferedImage buffer) {
        BufferedImage bottomBuffer = new BufferedImage(601, 601, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gBottom = bottomBuffer.createGraphics();
        gBottom.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawBottomHalfEgg(gBottom, bottomBuffer);
        gBottom.dispose();
        
        g.drawImage(bottomBuffer, 0, 0, null);
    }
    
    private void drawBackEgg(Graphics2D g, BufferedImage buffer) {
        // g.setColor(eggOutline);
        // g.setStroke(new BasicStroke(3));

        // drawCracks(g, Long.MAX_VALUE);
        
        // graphicsUtils.quadraticBezier(g, new Point(181, 297), new Point(194, 320), new Point(200, 328));
        // graphicsUtils.quadraticBezier(g, new Point(199, 328), new Point(226, 301), new Point(241, 299));
        // graphicsUtils.quadraticBezier(g, new Point(425, 291), new Point(399, 315), new Point(395, 324));
        // graphicsUtils.quadraticBezier(g, new Point(395, 324), new Point(373, 296), new Point(347, 297));
        // g.drawLine(240, 299, 348, 297);
        
        // graphicsUtils.floodFill(buffer, 390, 332, Color.WHITE, eggBack);
        Polygon backEggPolygon = createBackEggPolygon();
        
        // Fill back egg using Polygon
        g.setColor(eggBack);
        g.fillPolygon(backEggPolygon);
        
        // Draw cracks
        drawCracks(g, Long.MAX_VALUE);
        
        // Draw outline using Polygon
        g.setColor(eggOutline);
        // g.drawPolygon(backEggPolygon);
        graphicsUtils.drawThickPolygonOutline(g, backEggPolygon, 2);
    }

    private Polygon createBackEggPolygon() {
        Polygon polygon = new Polygon();
        List<Point> points = new ArrayList<>();
        
        // Convert the complex back egg path to discrete points using bezier
        addBezierPointsToList(points, new Point(314, 440), new Point(148, 490), new Point(181, 297));
        points.add(new Point(200, 328));
        addBezierPointsToList(points, new Point(199, 328), new Point(226, 301), new Point(241, 299));
        points.add(new Point(240, 299));
        points.add(new Point(348, 297));
        points.add(new Point(347, 297));
        addBezierPointsToList(points, new Point(347, 297), new Point(373, 296), new Point(395, 324));
        addBezierPointsToList(points, new Point(395, 324), new Point(399, 315), new Point(425, 291));
        points.add(new Point(425, 292));
        addBezierPointsToList(points, new Point(425, 292), new Point(449, 436), new Point(313, 441));
        points.add(new Point(314, 440));
        
        for (Point p : points) {
            polygon.addPoint(p.x, p.y);
        }
        
        return polygon;
    }
    
    private void drawCracks(Graphics2D g, long elapsed) {
        int totalTime = 3000;
        int segmentsToShow = (int)((elapsed - 1000) / (float)totalTime * crackSegments.size());
        segmentsToShow = Math.min(segmentsToShow, crackSegments.size());

        g.setColor(eggOutline);
        for (int i = 0; i < segmentsToShow; i++) {
            Point[] seg = crackSegments.get(i);
            // Use Bresenham line algorithm from GraphicsUtils
            graphicsUtils.bresenhamLine(g, seg[0].x, seg[0].y, seg[1].x, seg[1].y);
        }
    }
    
    private void drawEggOutline(Graphics2D g) {
        g.setColor(eggOutline);
        g.setStroke(new BasicStroke(3));
        
        graphicsUtils.quadraticBezier(g, new Point(237, 214), new Point(299, 151), new Point(372, 213));
        graphicsUtils.quadraticBezier(g, new Point(371, 213), new Point(415, 250), new Point(430, 304));
        graphicsUtils.quadraticBezier(g, new Point(429, 304), new Point(446, 356), new Point(439, 396));
        graphicsUtils.quadraticBezier(g, new Point(439, 395), new Point(436, 447), new Point(386, 489));
        graphicsUtils.quadraticBezier(g, new Point(229, 493), new Point(305, 545), new Point(387, 488));
        graphicsUtils.quadraticBezier(g, new Point(168, 395), new Point(173, 455), new Point(229, 493));
        graphicsUtils.quadraticBezier(g, new Point(237, 214), new Point(154, 283), new Point(168, 396));
    }
    
    // private void drawCracks(Graphics2D g, long elapsed) {
    //     int totalTime = 3000;
    //     int segmentsToShow = (int)((elapsed - 1000) / (float)totalTime * crackSegments.size());
    //     segmentsToShow = Math.min(segmentsToShow, crackSegments.size());

    //     g.setColor(eggOutline);
    //     g.setStroke(new BasicStroke(3));
    //     for (int i = 0; i < segmentsToShow; i++) {
    //         Point[] seg = crackSegments.get(i);
    //         g.drawLine(seg[0].x, seg[0].y, seg[1].x, seg[1].y);
    //     }
    // }
    
    private void drawTopHalfEgg(Graphics2D g, BufferedImage buffer) {
        g.setColor(eggOutline);
        // g.setStroke(new BasicStroke(3));
        
        graphicsUtils.quadraticBezier(g, new Point(237, 214), new Point(299, 151), new Point(372, 213));
        graphicsUtils.quadraticBezier(g, new Point(237, 214), new Point(202, 242), new Point(181, 296));
        graphicsUtils.quadraticBezier(g, new Point(371, 213), new Point(408, 245), new Point(425, 291));
        
        drawCracks(g, Long.MAX_VALUE);
        graphicsUtils.floodFill(buffer, 300, 250, new Color(0, 0, 0, 0), eggFill);
        
        shadowEggCustom(g, buffer);
    }

    
    private void drawBottomHalfEgg(Graphics2D g, BufferedImage buffer) {
        g.setColor(eggOutline);
        // g.setStroke(new BasicStroke(3));
        
        graphicsUtils.quadraticBezier(g, new Point(229, 493), new Point(305, 545), new Point(387, 488));
        graphicsUtils.quadraticBezier(g, new Point(168, 395), new Point(173, 455), new Point(229, 493));
        graphicsUtils.quadraticBezier(g, new Point(439, 395), new Point(436, 447), new Point(386, 489));
        graphicsUtils.quadraticBezier(g, new Point(181, 296), new Point(161, 333), new Point(168, 396));
        graphicsUtils.quadraticBezier(g, new Point(429, 304), new Point(427, 294), new Point(425, 291));
        graphicsUtils.quadraticBezier(g, new Point(429, 304), new Point(446, 356), new Point(439, 396));
        
        drawCracks(g, Long.MAX_VALUE);
        
        // g.setColor(eggInside);
        // graphicsUtils.quadraticBezier(g, new Point(174, 419), new Point(303, 536), new Point(364, 382));
        // graphicsUtils.floodFill(buffer, 177, 381, new Color(0, 0, 0, 0), eggInside);

        // drawEggDetails(g);
        // graphicsUtils.floodFill(buffer, 191, 445, new Color(0, 0, 0, 0), eggFill);
        g.setColor(eggInside);
        graphicsUtils.quadraticBezier(g, new Point(174, 419), new Point(303, 536), new Point(364, 382));
        graphicsUtils.floodFill(buffer, 177, 381, new Color(0, 0, 0, 0), eggInside);
        graphicsUtils.floodFill(buffer, 191, 445, new Color(0, 0, 0, 0), eggFill);
        
        drawEggDetails(g);
    }
    
    private void drawEggDetails(Graphics2D g) {
        g.setColor(eggDetail);
        g.setStroke(new BasicStroke(3));
        graphicsUtils.bresenhamLine(g, 261, 390, 258, 408);
        graphicsUtils.bresenhamLine(g, 244, 421, 259, 403);
        graphicsUtils.bresenhamLine(g, 249, 415, 258, 438);
        graphicsUtils.quadraticBezier(g, new Point(365, 385), new Point(369, 410), new Point(356, 427));
        graphicsUtils.bresenhamLine(g, 365, 402, 377, 408);
        graphicsUtils.bresenhamLine(g, 377, 407, 382, 423);
        graphicsUtils.quadraticBezier(g, new Point(356, 427), new Point(360, 431), new Point(360, 443));
    }
    
    private List<Point[]> initCrackSegments() {
        List<Point[]> segments = new ArrayList<>();
        
        segments.add(new Point[]{new Point(181, 296), new Point(195, 350)});
        segments.add(new Point[]{new Point(194, 350), new Point(231, 338)});
        segments.add(new Point[]{new Point(230, 338), new Point(261, 389)});
        segments.add(new Point[]{new Point(261, 389), new Point(318, 361)});
        segments.add(new Point[]{new Point(318, 361), new Point(364, 383)});
        segments.add(new Point[]{new Point(364, 383), new Point(371, 373)});
        segments.add(new Point[]{new Point(370, 373), new Point(381, 372)});
        segments.add(new Point[]{new Point(380, 373), new Point(395, 344)});
        segments.add(new Point[]{new Point(394, 344), new Point(413, 351)});
        segments.add(new Point[]{new Point(425, 291), new Point(412, 351)});
        
        return segments;
    }

    private enum AnimationPhase {
        SWINGING, CRACKING, OPENING, CAT_EMERGED
    }
}
