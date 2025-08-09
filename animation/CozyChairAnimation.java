package Cat5sec.animation;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.Random;

/**
 * CozyChairAnimation.java
 *
 * Single-file Java 2D animation (600x600) showing a cozy room and a purple armchair.
 * - Uses only Java 2D (Swing + Graphics2D).
 * - Animated: candle/chandelier flame flicker, lamp glow pulsing, chair gentle bob.
 *
 * Compile: javac CozyChairAnimation.java
 * Run:     java CozyChairAnimation
 */
public class CozyChairAnimation extends JPanel implements ActionListener {
    private static final int W = 600, H = 600;
    private final Timer timer;
    private float time = 0f;
    private final Random rnd = new Random();

    // Per-flame random flicker seeds
    private final float[] flameSeeds = new float[] { rnd.nextFloat()*10f, rnd.nextFloat()*10f, rnd.nextFloat()*10f, rnd.nextFloat()*10f };

    public CozyChairAnimation() {
        setPreferredSize(new Dimension(W, H));
        setBackground(new Color(35, 32, 40)); // fallback
        timer = new Timer(33, this); // ~30 fps
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0.create();
        // High quality rendering
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // time progression
        time += 0.033f;

        // Draw scene
        drawBackground(g);
        drawWindowAndCurtain(g);
        drawWallPanels(g);
        drawChandeliersAndSconces(g);
        drawLampAndSideChair(g);
        drawArmchair(g);

        g.dispose();
    }

    private void drawBackground(Graphics2D g) {
        // subtle vertical gradient wall
        GradientPaint wall = new GradientPaint(0, 0, new Color(44, 50, 70), 0, H, new Color(25, 27, 40));
        g.setPaint(wall);
        g.fillRect(0, 0, W, H);

        // floor area (lower 30% of canvas)
        int floorY = (int)(H * 0.62);
        GradientPaint floor = new GradientPaint(0, floorY, new Color(120, 75, 45), 0, H, new Color(80, 50, 25));
        g.setPaint(floor);
        g.fillRect(0, floorY, W, H - floorY);

        // rug under chair
        int rugW = 380, rugH = 120;
        int rugX = (W - rugW) / 2;
        int rugY = floorY + 10;
        g.setColor(new Color(170, 80, 90));
        g.fillOval(rugX, rugY, rugW, rugH);
    }

    private void drawWindowAndCurtain(Graphics2D g) {
        // window (left)
        int winX = 40, winY = 60, winW = 180, winH = 160;
        // window glow
        GradientPaint sun = new GradientPaint(winX, winY, new Color(255, 220, 120), winX+winW, winY+winH, new Color(255, 200, 80, 0));
        g.setPaint(sun);
        g.fillRect(winX, winY, winW, winH);

        // window panes
        g.setColor(new Color(230, 200, 130, 160));
        g.fillRect(winX, winY, winW, winH);

        g.setColor(new Color(180,140,90,200));
        g.setStroke(new BasicStroke(4f));
        g.drawRect(winX, winY, winW, winH);
        g.drawLine(winX + winW/2, winY, winX + winW/2, winY + winH);
        g.drawLine(winX, winY + winH/2, winX + winW, winY + winH/2);

        // left curtain
        Path2D.Double leftCurtain = new Path2D.Double();
        leftCurtain.moveTo(winX - 20, winY - 20);
        leftCurtain.curveTo(winX - 20, winY + 10, winX + 10, winY + winH/3, winX - 5, winY + winH + 40);
        leftCurtain.lineTo(winX + 20, winY + winH + 40);
        leftCurtain.curveTo(winX + 10, winY + winH/2, winX + 50, winY + 20, winX - 20, winY - 20);
        g.setColor(new Color(180, 30, 30));
        g.fill(leftCurtain);

        // curtain highlight
        g.setColor(new Color(255, 120, 120, 40));
        g.fill(new Rectangle(winX - 18, winY - 10, 30, winH + 50));
    }

