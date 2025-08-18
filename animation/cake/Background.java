package cake;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.*;
import java.util.List;

public class Background extends JPanel implements ActionListener {
    //colors 
	private final Color bgColor = new Color(144, 80, 39);
    private final Color wall = new Color(58, 62, 68);
    private final Color wallLine = new Color(172, 64, 4);
	private final Color tileColor = new Color(173, 79, 15);
    private final Color groutColor = new Color(137, 58, 18);
    private final Color shadowColor = new Color(0, 0, 0, 3);
    private final Color target = new Color(0, 0, 0,0);
    private final Color glassColor = new Color(253, 206, 74);
    private final Color frameColor = new Color(254, 160, 26);
    private final Color gridColor = new Color(255, 122, 6,80);

    // Separate buffers for different elements
    private BufferedImage carpetBuffer = null;
    private BufferedImage flameBuffer = null;
    
    // Rendering flags
    private boolean carpetRendered = false;

	private final GraphicsUtils utils = new GraphicsUtils();
    private static final int W = 600, H = 600;
    final Timer timer;
    private float time = 0f;
    private final Random rnd = new Random();

    // Per-flame random flicker seeds
    private final float[] flameSeeds = new float[] { 
		rnd.nextFloat()*10f, rnd.nextFloat()*10f, 
		rnd.nextFloat()*10f, rnd.nextFloat()*10f 
	};

