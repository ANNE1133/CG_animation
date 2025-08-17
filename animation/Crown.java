package finalVer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Crown extends JPanel implements ActionListener {
    // Colors
    private final Color crownOurliner = new Color(91, 33, 20);
    private final Color mainColor = new Color(255, 215, 0);
    private final Color gem = new Color(243, 226, 160);
    private long startTime;
    private final long animationDuration = 3000; // 3 seconds
    final Timer timer;
	private float time = 0f;
    private final long delay = 8500; 
    private GraphicsUtils utils;

	private boolean crownPositionCalculated = false;
    private int finalCrownY = -1;

    public Crown() {
        startTime = System.currentTimeMillis();
        timer = new Timer(10, this);
        timer.start();
        utils = new GraphicsUtils();
    }
	
     protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        long elapsed = System.currentTimeMillis() - startTime;
        
        if (elapsed > delay) {
            if (!crownPositionCalculated) {
                long animationTime = elapsed - delay;
                double progress = Math.min(animationTime / (double) animationDuration, 1.0);
                
                int startY = -50;
                int endY = 40;
                finalCrownY = (int) (startY + (endY - startY) * progress);
                
                if (progress >= 1.0) {
                    crownPositionCalculated = true;
                    timer.stop(); // Stop timer when animation complete
                }
            }
            
            drawCrown(g2d, finalCrownY);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    private void drawCrown(Graphics2D g, int yOffset) {
        int baseCrownX = 300; // This will be the horizontal center of the crown's base

        g.setStroke(new BasicStroke(2)); // Outline stroke
        
        // Crown dimensions - make rectangle width match ellipse width
        int ellipseWidth = 70; // Total width of crown
        int ellipseHeight = 10;
        int rectangleHeight = 15;
        int spikeHeight = 25;
        
        // Calculate positions
        int ellipseY = 110 + yOffset;
        int rectangleY = ellipseY - rectangleHeight/2;
        int rectangleWidth = ellipseWidth; // Match ellipse width
        
        // 1. Crown Base (Ellipse) - curved base
        g.setColor(mainColor);
        // fillEllipse takes center coordinates (xc, yc) and semi-axes (a, b)
        utils.fillEllipse(g, baseCrownX, ellipseY + ellipseHeight/2, ellipseWidth/2, ellipseHeight/2, mainColor);
        
        // 2. Crown Rectangular Base - same width as ellipse
        utils.myFillRect(g, baseCrownX - (rectangleWidth/2), rectangleY, rectangleWidth, rectangleHeight);

        // 3. Crown Spikes/Triangles - evenly distributed along rectangle border
        int spikesBaseY = rectangleY; // Top of the rectangle
        
        // Calculate spike positions to evenly share space along rectangle border
        int totalSpikeArea = rectangleWidth; // Leave small margins on sides
        int spikeWidth = totalSpikeArea /3; // 3 spikes + 2 gaps between them
        int spikeSpacing = 0;
        
        int leftStart = baseCrownX - (rectangleWidth/2)+1; // Small margin

        // Set color for triangles
        g.setColor(mainColor);
        
        // Triangle 1 (Left spike)
        int spike1Center = leftStart + spikeWidth/2;
        int[] xPointsTriangle1 = {
            spike1Center - spikeWidth/2, 
            spike1Center, 
            spike1Center + spikeWidth/2
        };
        int[] yPointsTriangle1 = {spikesBaseY, spikesBaseY - spikeHeight, spikesBaseY};
        g.fillPolygon(xPointsTriangle1, yPointsTriangle1, 3);
        
        // Triangle 2 (Middle spike)
        int spike2Center = spike1Center + spikeWidth + spikeSpacing;
        int[] xPointsTriangle2 = {
            spike2Center - spikeWidth/2, 
            spike2Center, 
            spike2Center + spikeWidth/2
        };
        int[] yPointsTriangle2 = {spikesBaseY, spikesBaseY - spikeHeight, spikesBaseY};
        g.fillPolygon(xPointsTriangle2, yPointsTriangle2, 3);
        
        // Triangle 3 (Right spike)
        int spike3Center = spike2Center + spikeWidth + spikeSpacing;
        int[] xPointsTriangle3 = {
            spike3Center - spikeWidth/2, 
            spike3Center, 
            spike3Center + spikeWidth/2
        };
        int[] yPointsTriangle3 = {spikesBaseY, spikesBaseY - spikeHeight, spikesBaseY};
        g.fillPolygon(xPointsTriangle3, yPointsTriangle3, 3);

        // 4. Gems on Spikes
        int gemY = spikesBaseY - spikeHeight + 1; // Center Y of gem
        int gemRadius = 2; // Semi-axis for circular gems (radius)

        // Gem 1 (Left spike)
        utils.fillEllipse(g, spike1Center, gemY, gemRadius, gemRadius, gem);

        // Gem 2 (Middle spike)
        utils.fillEllipse(g, spike2Center, gemY, gemRadius, gemRadius, gem);

        // Gem 3 (Right spike)
        utils.fillEllipse(g, spike3Center, gemY, gemRadius, gemRadius, gem);
    }
}