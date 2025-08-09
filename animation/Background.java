package Cat5sec.animation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.*;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;

public class Background extends JPanel implements ActionListener {
    
    private static final int W = 600, H = 600;
    private final Timer timer;
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
        // เรียกใช้เมธอด draw จากที่นี่
        draw(g2d);
    }
    // เมธอด draw ที่ใช้เรียกเมธอดวาดส่วนต่างๆ
    public void draw(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // เมธอดอื่นๆ ที่คุณสร้างไว้ เช่น drawBG, drawWall, etc.
        drawBG(g);
        drawWall(g);
        drawFloor(g);
        drawCarpet(g);
        drawWindow(g);
        drawCurtain(g);
        drawChandeliersAndSconces(g);// เรียกเมธอดวาดหลอดไฟ
		drawTable(g,94,345);
    }
    
	private void drawTable(Graphics2D g, int x, int y) {
        int tableTopWidth = 80;
		int tableTopHeight = 10;
		int legWidth = 14;
		int legHeight = 60;
		int baseWidth = 80;
		int baseHeight = 10;
		
		// ค่าความโค้งมนของมุม
		int cornerArc = 10; 
		
		Color tableTopColor = new Color(200, 69, 3); // SaddleBrown
		Color outerColor = new Color(147,55,10); // Brown
		
		// วาดส่วนบนของโต๊ะด้วย RoundRectangle2D.Double
		g.setColor(tableTopColor);
		RoundRectangle2D tableTop = new RoundRectangle2D.Double(
			x - tableTopWidth / 2, 
			y - tableTopHeight / 2, 
			tableTopWidth, 
			tableTopHeight, 
			cornerArc, 
			cornerArc
		);
		g.fill(tableTop);
		g.setStroke(new BasicStroke(1.5f));
		g.setColor(outerColor);
		g.draw(tableTop);
		g.setColor(tableTopColor);
		tableTop = new RoundRectangle2D.Double(
			x - tableTopWidth / 2, 
			y+10 - tableTopHeight / 2, 
			tableTopWidth, 
			tableTopHeight, 
			cornerArc, 
			cornerArc
		);
		g.fill(tableTop);
		g.setColor(outerColor);
		g.setStroke(new BasicStroke(1.5f));
		g.draw(tableTop);

		// วาดขาโต๊ะ (Leg)
		g.setColor(tableTopColor);
		g.fill(new Rectangle2D.Double(x - legWidth / 2, y+10 + tableTopHeight / 2, legWidth, legHeight));

		g.setColor(outerColor);
		g.draw(new Rectangle2D.Double(x - legWidth / 2, y+10 + tableTopHeight / 2, legWidth, legHeight));
		// // วาดฐานของโต๊ะด้วย RoundRectangle2D.Double
		// g.setColor(tableTopColor);
		// RoundRectangle2D base = new RoundRectangle2D.Double(
		// 	x - baseWidth / 2, 
		// 	y + tableTopHeight / 2 + legHeight, 
		// 	baseWidth, 
		// 	baseHeight, 
		// 	cornerArc, 
		// 	cornerArc
		// );
		// g.fill(base);
		
		// วาดเงาเพื่อเพิ่มมิติ
		Color shadowColor = new Color(0, 0, 0, 80);
		g.setColor(shadowColor);
		RoundRectangle2D shadow = new RoundRectangle2D.Double(
			x - baseWidth / 2 + 10, 
			y + tableTopHeight / 2 + legHeight + 15, 
			baseWidth - 20, 
			baseHeight - 10,
			cornerArc,
			cornerArc
		);
		g.fill(shadow);
	}
		private void drawChandeliersAndSconces(Graphics2D g) {
        // chandelier top center
        drawChandelier(g, 206, 200, 3, 0);
		drawChandelier(g, 558, 200, 3, 0);
		drawChandelier(g, 460, 56, 4, 0);
    }

    private void drawChandelier(Graphics2D g, int x, int y, int arms, int seed) {
        // central bar
        g.setColor(new Color(187,74,10));
		g.fillOval(x-8, y-10, 16, 10);
        g.fillRect(x-3, y-10, 6, 35);

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
			QuadCurve2D armCurve = new QuadCurve2D.Float(
				x, y + 15, // จุดเริ่ม
				cx, cy,    // จุดควบคุม (กำหนดให้สูงขึ้น/ต่ำลงได้)
				ax, ay + 15 // จุดปลาย
			);
			g.draw(armCurve);

            // candle base
            g.setColor(new Color(255,193,5));
            g.fillRect(ax-6, ay+8, 12, 18);
			g.setColor(new Color(125,59,18));
			g.setStroke(new BasicStroke(1));
			g.drawRect(ax-6, ay+8, 12, 18);


            // flame with flicker
            drawFlame(g, ax, ay, 12, i);
        }
    }

    private void drawFlame(Graphics2D g, int x, int y, int size, int flameIndex) {
		float seed = flameSeeds[flameIndex % flameSeeds.length];
		float jitter = (float)(Math.sin(time * 7 + seed * 3.3) * 2.0);
		int fx = x;
		int fy = y - 6 + (int)jitter;

		// outer glow
		RadialGradientPaint glow = new RadialGradientPaint(
			new Point2D.Float(fx, fy),
			size*1.6f,
			new float[] { 0f, 1f },
			new Color[] { new Color(255,200,100,120), new Color(255,200,100,0) }
		);
		g.setPaint(glow);
		g.fill(new Ellipse2D.Float(fx - size*1.6f/2, fy - size*1.6f/2, size*1.6f, size*1.6f));

		// inner flame
		Path2D flame = new Path2D.Float();
		flame.moveTo(fx, fy - size/2);
		flame.curveTo(fx + size*0.5, fy - size*0.2, fx + size*0.2, fy + size*0.6, fx, fy + size*0.8);
		flame.curveTo(fx - size*0.2, fy + size*0.6, fx - size*0.5, fy - size*0.2, fx, fy - size/2);
		GradientPaint flamePaint = new GradientPaint(fx, fy - size, new Color(255,230,120), fx, fy + size, new Color(255,120,30));
		g.setPaint(flamePaint);
		g.fill(flame);

		// inner core
		Shape core = new Ellipse2D.Float(fx - size*0.25f, fy - size*0.5f, size*0.5f, size*0.9f);
		g.setColor(new Color(255,250,200));
		g.fill(core);
	}

	private void drawBG(Graphics2D g) {
        // วาดพื้นหลัง
        g.setColor(new Color(144, 80, 39)); // สีน้ำเงินเข้ม
        g.fillRect(0, 0, 600, 600);
    }

    private void drawWall(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		int x = 121;                  // จุดซ้ายบน
		int y = 17;
		int width = 600 - 121;        // 815
		int height = 405 - 17;         // 659
        g.setColor(new Color(58, 62, 68)); // กำหนดสีที่ต้องการ
		g.fillRect(x, y, width, height);      // วาดสี่เหลี่ยมทึบ
		g.setStroke(new BasicStroke(1.5f));
		g.setColor(Color.darkGray);
		g.drawRect(x, y, width, height);
		//upbar
		x = 121;                  // จุดซ้ายบน
		y = 17;
		width = 600 - 121;        // 815
		height = 30 - 17;         // 659
        g.setColor(new Color(172, 64, 4)); // กำหนดสีที่ต้องการ
		g.fillRect(x, y, width, height);      // วาดสี่เหลี่ยมทึบ
		g.setColor(Color.darkGray);
        g.setStroke(new BasicStroke(1.5f)); // The stroke thickness is 5 pixels
        g.drawRect(x, y, width, height);
		//lowbar
		x = 121;                  // จุดซ้ายบน
		y = 389;
		width = 600 - x;        // 815
		height = 405 - y;         // 659
        g.setColor(new Color(55, 61, 71)); // กำหนดสีที่ต้องการ
		g.fillRect(x, y, width, height);      // วาดสี่เหลี่ยมทึบ
		g.setColor(Color.DARK_GRAY);
        g.setStroke(new BasicStroke(1.5f)); // The stroke thickness is 5 pixels
        g.drawRect(x, y, width, height);
		//line1
		x = 121;                  // จุดซ้ายบน
		y = 57;
		width = 600 - 121;        // 815
		height = 63 - 57;         // 659
        g.setColor(new Color(172, 64, 4)); // กำหนดสีที่ต้องการ
		g.fillRect(x, y, width, height);
		g.setStroke(new BasicStroke(1.5f));
		g.setColor(Color.darkGray);
		g.drawRect(x, y, width, height);
		//line2
		x = 121;                  // จุดซ้ายบน
		y = 284;
		width = 600 - 121;        // 815
		height = 290 - 284;         // 659
        g.setColor(new Color(172, 64, 4)); // กำหนดสีที่ต้องการ
		g.fillRect(x, y, width, height);
		g.setColor(Color.darkGray);
		g.setStroke(new BasicStroke(1.5f));
		g.drawRect(x, y, width, height);
		//squareout
		x = 286;                  // จุดซ้ายบน
		y = 79;
		width = 494 - 286;        // 815
		height = 261 - 79;         // 659
        g.setColor(new Color(172, 64, 4)); // กำหนดสีที่ต้องการ
		// g.fillRect(x, y, width, height);
		// g.setColor(Color.darkGray);
		g.setStroke(new BasicStroke(5));
		g.drawRect(x, y, width, height);
		//squarein
		// x = 291;                  // จุดซ้ายบน
		// y = 84;
		// width = 489 - 291;        // 815
		// height = 256 - 84;         // 659
        // g.setColor(new Color(58, 62, 68)); // กำหนดสีที่ต้องการ
		// // g.fillRect(x, y, width, height);
		// // g.setColor(Color.darkGray);
		// g.setStroke(new BasicStroke(5));
		// g.drawRect(x, y, width, height);
		//squareout
		x = 286;                  // จุดซ้ายบน
		y = 305;
		width = 494 - x;        // 815
		height = 372 - y;         // 659
        g.setColor(new Color(172, 64, 4)); // กำหนดสีที่ต้องการ
		// g.fillRect(x, y, width, height);
		// g.setColor(Color.darkGray);
		g.setStroke(new BasicStroke(5));
		g.drawRect(x, y, width, height);
		// //squarein
		// x = 291;                  // จุดซ้ายบน
		// y = 310;
		// width = 489 - x;        // 815
		// height = 367 - y;         // 659
        // g.setColor(new Color(58, 62, 68)); // กำหนดสีที่ต้องการ
		// g.fillRect(x, y, width, height);
		// g.setColor(Color.darkGray);
		// g.setStroke(new BasicStroke(1.5f));
		// g.drawRect(x, y, width, height);
		//squareout
		x = 153;                  // จุดซ้ายบน
		y = 305;
		width = 266 - x;        // 815
		height = 372 - y;         // 659
        g.setColor(new Color(172, 64, 4)); // กำหนดสีที่ต้องการ
		// g.fillRect(x, y, width, height);
		// g.setColor(Color.darkGray);
		g.setStroke(new BasicStroke(5));
		g.drawRect(x, y, width, height);
		// //squarein
		// x = 158;                  // จุดซ้ายบน
		// y = 310;
		// width = 261 - x;        // 815
		// height = 367 - y;         // 659
        // g.setColor(new Color(58, 62, 68)); // กำหนดสีที่ต้องการ
		// g.fillRect(x, y, width, height);
		// g.setColor(Color.darkGray);
		// g.setStroke(new BasicStroke(1.5f));
		// g.drawRect(x, y, width, height);
		//squareout
		x = 511;                  // จุดซ้ายบน
		y = 305;
		width = 600-511;        // 815
		height = 372 - y;         // 659
        g.setColor(new Color(172, 64, 4)); // กำหนดสีที่ต้องการ
		g.setStroke(new BasicStroke(5));
		g.drawRect(x, y, width, height);
		// //squarein
		// x = 516;                  // จุดซ้ายบน
		// y = 310;
		// width = 600 - x;        // 815
		// height = 367 - y;         // 659
        // g.setColor(new Color(58, 62, 68)); // กำหนดสีที่ต้องการ
		// g.fillRect(x, y, width, height);
		// g.setColor(Color.darkGray);
		// g.setStroke(new BasicStroke(1.5f));
		// g.drawRect(x, y, width, height);
		//superharedCorner
        Path2D path = new Path2D.Double();
        x = 153; y = 79; width = 266-x; height = 261-y;
        int cornerDepth = 20; // How deep the curve goes in

        // 1. Start at the top-left straight section
        path.moveTo(x + cornerDepth, y);
        // 2. Draw the top edge (straight line)
        path.lineTo(x + width - cornerDepth, y);
        path.append(new Arc2D.Double(x+width-cornerDepth, y-cornerDepth , 2*cornerDepth, 2*cornerDepth, 180, 90, Arc2D.OPEN), true); // Top-right corner

        // 4. Draw the right edge (straight line)
        path.lineTo(x + width, y + height - cornerDepth);

        // // 5. Draw the bottom-right curve
        path.append(new Arc2D.Double(x + width-cornerDepth, y+height-cornerDepth,2*cornerDepth,2*cornerDepth, 90, 90, Arc2D.OPEN), true); // Top-right corner

        // // 6. Draw the bottom edge (straight line)
        path.lineTo(x + cornerDepth, y + height);

        // // 7. Draw the bottom-left curve
        path.append(new Arc2D.Double(x-cornerDepth, y+ height - cornerDepth, 2*cornerDepth, 2*cornerDepth, 0, 90, Arc2D.OPEN), true); // Top-right corner

        // // 8. Draw the left edge (straight line)
        path.lineTo(x, y + cornerDepth);

        // // 9. Draw the top-left curve
        path.append(new Arc2D.Double(x-cornerDepth, y-cornerDepth, 2*cornerDepth, 2*cornerDepth, 270, 90, Arc2D.OPEN), true); // Top-right corner

        path.closePath(); // Connect back to the starting point

        // Draw a thick orange border
        g.setColor(new Color(172, 64, 4));
        g.setStroke(new BasicStroke(5));
        g.draw(path);

		x = 511; y = 79; width = 113; height = 261-y;
        cornerDepth = 20; // How deep the curve goes in

        // 1. Start at the top-left straight section
        path.moveTo(x + cornerDepth, y);
        // 2. Draw the top edge (straight line)
        path.lineTo(x + width - cornerDepth, y);
        path.append(new Arc2D.Double(x+width-cornerDepth, y-cornerDepth , 2*cornerDepth, 2*cornerDepth, 180, 90, Arc2D.OPEN), true); // Top-right corner

        // 4. Draw the right edge (straight line)
        path.lineTo(x + width, y + height - cornerDepth);

        // // 5. Draw the bottom-right curve
        path.append(new Arc2D.Double(x + width-cornerDepth, y+height-cornerDepth,2*cornerDepth,2*cornerDepth, 90, 90, Arc2D.OPEN), true); // Top-right corner

        // // 6. Draw the bottom edge (straight line)
        path.lineTo(x + cornerDepth, y + height);

        // // 7. Draw the bottom-left curve
        path.append(new Arc2D.Double(x-cornerDepth, y+ height - cornerDepth, 2*cornerDepth, 2*cornerDepth, 0, 90, Arc2D.OPEN), true); // Top-right corner

        // // 8. Draw the left edge (straight line)
        path.lineTo(x, y + cornerDepth);

        // // 9. Draw the top-left curve
        path.append(new Arc2D.Double(x-cornerDepth, y-cornerDepth, 2*cornerDepth, 2*cornerDepth, 270, 90, Arc2D.OPEN), true); // Top-right corner

        path.closePath(); // Connect back to the starting point

        // Draw a thick orange border
        g.setColor(new Color(172, 64, 4));
        g.setStroke(new BasicStroke(5));
        g.draw(path);
    }

    private void drawFloor(Graphics2D g) {        
        // Define colors
        Color tileColor = new Color(173, 79, 15);
        Color groutColor = new Color(137, 58, 18);
        Color shadowColor = new Color(0, 0, 0, 10); // Semi-transparent black

        // Fill the background with the tile color
        g.setColor(tileColor);
        g.fillRect(0, 405, 600, 600-405);

        int lineSpacing = 37; // The size of each floor tile
        int shadowOffset = 2; // How far the shadow line is offset

        // Loop to draw horizontal grout lines
        int i = 0;
        for (int y = 405; y < 600; y += lineSpacing) {
            
            // Calculate the current shadow offset based on the loop counter
            int currentShadowOffset = shadowOffset * (i + 3);

            // 1. Draw the shadow line
            g.setColor(shadowColor);
            g.setStroke(new BasicStroke(10-i));
            g.drawLine(0, y + currentShadowOffset, 600, y + currentShadowOffset);

            // 2. Draw the main line
            g.setColor(groutColor);
            g.setStroke(new BasicStroke(2));
            g.drawLine(0, y+ currentShadowOffset-5, 600, y+ currentShadowOffset-5);

            i++; // Increment the counter for the next line's shadow
        }
		g.setClip(new Rectangle(0, 405, 600, 195));

		lineSpacing = 120;
        // Loop to draw vertical grout lines
        int j = 0;
        for (int x = 0; x <= 600; x += lineSpacing) {
            
            // Calculate the current shadow offset based on the loop counter
            int currentShadowOffset = shadowOffset * (j + 3);

            // 1. Draw the shadow line
            g.setColor(shadowColor);
            g.setStroke(new BasicStroke(10-j));
            g.drawLine(335, 0, x +currentShadowOffset, 600);

            // 2. Draw the main line
            g.setColor(groutColor);
            g.setStroke(new BasicStroke(2));
            g.drawLine(335, 0,  x +currentShadowOffset -5, 600);

            i++; // Increment the counter for the next line's shadow
        }
		g.setClip(null);
    }

    private void drawCarpet(Graphics2D g) {
        g.setColor(new Color(180, 50, 40));
        g.fillOval(-50, 475, 700, 200);
		g.setColor(new Color(127, 46, 17));
		g.drawOval(-50, 475, 700, 200);
		g.setColor(new Color(127, 46, 17,60));
		g.setStroke(new BasicStroke(20));
		g.drawOval(-40, 485, 700, 200);
    }

    private void drawWindow(Graphics2D g) {
        // Window dimensions and position
		int[] xPoints = {0, 115, 115, 0};
		int[] yPoints = {0, 17, 405, 438};

		g.setColor(new Color(58, 62, 68)); 
		g.fillPolygon(xPoints, yPoints, 4); // วาดสี่เหลี่ยมที่ไม่จำเป็นต้องตั้งฉาก
		g.setStroke(new BasicStroke(1.5f));
		g.setColor(Color.darkGray);
		g.drawPolygon(xPoints, yPoints, 4);

		int[] x1 = {0, 115, 115, 0};
		int[] y1 = {0, 17, 30, 13};
		g.setColor(new Color(172, 64, 4)); 
		g.fillPolygon(x1, y1, 4); // วาดสี่เหลี่ยมที่ไม่จำเป็นต้องตั้งฉาก
		g.setStroke(new BasicStroke(1.5f));
		g.setColor(Color.darkGray);
		g.drawPolygon(x1, y1, 4);

		int[] x2 = {0, 115, 115, 0};
		int[] y2 = {425, 392, 405, 438};
		g.setColor(new Color(55, 61, 71)); 
		g.fillPolygon(x2, y2, 4); // วาดสี่เหลี่ยมที่ไม่จำเป็นต้องตั้งฉาก
		g.setStroke(new BasicStroke(1.5f));
		g.setColor(Color.darkGray);
		g.drawPolygon(x2, y2, 4);

		   // Colors
        Color glassColor = new Color(253, 206, 74); // Simulates a warm, lit-up window
        Color frameColor = new Color(254, 160, 26); // Darker brown for the frame
        Color gridColor = new Color(255, 122, 6,80); // Same color for the grid lines
        
        g.setColor(glassColor);
		int[] x3 = {10,105,105, 10};
		int[] y3 = {60, 70, 310, 335};
		g.fillPolygon(x3, y3, 4); // วาดสี่เหลี่ยมที่ไม่จำเป็นต้องตั้งฉาก
		g.setColor(gridColor);
		g.setStroke(new BasicStroke(3));
		g.drawPolygon(x3, y3, 4); // วาดสี่เหลี่ยมที่ไม่จำเป็นต้องตั้งฉาก
        // 2. Draw the thick outer window frame
        int frameThickness = 11;
        g.setColor(frameColor);
        g.setStroke(new BasicStroke(frameThickness));
		g.drawPolygon(x3, y3, 4);
		g.setColor(gridColor);
		int[] x31 = {15,100,100, 15};
		int[] y31 = {65, 75, 305, 330};
		g.setStroke(new BasicStroke(3));
		g.drawPolygon(x31, y31, 4); // วาดสี่เหลี่ยมที่ไม่จำเป็นต้องตั้งฉาก
		int[] x4 = {20,95,95, 20};
		int[] y4 = {140, 145, 305, 325};
		
		g.setColor(frameColor);
		g.setStroke(new BasicStroke(frameThickness));
		g.drawPolygon(x4, y4, 4); // วาดสี่เหลี่ยมที่ไม่จำเป็นต้องตั้งฉาก
		int[] x5 = {15,100,100, 15};
		int[] y5 = {135, 140, 305, 330};
		g.setColor(gridColor);
		g.setStroke(new BasicStroke(3));
		g.drawPolygon(x5, y5, 4); // วาดสี่เหลี่ยมที่ไม่จำเป็นต้องตั้งฉาก

        g.setColor(frameColor);
		g.setStroke(new BasicStroke(frameThickness));
		g.drawLine(55, 145, 55, 312);

        g.setColor(gridColor);
		g.setStroke(new BasicStroke(3));
		g.drawLine(50, 145, 50, 312);

		g.setColor(gridColor);
		g.setStroke(new BasicStroke(3));
		g.drawLine(60, 145, 60, 312);

		 g.setColor(frameColor);
		g.setStroke(new BasicStroke(frameThickness));
		g.drawLine(15, 225, 100, 220);

        g.setColor(gridColor);
		g.setStroke(new BasicStroke(3));
		g.drawLine(15, 220, 100, 215);

		g.setColor(gridColor);
		g.setStroke(new BasicStroke(3));
		g.drawLine(15, 230, 100, 225);
    }

    private void drawCurtain(Graphics2D g) {
         // วาดราวแขวนผ้าม่าน
		GeneralPath path = new GeneralPath();
        g.setColor(new Color(100, 70, 50)); // สีของราว
        g.setStroke(new BasicStroke(5));
        g.drawLine(0, 0, 115, 18);

        // วาดผ้าม่านด้านซ้าย
		Color baseColor = new Color(180, 50, 40); // สีผ้าม่านหลัก
        g.setColor(baseColor); // สีแดงอ่อน
        path.moveTo(0, 0);
        path.lineTo(60, 11);
        path.quadTo(70, 90, 0, 220);
        path.closePath();
        g.fill(path);
        
        // 7. วาดเส้นขอบ
        g.setColor(new Color(150, 0, 0));
        g.setStroke(new BasicStroke(3));
        g.draw(path);
        // วาดผ้าม่านด้านขวา
		g.setColor(baseColor); // สีแดงอ่อน
        path.moveTo(60, 11);
        path.lineTo(115, 18);
		path.quadTo(130,135,115, 250);
        path.quadTo(55, 115, 60, 12);
        path.closePath();
        g.fill(path);
        
        // 7. วาดเส้นขอบ
        g.setColor(new Color(150, 0, 0));
        g.setStroke(new BasicStroke(3));
        g.draw(path);
		// วาดผ้าม่านด้านขวา

		// วาดเส้นโค้งที่ประกอบจากเส้นตรง
		g.setColor(baseColor); // สีแดงอ่อน
        path.moveTo(111, 262);
		path.quadTo(90, 370, 93, 398);
		path.quadTo(113, 388, 118, 401);
		path.quadTo(135, 420, 148, 404);
        path.quadTo(122, 260, 120, 261);
        path.closePath();
        g.fill(path);
        
        // 7. วาดเส้นขอบ
        g.setColor(new Color(150, 0, 0));
        g.setStroke(new BasicStroke(3));
        g.draw(path);
		g.setColor(baseColor); // สีแดงอ่อน
        // คำนวณพิกัดมุมบนซ้ายของวงกลมจากจุดศูนย์กลางและรัศมี
        int x = 115 - 10;
        int y = 250 - 10;
        int diameter = 10 * 2;

        // วาดวงกลมแบบมีสีเติม
        g.fillOval(x, y, diameter, diameter);

        // วาดเส้นขอบวงกลมทับลงไป
		g.setColor(new Color(150, 0, 0));
        g.setStroke(new BasicStroke(3));
        g.drawOval(x, y, diameter, diameter);
    }
}