    private void drawWallPanels(Graphics2D g) {
        // decorative rectangular panels on wall
        g.setStroke(new BasicStroke(3f));
        g.setColor(new Color(55, 69, 98));
        int pad = 30;
        for (int r = 0; r < 2; r++) {
            int py = pad + r * 160;
            for (int c = 0; c < 2; c++) {
                int px = 220 + c * 160;
                RoundRectangle2D rr = new RoundRectangle2D.Double(px, py, 130, 120, 10, 10);
                g.draw(rr);
            }
        }
    }

    private void drawChandeliersAndSconces(Graphics2D g) {
        // chandelier top center
        int cx = W/2, cy = 60;
        drawChandelier(g, cx, cy, 5, 0);

        // small wall sconce left of chair
        drawSconce(g, 420, 220, 0);
        // small wall sconce right of chair
        drawSconce(g, 520, 260, 1);
    }

    private void drawChandelier(Graphics2D g, int x, int y, int arms, int seed) {
        // central bar
        g.setColor(new Color(200,150,80));
        g.fillRect(x-6, y-10, 12, 30);

        // arms and candles
        int armLen = 70;
        for (int i = 0; i < arms; i++) {
            double a = Math.toRadians(-60 + i * (120.0 / (arms-1)));
            int ax = x + (int)(Math.cos(a) * armLen);
            int ay = y + (int)(Math.sin(a) * (armLen*0.3));
            g.setStroke(new BasicStroke(6f));
            g.setColor(new Color(200,150,80));
            g.drawLine(x, y+15, ax, ay+15);

            // candle base
            g.setColor(new Color(220,190,150));
            g.fillRect(ax-6, ay+8, 12, 18);

            // flame with flicker
            drawFlame(g, ax, ay, 12, i);
        }
    }

    private void drawSconce(Graphics2D g, int x, int y, int idx) {
        // mount
        g.setColor(new Color(190,130,70));
        g.fillOval(x-8, y-2, 16, 8);
        // candle
        g.setColor(new Color(235,215,180));
        g.fillRect(x-6, y+6, 12, 20);
        drawFlame(g, x, y, 10, 2 + idx);
    }

