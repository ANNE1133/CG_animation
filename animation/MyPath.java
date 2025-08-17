package finalVer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class MyPath {
	private BufferedImage cachedFill = null;
	private Color lastFillColor = null;
	private Color lastOutlineColor = null;
	private Color lastBackgroundColor = null;
	private boolean pathChanged = true;
	private int lastMinX = 0, lastMinY = 0, lastMaxX = 0, lastMaxY = 0;
	
    private static class PathSegmentCommand {
        enum CommandType {
            MOVE_TO, LINE_TO, QUAD_TO, CURVE_TO, CLOSE_PATH
        }

        CommandType type;
        Point p1, p2, p3, p4;

        PathSegmentCommand(CommandType type, Point p1) {
            this.type = type;
            this.p1 = p1;
        }

        PathSegmentCommand(CommandType type, Point p1, Point p2) {
            this.type = type;
            this.p1 = p1;
            this.p2 = p2;
        }

        PathSegmentCommand(CommandType type, Point p1, Point p2, Point p3) {
            this.type = type;
            this.p1 = p1;
            this.p2 = p2;
            this.p3 = p3;
        }

        PathSegmentCommand(CommandType type, Point p1, Point p2, Point p3, Point p4) {
            this.type = type;
            this.p1 = p1;
            this.p2 = p2;
            this.p3 = p3;
            this.p4 = p4;
        }
    }

    private Point current;
    private Point subpathStart;
    private List<PathSegmentCommand> commands = new ArrayList<>();
    private GraphicsUtils utils = new GraphicsUtils();
    private Point firstPointInPath = null;

	private void invalidateCache() {
		pathChanged = true;
		if (cachedFill != null) {
			cachedFill.flush();
			cachedFill = null;
		}
	}
	public void moveTo(int x, int y) {
		current = new Point(x, y);
		subpathStart = current;
		if (firstPointInPath == null) {
			firstPointInPath = current;
		}
		commands.add(new PathSegmentCommand(PathSegmentCommand.CommandType.MOVE_TO, current));
		invalidateCache(); // Add this line
	}
    public void lineTo(int x, int y) {
        if (current == null) {
            throw new IllegalStateException("moveTo() must be called before lineTo().");
        }
        Point start = current;
        Point end = new Point(x, y);
        commands.add(new PathSegmentCommand(PathSegmentCommand.CommandType.LINE_TO, start, end));
        current = end;
		invalidateCache();
    }

    public void quadTo(int cx, int cy, int x, int y) {
        if (current == null) {
            throw new IllegalStateException("moveTo() must be called before quadTo().");
        }
        Point start = current;
        Point control = new Point(cx, cy);
        Point end = new Point(x, y);
        commands.add(new PathSegmentCommand(PathSegmentCommand.CommandType.QUAD_TO, start, control, end));
        current = end;
		invalidateCache();
    }

    public void curveTo(int cx1, int cy1, int cx2, int cy2, int x, int y) {
        if (current == null) {
            throw new IllegalStateException("moveTo() must be called before curveTo().");
        }
        Point start = current;
        Point c1 = new Point(cx1, cy1);
        Point c2 = new Point(cx2, cy2);
        Point end = new Point(x, y);
        commands.add(new PathSegmentCommand(PathSegmentCommand.CommandType.CURVE_TO, start, c1, c2, end));
        current = end;
		invalidateCache();
    }

    public void closePath() {
        if (current == null || subpathStart == null) {
            throw new IllegalStateException("Cannot close path: moveTo() must be called first to start a subpath.");
        }
        if (!current.equals(subpathStart)) {
            commands.add(new PathSegmentCommand(PathSegmentCommand.CommandType.CLOSE_PATH, current, subpathStart));
            current = subpathStart;
        }
		invalidateCache();
    }

    public void drawAll(Graphics g, int thickness) {
        Point currentDrawingPoint = null; // Initialize to null
        Point currentSubpathStart = null;

        for (PathSegmentCommand cmd : commands) {
            switch (cmd.type) {
                case MOVE_TO:
                    currentDrawingPoint = cmd.p1;
                    currentSubpathStart = currentDrawingPoint;
                    break;
                case LINE_TO:
                    if (currentDrawingPoint == null) { // Handle case if path starts with lineTo (though forbidden by current design)
                        throw new IllegalStateException("Path must start with moveTo before drawing lineTo.");
                    }
                    utils.drawThickLine(g, currentDrawingPoint.x, currentDrawingPoint.y, cmd.p2.x, cmd.p2.y, thickness);
                    currentDrawingPoint = cmd.p2;
                    break;
                case QUAD_TO:
                    if (currentDrawingPoint == null) {
                        throw new IllegalStateException("Path must start with moveTo before drawing quadTo.");
                    }
                    utils.quadraticThickBezier(g, currentDrawingPoint, cmd.p2, cmd.p3, thickness);
                    currentDrawingPoint = cmd.p3;
                    break;
                case CURVE_TO:
                    if (currentDrawingPoint == null) {
                        throw new IllegalStateException("Path must start with moveTo before drawing curveTo.");
                    }
                    utils.drawThickBezier(g, currentDrawingPoint, cmd.p2, cmd.p3, cmd.p4, thickness);
                    currentDrawingPoint = cmd.p4;
                    break;
                case CLOSE_PATH:
                    if (currentDrawingPoint != null && currentSubpathStart != null &&
                        !currentDrawingPoint.equals(currentSubpathStart)) {
                        utils.drawThickLine(g, currentDrawingPoint.x, currentDrawingPoint.y,
                                            currentSubpathStart.x, currentSubpathStart.y, thickness);
                    }
                    currentDrawingPoint = currentSubpathStart;
                    break;
            }
        }
    }

    public void append(MyPath other, boolean connect) {
        // Ensure that other's points are properly retrieved
        Point otherFirstPoint = other.getFirstPoint(); 
        Point otherCurrent = other.getCurrent();     

        if (connect && this.current != null && otherFirstPoint != null) {
            commands.add(new PathSegmentCommand(PathSegmentCommand.CommandType.LINE_TO, this.current, otherFirstPoint));
        }
        this.commands.addAll(other.commands);
        this.current = otherCurrent; // Update with the correct current point from 'other'
        if (this.firstPointInPath == null && otherFirstPoint != null) {
            this.firstPointInPath = otherFirstPoint;
        }
        if (this.subpathStart == null && other.subpathStart != null) {
            this.subpathStart = other.subpathStart;
        } else if (other.subpathStart != null && other.commands.size() > 0 &&
                   other.commands.get(0).type == PathSegmentCommand.CommandType.MOVE_TO) {
            this.subpathStart = other.subpathStart;
        }
		invalidateCache();
    }

    public void append(Shape s, boolean connect) {
        PathIterator pi = s.getPathIterator(null);
        double[] coords = new double[6];
        Point2D lastSegPoint = null;
        Point2D firstSegPointOfAppendedShapeSubpath = null;

        while (!pi.isDone()) {
            int segType = pi.currentSegment(coords);

            switch (segType) {
                case PathIterator.SEG_MOVETO:
                    if (firstSegPointOfAppendedShapeSubpath == null) {
                        firstSegPointOfAppendedShapeSubpath = new Point2D.Double(coords[0], coords[1]);
                    }

                    if (connect && this.current != null) {
                        commands.add(new PathSegmentCommand(PathSegmentCommand.CommandType.LINE_TO,
                                                            this.current,
                                                            new Point((int)coords[0], (int)coords[1])));
                        connect = false;
                    }
                    this.moveTo((int) coords[0], (int) coords[1]);
                    lastSegPoint = new Point2D.Double(coords[0], coords[1]);
                    firstSegPointOfAppendedShapeSubpath = lastSegPoint;
                    break;

                case PathIterator.SEG_LINETO:
                    this.lineTo((int) coords[0], (int) coords[1]);
                    lastSegPoint = new Point2D.Double(coords[0], coords[1]);
                    break;

                case PathIterator.SEG_QUADTO:
                    this.quadTo((int) coords[0], (int) coords[1], (int) coords[2], (int) coords[3]);
                    lastSegPoint = new Point2D.Double(coords[2], coords[3]);
                    break;

                case PathIterator.SEG_CUBICTO:
                    this.curveTo((int) coords[0], (int) coords[1], (int) coords[2], (int) coords[3], (int) coords[4], (int) coords[5]);
                    lastSegPoint = new Point2D.Double(coords[4], coords[5]);
                    break;

                case PathIterator.SEG_CLOSE:
                    if (firstSegPointOfAppendedShapeSubpath != null) {
                         lastSegPoint = firstSegPointOfAppendedShapeSubpath;
                    }
                    break;
            }
            pi.next();
        }

        if (lastSegPoint != null) {
            this.current = new Point((int)lastSegPoint.getX(), (int)lastSegPoint.getY());
        }

        if (this.firstPointInPath == null && !commands.isEmpty() && commands.get(0).type == PathSegmentCommand.CommandType.MOVE_TO) {
            this.firstPointInPath = commands.get(0).p1;
        }
      
        if (firstSegPointOfAppendedShapeSubpath != null && !s.getPathIterator(null).isDone()) {
             PathIterator re_pi = s.getPathIterator(null);
             double[] re_coords = new double[6];
             if (!re_pi.isDone() && re_pi.currentSegment(re_coords) == PathIterator.SEG_MOVETO) {
                 this.subpathStart = new Point((int)re_coords[0], (int)re_coords[1]);
             }
        }invalidateCache();
    }

    public Point getFirstPoint() {
        return firstPointInPath;
    }

    public Point getCurrent() {
        return current;
    }
	public void fillShapeWithPaint(Graphics2D g2d, Shape shape, Paint paint) {
        // บันทึก Paint เดิม
        Paint originalPaint = g2d.getPaint();

        // กำหนด Paint ใหม่ (gradient)
        g2d.setPaint(paint);

        // วาดและเติมรูปทรงด้วย Paint ที่กำหนด
        g2d.fill(shape);

        // คืนค่า Paint เดิม
        g2d.setPaint(originalPaint);
    }
	    // --- NEW fillPath method using Flood Fill ---
    public void fillPath(Graphics g, Color fillColor, Color outlineColor, Color backgroundColor) {
        boolean needsRegeneration = pathChanged || 
                               cachedFill == null ||
                               !fillColor.equals(lastFillColor) || 
                               !outlineColor.equals(lastOutlineColor) ||
                               !backgroundColor.equals(lastBackgroundColor);

    	if (needsRegeneration) {
        // Calculate bounds (same logic as your original method)
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;

        Point currentBoundsPoint = null;
        for (PathSegmentCommand cmd : commands) {
            switch (cmd.type) {
                case MOVE_TO:
                    currentBoundsPoint = cmd.p1;
                    break;
                case LINE_TO:
                    currentBoundsPoint = cmd.p2;
                    break;
                case QUAD_TO:
                    currentBoundsPoint = cmd.p3;
                    break;
                case CURVE_TO:
                    currentBoundsPoint = cmd.p4;
                    break;
                case CLOSE_PATH:
                    if (cmd.p2 != null) currentBoundsPoint = cmd.p2;
                    break;
            }
            if (currentBoundsPoint != null) {
                minX = Math.min(minX, currentBoundsPoint.x);
                minY = Math.min(minY, currentBoundsPoint.y);
                maxX = Math.max(maxX, currentBoundsPoint.x);
                maxY = Math.max(maxY, currentBoundsPoint.y);
            }
        }

        if (minX == Integer.MAX_VALUE) {
            return; // Empty path
        }

        int padding = 5;
        minX = Math.max(0, minX - padding);
        minY = Math.max(0, minY - padding);
        maxX += padding;
        maxY += padding;

        int imageWidth = maxX - minX + 1;
        int imageHeight = maxY - minY + 1;

        if (imageWidth <= 0 || imageHeight <= 0) {
            System.err.println("Cannot fill path: Image dimensions are invalid.");
            return;
        }

        // Create and render the cached image
        cachedFill = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2dTemp = cachedFill.createGraphics();

        // Fill background
        g2dTemp.setColor(backgroundColor);
        utils.myFillRect(g2dTemp, 0, 0, imageWidth, imageHeight);
        
        // Translate for drawing
        g2dTemp.translate(-minX, -minY);
        
        // Draw outline
        g2dTemp.setColor(outlineColor);
        drawAll(g2dTemp, 3);

        // Find seed point and flood fill
        int centerX = (minX + maxX) / 2;
        int centerY = (minY + maxY) / 2;
        int relativeCenterX = centerX - minX;
        int relativeCenterY = centerY - minY;
        
        boolean seedFound = false;
        int finalSeedX = -1;
        int finalSeedY = -1;
        int[] dx = {0, 3, -3, 0, 0}; 
        int[] dy = {0, 0, 0, 3, -3};

        for (int i = 0; i < dx.length; i++) {
            int currentSeedX = relativeCenterX + dx[i];
            int currentSeedY = relativeCenterY + dy[i];

            if (currentSeedX >= 0 && currentSeedX < imageWidth &&
                currentSeedY >= 0 && currentSeedY < imageHeight) {
                if (cachedFill.getRGB(currentSeedX, currentSeedY) == backgroundColor.getRGB()) {
                    finalSeedX = currentSeedX;
                    finalSeedY = currentSeedY;
                    seedFound = true;
                    break;
                }
            }
        }

        if (seedFound) {
            utils.floodFill(cachedFill, finalSeedX, finalSeedY, backgroundColor, fillColor);
        }

        g2dTemp.dispose();

        // Update cache state
        lastFillColor = fillColor;
        lastOutlineColor = outlineColor;
        lastBackgroundColor = backgroundColor;
        lastMinX = minX;
        lastMinY = minY;
        lastMaxX = maxX;
        lastMaxY = maxY;
        pathChanged = false;
		// Draw the cached image
		if (cachedFill != null) {
			((Graphics2D) g).drawImage(cachedFill, lastMinX, lastMinY, null);
		}
	}
}}