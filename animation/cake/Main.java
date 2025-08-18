package cake;

import java.awt.Dimension;

import javax.swing.*;

public class Main {
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
