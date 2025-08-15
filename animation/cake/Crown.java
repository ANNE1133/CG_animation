package cake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class Crown extends JPanel implements ActionListener {
	// Colors
    private final Color crownOurliner = new Color(91, 33, 20);
    private final Color mainColor = new Color(89, 65, 35);
    private final Color gem = new Color(243, 226, 160);
    private long startTime;
    private final long animationDuration = 3000; // 3 seconds
    private final Timer timer;
	private final long delay = 8500; 
    public Crown() {
        startTime = System.currentTimeMillis();
        timer = new Timer(10, this);
        timer.start();
    }

	protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        long elapsed = System.currentTimeMillis() - startTime;
        
        // Check if the delay has passed
        if (elapsed > delay) {
            long animationTime = elapsed - delay;
            double progress = Math.min(animationTime / (double) animationDuration, 1.0);
            
            // Calculate crown's Y position based on progress
            int startY = -50; // Crown starts above the screen
            int endY = 50; // Target Y position on the cat's head
            
            // Interpolate the crown's Y position
            int currentY = (int) (startY + (endY - startY) * progress);

            // Draw the crown at the calculated position
            drawCrown(g2d, currentY);

            // If animation is complete, stop the timer
            if (progress >= 1.0 || currentY > endY) {
                timer.stop();
            }
        }
	}

    @Override
    public void actionPerformed(ActionEvent e) {
        // Just call repaint, the logic is in paintComponent
        repaint();
    }

	private void drawCrown(Graphics2D g, int yOffset) {
		int x = getWidth()/2;
        g.setColor(new Color(255, 215, 0)); // สีทองของมงกุฎ
        g.setStroke(new BasicStroke(2));
        
        // ฐานมงกุฎ
        g.fillRect(40, 110 + yOffset, 50, 10);

        // สามเหลี่ยมยอดมงกุฎ
        int[] xPointsTriangle1 = {40, 50, 60};
        int[] yPointsTriangle1 = {110 + yOffset, 100 + yOffset, 110 + yOffset};
        g.fillPolygon(xPointsTriangle1, yPointsTriangle1, 3);
        
        int[] xPointsTriangle2 = {60, 70, 80};
        int[] yPointsTriangle2 = {110 + yOffset, 100 + yOffset, 110 + yOffset};
        g.fillPolygon(xPointsTriangle2, yPointsTriangle2, 3);
        
        int[] xPointsTriangle3 = {80, 90, 90};
        int[] yPointsTriangle3 = {110 + yOffset, 100 + yOffset, 110 + yOffset};
        g.fillPolygon(xPointsTriangle3, yPointsTriangle3, 3);

        // จุดประดับ
        g.setColor(Color.RED);
        g.fillOval(48, 97 + yOffset, 5, 5);
        g.fillOval(68, 97 + yOffset, 5, 5);
        g.fillOval(88, 97 + yOffset, 5, 5);
	}
}
