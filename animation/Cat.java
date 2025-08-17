package finalVer;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Cat {
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
