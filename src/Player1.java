import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player1 extends JComponent implements Runnable {
    private static final long serialVersionUID = 1L;

    private BufferedImage image;
    private Point location;
    private int dx, dy;
    private boolean inAir;
    private final GamePanel gamePanel;
    private final Thread playerThread;
    private final int groundLevel;
    private final int leftBoundary;
    private final int rightBoundary;
    private Point centerOfTheHead;
    private Point upperRight, lowerLeft;
    private volatile boolean running = true;

    private final AudioPlayer jumpSound = new AudioPlayer("Audio/Audio_jumppp11.wav");

    public Player1(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        try {
            image = ImageIO.read(new File("images/sizedPlayer1.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        int newWidth = 100;
        int newHeight = 60;
        image = gamePanel.paintImage(image, newWidth, newHeight);

        leftBoundary = 70;
        rightBoundary = 800 - 80;
        groundLevel = ((800 * 2) / 10) + 15;

        location = new Point(800 - 60, groundLevel);
        centerOfTheHead = new Point(location.getX() + 50, location.getY() + 15);
        upperRight = new Point(location.getX() + 35, location.getY() + 30);
        lowerLeft = new Point(location.getX() + 65, location.getY() + 60);

        setOpaque(false);
        setBounds(0, 0, 800, 381);

        gamePanel.setFocusable(true);
        gamePanel.addKeyListener(new TAdapter());

        playerThread = new Thread(this, "Player1-Thread");
    }

    public void start() { playerThread.start(); }
    public void stopLoop() { running = false; }

    public Point getUpperRight() { return upperRight; }
    public Point getLowerLeft() { return lowerLeft; }
    public Point getCenterOfTheHead() { return centerOfTheHead; }

    private void updatePosition() {
        location.setX(location.getX() + dx);
        centerOfTheHead.setX(centerOfTheHead.getX() + dx);
        upperRight.setX(location.getX() + 35);
        lowerLeft.setX(location.getX() + 65);

        if (inAir) dy += 1;

        location.setY(location.getY() + dy);
        centerOfTheHead.setY(centerOfTheHead.getY() + dy);
        upperRight.setY(location.getY() + 30);
        lowerLeft.setY(location.getY() + 60);

        if (location.getX() < leftBoundary) {
            location.setX(leftBoundary);
            centerOfTheHead.setX(location.getX() + 50);
            upperRight.setX(location.getX() + 35);
            lowerLeft.setX(location.getX() + 65);
            dx = 0;
        } else if (location.getX() + image.getWidth() > rightBoundary) {
            location.setX(rightBoundary - image.getWidth());
            centerOfTheHead.setX(location.getX() + 50);
            upperRight.setX(location.getX() + 35);
            lowerLeft.setX(location.getX() + 65);
            dx = 0;
        }

        if (location.getY() > groundLevel) {
            location.setY(groundLevel);
            centerOfTheHead.setY(groundLevel + 15);
            upperRight.setY(location.getY() + 30);
            lowerLeft.setY(location.getY() + 60);
            dy = 0;
            inAir = false;
        }
    }

    public void setGoalLocation() {
        location.setX(800 - 60);
        location.setY(groundLevel);
        centerOfTheHead.setX(location.getX() + 50);
        centerOfTheHead.setY(location.getY() + 15);
        upperRight.setX(location.getX() + 35);
        upperRight.setY(location.getY() + 30);
        lowerLeft.setX(location.getX() + 65);
        lowerLeft.setY(location.getY() + 60);
        dx = dy = 0;
        inAir = false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, location.getX(), location.getY(), this);
    }

    @Override
    public void run() {
        while (running) {
            updatePosition();
            SwingUtilities.invokeLater(gamePanel::repaint);
            try { Thread.sleep(10); } catch (InterruptedException e) { break; }
        }
        jumpSound.close();
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT)  dx = -2;
            if (key == KeyEvent.VK_RIGHT) dx = 2;
            if (key == KeyEvent.VK_UP && !inAir) {
                dy = -13;
                inAir = true;
                jumpSound.play();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) dx = 0;
        }
    }
}
