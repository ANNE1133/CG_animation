package Cat5sec.animation;
import java.awt.*;

public class Egg {
    private long startTime;
    private Timer timer;
    private List<Point[]> crackSegments;
 
    public Egg() {
        setPreferredSize(new Dimension(600, 600));
        setBackground(new Color(250, 248, 240)); // สีพื้นหลัง

        initCrackSegments();
        startTime = System.currentTimeMillis();
        timer = new Timer(30, e -> repaint());
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        BufferedImage buffer = new BufferedImage(601, 601, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = buffer.createGraphics();

        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, 600, 600);

        long elapsed = System.currentTimeMillis() - startTime;

        // วาดไข่
        if (elapsed < 1000) {
            drawWholeEgg(g2, buffer);
        } else if (elapsed < 5000) {
            drawCrackedEgg(g2, elapsed, buffer);
        } else if (elapsed < 8000) {
            drawOpenedEgg(g2, elapsed, buffer); // ไข่แยก
            drawCatEmerging(g2, elapsed, buffer);

        } else {
            drawCatEmerging(g2, elapsed, buffer);
            drawBottomHalfEgg(g2);
        }

        g2d.drawImage(buffer, 0, 0, null);
    }
    private void drawWholeEgg(Graphics2D g, BufferedImage buffer) {
        g.setColor(new Color(91, 33, 20));
        QuadraticBezier(g, new Point(237, 214), new Point(299, 151), new Point(372, 213));
        QuadraticBezier(g, new Point(371, 213), new Point(415, 250), new Point(430, 304));
        QuadraticBezier(g, new Point(429, 304), new Point(446, 356), new Point(439, 396));
        QuadraticBezier(g, new Point(439, 395), new Point(436, 447), new Point(386, 489));
        QuadraticBezier(g, new Point(229, 493), new Point(305, 545), new Point(387, 488));
        QuadraticBezier(g, new Point(168, 395), new Point(173, 455), new Point(229, 493));
        QuadraticBezier(g, new Point(237, 214), new Point(154, 283), new Point(168, 396));

        floodFill(buffer, 350, 300, Color.WHITE, new Color(253, 217, 146));
    }   

    private void drawCrackedEgg(Graphics2D g, long elapsed, BufferedImage buffer) {
        drawWholeEgg(g, buffer); // วาดไข่ปกติก่อน
        int totalTime = 3000; // เวลาแสดงรอยร้าวทั้งหมด (ms)
        int segmentsToShow = (int)((elapsed - 1000) / (float)totalTime * crackSegments.size());

        segmentsToShow = Math.min(segmentsToShow, crackSegments.size());

        for (int i = 0; i < segmentsToShow; i++) {
            Point[] seg = crackSegments.get(i);
            g.setStroke(new BasicStroke(3));
            g.drawLine(seg[0].x, seg[0].y, seg[1].x, seg[1].y);
        }
    }

    private void drawOpenedEgg(Graphics2D g, long elapsed, BufferedImage buffer) {
        int moveAmount = (int) Math.min((elapsed - 5000) / 5.0, 400); // ขยับสูงสุด 30px
        // วาดครึ่งบนของไข่
        Graphics2D gTop = (Graphics2D) g.create();
        gTop.translate(0, -moveAmount); // เลื่อนขึ้น
        drawTopHalfEgg(gTop, buffer);
        gTop.dispose();

        // วาดครึ่งล่างของไข่
        Graphics2D gBottom = (Graphics2D) g.create();
        drawBottomHalfEgg(gBottom);
    }