    public Background() {
        setPreferredSize(new Dimension(W, H));
        // setBackground(new Color(35, 32, 40)); // fallback
		setBackground(Color.darkGray);
        timer = new Timer(33, this); // ~30 fps
        timer.start();
    }
	@Override
	public void actionPerformed(ActionEvent e) {
		time += 0.05f;
		flameBuffer = null;
		repaint();
	}
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        draw(g2d);
    }

	public void draw(Graphics2D g) {
        drawWall(g); //กำแพง
        drawFloor(g); //พื้น
        drawCarpet(g); //พรม
        drawWindow(g); //หน้าต่าง
        drawCurtain(g); //ม่าน
        drawChandeliersAndSconces(g); //โคมแขวน
    }

	private void drawStrokedRect(Graphics2D g2d, int x, int y, int width, int height, Color strokeColor, int strokeWidth) {
		g2d.setColor(strokeColor);
		utils.myDrawRect(g2d, x, y, width, height,strokeWidth);
	}

	private void drawWall(Graphics2D g) {
		g.setColor(bgColor); 
		utils.myFillRect(g,0, 0, 600, 17);    
		// drawStrokedRect(g, 121, 17, 600 - 121, 405 - 17, Color.darkGray, 1);
		//upbar
		g.setColor(wallLine);
		utils.myFillRect(g,120, 17, 600 - 120, 30 - 17);    
		// drawStrokedRect(g, 121, 17, 600 - 121, 30 - 17, Color.darkGray, 1);
		//lowbar
		g.setColor(new Color(55, 61, 71));
		utils.myFillRect(g,120, 389, 600 - 120, 405 - 389);  
		drawStrokedRect(g, 120, 389, 600 - 120, 405 - 389, Color.darkGray, 1);
		//line1
		g.setColor(wallLine); 
		utils.myFillRect(g,120, 57, 600 - 120, 63 - 57);
		drawStrokedRect(g, 120, 57, 600 - 120, 63 - 57, Color.darkGray, 1);
		//line2
		g.setColor(wallLine);
		utils.myFillRect(g,120, 284, 600 - 120, 290 - 284);
		drawStrokedRect(g,120, 284, 600 - 120, 290 - 284, Color.darkGray, 1);
		//squareout
		drawStrokedRect(g,286, 79, 494 - 286, 261 - 79, wallLine, 3);
		drawStrokedRect(g,286, 305, 494 - 286, 372 - 305, wallLine, 3);
		drawStrokedRect(g,153, 305, 266 - 153, 372 - 305, wallLine, 3);
		drawStrokedRect(g,511, 305, 600 - 511, 372 - 305, wallLine, 3);

        int x = 153; int y = 79; int width = 266-x; int height = 261-y;
        int cornerDepth = 20; // How deep the curve goes in

		utils.bresenhamLine(g,x + cornerDepth, y, x + width - cornerDepth, y);
		utils.quadraticBezier(g, new Point(x + width - cornerDepth, y), new Point(x+width-cornerDepth, y+cornerDepth), new Point(x+width, y+cornerDepth));
		utils.bresenhamLine(g,x+width, y+cornerDepth,x + width, y + height - cornerDepth);
		utils.quadraticBezier(g, new Point(x + width, y + height - cornerDepth), new Point(x+width-cornerDepth, y+height-cornerDepth ), new Point(x+width-cornerDepth, y+height));
		utils.bresenhamLine(g,x+width-cornerDepth, y+height, x + cornerDepth, y + height);
		utils.quadraticBezier(g, new Point(x + cornerDepth, y + height), new Point(x+cornerDepth, y+height-cornerDepth), new Point(x, y+height-cornerDepth ));
		utils.bresenhamLine(g,x, y+height-cornerDepth , x, y + cornerDepth);
		utils.quadraticBezier(g, new Point(x, y + cornerDepth), new Point(x+cornerDepth, y+cornerDepth ), new Point(x+cornerDepth, y));

        x = 511; y = 79; width = 113; height = 261-y;

		utils.bresenhamLine(g,x + cornerDepth, y, x + width - cornerDepth, y);
		utils.quadraticBezier(g, new Point(x + width - cornerDepth, y), new Point(x+width-cornerDepth, y+cornerDepth), new Point(x+width, y+cornerDepth));
		utils.bresenhamLine(g,x+width, y+cornerDepth,x + width, y + height - cornerDepth);
		utils.quadraticBezier(g, new Point(x + width, y + height - cornerDepth), new Point(x+width-cornerDepth, y+height-cornerDepth ), new Point(x+width-cornerDepth, y+height));
		utils.bresenhamLine(g,x+width-cornerDepth, y+height, x + cornerDepth, y + height);
		utils.quadraticBezier(g, new Point(x + cornerDepth, y + height), new Point(x+cornerDepth, y+height-cornerDepth), new Point(x, y+height-cornerDepth ));
		utils.bresenhamLine(g,x, y+height-cornerDepth , x, y + cornerDepth);
		utils.quadraticBezier(g, new Point(x, y + cornerDepth), new Point(x+cornerDepth, y+cornerDepth ), new Point(x+cornerDepth, y));

	}
	
	private void drawFloor(Graphics2D g) {
		// Fill the background with the tile color
		g.setColor(tileColor);
		utils.myFillRect(g,0, 405, 600, 600-405);

		int lineSpacing = 37; // The size of each floor tile
		int shadowOffset = 2; // How far the shadow line is offset

		// Loop to draw horizontal grout lines
		int i = 0;
		for (int y = 405; y < 600; y += lineSpacing) {
			int currentShadowOffset = shadowOffset * (i + 3);

			// 1. Draw the shadow line
			g.setColor(shadowColor);
			utils.drawThickLine(g, 0, y + currentShadowOffset, 600, y + currentShadowOffset,10-i);

			// 2. Draw the main line
			g.setColor(groutColor);
			// utils.drawThickLine(g,0, y+ currentShadowOffset-5, 600, y+ currentShadowOffset-5,1);
			utils.bresenhamLine(g,0, y+ currentShadowOffset-5, 600, y+ currentShadowOffset-5);
			i++;
		}
		g.setClip(new Rectangle(0, 405, 600, 195));

		lineSpacing = 120;
		// Loop to draw vertical grout lines
		int j = 0;
		for (int x = 0; x <= 600; x += lineSpacing) {
			
			int currentShadowOffset = shadowOffset * (j + 3);

			// 1. Draw the shadow line
			g.setColor(shadowColor);
			utils.drawThickLine(g,335, 0, x +currentShadowOffset, 600,10-j);

			// 2. Draw the main line
			g.setColor(groutColor);
			// utils.drawThickLine(g,335, 0,  x +currentShadowOffset -5, 600,1);
			utils.bresenhamLine(g,335, 0,  x +currentShadowOffset -5, 600);
			i++; // Increment the counter for the next line's shadow -> more realistic
		}
		g.setClip(null);
	}

	private void drawCarpet(Graphics2D g) {
		if (!carpetRendered) {
			// Only create carpet buffer once
			carpetBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D tempG2d = carpetBuffer.createGraphics();
			
			Color mainCarpet = new Color(180, 50, 40);
			Color BorderCarpet = new Color(127, 46, 17);
			int xc = 300;
			int yc = 575;
			int rx = 350;
			int ry = 100;
			
			// Clear the buffer
			tempG2d.setColor(target); // Assuming target is transparent
			utils.myFillRect(tempG2d, 0, 0, carpetBuffer.getWidth(), carpetBuffer.getHeight());

			// Draw border
			tempG2d.setColor(BorderCarpet);
			utils.midpointEllipse(tempG2d, xc, yc, rx, ry);
			
			// Fill carpet (use the same buffer for flood fill)
			tempG2d.setColor(mainCarpet);
			utils.floodFill(carpetBuffer, xc, yc, target, mainCarpet);
			
			// Add decorative ellipses
			// utils.midpointEllipse(tempG2d, xc, yc, rx, ry, new Color(127, 46, 17, 50), 10);
			// utils.midpointEllipse(tempG2d, xc+10, yc+10, rx, ry, new Color(168, 52, 18, 50), 20);
			utils.midpointEllipse(tempG2d, xc+10, yc+10, rx, ry);
			

			tempG2d.dispose();
			carpetRendered = true;
		}
		
		// Draw the cached buffer
		g.drawImage(carpetBuffer, 0, 0, null);
	}
	
	private void drawWindow(Graphics2D g) {
		// Window dimensions and position
		int[] xPoints = {0, 115, 115, 0};
		int[] yPoints = {0, 17, 405, 438};

		g.setColor(wall); 
		g.fillPolygon(xPoints, yPoints, 4);
		g.setStroke(new BasicStroke(1.5f));
		g.setColor(Color.darkGray);
		g.drawPolygon(xPoints, yPoints, 4);

		int[] x1 = {0, 115, 115, 0};
		int[] y1 = {0, 17, 30, 13};
		g.setColor(wallLine); 
		g.fillPolygon(x1, y1, 4); 
		g.setStroke(new BasicStroke(1.5f));
		g.setColor(Color.darkGray);
		g.drawPolygon(x1, y1, 4);

		int[] x2 = {0, 115, 115, 0};
		int[] y2 = {425, 392, 405, 438};
		g.setColor(new Color(55, 61, 71)); 
		g.fillPolygon(x2, y2, 4); 
		g.setStroke(new BasicStroke(1.5f));
		g.setColor(Color.darkGray);
		g.drawPolygon(x2, y2, 4);
		
		g.setColor(glassColor);
		int[] x3 = {10,105,105, 10};
		int[] y3 = {60, 70, 310, 335};
		g.fillPolygon(x3, y3, 4); 

		g.setColor(gridColor);
		g.setStroke(new BasicStroke(3));
		g.drawPolygon(x3, y3, 4); 

		int frameThickness = 11;
		g.setColor(frameColor);
		g.setStroke(new BasicStroke(frameThickness));
		g.drawPolygon(x3, y3, 4);
		g.setColor(gridColor);
		int[] x31 = {15,100,100, 15};
		int[] y31 = {65, 75, 305, 330};
		g.setStroke(new BasicStroke(3));
		g.drawPolygon(x31, y31, 4);

		int[] x4 = {20,95,95, 20};
		int[] y4 = {140, 145, 305, 325};
		g.setColor(frameColor);
		g.setStroke(new BasicStroke(frameThickness));
		g.drawPolygon(x4, y4, 4);

		int[] x5 = {15,100,100, 15};
		int[] y5 = {135, 140, 305, 330};
		g.setColor(gridColor);
		g.setStroke(new BasicStroke(3));
		g.drawPolygon(x5, y5, 4); 
		
		g.setColor(gridColor);
		utils.drawThickLine(g, 50, 145, 50, 312, 1);
		utils.drawThickLine(g, 60, 145, 60, 312, 1);
		utils.drawThickLine(g, 15, 230, 100, 225, 1);
		utils.drawThickLine(g, 15, 220, 100, 215, 1);
		g.setColor(frameColor);
		utils.drawThickLine(g, 15, 225, 100, 220, frameThickness);
		utils.drawThickLine(g, 55, 145, 55, 312, frameThickness);
	}

	private void drawCurtain(Graphics2D g2) {
		BufferedImage curtainBuffer = new BufferedImage(601, 601, BufferedImage.TYPE_INT_ARGB);
    	Graphics2D g = curtainBuffer.createGraphics();
		 // วาดราวแขวนผ้าม่าน
		g.setColor(new Color(100, 70, 50));
		utils.drawThickLine(g,0, 0, 115, 18,5);

		// วาดผ้าม่านด้านซ้าย
		Color baseColor = new Color(180, 50, 40);
		// g.setColor(baseColor);
		g.setColor(new Color(150, 0, 0));
		utils.bresenhamLine(g, 0, 0, 60, 11);
		utils.quadraticBezier(g, new Point(60, 11), new Point(70, 90), new Point(0, 220));

		// // วาดผ้าม่านด้านขวา
		utils.bresenhamLine(g, 60, 11,115, 18 );
		utils.quadraticBezier(g, new Point(115, 18), new Point(130,135), new Point(115, 250));
		utils.quadraticBezier(g, new Point(115, 250), new Point(55, 115), new Point(60, 12));

		utils.quadraticBezier(g, new Point(111, 262), new Point(90, 370), new Point(93, 398));
		utils.quadraticBezier(g, new Point(93, 398), new Point(113, 388), new Point(118, 401));
		utils.quadraticBezier(g, new Point(118, 401), new Point(135, 420), new Point(148, 404));
		utils.quadraticBezier(g, new Point(148, 404), new Point(122, 260), new Point(120, 261));


		// วาดวงกลมแบบมีสีเติม
		utils.midpointEllipse(g,115, 250, 10, 10);
		// utils.midpointEllipse(g, 115, 250, 10, 10, new Color(150, 0, 0), 3);    
		    // Color baseColor = new Color(180, 50, 40);
		Color bgColor = new Color(0,0,0,0); // pixel โปร่งใส
		utils.floodFill(curtainBuffer, 110, 95, bgColor, baseColor);
		utils.floodFill(curtainBuffer, 34, 68, bgColor, baseColor);
		utils.floodFill(curtainBuffer, 120, 250, bgColor, new Color(150, 0, 0));
		utils.floodFill(curtainBuffer, 113, 322, bgColor, baseColor);

		// วาด buffer ลงจอจริง
		g2.drawImage(curtainBuffer, 0, 0, null);
		g.dispose();
	}

    // Cubic Bézier
    public static List<Point> getBezierPoints(Point p0, Point p1, Point p2, Point p3, int steps) {
        List<Point> points = new ArrayList<>();
        for (int i = 0; i <= steps; i++) {
            double t = i / (double) steps;

            double x = Math.pow(1 - t, 3) * p0.x
                     + 3 * t * Math.pow(1 - t, 2) * p1.x
                     + 3 * Math.pow(t, 2) * (1 - t) * p2.x
                     + Math.pow(t, 3) * p3.x;

            double y = Math.pow(1 - t, 3) * p0.y
                     + 3 * t * Math.pow(1 - t, 2) * p1.y
                     + 3 * Math.pow(t, 2) * (1 - t) * p2.y
                     + Math.pow(t, 3) * p3.y;

            points.add(new Point((int) x, (int) y));
        }
        return points;
    }

	private void drawChandeliersAndSconces(Graphics2D g) {
        // Generate flame buffer each frame for animation
        if (flameBuffer == null) {
            flameBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        }
        
        Graphics2D flameG2d = flameBuffer.createGraphics();
        flameG2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Clear flame buffer
        flameG2d.setComposite(AlphaComposite.Clear);
        flameG2d.fillRect(0, 0, flameBuffer.getWidth(), flameBuffer.getHeight());
        flameG2d.setComposite(AlphaComposite.SrcOver);
        
        // Draw chandeliers with flames to buffer
        drawChandelier(g, flameG2d, 206, 200, 3, 0);
        drawChandelier(g, flameG2d, 558, 200, 3, 0);
        drawChandelier(g, flameG2d, 460, 56, 4, 0);
        
        flameG2d.dispose();
        
        // Draw the flame buffer
        g.drawImage(flameBuffer, 0, 0, null);
    }

    private void drawChandelier(Graphics2D g, Graphics2D flameG2d, int x, int y, int arms, int seed) {
        // Draw chandelier structure to main graphics
        g.setColor(new Color(187, 74, 10));
        utils.midpointEllipse(g, x, y - 5, 8, 5);
        utils.myFillRect(g, x-3, y-10, 6, 35);

        int armLen = 40;
        for (int i = 0; i < arms; i++) {
            double a = Math.toRadians(-60 + i * (120.0 / (arms - 1)));
            int ax = x + (int)(Math.sin(a) * armLen);
            int ay = y + (int)(Math.cos(a) * armLen);

            int cx = (x + ax) / 2;
            int cy = y + 60;

            g.setStroke(new BasicStroke(6f));
            g.setColor(new Color(187, 74, 10));

            Point p1 = new Point(x, y + 15);
            Point p2 = new Point(cx, cy);
            Point p3 = new Point(ax, ay + 15);
            utils.quadraticBezier(g, p1, p2, p3);

            // Candle base
            g.setColor(new Color(255, 193, 5));
            utils.myFillRect(g, ax-6, ay+8, 12, 18);
            g.setColor(new Color(125, 59, 18));
            utils.myDrawRect(g, ax-6, ay+8, 12, 18, 1);

            // Draw flame to flame buffer with flood fill
            drawFlameToBuffer(flameG2d, ax, ay, 12, i);
        }
    }

    private void drawFlameToBuffer(Graphics2D flameG2d, int x, int y, int size, int flameIndex) {
        float seed = flameSeeds[flameIndex % flameSeeds.length];
        float currentTime = time; // Use class time instead of AnimationTime
        float jitter = (float)(Math.sin(currentTime * 10 + seed * 5.0) * 3.0);
        int fx = x;
        int fy = y - 6 + (int)jitter;

        // Create flame outline for flood fill
        flameG2d.setColor(new Color(255,193,5)); // Outline color
        
        List<Point> flameOutline = new ArrayList<>();
        
        // Create flame shape points
        Point p0 = new Point(fx, fy - size / 2);
        Point p1 = new Point((int)(fx + size * 0.5), (int)(fy - size * 0.2));
        Point p2 = new Point((int)(fx + size * 0.2), (int)(fy + size * 0.6));
        Point p3 = new Point(fx, (int)(fy + size * 0.8));
        Point p4 = new Point((int)(fx - size * 0.2), (int)(fy + size * 0.6));
        Point p5 = new Point((int)(fx - size * 0.5), (int)(fy - size * 0.2));
        
        // Add bezier curve points to outline
        flameOutline.addAll(getBezierPoints(p0, p1, p2, p3, 30));
        flameOutline.addAll(getBezierPoints(p3, p4, p5, p0, 30));
        
        // Draw flame outline
        for (int i = 0; i < flameOutline.size() - 1; i++) {
            Point pt1 = flameOutline.get(i);
            Point pt2 = flameOutline.get(i + 1);
            utils.bresenhamLine(flameG2d, pt1.x, pt1.y, pt2.x, pt2.y);
        }
        
        // Close the outline
        if (!flameOutline.isEmpty()) {
            Point first = flameOutline.get(0);
            Point last = flameOutline.get(flameOutline.size() - 1);
            utils.bresenhamLine(flameG2d, last.x, last.y, first.x, first.y);
        }
        
        // Flood fill with gradient effect (use base color first)
        Color flameColor = new Color(255, 200, 80);
        flameG2d.setColor(flameColor);
        utils.floodFill(flameBuffer, fx, fy, target, flameColor);
        
        // Add inner core
        flameG2d.setColor(new Color(255, 250, 200));
        int coreRx = (int)(size * 0.25f);
        int coreRy = (int)(size * 0.45f);
        utils.midpointEllipse(flameG2d, fx, fy - (int)(size * 0.05f), coreRx, coreRy);
        utils.floodFill(flameBuffer, fx, fy - (int)(size * 0.05f), flameColor, new Color(255, 250, 200));
    }
}
