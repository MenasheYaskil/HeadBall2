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
    private Point location;
    private int dx, dy;
    private boolean inAir;
    private GamePanel gamePanel;
    private Thread playerThread;
    private final int groundLevel; // Fixed ground level
    private final int leftBoundary; // Left boundary
    private final int rightBoundary; // Right boundary
    private Point centerOfTheHead ;
    private Point upperRight,lowerLeft;



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
        location=new Point( leftBoundary,groundLevel) ; // Right side
        centerOfTheHead=new Point(location.getX()+50, location.getY()+15);
        upperRight=new Point(location.getX()+35, location.getY()+30 );
        lowerLeft=new Point(location.getX()+65, location.getY()+60 );

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
        g.drawImage(image, location.getX(), location.getY(), this);
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
    public Point getUpperRight() {
        return upperRight;
    }

    public Point getLowerLeft() {
        return lowerLeft;
    }

    public Point getCenterOfTheHead() {
        return centerOfTheHead;
    }

    private void updatePosition() {
        location.setX(location.getX()+dx);
        centerOfTheHead.setX(centerOfTheHead.getX()+dx);
        upperRight.setX(location.getX()+35);
        lowerLeft.setX(location.getX()+65);
        if (inAir) {
            dy += 1; // Gravity effect
        }
        location.setY(location.getY()+dy);
        centerOfTheHead.setY(centerOfTheHead.getY()+dy);
        upperRight.setY(location.getY()+30);
        lowerLeft.setY(location.getY()+60);
        // Check horizontal boundaries
        if (location.getX() < leftBoundary) {
            location.setX(leftBoundary);
            centerOfTheHead.setX(location.getX()+50);
            upperRight.setX(location.getX()+35);
            lowerLeft.setX(location.getX()+65);

            dx = 0;
        } else if (location.getX() + image.getWidth() > rightBoundary) {
            location.setX(rightBoundary - image.getWidth());
            centerOfTheHead.setX(location.getX()+50);
            upperRight.setX(location.getX()+35);
            lowerLeft.setX(location.getX()+65);
            dx = 0;
        }

        // Check vertical boundaries
        if (location.getY() > groundLevel) {
            location.setY(groundLevel);
            centerOfTheHead.setY(groundLevel+15);
            upperRight.setY(location.getY()+30);
            lowerLeft.setY(location.getY()+60);
            dy = 0;
            inAir = false;
        }

    }
    public void setGoalLocation(){
        location.setX(60);
        centerOfTheHead.setX(centerOfTheHead.getX()+50);

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
                AudioPlayer audioPlayer = new AudioPlayer("Audio/Audio_jumppp11.wav");
                audioPlayer.play();
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