    private void drawTopHalfEgg(Graphics2D g, BufferedImage buffer) {
        g.setColor(new Color(91, 33, 20));
        g.setStroke(new BasicStroke(3));
        // ส่วนของเปลือกบน
        QuadraticBezier(g, new Point(237, 214), new Point(299, 151), new Point(372, 213));
        QuadraticBezier(g, new Point(237, 214), new Point(202, 242), new Point(181, 296));
        QuadraticBezier(g, new Point(371, 213), new Point(408, 245), new Point(425, 291));

        // วาดรอยแตกบน
        for (int i = 0; i < crackSegments.size(); i++) {
            Point[] seg = crackSegments.get(i);
            g.drawLine(seg[0].x, seg[0].y, seg[1].x, seg[1].y);
        }
        floodFill(buffer, 300, 250, Color.WHITE, new Color(253, 217, 146));
    }

    private void drawBottomHalfEgg(Graphics2D g) {
        g.setColor(new Color(91, 33, 20));
        g.setStroke(new BasicStroke(3));
        // ส่วนของเปลือกล่าง
        QuadraticBezier(g, new Point(229, 493), new Point(305, 545), new Point(387, 488));
        QuadraticBezier(g, new Point(168, 395), new Point(173, 455), new Point(229, 493));
        QuadraticBezier(g, new Point(439, 395), new Point(436, 447), new Point(386, 489));
        QuadraticBezier(g, new Point(181, 296), new Point(161, 333), new Point(168, 396));
        QuadraticBezier(g, new Point(429, 304), new Point(427, 294), new Point(425, 291));
        QuadraticBezier(g, new Point(429, 304), new Point(446, 356), new Point(439, 396));
        // วาดรอยแตกล่าง
        for (int i = 0; i < crackSegments.size(); i++) {
            Point[] seg = crackSegments.get(i);
            g.drawLine(seg[0].x, seg[0].y, seg[1].x, seg[1].y);
        }
        // QuadraticBezier(g, new Point(181, 297), new Point(194, 320), new Point(200, 328));
        // QuadraticBezier(g, new Point(199, 328), new Point(226, 301), new Point(241, 299));
        // QuadraticBezier(g, new Point(425, 291), new Point(399, 315), new Point(395, 324));
        // QuadraticBezier(g, new Point(395, 324), new Point(373, 296), new Point(347, 297));
        // g.drawLine(240, 299, 348, 297);
    }

    private void initCrackSegments() {
        crackSegments = new ArrayList<>();

        crackSegments.add(new Point[]{new Point(181, 296), new Point(195, 350)});
        crackSegments.add(new Point[]{new Point(194, 350), new Point(231, 338)});
        crackSegments.add(new Point[]{new Point(230, 338), new Point(261, 389)});
        crackSegments.add(new Point[]{new Point(261, 389), new Point(318, 361)});
        crackSegments.add(new Point[]{new Point(318, 361), new Point(364, 383)});
        crackSegments.add(new Point[]{new Point(364, 383), new Point(371, 373)});
        crackSegments.add(new Point[]{new Point(370, 373), new Point(381, 372)});
        crackSegments.add(new Point[]{new Point(380, 373), new Point(395, 344)});
        crackSegments.add(new Point[]{new Point(394, 344), new Point(413, 351)});
        crackSegments.add(new Point[]{new Point(425, 291), new Point(412, 351)});
    }
    
    private void cat(Graphics g, BufferedImage buffer ) {
        g.setColor(new Color(91, 33, 20));
        QuadraticBezier(g, new Point(205, 211), new Point(150, 290), new Point(245, 324));
        QuadraticBezier(g, new Point(204, 211), new Point(185, 68), new Point(266, 167));
        QuadraticBezier(g, new Point(266, 166), new Point(303, 159), new Point(331, 172));
        QuadraticBezier(g, new Point(331, 172), new Point(373, 137), new Point(395, 143));
        QuadraticBezier(g, new Point(394, 143), new Point(415, 178), new Point(394, 227));
        QuadraticBezier(g, new Point(395, 227), new Point(431, 321), new Point(319, 328));
        QuadraticBezier(g, new Point(352, 322), new Point(421, 385), new Point(398, 401));
        QuadraticBezier(g, new Point(244, 323), new Point(223, 371), new Point(250, 426));
        QuadraticBezier(g, new Point(244, 323), new Point(250, 326), new Point(261, 327));
        QuadraticBezier(g, new Point(399, 401), new Point(258, 443), new Point(250, 425));
        // floodFill(buffer, 287, 316, Color.WHITE, new Color(241, 130, 31));
    }

