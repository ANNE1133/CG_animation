package finalVer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;//only use in drawchair
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.*;
public class Background extends JPanel implements ActionListener {
    //colors 
	private final Color bgColor = new Color(144, 80, 39);
    private final Color wall = new Color(58, 62, 68);
    private final Color wallLine = new Color(172, 64, 4);
	private final Color tileColor = new Color(173, 79, 15);
    private final Color groutColor = new Color(137, 58, 18);
    private final Color shadowColor = new Color(0, 0, 0, 5);
    private final Color target = new Color(0, 0, 0,0);
    private final Color glassColor = new Color(253, 206, 74);
    private final Color frameColor = new Color(254, 160, 26);
    private final Color gridColor = new Color(255, 122, 6,80);

	private BufferedImage carpetBuffer = null;
	private boolean carpetRendered = false;
	private final GraphicsUtils utils = new GraphicsUtils();
    private static final int W = 600, H = 600;
    final Timer timer;
    private float time = 0f;
    private final Random rnd = new Random();

    // Per-flame random flicker seeds
    private final float[] flameSeeds = new float[] { rnd.nextFloat()*10f, rnd.nextFloat()*10f, rnd.nextFloat()*10f, rnd.nextFloat()*10f };

    public Background() {
        setPreferredSize(new Dimension(W, H));
        setBackground(new Color(35, 32, 40)); // fallback
        timer = new Timer(33, this); // ~30 fps
        timer.start();
    }
	@Override
	public void actionPerformed(ActionEvent e) {
		time += 0.05f;
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
		drawBG(g);
        drawWall(g);
        drawFloor(g);
        drawCarpet(g);
        drawWindow(g);
        drawCurtain(g);
        drawChandeliersAndSconces(g);
		drawTable(g,94,350);
		drawLamp(g);
		drawChair(g);
    }
    
