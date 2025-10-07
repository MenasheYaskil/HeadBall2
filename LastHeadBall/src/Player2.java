import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player2 extends JComponent implements Runnable {
    private BufferedImage image;
    private int x, y;
    private int dx, dy;
    private boolean inAir;
    private GamePanel gamePanel;
    private Thread playerThread;
    private final int groundLevel; // Fixed ground level
    private final int leftBoundary; // Left boundary
    private final int rightBoundary; // Right boundary
    private Point centerOfTheHead ;

    public Player2(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        try {
            image = ImageIO.read(new File("images/sizedPlayer2.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        int newWidth = 100;
        int newHeight = 60;
        image = gamePanel.paintImage(image, newWidth, newHeight);

        // Set initial position
        leftBoundary = 70; // Adjust this value to set the left boundary
        rightBoundary = 730; // Adjust this value to set the right boundary
        groundLevel = (((800 * 2)/10))+15; // Fixed ground level
        x = leftBoundary ; // Right side
        y = groundLevel;

        gamePanel.setFocusable(true);
        gamePanel.addKeyListener(new TAdapter());

        playerThread = new Thread(this);
    }

    public void start() {
        playerThread.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, x, y, this);
    }

    @Override
    public void run() {
        while (true) {
            updatePosition();
            gamePanel.repaint();
            try {
                Thread.sleep(10); // Update every 10 ms
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updatePosition() {
        x += dx;
        if (inAir) {
            dy += 1; // Gravity effect
        }
        y += dy;

        // Check horizontal boundaries
        if (x < leftBoundary) {
            x = leftBoundary;
            dx = 0;
        } else if (x + image.getWidth() > rightBoundary) {
            x = rightBoundary - image.getWidth();
            dx = 0;
        }

        // Check vertical boundaries
        if (y > groundLevel) {
            y = groundLevel;
            dy = 0;
            inAir = false;
        }
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_A) {
                dx = -2;
            }
            if (key == KeyEvent.VK_D) {
                dx = 2;
            }
            if (key == KeyEvent.VK_W && !inAir) {
                dy = -13;
                inAir = true;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_A || key == KeyEvent.VK_D) {
                dx = 0;
            }
        }
    }
}
