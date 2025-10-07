import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Ball extends JComponent implements Runnable {
    private static final long serialVersionUID = 1L;

    public static final int GROUND_Y = 235;
    private static final int FIELD_WIDTH = 786;
    private static final int PANEL_WIDTH = 800;
    private static final int PANEL_HEIGHT = 381;
    private static final int SIDE_MARGIN = 40;
    private static final int GOAL_MARGIN = 110;
    private static final int DIAMETER = 30;

    private static final double GRAVITY = 0.10;
    private static final double ELASTICITY = 0.90;
    private static final double MAX_SPEED = 10.0;

    private double x = 375, y = 0, vx = 0, vy = 0;
    private final Point center = new Point((int) x + DIAMETER / 2, (int) y + DIAMETER / 2);

    private final GamePanel gamePanel;
    private final Thread animator;
    private volatile boolean running = true;

    private volatile double renderScale = 1.0;
    private volatile int renderOffsetX = 0, renderOffsetY = 0;

    private BufferedImage ballTexture;
    private final AudioPlayer hitSound = new AudioPlayer("Audio/ball_hit.wav");
    private long lastHitSoundMs = 0;

    public Ball(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        setOpaque(false);
        setBounds(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
        loadBallTexture();
        animator = new Thread(this, "Ball-Animator");
        animator.start();
    }

    public void setRenderScale(double s, int ox, int oy) {
        renderScale = s; renderOffsetX = ox; renderOffsetY = oy;
    }

    public void stopLoop() {
        running = false;
        hitSound.close();
    }

    @Override
    public void run() {
        while (running) {
            moveBall();
            SwingUtilities.invokeLater(gamePanel::repaint);
            try { Thread.sleep(10); } catch (InterruptedException e) { break; }
        }
    }

    private void moveBall() {
        vy += GRAVITY;
        x += vx; y += vy; syncCenter();

        vx = clamp(vx, -MAX_SPEED, MAX_SPEED);
        vy = clamp(vy, -MAX_SPEED, MAX_SPEED);

        if (x <= SIDE_MARGIN || x >= FIELD_WIDTH - DIAMETER - SIDE_MARGIN) {
            vx = -vx * ELASTICITY;
            x = Math.max(SIDE_MARGIN, Math.min(x, FIELD_WIDTH - DIAMETER - SIDE_MARGIN));
            syncCenter(); playHit();
        }

        if ((x <= GOAL_MARGIN && y <= GOAL_MARGIN) ||
                (x >= FIELD_WIDTH - DIAMETER - GOAL_MARGIN && y <= GOAL_MARGIN)) {
            vx = -vx * ELASTICITY;
            x = (x <= GOAL_MARGIN) ? GOAL_MARGIN : FIELD_WIDTH - DIAMETER - GOAL_MARGIN;
            syncCenter(); playHit();
        }

        if (center.getX() <= GOAL_MARGIN) { gamePanel.goal(1); resetBall(-3, 3); }
        if (center.getX() >= FIELD_WIDTH - GOAL_MARGIN) { gamePanel.goal(2); resetBall(3, 3); }

        if (y >= GROUND_Y - DIAMETER) { y = GROUND_Y - DIAMETER; syncCenter(); vy = -vy * ELASTICITY; playHit(); }
        if (y <= 0) { y = 0; syncCenter(); vy = -vy * ELASTICITY; playHit(); }

        handleHeadCollision(gamePanel.getPlayer1().getCenterOfTheHead(), gamePanel.getPlayer1().getHeadRadius());
        handleHeadCollision(gamePanel.getPlayer2().getCenterOfTheHead(), gamePanel.getPlayer2().getHeadRadius());
    }

    private void resetBall(double newVX, double newVY) {
        x = 375; y = 0; syncCenter();
        vx = newVX; vy = newVY;
    }

    private void handleHeadCollision(Point head, int headRadius) {
        int ballRadius = DIAMETER / 2;
        double dist = head.distance(center);
        double hitDist = headRadius + ballRadius;
        if (dist > hitDist) return;

        double dx = head.getX() - center.getX();
        double dy = head.getY() - center.getY();

        if (dx != 0) {
            double m = dy / dx;
            if (dx < 0 && dy > 0) { if (m <= -1) { vy = 6 / (-m + 1) * m; vx = 6 / (-m + 1); } if (m > -1 && m < 0) { m = 1 / m; vx = 6 / -(-m + 1) * m; vy = 6 / (m + 1); } }
            if (dy < 0 && dx < 0) { if (m >= 1) { vy = 6 / (m + 1) * m; vx = 6 / (m + 1); } if (m < 1 && m > 0) { m = 1 / m; vx = 6 / (m + 1) * m; vy = 6 / (m + 1); } }
            if (dy < 0 && dx > 0) { if (m <= -1) { vy = 6 / -(-m + 1) * m; vx = 6 / (m + 1); } if (m > -1 && m < 0) { m = 1 / m; vx = 6 / (-m + 1) * m; vy = 6 / (-m + 1); } }
            if (dy > 0 && dx > 0) { if (m >= 1) { vy = 6 / -(m + 1) * m; vx = 6 / -(m + 1); } if (m < 1 && m > 0) { m = 1 / m; vx = 6 / -(m + 1) * m; vy = 6 / -(m + 1); } }
            if (m == 0) { if (dx < 0) { vx = -6; vy = 0; } else { vx = 6; vy = 0; } }
        } else {
            if (dy >= 0) { vx = 0; vy = -6; } else { vx = 0; vy = 6; }
        }

        if (dist < 1e-4) dist = 1e-4;
        double nx = (center.getX() - head.getX()) / dist;
        double ny = (center.getY() - head.getY()) / dist;
        double push = (hitDist - dist) + 0.8;
        x += nx * push; y += ny * push; syncCenter();

        vx = clamp(vx, -MAX_SPEED, MAX_SPEED);
        vy = clamp(vy, -MAX_SPEED, MAX_SPEED);
        playHit();
    }

    private void playHit() {
        long now = System.currentTimeMillis();
        if (now - lastHitSoundMs > 80) {
            hitSound.play();
            lastHitSoundMs = now;
        }
    }

    private void loadBallTexture() {
        String[] paths = { "/images/ball_soccer.png", "images/ball_soccer.png" };
        for (String p : paths) {
            try {
                ballTexture = p.startsWith("/") ? ImageIO.read(getClass().getResource(p)) : ImageIO.read(new File(p));
                if (ballTexture != null) return;
            } catch (Exception ignored) {}
        }
        ballTexture = null;
    }

    private void syncCenter() {
        center.setX((int) x + DIAMETER / 2);
        center.setY((int) y + DIAMETER / 2);
    }

    private static double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int drawX = renderOffsetX + (int) Math.round(x * renderScale);
        int drawY = renderOffsetY + (int) Math.round(y * renderScale);
        int d = (int) Math.round(DIAMETER * renderScale);

        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            double bottom = y + DIAMETER;
            double above = Math.max(0, GROUND_Y - bottom);
            double t = Math.max(0.0, Math.min(1.0, 1.0 - above / 120.0));

            int sw = (int) Math.round(d * (0.55 + 0.35 * t));
            int sh = (int) Math.round(d * (0.25 + 0.20 * t));
            int sx = renderOffsetX + (int) Math.round((x + DIAMETER / 2.0) * renderScale) - sw / 2;
            int sy = renderOffsetY + (int) Math.round(GROUND_Y * renderScale) - sh / 2;

            g2.setColor(new Color(0, 0, 0, (int) (80 + 80 * t)));
            g2.fillOval(sx, sy, sw, sh);

            if (ballTexture != null) g2.drawImage(ballTexture, drawX, drawY, d, d, null);
            else { g2.setColor(Color.RED); g2.fillOval(drawX, drawY, d, d); }
        } finally {
            g2.dispose();
            Toolkit.getDefaultToolkit().sync();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(PANEL_WIDTH, PANEL_HEIGHT);
    }
}