    private void drawFlame(Graphics2D g, int x, int y, int size, int flameIndex) {
        // flicker using time + unique seed
        float seed = flameSeeds[flameIndex % flameSeeds.length];
        float jitter = (float)(Math.sin(time * 7 + seed * 3.3) * 1.2 + (rnd.nextFloat()-0.5) * 0.8);
        int fx = x;
        int fy = y - 6 + (int)jitter;

        // outer glow (soft)
        RadialGradientPaint glow = new RadialGradientPaint(
                new Point2D.Float(fx, fy),
                size*1.6f,
                new float[] { 0f, 1f },
                new Color[] { new Color(255,200,100,120), new Color(255,200,100,0) }
        );
        g.setPaint(glow);
        g.fill(new Ellipse2D.Float(fx - size*1.6f/2, fy - size*1.6f/2, size*1.6f, size*1.6f));

        // inner flame shapes
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

    private void drawLampAndSideChair(Graphics2D g) {
        // side armchair in background left
        int scX = 80, scY = 380;
        g.setColor(new Color(80, 70, 110));
        g.fillRoundRect(scX, scY, 110, 80, 16, 16);

        // lamp and table
        int tblX = 200, tblY = 380;
        g.setColor(new Color(140, 90, 50));
        g.fillRect(tblX, tblY + 20, 80, 12); // top
        g.setColor(new Color(110, 70, 40));
        g.fillRect(tblX+18, tblY + 30, 12, 30); // leg

        // lamp shade with pulsing glow
        float pulse = (float)((Math.sin(time * 2.0) + 1f) * 0.5f); // 0..1
        int lampX = tblX + 28, lampY = tblY - 10;
        GradientPaint shadePaint = new GradientPaint(lampX, lampY, new Color(240, 210, 140), lampX+40, lampY+30, new Color(220,180,100));
        g.setPaint(shadePaint);
        g.fill(new RoundRectangle2D.Float(lampX, lampY, 40, 36, 8, 8));

        // lamp glow on wall/floor - vary alpha with pulse
        int glowAlpha = 70 + (int)(pulse * 70);
        RadialGradientPaint lampGlow = new RadialGradientPaint(
                new Point2D.Float(lampX + 20, lampY + 20),
                80f,
                new float[] { 0f, 1f },
                new Color[] { new Color(255,220,140, glowAlpha), new Color(255,220,140, 0) }
        );
        g.setPaint(lampGlow);
        g.fill(new Ellipse2D.Float(lampX - 40, lampY - 10, 160, 120));
    }

    private void drawArmchair(Graphics2D g) {
        // chair bob (subtle up/down)
        float bob = (float)Math.sin(time * 1.2) * 4f;

        // main chair frame coordinates
        int cx = W/2, cy = 220;
        int chairW = 320, chairH = 300;
        int x = cx - chairW/2;
        int y = cy + (int)bob;

        // wooden frame outer (orange/golden)
        g.setColor(new Color(200,120,40));
        RoundRectangle2D frame = new RoundRectangle2D.Double(x, y, chairW, chairH, 40, 40);
        g.fill(frame);

        // inner cushion (purple)
        Insets pad = new Insets(28, 28, 40, 28);
        RoundRectangle2D cushion = new RoundRectangle2D.Double(x + pad.left, y + pad.top, chairW - pad.left - pad.right, chairH - pad.top - pad.bottom, 30, 30);
        GradientPaint cushionSh = new GradientPaint(0, y, new Color(110, 40, 150), 0, y + chairH, new Color(90, 30, 120));
        g.setPaint(cushionSh);
        g.fill(cushion);

        // arm rests (rounded shapes)
        // left arm
        Shape leftArm = createArmShape(x, y + 160, true);
        g.setColor(new Color(200,120,40));
        g.fill(leftArm);
        // purple pad on left arm
        g.setColor(new Color(120, 40, 150));
        g.fill(new RoundRectangle2D.Double(x + 18, y + 160 + 10, 84, 36, 20, 20));

        // right arm
        Shape rightArm = createArmShape(x + chairW - 100, y + 160, false);
        g.setColor(new Color(200,120,40));
        g.fill(rightArm);
        g.setColor(new Color(120, 40, 150));
        g.fill(new RoundRectangle2D.Double(x + chairW - 102, y + 160 + 10, 84, 36, 20, 20));

        // seat cushion top shadow/highlight
        g.setColor(new Color(0,0,0,60));
        g.fill(new RoundRectangle2D.Double(x + 36, y + chairH - 130, chairW - 72, 26, 18, 18));

        // small foot/paw-like pads (decorative)
        int pawY = y + chairH - 40;
        g.setColor(new Color(255, 210, 160, 50));
        g.fillOval(x + chairW/2 - 40, pawY, 28, 12);
        g.fillOval(x + chairW/2 + 10, pawY, 28, 12);
    }

    private Shape createArmShape(int x, int y, boolean left) {
        // create a decorative scroll arm shape
        Path2D p = new Path2D.Double();
        if (left) {
            p.moveTo(x + 80, y);
            p.curveTo(x + 30, y - 10, x + 10, y - 40, x + 20, y - 60);
            p.curveTo(x + 40, y - 80, x + 90, y - 60, x + 120, y - 40);
            p.lineTo(x + 100, y + 40);
            p.closePath();
        } else {
            p.moveTo(x + 20, y);
            p.curveTo(x + 70, y - 10, x + 90, y - 40, x + 80, y - 60);
            p.curveTo(x + 60, y - 80, x + 10, y - 60, x - 20, y - 40);
            p.lineTo(x, y + 40);
            p.closePath();
        }
        return p;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // repaint to animate
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Cozy Armchair — Java2D Animation (600x600)");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            CozyChairAnimation panel = new CozyChairAnimation();
            f.setContentPane(panel);
            f.pack();
            f.setLocationRelativeTo(null);
            f.setResizable(false);
            f.setVisible(true);
        });
    }
}