	private void drawChair(Graphics2D g) {
		Color bgColor = new Color(70, 68, 74);
		Color borderChair = new Color(97, 64, 54);
		Color wood = new Color(164, 67, 8);
		
		Path2D downcushion = new Path2D.Double();
		downcushion.moveTo(0, 366);
		downcushion.curveTo(20, 375, 10, 395, 15, 399);
		downcushion.lineTo(0,400);
		downcushion.lineTo(0,366);
		downcushion.closePath();
		g.setColor(bgColor);
		g.fill(downcushion);
		Path2D sitwood = new Path2D.Double();
		sitwood.moveTo(0, 401);
		sitwood.curveTo(125,390,125,417,0,411);
		sitwood.closePath();
		g.setColor(wood);
		g.fill(sitwood);
		Path2D curveWood = new Path2D.Double();
		curveWood.moveTo(0, 353);
		curveWood.curveTo(33, 365, 23, 390, 28, 399);
		curveWood.lineTo(17, 399);
		curveWood.curveTo(10, 395,20, 375, 0, 366);
		curveWood.closePath();
		g.setColor(wood);
		g.fill(curveWood);

		Path2D cushion = new Path2D.Double();
		cushion.moveTo(0, 290);
		cushion.quadTo(35,300,37,348);
		cushion.quadTo(104,335,87,380);
		cushion.quadTo(90,370,82,376);
		cushion.quadTo(79,370,80,384);
		cushion.quadTo(105,395,93,401);
		cushion.lineTo(30, 400);
		cushion.curveTo(23, 390,33, 365,  0, 353);
		cushion.lineTo(0,352);
		cushion.closePath();
		g.setColor(bgColor);
		g.fill(cushion);
		Path2D chairleg = new Path2D.Double();
		chairleg.moveTo(17, 412);
		chairleg.curveTo(15, 450, 25, 450,27, 412);
		chairleg.closePath();
		g.setColor(wood);
		g.fill(chairleg);
		Path2D chairleg2 = new Path2D.Double();
		chairleg2.moveTo(80, 412);
		chairleg2.curveTo(75, 450, 90, 450,88, 410);
		chairleg2.closePath();
		g.setColor(wood);
		g.fill(chairleg2);
		MyPath linepath = new MyPath();
		g.setColor(borderChair);
		linepath.moveTo(0, 290);
		linepath.quadTo(35,300,37,348);
		linepath.moveTo(30, 371);
		linepath.quadTo(33,352,37,348);
		linepath.quadTo(84,345,86,351);
		linepath.quadTo(95,372,80,378);
		linepath.quadTo(67,364,74,352);
		linepath.moveTo(23, 373);
		linepath.quadTo(61,370,79,380);
		linepath.quadTo(79,370,79,386);
		linepath.moveTo(25, 385);
		linepath.quadTo(105,384,93,401);
		linepath.lineTo(0, 399);
		linepath.moveTo(90, 402);
		linepath.quadTo(105,415,0,411);
		linepath.moveTo(0, 353);
		linepath.curveTo(33, 365, 23, 390, 28, 399);
		linepath.moveTo(0, 366);
		linepath.curveTo(20, 375, 10, 395, 15, 399);
		linepath.moveTo(17, 412);
		linepath.curveTo(15, 450, 25, 450,27, 412);
		linepath.moveTo(80, 412);
		linepath.curveTo(75, 450, 90, 450,88, 410);
		linepath.closePath();
		linepath.drawAll(g,2);
	}
	private void drawLamp(Graphics2D g) {
		Color baseLamp = new Color(255, 176, 24);
		Color ShaLamp = new Color(243, 104, 11);
        // Draw lampshade top
		MyPath TopLamp = new MyPath();
		g.setColor(new Color(255, 176, 24));
		TopLamp.moveTo(68, 224);
		TopLamp.lineTo(105, 224);
		TopLamp.lineTo(123, 281);
		TopLamp.quadTo(80,290,48, 282);
		TopLamp.closePath();
		TopLamp.fillPath(g,baseLamp, shadowColor, target);
        
		MyPath neckLamp = new MyPath();
		neckLamp.moveTo(90, 288);
		neckLamp.curveTo(85,295,95,295,90, 307);
		neckLamp.lineTo(98, 340);
		neckLamp.lineTo(71, 340);
		neckLamp.lineTo(81, 307);
		neckLamp.curveTo(74,295,86,300, 82, 288);
		neckLamp.closePath();
		neckLamp.fillPath(g,ShaLamp, new Color(182, 70, 8),target);

		//shadow in the top lamp
        g.setColor(ShaLamp);
		MyPath borderLamp = new MyPath();
		borderLamp.moveTo(71, 226);
		borderLamp.lineTo(103, 226);
		borderLamp.lineTo(118, 278);
		borderLamp.quadTo(80,290,51, 278);
		borderLamp.lineTo(71, 226);
		borderLamp.drawAll(g,6);
	
		utils.drawThickLine(g,82, 226, 74, 279,2);
		utils.drawThickLine(g,92, 226, 97, 279,2);
		
		MyPath borderthin = new MyPath();
		borderthin.moveTo(69, 223);
		borderthin.lineTo(105, 223);
		borderthin.lineTo(123, 281);
		borderthin.quadTo(80,295,47, 281);
		borderthin.closePath();
        g.setColor(new Color(182, 70, 8));
		borderthin.drawAll(g,2);
	}
	private void drawTable(Graphics2D g, int x, int y) {
		int tableTopWidth = 80;
		int tableTopHeight = 10;
		int legHeight = 60;
		int baseWidth = 80;
		int baseHeight = 10;
		
		// กำหนดรัศมีสำหรับ Ellipse
		int tableEllipseRx = tableTopWidth / 2;
		int tableEllipseRy = tableTopHeight / 2;
		int borderThickness = 2;

		Color tableTopColor = new Color(200, 69, 3); 
		Color outerColor = new Color(147, 55, 10);
		Color shadowColor = new Color(0, 0, 0, 80);

		// --- วาดส่วนบนของโต๊ะ (First Layer as an Ellipse) ---
		int tableTop1CenterX = x;
		int tableTop1CenterY = y - tableTopHeight / 2;

		// เติมวงรีส่วนบน
		utils.fillEllipse(g, tableTop1CenterX, tableTop1CenterY, tableEllipseRx, tableEllipseRy, tableTopColor);
		utils.midpointEllipse(g, tableTop1CenterX, tableTop1CenterY, tableEllipseRx, tableEllipseRy, outerColor, borderThickness);

		// --- วาดส่วนบนของโต๊ะ (Second Layer as an Ellipse) ---
		int tableTop2CenterX = x;
		int tableTop2CenterY = y + 10 - tableTopHeight / 2; // Offset in Y

		// เติมวงรีส่วนบนชั้นที่สอง
		utils.fillEllipse(g, tableTop2CenterX, tableTop2CenterY, tableEllipseRx, tableEllipseRy, tableTopColor);
		utils.midpointEllipse(g, tableTop2CenterX, tableTop2CenterY, tableEllipseRx, tableEllipseRy, outerColor, borderThickness);

		// --- วาดขาโต๊ะ (Leg) ---
		g.setColor(outerColor);
		MyPath path3 = new MyPath();
		path3.moveTo(126, 361);
		path3.lineTo(112,361);
		path3.curveTo(118, 440,126, 440,126, 361);
		path3.closePath();
		path3.drawAll(g, 2);
		BufferedImage buffer = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = buffer.createGraphics();

		g2.setColor(outerColor); 
		path3.drawAll(g2, 1);
		utils.floodFill(buffer, 120, 390, target, tableTopColor);
		g.drawImage(buffer, 0, 0, null);
		g2.dispose();

		// --- วาดเงา (Shadow as an Ellipse) ---
		int shadowEllipseRx = (baseWidth) / 2; 
		int shadowEllipseRy = (baseHeight-2) / 2;
		int shadowCenterX = x - (baseWidth / 2) + shadowEllipseRx;
		int shadowCenterY = y + tableTopHeight / 2 + legHeight+5 + shadowEllipseRy;

		utils.fillEllipse(g, shadowCenterX, shadowCenterY, shadowEllipseRx, shadowEllipseRy, shadowColor);
	}
	private void drawChandeliersAndSconces(Graphics2D g) {
		drawChandelier(g, 206, 200, 3, 0);
		drawChandelier(g, 558, 200, 3, 0);
		drawChandelier(g, 460, 56, 4, 0);
	}
    private void drawChandelier(Graphics2D g,int x, int y, int arms, int seed) {
        // central bar
		utils.fillEllipse(g, x, y - 5, 8, 5, new Color(187,74,10));
        utils.myFillRect(g,x-3, y-10, 6, 35);

        // arms and candles
        int armLen = 40;
        for (int i = 0; i < arms; i++) {
			//arm
            double a = Math.toRadians(-60 + i * (120.0 / (arms - 1)));
			int ax = x + (int)(Math.sin(a) * armLen);
			int ay = y + (int)(Math.cos(a) * (armLen));

			// จุดควบคุม (control point) สำหรับความโค้ง
			int cx = (x + ax) / 2;           // จุดกลางระหว่างต้นและปลาย
			int cy = y + 60;                 // ยกขึ้นเพื่อให้โค้ง

			g.setStroke(new BasicStroke(6f));
			g.setColor(new Color(187,74,10));

			// วาดโค้ง
			Point p1 = new Point(x, y + 15);     // Start point
			Point p2 = new Point(cx, cy);         // Control point
			Point p3 = new Point(ax, ay + 15);    // End point

			utils.quadraticThickBezier(g, p1, p2, p3,3);

            // candle base
            g.setColor(new Color(255,193,5));
            utils.myFillRect(g,ax-6, ay+8, 12, 18);
			g.setColor(new Color(125,59,18));
			utils.myDrawRect(g,ax-6, ay+8, 12, 18,1);

            // flame with flicker
            drawFlame(g, ax, ay, 12, i);
        }
    }
	private void drawFlame(Graphics2D g, int x, int y, int size, int flameIndex) {
		float seed = flameSeeds[flameIndex % flameSeeds.length];
		float currentTime = AnimationTime.getTime(); // ใช้ global time แทน instance variable
		float jitter = (float)(Math.sin(currentTime * 10 + seed * 5.0) * 3.0);
		int fx = x;
		int fy = y - 6 + (int)jitter;

		float ovalWidth = size * 1.6f;
		float ovalHeight = size * 1.6f;
		float ovalX_topLeft = fx - ovalWidth / 2;
		float ovalY_topLeft = fy - ovalHeight / 2;

		int ellipseCenterX = (int)(ovalX_topLeft + ovalWidth / 2); // = (int)fx
		int ellipseCenterY = (int)(ovalY_topLeft + ovalHeight / 2); // = (int)fy
		int ellipseRadiusX = (int)(ovalWidth / 2);
		int ellipseRadiusY = (int)(ovalHeight / 2);

		utils.fillEllipse(g, ellipseCenterX, ellipseCenterY, ellipseRadiusX, ellipseRadiusY, g.getColor());
		Path2D flame = new Path2D.Float();
		flame.moveTo(fx, fy - size / 2);
		flame.curveTo(
			(int)(fx + size * 0.5),
			(int)(fy - size * 0.2),
			(int)(fx + size * 0.2),
			(int)(fy + size * 0.6),
			(int)fx,
			(int)(fy + size * 0.8)
		);
		flame.curveTo(
			(int)(fx - size * 0.2),
			(int)(fy + size * 0.6),
			(int)(fx - size * 0.5),
			(int)(fy - size * 0.2),
			(int)fx,
			(int)(fy - size / 2)
		);
		GradientPaint flamePaint = new GradientPaint(fx, fy - size, new Color(255, 230, 120), fx, fy + size, new Color(255, 120, 30));
		g.setPaint(flamePaint);
		g.fill(flame); 
		// inner core
		g.setColor(new Color(255, 250, 200)); 
		int coreRx = (int)(size * 0.25f); // Half of size*0.5f (width)
		int coreRy = (int)(size * 0.45f); // Half of size*0.9f (height)
		
		int coreCenterX = fx;
		int coreCenterY = fy - (int)(size * 0.05f); 

		utils.fillEllipse(g, coreCenterX, coreCenterY, coreRx, coreRy, g.getColor()); 
	}
	private void drawBG(Graphics2D g){
    	g.setColor(bgColor);
    	utils.myFillRect(g, 0, 0, 600, 600);
	}
    private void drawStrokedRect(Graphics2D g2d, int x, int y, int width, int height, Color strokeColor, int strokeWidth) {
		g2d.setColor(strokeColor);
		utils.myDrawRect(g2d, x, y, width, height,strokeWidth);
	}
	private void drawWall(Graphics2D g) {
        g.setColor(wall); 
		utils.myFillRect(g,121, 17, 600 - 121, 405 - 17);    
		drawStrokedRect(g, 121, 17, 600 - 121, 405 - 17, Color.darkGray, 1);
		//upbar
        g.setColor(wallLine);
		utils.myFillRect(g,121, 17, 600 - 121, 30 - 17);    
		drawStrokedRect(g, 121, 17, 600 - 121, 30 - 17, Color.darkGray, 1);
		//lowbar
        g.setColor(new Color(55, 61, 71));
		utils.myFillRect(g,121, 389, 600 - 121, 405 - 389);  
		drawStrokedRect(g, 121, 389, 600 - 121, 405 - 389, Color.darkGray, 1);
		//line1
        g.setColor(wallLine); 
		utils.myFillRect(g,121, 57, 600 - 121, 63 - 57);
		drawStrokedRect(g, 121, 57, 600 - 121, 63 - 57, Color.darkGray, 1);
		//line2
		g.setColor(wallLine);
		utils.myFillRect(g,121, 284, 600 - 121, 290 - 284);
		drawStrokedRect(g,121, 284, 600 - 121, 290 - 284, Color.darkGray, 1);
		//squareout
		drawStrokedRect(g,286, 79, 494 - 286, 261 - 79, wallLine, 3);
		drawStrokedRect(g,286, 305, 494 - 286, 372 - 305, wallLine, 3);
		drawStrokedRect(g,153, 305, 266 - 153, 372 - 305, wallLine, 3);
       	drawStrokedRect(g,511, 305, 600 - 511, 372 - 305, wallLine, 3);
		//superharedCorner
        MyPath path = new MyPath();
        int x = 153; int y = 79; int width = 266-x; int height = 261-y;
        int cornerDepth = 20; // How deep the curve goes in
        // 1. Start at the top-left straight section
        path.moveTo(x + cornerDepth, y);
        path.lineTo(x + width - cornerDepth, y);
        path.append(new Arc2D.Double(x+width-cornerDepth, y-cornerDepth , 2*cornerDepth, 2*cornerDepth, 180, 90, Arc2D.OPEN), true); // Top-right corner
        
		path.lineTo(x + width, y + height - cornerDepth);
        path.append(new Arc2D.Double(x + width-cornerDepth, y+height-cornerDepth,2*cornerDepth,2*cornerDepth, 90, 90, Arc2D.OPEN), true); // Top-right corner

		path.lineTo(x + cornerDepth, y + height);
        path.append(new Arc2D.Double(x-cornerDepth, y+ height - cornerDepth, 2*cornerDepth, 2*cornerDepth, 0, 90, Arc2D.OPEN), true); // Top-right corner

		path.lineTo(x, y + cornerDepth);
        path.append(new Arc2D.Double(x-cornerDepth, y-cornerDepth, 2*cornerDepth, 2*cornerDepth, 270, 90, Arc2D.OPEN), true); // Top-right corner
        
		g.setColor(wallLine);
        path.drawAll(g,3);

		x = 511; y = 79; width = 113; height = 261-y;
        cornerDepth = 20;
        path.moveTo(x + cornerDepth, y);
        path.lineTo(x + width - cornerDepth, y);
        path.append(new Arc2D.Double(x+width-cornerDepth, y-cornerDepth , 2*cornerDepth, 2*cornerDepth, 180, 90, Arc2D.OPEN), true); // Top-right corner
        path.lineTo(x + width, y + height - cornerDepth);
        path.append(new Arc2D.Double(x + width-cornerDepth, y+height-cornerDepth,2*cornerDepth,2*cornerDepth, 90, 90, Arc2D.OPEN), true); // Top-right corner
        path.lineTo(x + cornerDepth, y + height);
        path.append(new Arc2D.Double(x-cornerDepth, y+ height - cornerDepth, 2*cornerDepth, 2*cornerDepth, 0, 90, Arc2D.OPEN), true); // Top-right corner
        path.lineTo(x, y + cornerDepth);
        path.append(new Arc2D.Double(x-cornerDepth, y-cornerDepth, 2*cornerDepth, 2*cornerDepth, 270, 90, Arc2D.OPEN), true); // Top-right corner

		g.setColor(wallLine);
        path.drawAll(g,3);
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
            utils.drawThickLine(g,0, y+ currentShadowOffset-5, 600, y+ currentShadowOffset-5,1);

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
            utils.drawThickLine(g,335, 0,  x +currentShadowOffset -5, 600,1);
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
			utils.midpointEllipse(tempG2d, xc, yc, rx, ry, new Color(127, 46, 17, 50), 10);
			utils.midpointEllipse(tempG2d, xc+10, yc+10, rx, ry, new Color(168, 52, 18, 50), 20);
			
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
    private void drawCurtain(Graphics2D g) {
         // วาดราวแขวนผ้าม่าน
		MyPath path = new MyPath();
        g.setColor(new Color(100, 70, 50));
        utils.drawThickLine(g,0, 0, 115, 18,5);

        // วาดผ้าม่านด้านซ้าย
		Color baseColor = new Color(180, 50, 40);
        g.setColor(baseColor);
        path.moveTo(0, 0);
        path.lineTo(60, 11);
        path.quadTo(70, 90, 0, 220);
		path.closePath();
        path.fillPath(g, baseColor, new Color(150, 0, 0), target);
        
        // วาดผ้าม่านด้านขวา
		MyPath path1 = new MyPath();
		g.setColor(baseColor);
        path1.moveTo(60, 11);
        path1.lineTo(115, 18);
		path1.quadTo(130,135,115, 250);
        path1.quadTo(55, 115, 60, 12);
		path1.closePath();
        path1.fillPath(g, baseColor, new Color(150, 0, 0), target);
        
		MyPath path2 = new MyPath();
		g.setColor(baseColor); 
        path2.moveTo(111, 262);
		path2.quadTo(90, 370, 93, 398);
		path2.quadTo(113, 388, 118, 401);
		path2.quadTo(135, 420, 148, 404);
        path2.quadTo(122, 260, 120, 261);
		path2.closePath();
        path2.fillPath(g, baseColor, new Color(150, 0, 0), target);
        
        // วาดวงกลมแบบมีสีเติม
		utils.fillEllipse(g,115, 250, 10, 10,baseColor);
		utils.midpointEllipse(g, 115, 250, 10, 10, new Color(150, 0, 0), 3);    
	}
}
