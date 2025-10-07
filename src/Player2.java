import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player2 extends JComponent implements Runnable {
    private static final long serialVersionUID = 1L;

    private static final int SPRITE_W = 100, SPRITE_H = 60;
    private static final int LEFT_BOUND = 70;
    private static final int RIGHT_BOUND = 730;
    private static final int FOOT_OFFSET = 8;
    private static final double RUN_SPEED = 3.0;
    private static final double AIR_CONTROL = 0.7;
    private static final int JUMP_IMPULSE = -13;
    private static final int GRAVITY_Y = 1;
    private static final int HEAD_OFFSET_X = 50;
    private static final int HEAD_OFFSET_Y = 15;
    private static final int HEAD_RADIUS = 13;

    private BufferedImage image;
    private Point location;
    private double dx;
    private int dy;
    private boolean inAir;

    private final GamePanel gamePanel;
    private final Thread playerThread;
    private final int groundLevel;
    private Point centerOfTheHead, upperRight, lowerLeft;
    private volatile boolean running = true;

    private final AudioPlayer jumpSound = new AudioPlayer("Audio/Audio_jumppp11.wav");

    private volatile double renderScale = 1.0;
    private volatile int renderOffsetX = 0, renderOffsetY = 0;

    public Player2(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        try {
            image = ImageIO.read(new File("images/sizedPlayer2.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        groundLevel = Ball.GROUND_Y - SPRITE_H + FOOT_OFFSET;

        location = new Point(LEFT_BOUND, groundLevel);
        centerOfTheHead = new Point(location.getX() + HEAD_OFFSET_X, location.getY() + HEAD_OFFSET_Y);
        upperRight = new Point(location.getX() + 35, location.getY() + 30);
        lowerLeft  = new Point(location.getX() + 65, location.getY() + 60);

        setOpaque(false);
        setBounds(0, 0, 800, 381);

        gamePanel.setFocusable(true);
        gamePanel.addKeyListener(new TAdapter());

        playerThread = new Thread(this, "Player2-Thread");
    }

    public void setRenderScale(double s, int ox, int oy) { renderScale = s; renderOffsetX = ox; renderOffsetY = oy; }
    public void start() { playerThread.start(); }
    public void stopLoop() { running = false; }
    public Point getUpperRight() { return upperRight; }
    public Point getLowerLeft() { return lowerLeft; }
    public Point getCenterOfTheHead() { return centerOfTheHead; }
    public int getHeadRadius() { return HEAD_RADIUS; }

    private void syncPoints() {
        centerOfTheHead.setX(location.getX() + HEAD_OFFSET_X);
        centerOfTheHead.setY(location.getY() + HEAD_OFFSET_Y);
        upperRight.setX(location.getX() + 35);
        upperRight.setY(location.getY() + 30);
        lowerLeft.setX(location.getX() + 65);
        lowerLeft.setY(location.getY() + 60);
    }

    private void updatePosition() {
        double control = inAir ? AIR_CONTROL : 1.0;
        location.setX(location.getX() + (int) Math.round(dx * control));
        if (inAir) dy += GRAVITY_Y;
        location.setY(location.getY() + dy);

        if (location.getX() < LEFT_BOUND) {
            location.setX(LEFT_BOUND);
            dx = 0;
        } else if (location.getX() + SPRITE_W > RIGHT_BOUND) {
            location.setX(RIGHT_BOUND - SPRITE_W);
            dx = 0;
        }

        if (location.getY() > groundLevel) {
            location.setY(groundLevel);
            dy = 0;
            inAir = false;
        }
        syncPoints();
    }

    public void setGoalLocation() {
        location.setX(LEFT_BOUND);
        location.setY(groundLevel);
        dx = 0; dy = 0; inAir = false;
        syncPoints();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int drawX = renderOffsetX + (int) Math.round(location.getX() * renderScale);
        int drawY = renderOffsetY + (int) Math.round(location.getY() * renderScale);
        int w = (int) Math.round(SPRITE_W * renderScale);
        int h = (int) Math.round(SPRITE_H * renderScale);
        g.drawImage(image, drawX, drawY, w, h, this);
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
        @Override public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A -> dx = -RUN_SPEED;
                case KeyEvent.VK_D -> dx = RUN_SPEED;
                case KeyEvent.VK_W -> { if (!inAir) { dy = JUMP_IMPULSE; inAir = true; jumpSound.play(); } }
            }
        }
        @Override public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_D) dx = 0;
        }
    }
}