    private void drawCatEmerging(Graphics2D g, long elapsed, BufferedImage buffer) {
        // เวลาที่แมวใช้ในการโผล่ขึ้นมา (3 วินาที)
        long catEmergeDuration = 3000;
        long catStartTime = elapsed - 5000; // เริ่มที่ 8 วินาที
        
        if (catStartTime < 0) return;
        
        // คำนวณระยะทางที่แมวโผล่ขึ้นมา (0-1)
        double progress = Math.min(catStartTime / (double)catEmergeDuration, 1.0);
        
        // ตำแหน่งเริ่มต้นของแมว (ลงไปจากตำแหน่งปกติ 150px)
        int initialOffset = 100;
        int currentOffset = (int)(initialOffset * (1 - progress));
        
        // สร้าง Graphics2D สำหรับวาดแมว
        Graphics2D gCat = (Graphics2D) g.create();
        gCat.translate(0, currentOffset); // เลื่อนแมวตามการโผล่ขึ้น
        
        // ใช้ clipping เพื่อให้แมวโผล่ขึ้นมาจากในไข่
        Shape originalClip = gCat.getClip();
        Rectangle clipRect = new Rectangle(0, 0, getWidth(), getHeight() - currentOffset);
        gCat.setClip(clipRect);
        
        cat(gCat, buffer); // วาดแมว
        
        gCat.setClip(originalClip);
        gCat.dispose();
        
        // // วาดเปลือกบนที่ขยับขึ้นไปแล้ว
        // Graphics2D gTop = (Graphics2D) g.create();
        // gTop.translate(0, -400); // เลื่อนขึ้น 400px (ตำแหน่งสุดท้าย)
        // drawTopHalfEgg(gTop);
        // gTop.dispose();
    }

    private void QuadraticBezier(Graphics g, Point p1, Point p2, Point p3 ) {
        for (int i = 0; i <= 1000; i++) {
            double t = i / 1000.0;

            double x = Math.pow(1 - t, 2) * p1.x + 2 * (1-t) * t * p2.x + t * t * p3.x;
            double y = Math.pow(1 - t, 2) * p1.y + 2 * (1-t) * t * p2.y + t * t * p3.y;

            plot(g, (int)x, (int)y, 3);
        }
    }
    private void drawBezier(Graphics g, Point p0, Point p1, Point p2, Point p3 ) {
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

        Graphics2D g2 = m.createGraphics();
        Queue<Point> q = new LinkedList<>();
        q.add(new Point(x, y));
        m.setRGB(x, y, replacementRGB);
        plot(g2, x, y, 5);


        while (!q.isEmpty()) {
            Point p = q.poll();

            int px = p.x;
            int py = p.y;

            //n
            if (py > 0 && m.getRGB(px, py - 1) == targetRGB) {
                m.setRGB(px, py - 1, replacementRGB);
                q.add(new Point(px, py - 1));
            }

            //s
            if (py < m.getHeight() - 1 && m.getRGB(px, py + 1) == targetRGB) {
                m.setRGB(px, py + 1, replacementRGB);
                q.add(new Point(px, py + 1));
            }

            //e
            if (px < m.getWidth() - 1 && m.getRGB(px + 1, py) == targetRGB) {
                m.setRGB(px + 1, py, replacementRGB);
                q.add(new Point(px + 1, py));
            }

            //w
            if (px > 0 && m.getRGB(px - 1, py) == targetRGB) {
                m.setRGB(px - 1, py, replacementRGB);
                q.add(new Point(px - 1, py));
            }
        }
        return m;
    }
    }
}

