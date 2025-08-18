package sendwork;
/**
 * The Project class is the main entry point for the Egg Cracking Animation application.
 * It sets up the JFrame and JLayeredPane to display the different components.
 * This class contains the main method to run the application.
 * @author 66051133 Anecha Yoksombat 66050998 Rapeepat Kamsen
 * @version 1.0
 */
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import javax.swing.Timer;
import java.util.List;
import java.util.Queue;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Project {
	public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Egg Cracking Animation");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            Background background = new Background();
            Egg eggPanel = new Egg();

            JLayeredPane layeredPane = new JLayeredPane();
            layeredPane.setPreferredSize(new Dimension(600, 600));
            
            background.setBounds(0, 0, 600, 600);
            eggPanel.setBounds(0, 0, 600, 600);
            eggPanel.setOpaque(false);

            layeredPane.add(background, JLayeredPane.DEFAULT_LAYER);
            layeredPane.add(eggPanel, JLayeredPane.PALETTE_LAYER);

            frame.add(layeredPane);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
/**
 * This Class draws the background include the chandelier 
 * and it will initiate in The Project
 * @param g The Graphics2D object to draw on.
 */
class Background extends JPanel implements ActionListener {
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

		//drawcomponaint
        drawWall(g2d); //กำแพง
        drawFloor(g2d); //พื้น
        drawCarpet(g2d); //พรม
        drawWindow(g2d); //หน้าต่าง
        drawCurtain(g2d); //ม่าน
        drawChandeliersAndSconces(g2d); //โคมแขวน
    }

	//sub method for Stroke Rectangle
	private void drawStrokedRect(Graphics2D g2d, int x, int y, int width, int height, Color strokeColor, int strokeWidth) {
		g2d.setColor(strokeColor);
		utils.myDrawRect(g2d, x, y, width, height,strokeWidth);
	}

	private void drawWall(Graphics2D g) {
		//backgroundWall
		g.setColor(bgColor); 
		utils.myFillRect(g,0, 0, 600, 17);    
		//upbar
		g.setColor(wallLine);
		utils.myFillRect(g,120, 17, 480, 13);    
		//lowbar
		g.setColor(new Color(55, 61, 71));
		utils.myFillRect(g,120, 389, 480, 16);  
		drawStrokedRect(g, 120, 389, 480, 16, Color.darkGray, 1);
		//line1
		g.setColor(wallLine); 
		utils.myFillRect(g,120, 57, 480, 6);
		drawStrokedRect(g, 120, 57, 480, 6, Color.darkGray, 1);
		//line2
		g.setColor(wallLine);
		utils.myFillRect(g,120, 284, 480, 6);
		drawStrokedRect(g,120, 284, 480, 6, Color.darkGray, 1);
		//square on the wall
		drawStrokedRect(g,286, 79, 208, 182, wallLine, 3);
		drawStrokedRect(g,286, 305, 208, 67, wallLine, 3);
		drawStrokedRect(g,153, 305, 113, 67, wallLine, 3);
		drawStrokedRect(g,511, 305, 89, 67, wallLine, 3);

		//first curve rectangle
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

		//second curve rectangle
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
			utils.bresenhamLine(g,335, 0,  x +currentShadowOffset -5, 600);
			i++; // Increment the counter for the next line's shadow -> more realistic
		}
		g.setClip(null);
	}

	private void drawCarpet(Graphics2D g) {
		Color mainCarpet = new Color(180, 50, 40);
		Color BorderCarpet = new Color(127, 46, 17);
		if (!carpetRendered) {
			// Only create carpet buffer once
			carpetBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D tempG2d = carpetBuffer.createGraphics();
			int xc = 300;
			int yc = 575;
			int rx = 350;
			int ry = 100;
			
			// Clear the buffer
			tempG2d.setColor(target);
			utils.myFillRect(tempG2d, 0, 0, carpetBuffer.getWidth(), carpetBuffer.getHeight());

			// Draw border
			tempG2d.setColor(BorderCarpet);
			utils.midpointEllipse(tempG2d, xc, yc, rx, ry);
			
			// Fill carpet
			tempG2d.setColor(mainCarpet);
			utils.floodFill(carpetBuffer, xc, yc, target, mainCarpet);
			
			tempG2d.dispose();
			carpetRendered = true;
		}
		g.drawImage(carpetBuffer, 0, 0, null);
	}
	
	private void drawWindow(Graphics2D g) {
		// Window dimensions and position
		int[] xPoints = {0, 115, 115, 0};
		int[] yPoints = {0, 17, 405, 438};
		//Wall
		g.setColor(wall); 
		g.fillPolygon(xPoints, yPoints, 4);
		g.setStroke(new BasicStroke(1.5f));
		g.setColor(Color.darkGray);
		g.drawPolygon(xPoints, yPoints, 4);

		//border of wall
		int[] x1 = {0, 115, 115, 0};
		int[] y1 = {0, 17, 30, 13};
		g.setColor(wallLine); 
		g.fillPolygon(x1, y1, 4); 
		g.setStroke(new BasicStroke(1.5f));
		g.setColor(Color.darkGray);
		g.drawPolygon(x1, y1, 4);

		//border winndow
		int[] x2 = {0, 115, 115, 0};
		int[] y2 = {425, 392, 405, 438};
		g.setColor(new Color(55, 61, 71)); 
		g.fillPolygon(x2, y2, 4); 
		g.setStroke(new BasicStroke(1.5f));
		g.setColor(Color.darkGray);
		g.drawPolygon(x2, y2, 4);
		
		//fill glass of window
		g.setColor(glassColor);
		int[] x3 = {10,105,105, 10};
		int[] y3 = {60, 70, 310, 335};
		g.fillPolygon(x3, y3, 4); 

		//draw each cell of windpow
		g.setColor(gridColor);
		g.setStroke(new BasicStroke(3));
		g.drawPolygon(x3, y3, 4); 

		//draw each cell of windpow
		int frameThickness = 11;
		g.setColor(frameColor);
		g.setStroke(new BasicStroke(frameThickness));
		g.drawPolygon(x3, y3, 4);

		//draw subwindow
		g.setColor(gridColor);
		int[] x3_1 = {15,100,100, 15};
		int[] y3_1 = {65, 75, 305, 330};
		g.setStroke(new BasicStroke(3));
		g.drawPolygon(x3_1, y3_1, 4);

		//draw subwindow
		int[] x4 = {20,95,95, 20};
		int[] y4 = {140, 145, 305, 325};
		g.setColor(frameColor);
		g.setStroke(new BasicStroke(frameThickness));
		g.drawPolygon(x4, y4, 4);

		//draw subwindow
		int[] x5 = {15,100,100, 15};
		int[] y5 = {135, 140, 305, 330};
		g.setColor(gridColor);
		g.setStroke(new BasicStroke(3));
		g.drawPolygon(x5, y5, 4); 
		
		//draw line on window
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
		Color baseColor = new Color(180, 50, 40);
		Color borderCurtain = new Color(150, 0, 0);
		 // วาดราวแขวนผ้าม่าน
		g.setColor(new Color(100, 70, 50));
		utils.drawThickLine(g,0, 0, 115, 18,5);

		// วาดผ้าม่านด้านซ้าย
		g.setColor(borderCurtain);
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

		// วาดวงกลม
		utils.midpointEllipse(g,115, 250, 10, 10);
		//target คือ สีใส
		utils.floodFill(curtainBuffer, 110, 95, target, baseColor);
		utils.floodFill(curtainBuffer, 34, 68, target, baseColor);
		utils.floodFill(curtainBuffer, 120, 250, target, baseColor);
		utils.floodFill(curtainBuffer, 113, 322, target, baseColor);

		// วาด buffer ลงจอจริง
		g2.drawImage(curtainBuffer, 0, 0, null);
		g.dispose();
	}

    // find bezier point
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

	//draw chandelier candle base and arm
    private void drawChandelier(Graphics2D g, Graphics2D flameG2d, int x, int y, int arms, int seed) {
        // Draw chandelier structure to main graphics
        g.setColor(new Color(187, 74, 10));
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

            // Draw flame to flame buffer with flood fill
            drawFlameToBuffer(flameG2d, ax, ay, 12, i);
        }
    }

    private void drawFlameToBuffer(Graphics2D flameG2d, int x, int y, int size, int flameIndex) {
        float seed = flameSeeds[flameIndex % flameSeeds.length];
        float currentTime = time; 
        float jitter = (float)(Math.sin(currentTime * 10 + seed * 5.0) * 3.0);
        int fx = x;
        int fy = y - 6 + (int)jitter;
		Color flameYellow = new Color(255,193,5);
		Color flameColor = new Color(255, 200, 80);

        // Create flame outline for flood fill
        flameG2d.setColor(flameYellow);
        
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

class Egg extends JPanel {
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
        
        Timer timer = new Timer(16, e -> repaint());
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
    
    // private void drawEggOutline(Graphics2D g) {
    //     g.setColor(eggOutline);
    //     g.setStroke(new BasicStroke(3));
        
    //     graphicsUtils.quadraticBezier(g, new Point(237, 214), new Point(299, 151), new Point(372, 213));
    //     graphicsUtils.quadraticBezier(g, new Point(371, 213), new Point(415, 250), new Point(430, 304));
    //     graphicsUtils.quadraticBezier(g, new Point(429, 304), new Point(446, 356), new Point(439, 396));
    //     graphicsUtils.quadraticBezier(g, new Point(439, 395), new Point(436, 447), new Point(386, 489));
    //     graphicsUtils.quadraticBezier(g, new Point(229, 493), new Point(305, 545), new Point(387, 488));
    //     graphicsUtils.quadraticBezier(g, new Point(168, 395), new Point(173, 455), new Point(229, 493));
    //     graphicsUtils.quadraticBezier(g, new Point(237, 214), new Point(154, 283), new Point(168, 396));
    // }
    
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

class Cat {
    // Colors
    private final Color catOutline = new Color(91, 33, 20);
    private final Color eyeColor = new Color(89, 65, 35);
    private final Color mouthArea = new Color(243, 226, 160);
    private final Color nose = new Color(218, 157, 86);
    private final Color earInner = new Color(234, 134, 50);
    private final Color earDetail = new Color(239, 149, 94);
    private final Color stripe = new Color(241, 150, 49);
    private final Color chest = new Color(249, 182, 80);
    private final Color mainFur = new Color(252, 192, 90);
    private final Color paw = new Color(226, 176, 114);

    private final GraphicsUtils graphicsUtils;
    
    public Cat() {
        this.graphicsUtils = new GraphicsUtils();
    }
    
    public void drawCatEmerging(Graphics2D g, long elapsed, BufferedImage buffer) {
        long catEmergeDuration = 3000;
        long catStartTime = elapsed - 5000;
        
        if (catStartTime < 0) return;
        
        double progress = Math.min(catStartTime / (double)catEmergeDuration, 1.0);
        int initialOffset = 100;
        int currentOffset = (int)(initialOffset * (1 - progress));
        
        BufferedImage catBuffer = new BufferedImage(601, 601, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gCat = catBuffer.createGraphics();
        gCat.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        drawStripes(gCat, catBuffer);
        drawCatBody(gCat, catBuffer);
        drawCatFace(gCat, catBuffer);
        gCat.dispose();
        
        Shape originalClip = g.getClip();
        Rectangle clipRect = new Rectangle(0, 0, 600, 600 - currentOffset);
        g.setClip(clipRect);
        g.drawImage(catBuffer, 0, currentOffset, null);
        g.setClip(originalClip);
    }
    
    public void drawHands(Graphics2D g, BufferedImage buffer) {
        drawLeftHand(g, buffer);
        drawRightHand(g, buffer);
    }
    
    private void drawCatFace(Graphics2D g, BufferedImage buffer) {
        // Eyes using Polygon instead of fillOval
        Polygon leftEye = createEllipsePolygon(224, 240, 26, 34);
        Polygon rightEye = createEllipsePolygon(303, 247, 30, 36);
        
        g.setColor(eyeColor);
        g.fillPolygon(leftEye);
        g.fillPolygon(rightEye);
        
        // Eye highlights using Polygon
        Polygon leftHighlight = createEllipsePolygon(230, 249, 4, 6);
        Polygon rightHighlight = createEllipsePolygon(309, 256, 6, 6);
        
        g.setColor(Color.WHITE);
        g.fillPolygon(leftHighlight);
        g.fillPolygon(rightHighlight);

        // Eyebrows using quadratic bezier
        g.setColor(catOutline);
        graphicsUtils.quadraticBezier(g, new Point(231, 218), new Point(236, 211), new Point(243, 215));
        graphicsUtils.quadraticBezier(g, new Point(319, 221), new Point(329, 216), new Point(332, 226));
        
        // Mouth area using Polygon
        Polygon mouthAreaPolygon = createEllipsePolygon(242, 266, 55, 47);
        g.setColor(mouthArea);
        g.fillPolygon(mouthAreaPolygon);

        drawMouth(g);
        drawNose(g, buffer);
        drawEars(g, buffer);
        drawWhiskers(g);
    }
        // Create ellipse as polygon using midpoint ellipse algorithm concept
    private Polygon createEllipsePolygon(int x, int y, int width, int height) {
        Polygon polygon = new Polygon();
        int centerX = x + width / 2;
        int centerY = y + height / 2;
        int a = width / 2;
        int b = height / 2;
        
        // Generate ellipse points
        for (double angle = 0; angle < 2 * Math.PI; angle += 0.1) {
            int px = (int)(centerX + a * Math.cos(angle));
            int py = (int)(centerY + b * Math.sin(angle));
            polygon.addPoint(px, py);
        }
        
        return polygon;
    }
    
    private void drawMouth(Graphics2D g) {
        g.setColor(catOutline);
        graphicsUtils.quadraticBezier(g, new Point(287, 294), new Point(276, 305), new Point(268, 293));
        graphicsUtils.quadraticBezier(g, new Point(268, 285), new Point(271, 297), new Point(260, 296));
        graphicsUtils.quadraticBezier(g, new Point(253, 290), new Point(258, 298), new Point(264, 296));
    }
    
    private void drawNose(Graphics2D g, BufferedImage buffer) {
        g.setColor(catOutline);
        graphicsUtils.quadraticBezier(g, new Point(259, 274), new Point(265, 270), new Point(277, 274));
        graphicsUtils.quadraticBezier(g, new Point(259, 273), new Point(258, 278), new Point(268, 285));
        graphicsUtils.quadraticBezier(g, new Point(268, 285), new Point(280, 276), new Point(276, 274));
        
        graphicsUtils.floodFill(buffer, 268, 277, mouthArea, nose);
    }
    
    private void drawEars(Graphics2D g, BufferedImage buffer) {
        g.setColor(catOutline);
        graphicsUtils.quadraticBezier(g, new Point(240, 179), new Point(250, 179), new Point(224, 150));
        graphicsUtils.quadraticBezier(g, new Point(351, 189), new Point(362, 168), new Point(382, 159));
        
        g.setColor(earInner);
        graphicsUtils.quadraticBezier(g, new Point(220, 196), new Point(228, 182), new Point(240, 179));
        graphicsUtils.quadraticBezier(g, new Point(352, 188), new Point(372, 198), new Point(380, 219));

        g.setColor(earDetail);
        graphicsUtils.quadraticBezier(g, new Point(220, 196), new Point(211, 213), new Point(213, 175));
        graphicsUtils.quadraticBezier(g, new Point(213, 175), new Point(214, 142), new Point(224, 150));
        graphicsUtils.quadraticBezier(g, new Point(380, 219), new Point(402, 148), new Point(381, 159));
        
        graphicsUtils.floodFill(buffer, 227, 171, mainFur, earDetail);
        graphicsUtils.floodFill(buffer, 377, 185, mainFur, earDetail);
    }
    
    private void drawStripes(Graphics2D g, BufferedImage buffer) {
        g.setColor(stripe);
        
        graphicsUtils.quadraticBezier(g, new Point(269, 166), new Point(246, 197), new Point(262, 209));
        graphicsUtils.quadraticBezier(g, new Point(278, 165), new Point(261, 195), new Point(263, 209));
        graphicsUtils.quadraticBezier(g, new Point(280, 219), new Point(265, 203), new Point(287, 165));
        graphicsUtils.quadraticBezier(g, new Point(305, 165), new Point(281, 234), new Point(279, 216));
        graphicsUtils.quadraticBezier(g, new Point(303, 212), new Point(296, 187), new Point(318, 168));
        graphicsUtils.quadraticBezier(g, new Point(329, 171), new Point(303, 219), new Point(303, 212));
        graphicsUtils.quadraticBezier(g, new Point(195, 229), new Point(228, 244), new Point(192, 239));
        graphicsUtils.quadraticBezier(g, new Point(192, 280), new Point(236, 288), new Point(200, 294));
        graphicsUtils.quadraticBezier(g, new Point(189, 255), new Point(242, 280), new Point(189, 263));
        graphicsUtils.quadraticBezier(g, new Point(398, 277), new Point(322, 285), new Point(392, 294));
        graphicsUtils.drawBezier(g, new Point(398, 243), new Point(344, 257), new Point(367, 260), new Point(400, 258));
        graphicsUtils.quadraticBezier(g, new Point(372, 342), new Point(327, 398), new Point(383, 355));
        graphicsUtils.quadraticBezier(g, new Point(357, 328), new Point(310, 372), new Point(367, 337));
        
        graphicsUtils.bresenhamLine(g, 269, 166, 277, 165);
        graphicsUtils.bresenhamLine(g, 286, 165, 305, 165);
        graphicsUtils.bresenhamLine(g, 318, 168, 329, 171);
        graphicsUtils.quadraticBezier(g, new Point(399, 243), new Point(403, 250), new Point(399, 257));
        graphicsUtils.quadraticBezier(g, new Point(398, 278), new Point(398, 285), new Point(392, 294));
        graphicsUtils.bresenhamLine(g, 357, 328, 367, 337);
        graphicsUtils.bresenhamLine(g, 372, 343, 383, 356);
        graphicsUtils.quadraticBezier(g, new Point(192, 281), new Point(192, 292), new Point(202, 294));
        graphicsUtils.bresenhamLine(g, 189, 255, 189, 263);
        graphicsUtils.quadraticBezier(g, new Point(195, 230), new Point(191, 234), new Point(193, 239));
        
        // Fill stripe areas
        graphicsUtils.floodFill(buffer, 263, 186, new Color(0, 0, 0, 0), stripe);
        graphicsUtils.floodFill(buffer, 285, 198, new Color(0, 0, 0, 0), stripe);
        graphicsUtils.floodFill(buffer, 310, 197, new Color(0, 0, 0, 0), stripe);
        graphicsUtils.floodFill(buffer, 387, 287, new Color(0, 0, 0, 0), stripe);
        graphicsUtils.floodFill(buffer, 386, 253, new Color(0, 0, 0, 0), stripe);
        graphicsUtils.floodFill(buffer, 201, 237, new Color(0, 0, 0, 0), stripe);
        graphicsUtils.floodFill(buffer, 195, 261, new Color(0, 0, 0, 0), stripe);
        graphicsUtils.floodFill(buffer, 202, 288, new Color(0, 0, 0, 0), stripe);
        graphicsUtils.floodFill(buffer, 354, 339, new Color(0, 0, 0, 0), stripe);
        graphicsUtils.floodFill(buffer, 371, 356, new Color(0, 0, 0, 0), stripe);
    }
    
    private void drawWhiskers(Graphics2D g) {
        g.setColor(catOutline);
        graphicsUtils.quadraticBezier(g, new Point(165, 255), new Point(187, 254), new Point(198, 263));
        graphicsUtils.quadraticBezier(g, new Point(166, 280), new Point(183, 274), new Point(199, 278));
        graphicsUtils.quadraticBezier(g, new Point(353, 283), new Point(382, 277), new Point(409, 290));
        graphicsUtils.quadraticBezier(g, new Point(350, 297), new Point(382, 305), new Point(394, 324));
    }
    
    private void drawCatBody(Graphics2D g, BufferedImage buffer) {
        g.setColor(catOutline);
        
        graphicsUtils.quadraticBezier(g, new Point(205, 211), new Point(150, 290), new Point(245, 324));
        graphicsUtils.quadraticBezier(g, new Point(204, 211), new Point(185, 68), new Point(266, 167));
        graphicsUtils.quadraticBezier(g, new Point(266, 166), new Point(303, 159), new Point(331, 172));
        graphicsUtils.quadraticBezier(g, new Point(331, 172), new Point(373, 137), new Point(395, 143));
        graphicsUtils.quadraticBezier(g, new Point(394, 143), new Point(415, 178), new Point(394, 227));
        graphicsUtils.quadraticBezier(g, new Point(395, 227), new Point(431, 321), new Point(319, 328));
        graphicsUtils.quadraticBezier(g, new Point(352, 322), new Point(408, 371), new Point(389, 384));
        graphicsUtils.quadraticBezier(g, new Point(244, 323), new Point(229, 364), new Point(242, 403));
        graphicsUtils.bresenhamLine(g, 242, 402, 390, 384);
        graphicsUtils.quadraticBezier(g, new Point(244, 323), new Point(250, 326), new Point(261, 327));
        
        g.setColor(chest);
        graphicsUtils.quadraticBezier(g, new Point(238, 355), new Point(255, 328), new Point(261, 327));
        graphicsUtils.floodFill(buffer, 251, 331, new Color(0, 0, 0, 0), chest);
        graphicsUtils.floodFill(buffer, 285, 232, new Color(0, 0, 0, 0), mainFur);
    }
    
    private void drawLeftHand(Graphics2D g, BufferedImage buffer) {
        BufferedImage handLBuffer = new BufferedImage(601, 601, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gHandL = handLBuffer.createGraphics();
        gHandL.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        gHandL.setColor(paw);
        graphicsUtils.quadraticBezier(gHandL, new Point(247, 363), new Point(233, 388), new Point(214, 366));
        drawHandDetails(gHandL, handLBuffer, true);
        gHandL.dispose();
        g.drawImage(handLBuffer, 0, 0, null);
    }
    
    private void drawRightHand(Graphics2D g, BufferedImage buffer) {
        BufferedImage handRBuffer = new BufferedImage(601, 601, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gHandR = handRBuffer.createGraphics();
        gHandR.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        gHandR.setColor(paw);
        graphicsUtils.quadraticBezier(gHandR, new Point(311, 385), new Point(346, 396), new Point(342, 370));
        drawHandDetails(gHandR, handRBuffer, false);
        gHandR.dispose();
        g.drawImage(handRBuffer, 0, 0, null);
    }
    
    private void drawHandDetails(Graphics2D g, BufferedImage buffer, boolean isLeft) {
        g.setColor(catOutline);
        
        if (isLeft) {
            graphicsUtils.quadraticBezier(g, new Point(247, 342), new Point(237, 327), new Point(215, 337));
            graphicsUtils.quadraticBezier(g, new Point(207, 356), new Point(209, 365), new Point(219, 368));
            graphicsUtils.quadraticBezier(g, new Point(215, 337), new Point(205, 341), new Point(207, 356));
            graphicsUtils.quadraticBezier(g, new Point(246, 361), new Point(235, 374), new Point(219, 368));
            graphicsUtils.quadraticBezier(g, new Point(220, 350), new Point(212, 367), new Point(223, 369));
            graphicsUtils.quadraticBezier(g, new Point(235, 352), new Point(228, 375), new Point(241, 366));
            graphicsUtils.quadraticBezier(g, new Point(248, 364), new Point(262, 358), new Point(247, 341));
            graphicsUtils.bresenhamLine(g, 245, 361, 248, 364);
            
            graphicsUtils.floodFill(buffer, 230, 350, new Color(0, 0, 0, 0), mainFur);
            graphicsUtils.floodFill(buffer, 232, 372, new Color(0, 0, 0, 0), paw);
        } else {
            graphicsUtils.quadraticBezier(g, new Point(352, 362), new Point(330, 330), new Point(302, 354));
            graphicsUtils.quadraticBezier(g, new Point(302, 353), new Point(284, 370), new Point(307, 383));
            graphicsUtils.quadraticBezier(g, new Point(308, 368), new Point(294, 389), new Point(323, 385));
            graphicsUtils.quadraticBezier(g, new Point(327, 383), new Point(317, 384), new Point(323, 372));
            graphicsUtils.quadraticBezier(g, new Point(342, 369), new Point(340, 382), new Point(322, 385));
            
            g.setColor(mainFur);
            graphicsUtils.quadraticBezier(g, new Point(352, 362), new Point(358, 372), new Point(359, 377));
            graphicsUtils.quadraticBezier(g, new Point(342, 367), new Point(353, 372), new Point(358, 377));
            
            graphicsUtils.floodFill(buffer, 330, 365, new Color(0, 0, 0, 0), mainFur);
            graphicsUtils.floodFill(buffer, 334, 384, new Color(0, 0, 0, 0), paw);
        }
    }
}

class GraphicsUtils {
    
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

    public void bresenhamLine(Graphics g, int x1, int y1, int x2, int y2) {
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
            plot(g, x, y, 3);
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
    public void midpointCircle(Graphics g, int xc, int yc, int r) {
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

    public void midpointEllipse(Graphics g, int xc, int yc, int a, int b) {
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
       // Draw thick polygon outline using Bresenham algorithm
    public void drawThickPolygonOutline(Graphics g, Polygon polygon, int thickness) {
        int[] xPoints = polygon.xpoints;
        int[] yPoints = polygon.ypoints;
        int nPoints = polygon.npoints;
        
        // Draw multiple lines for thickness
        for (int i = 0; i < nPoints; i++) {
            int x1 = xPoints[i];
            int y1 = yPoints[i];
            int x2 = xPoints[(i + 1) % nPoints];
            int y2 = yPoints[(i + 1) % nPoints];
            
            // Draw thick line by drawing multiple parallel lines
            drawThickLine(g, x1, y1, x2, y2, thickness);
        }
    }
    
    // Draw thick line using multiple Bresenham lines
    public void drawThickLine(Graphics g, int x1, int y1, int x2, int y2, int thickness) {
        // Calculate perpendicular direction for thickness
        int dx = x2 - x1;
        int dy = y2 - y1;
        double length = Math.sqrt(dx * dx + dy * dy);
        
        if (length == 0) return;
        
        // Perpendicular unit vector
        double perpX = -dy / length;
        double perpY = dx / length;
        
        // Draw multiple parallel lines
        int halfThickness = thickness / 2;
        for (int i = -halfThickness; i <= halfThickness; i++) {
            int offsetX = (int)(i * perpX);
            int offsetY = (int)(i * perpY);
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
    public static List<Point> getQuadraticBezierPoints(Point p0, Point p1, Point p2, int steps) {
        List<Point> points = new ArrayList<>();
        for (int i = 0; i <= steps; i++) {
            double t = i / (double) steps;

            double x = Math.pow(1 - t, 2) * p0.x
                     + 2 * (1 - t) * t * p1.x
                     + Math.pow(t, 2) * p2.x;

            double y = Math.pow(1 - t, 2) * p0.y
                     + 2 * (1 - t) * t * p1.y
                     + Math.pow(t, 2) * p2.y;

            points.add(new Point((int) x, (int) y));
        }
        return points;
    }

    // Cubic Bézier
    public static List<Point> getCubicBezierPoints(Point p0, Point p1, Point p2, Point p3, int steps) {
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
}
