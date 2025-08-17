package finalVer;

import java.awt.Dimension;

import javax.swing.*;

public class Main {
	private static Timer masterTimer;
    public static void main(String[] args) {
		
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Egg Cracking Animation");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            System.setProperty("sun.java2d.opengl", "true");
            // Create background panel and animation panel
            Background background = new Background();
            Egg eggPanel = new Egg();
            Crown crown = new Crown();

			background.timer.stop(); // You'll need to make timer public or add a stop method
            crown.timer.stop();

			masterTimer = new Timer(33, e -> { // ~30 FPS
				AnimationTime.update();
			                background.repaint();
                eggPanel.repaint();
                crown.repaint();
            });
            // Set up layered pane for background and animation
            JLayeredPane layeredPane = new JLayeredPane();
            layeredPane.setPreferredSize(new Dimension(600, 600));
            
            // Enable double buffering explicitly
            background.setDoubleBuffered(true);
            eggPanel.setDoubleBuffered(true);
            crown.setDoubleBuffered(true);
            
            background.setBounds(0, 0, 600, 600);
            eggPanel.setBounds(0, 0, 600, 600);
            eggPanel.setOpaque(false);
            crown.setBounds(0, 0, 600, 600);
            crown.setOpaque(false);
            
            layeredPane.add(background, JLayeredPane.DEFAULT_LAYER);
            layeredPane.add(eggPanel, JLayeredPane.PALETTE_LAYER);
            layeredPane.add(crown, JLayeredPane.PALETTE_LAYER);

            frame.add(layeredPane);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            
            masterTimer.start();
        });
    }
}
