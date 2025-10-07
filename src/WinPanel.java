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
        newFrame = new JFrame("winner Screen");
        newFrame.setSize(800, 381);
        newFrame.setLocationRelativeTo(null);
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newFrame.setResizable(false);
        if(playerID==2){
            System.out.println(5000);
            try {
                backgroundImage = ImageIO.read(new File("images/red wins.jpg"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else if (playerID==1){
            try {
                backgroundImage = ImageIO.read(new File("images/green wins.jpg"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }else if (playerID==0){
            try {
                backgroundImage = ImageIO.read(new File("images/draw.jpg"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        newFrame.add(this);


        newFrame.setVisible(true);


    }
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the background image
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);
        }
    }
}
