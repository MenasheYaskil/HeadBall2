import javax.swing.*;
import java.awt.*;

public class Ball extends JComponent implements Runnable {
    private static final long serialVersionUID = 1L;
    private double x = 375; // Initial x position
    private double y = 0; // Initial y position
    private double xVelocity = -5; // Initial x velocity
    private double yVelocity = 0; // Initial y velocity
    private final double gravity = 0.1; // כוח משיכה
    private final double elasticity = 0.9; // גמישות
    private final int diameter = 30; // קוטר הכדור
    private final int bottomBoundary = 235; // הגבול התחתון החדש
    private final int topBoundary = 0;
    private final int leftWall = 100;
    private final int rightWall =700;
    private GamePanel gamePanel;
    private Thread animator;
    private Point center= new Point((int)x+15,(int)y+15);

    public Ball(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        animator = new Thread(this);
        animator.start();


    }

    @Override
    public void run() {
        while (true) {
            moveBall();
            gamePanel.repaint();
            try {
                Thread.sleep(10); // שינה של 10 מילישניות
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void moveBall() {
        yVelocity += gravity; // עדכון מהירות הכדור בהתחשב בכוח המשיכה
        x += xVelocity;
        center.setX((int)x+15);
        y += yVelocity;
        center.setY((int)y+15);
// התנגשות עם הקיר מעל השער השמאלי
        if((x <= leftWall&&y<=381-120)){
            xVelocity = -xVelocity * elasticity;// הכדור קופץ חזרה בהתאם לגמישות
            x = 100;
            center.setX((int)x+15);

        }
        // התנגשות עם הקיר מעל השער הימני
        if((x>=rightWall)&&y<=381-120 ){
            xVelocity = -xVelocity * elasticity*0.3;// הכדור קופץ חזרה בהתאם לגמישות
            x =800 - diameter-100;
            center.setX((int)x+15);
        }

//         בדיקת התנגשות עם גבולות המסך
        if (x <= 80 || x >= 800 - diameter-90) {
            xVelocity = 0;

            if (x <= 80) {
                x = 80;
                center.setX((int)x+15);

            } else {
                x = 800 - diameter-90;
                center.setX((int)x+15);
            }
        }
        // התנגשות עם הקיר מעל השער השמאלי
        if((x <= leftWall&&y>=381-120)){
            xVelocity = -xVelocity * elasticity;// הכדור קופץ חזרה בהתאם לגמישות
                x = 100;
            center.setX((int)x+15);
        }
        // התנגשות עם הקיר מעל השער הימני
        if((x>=800 -rightWall)&&y>=381-120 ){
            xVelocity = -xVelocity * elasticity;// הכדור קופץ חזרה בהתאם לגמישות
            x =800 - diameter-100;
            center.setX((int)x+15);
        }


        if (y >= bottomBoundary - diameter) {
            y = bottomBoundary - diameter;
            center.setY((int)y+15);
            yVelocity = -yVelocity * elasticity; // הכדור קופץ חזרה בהתאם לגמישות
        }
        if(y<=0){
            yVelocity = -yVelocity * elasticity; // הכדור קופץ חזרה בהתאם לגמישות

        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.RED);
        g2d.fillOval((int) x, (int) y, diameter, diameter);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 600); // Set preferred size to match the frame
    }
}
