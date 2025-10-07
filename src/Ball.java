import javax.swing.*;
import java.awt.*;

public class Ball extends JComponent implements Runnable {
    private static final long serialVersionUID = 1L;

    // Field & geometry constants
    private static final int FIELD_WIDTH = 786;
    private static final int PANEL_WIDTH = 800;
    private static final int PANEL_HEIGHT = 381;

    private static final int SIDE_MARGIN = 40;
    private static final int GOAL_MARGIN = 110;
    private static final int DIAMETER = 30;

    private final double gravity = 0.1;
    private final double elasticity = 0.9;
    private final int bottomBoundary = 235;

    private double x = 375; // Initial x position
    private double y = 0;   // Initial y position
    private double xVelocity = 0;
    private double yVelocity = 0;

    private final GamePanel gamePanel;
    private final Thread animator;
    private final Point center = new Point((int)x + DIAMETER/2, (int)y + DIAMETER/2);
    private volatile boolean running = true;

    public Ball(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        setOpaque(false);
        setBounds(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
        animator = new Thread(this, "Ball-Animator");
        animator.start();
    }

    public void stopLoop() { running = false; }

    @Override
    public void run() {
        while (running) {
            moveBall();
            SwingUtilities.invokeLater(gamePanel::repaint);
            try { Thread.sleep(10); } catch (InterruptedException e) { break; }
        }
    }

    private void moveBall() {
        // Clamp velocity
        if (xVelocity > 10) xVelocity = 10;
        if (yVelocity > 10) yVelocity = 10;

        yVelocity += gravity;
        x += xVelocity; center.setX((int)x + DIAMETER/2);
        y += yVelocity; center.setY((int)y + DIAMETER/2);

        if (xVelocity > 10) xVelocity = 10;
        if (yVelocity > 10) yVelocity = 10;

        // Walls
        if (x <= SIDE_MARGIN || x >= FIELD_WIDTH - DIAMETER - SIDE_MARGIN) {
            xVelocity = -xVelocity * elasticity;
            if (x <= SIDE_MARGIN) {
                x = SIDE_MARGIN;
            } else {
                x = FIELD_WIDTH - DIAMETER - SIDE_MARGIN;
            }
            center.setX((int)x + DIAMETER/2);
        }

        // Upper corners (goal posts top)
        if ((x <= GOAL_MARGIN && y <= GOAL_MARGIN) || (x >= FIELD_WIDTH - DIAMETER - GOAL_MARGIN && y <= GOAL_MARGIN)) {
            xVelocity = -xVelocity * elasticity;
            if (x <= GOAL_MARGIN) {
                x = GOAL_MARGIN;
            } else {
                x = FIELD_WIDTH - DIAMETER - GOAL_MARGIN;
            }
            center.setX((int)x + DIAMETER/2);
        }

        // Goals
        if (center.getX() <= GOAL_MARGIN) {
            gamePanel.goal(1);
            resetBall(-3, 3);
        }
        if (center.getX() >= FIELD_WIDTH - GOAL_MARGIN) {
            gamePanel.goal(2);
            resetBall(3, 3);
        }

        // Floor
        if (y >= bottomBoundary - DIAMETER) {
            y = bottomBoundary - DIAMETER;
            center.setY((int)y + DIAMETER/2);
            yVelocity = -yVelocity * elasticity;
        }
        // Ceiling
        if (y <= 0) {
            y = 0;
            center.setY((int)y + DIAMETER/2);
            yVelocity = -yVelocity * elasticity;
        }

        // Collisions with heads
        handleHeadCollision(gamePanel.getPlayer1().getCenterOfTheHead());
        handleHeadCollision(gamePanel.getPlayer2().getCenterOfTheHead());
    }

    private void resetBall(double newVX, double newVY) {
        x = 375; y = 0;
        center.setX((int)x + DIAMETER/2);
        center.setY((int)y + DIAMETER/2);
        xVelocity = newVX;
        yVelocity = newVY;
    }

    private void handleHeadCollision(Point headCenter) {
        if (headCenter.distance(center) > 30) return;

        double xCalculation = headCenter.getX() - center.getX();
        double yCalculation = headCenter.getY() - center.getY();

        if (xCalculation != 0) {
            double incline = yCalculation / xCalculation;
            // Quadrants
            if (xCalculation < 0 && yCalculation > 0) {                 // from top-right
                if (incline <= -1) { yVelocity = 6 / (-incline + 1) * incline; xVelocity = 6 / (-incline + 1); }
                if (incline > -1 && incline < 0) { incline = 1 / incline; xVelocity = 6 / -(-incline + 1) * incline; yVelocity = 6 / (incline + 1); }
            }
            if (yCalculation < 0 && xCalculation < 0) {                  // from bottom-right
                if (incline >= 1) { yVelocity = 6 / (incline + 1) * incline; xVelocity = 6 / (incline + 1); }
                if (incline < 1 && incline > 0) { incline = 1 / incline; xVelocity = 6 / (incline + 1) * incline; yVelocity = 6 / (incline + 1); }
            }
            if (yCalculation < 0 && xCalculation > 0) {                  // from bottom-left
                if (incline <= -1) { yVelocity = 6 / -(-incline + 1) * incline; xVelocity = 6 / (incline + 1); }
                if (incline > -1 && incline < 0) { incline = 1 / incline; xVelocity = 6 / (-incline + 1) * incline; yVelocity = 6 / (-incline + 1); }
            }
            if (yCalculation > 0 && xCalculation > 0) {                  // from top-left
                if (incline >= 1) { yVelocity = 6 / -(incline + 1) * incline; xVelocity = 6 / -(incline + 1); }
                if (incline < 1 && incline > 0) { incline = 1 / incline; xVelocity = 6 / -(incline + 1) * incline; yVelocity = 6 / -(incline + 1); }
            }
            if (incline == 0) {
                if (xCalculation < 0) { xVelocity = -6; yVelocity = 0; }
                else { xVelocity = 6; yVelocity = 0; }
            }
        } else {
            if (yCalculation >= 0) { xVelocity = 0; yVelocity = -6; }
            else { xVelocity = 0; yVelocity = 6; }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        try {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(Color.RED);
            g2d.fillOval((int) x, (int) y, DIAMETER, DIAMETER);
        } finally {
            g2d.dispose();
        }
    }

    @Override
    public Dimension getPreferredSize() { return new Dimension(PANEL_WIDTH, PANEL_HEIGHT); }
}
