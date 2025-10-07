//import javax.imageio.ImageIO;
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.KeyAdapter;
//import java.awt.event.KeyEvent;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//
//public class Player extends JComponent implements Runnable {
//    private BufferedImage image;
//    private int x, y;
//    private int dx1, dy1, dx2, dy2;
//    private boolean inAir;
//    private GamePanel gamePanel;
//    private Thread playerThread;
//    private final int groundLevel; // Fixed ground level
//    private final int leftBoundary; // Left boundary
//    private final int rightBoundary; // Right boundary
//
//    public Player(GamePanel gamePanel) {
//        this.gamePanel = gamePanel;
//        try {
//            image = ImageIO.read(new File("images/sizedPlayer1.png"));
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.exit(1);
//        }
//
//        int newWidth = 100;
//        int newHeight = 60;
//        image = gamePanel.paintImage(image, newWidth, newHeight);
//
//        // Set initial position
//        leftBoundary = 80; // Adjust this value to set the left boundary
//        rightBoundary = 800 - 80; // Adjust this value to set the right boundary
//        groundLevel = (((800 * 2)/10))+15; // Fixed ground level
//        x = 800- newHeight; // Center horizontally
//        y = groundLevel;
//
//        gamePanel.setFocusable(true);
//        gamePanel.addKeyListener(new TAdapter());
//
//        playerThread = new Thread(this);
//    }
//
//    public void setX(int x) {
//        this.x = x;
//    }
//
//    public void setImage(BufferedImage image) {
//        this.image = image;
//    }
//
//    public void start() {
//        playerThread.start();
//    }
//
//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        g.drawImage(image, x, y, this);
//    }
//
//    @Override
//    public void run() {
//        while (true) {
//            updatePosition();
//            gamePanel.repaint();
//            try {
//                Thread.sleep(10); // Update every 10 ms
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private void updatePosition() {
//        x += dx1;
//        if (inAir) {
//            dy1 += 1; // Gravity effect
//        }
//        y += dy1;
//
//        // Check horizontal boundaries
//        if (x < leftBoundary) {
//            x = leftBoundary;
//            dx1 = 0;
//            dx2 = 800;
//        } else if (x + image.getWidth() > rightBoundary) {
//            x = rightBoundary - image.getWidth();
//            dx1 = 0;
//            dx2=800;
//        }
//
//        // Check vertical boundaries
//        if (y > groundLevel) {
//            y = groundLevel;
//            dy1 = 0;
//            dy2 =0;
//            inAir = false;
//        }
//    }
//
//    private class TAdapter extends KeyAdapter {
//        @Override
//        public void keyPressed(KeyEvent e) {
//            int key = e.getKeyCode();
//            if (key == KeyEvent.VK_LEFT) {
//                dx1 = -2;
//            }
//            if (key == KeyEvent.VK_A) {
//                dx2 = +2;
//            }
//            if (key == KeyEvent.VK_RIGHT) {
//                dx1 = 2;
//            }
//            if (key == KeyEvent.VK_D) {
//                dx2 = -2;
//            }
//            if (key == KeyEvent.VK_UP && !inAir) {
//                dy1 = -15;
//                inAir = true;
//            }
//            if (key == KeyEvent.VK_W && !inAir) {
//                dy2 = -15;
//                inAir = true;
//            }
//        }
//
//        @Override
//        public void keyReleased(KeyEvent e) {
//            int key = e.getKeyCode();
//            if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
//                dx1 = 0;
//            }
//            if (key == KeyEvent.VK_A || key == KeyEvent.VK_D) {
//                dx2 = 0;
//            }
//        }
//    }
//}
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.*;

public class Player1 extends JComponent implements Runnable {
    private BufferedImage image;
    private Point location;
    private int dx, dy;
    private boolean inAir;
    private GamePanel gamePanel;
    private Thread playerThread;
    private final int groundLevel; // Fixed ground level
    private final int leftBoundary; // Left boundary
    private final int rightBoundary; // Right boundary
    private static final long serialVersionUID = 1L;
    private Point centerOfTheHead ;

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

        // Set initial position
        leftBoundary = 70; // Adjust this value to set the left boundary
        rightBoundary = 800 - 80; // Adjust this value to set the right boundary
        groundLevel = (((800 * 2)/10))+15; // Fixed ground level
        location=new Point(800- newHeight,groundLevel);
        centerOfTheHead=new Point(location.getX()+50, location.getY()+15);


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
        g.setColor(Color.BLUE);
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
        location.setX(location.getX()+dx);
        centerOfTheHead.setX(centerOfTheHead.getX()+dx);
        if (inAir) {
            dy += 1; // Gravity effect
        }
        location.setY(location.getY()+dy);
        centerOfTheHead.setY(centerOfTheHead.getY()+dy);
        // Check horizontal boundaries
        if (location.getX() < leftBoundary) {
            location.setX(leftBoundary);
            centerOfTheHead.setX(location.getX()+50);

            dx = 0;
        } else if (location.getX() + image.getWidth() > rightBoundary) {
            location.setX(rightBoundary - image.getWidth());
            centerOfTheHead.setX(location.getX()+50);
            dx = 0;
        }

        // Check vertical boundaries
        if (location.getY() > groundLevel) {
            location.setY(groundLevel);
            centerOfTheHead.setY(groundLevel+15);
            dy = 0;
            inAir = false;
        }
    }
    //@Override
//    public Dimension getPreferredSize() {
//        return new Dimension(100, 60);
//    }

//    public Rectangle getBounds() {
//        return new Rectangle(x, y, 100, 60);
//    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT) {
                dx = -2;
            }
            if (key == KeyEvent.VK_RIGHT) {
                dx = 2;
            }
            if (key == KeyEvent.VK_UP && !inAir) {
                dy = -13;
                inAir = true;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
                dx = 0;
            }
        }
    }
}
