package rendering;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class TestGui {

    private final JFrame frame;
    private final JPanel panel;

    public TestGui(Consumer<Graphics2D> drawCallback) {
        frame = new JFrame("TestGui");
        panel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawCallback.accept((Graphics2D) g);
            }
        };
        panel.setPreferredSize(new Dimension(800, 600));
        frame.setContentPane(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void repaint() {
        panel.repaint();
    }

}
