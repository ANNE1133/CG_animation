import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import cake.Background;

public class AnimationMain extends JPanel {

    private Background background;
    private Cat cat;
    // private Light light;

    public AnimationMain() {
        // สร้าง object แต่ละตัว
        background = new Background();
        // cat = new Cat();
        // light = new Light();

        // ขนาดหน้าจอ
        setPreferredSize(new Dimension(600, 600));
        setBackground(Color.BLACK);
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;

        // เรียกวาดจาก object
        background.draw(g);
        // cat.draw(g);
        // light.draw(g);
    }

    public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        JFrame frame = new JFrame("Cat in Cozy Room");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Background bg = new Background();
        frame.setContentPane(bg); // ใช้ Background เป็น content

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    });
}

}
