import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WinPanel extends JPanel {
    private BufferedImage backgroundImage;
    private JFrame newFrame;

    public WinPanel(int playerID) {
        newFrame = new JFrame("Winner Screen");
        newFrame.setSize(800, 381);
        newFrame.setLocationRelativeTo(null);
        newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        newFrame.setResizable(false);

        try {
            if (playerID == 2) {
                backgroundImage = ImageIO.read(new File("images/red wins.jpg"));
            } else if (playerID == 1) {
                backgroundImage = ImageIO.read(new File("images/green wins.jpg"));
            } else {
                backgroundImage = ImageIO.read(new File("images/draw.jpg"));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        newFrame.setContentPane(this);
        newFrame.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        }
    }
}
