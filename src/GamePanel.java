import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GamePanel extends JPanel {
    private Player1 player1;
    private Player2 player2;
    private Ball ball;
    private BufferedImage backgroundImage;
    private JFrame newFrame;
    private int scoreOfPlayer1;
    private int scoreOfPlayer2;
    private Timer gameTimer;
    private int timeLeft = 90; // seconds

    public GamePanel() {
        setDoubleBuffered(true);

        newFrame = new JFrame("Game");
        newFrame.setSize(800, 381);
        newFrame.setLocationRelativeTo(null);
        newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        newFrame.setResizable(false);

        this.scoreOfPlayer1 = 0;
        this.scoreOfPlayer2 = 0;

        try {
            backgroundImage = ImageIO.read(new File("images/backraundAfterPressed.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        setLayout(null);

        player1 = new Player1(this);
        player1.setBounds(0, 0, 800, 381);
        add(player1);

        player2 = new Player2(this);
        player2.setBounds(0, 0, 800, 381);
        add(player2);

        ball = new Ball(this);
        ball.setBounds(0, 0, 800, 381);
        add(ball);

        newFrame.setContentPane(this);
        newFrame.setVisible(true);
        setFocusable(true);
        requestFocusInWindow();

        // Start threads
        player1.start();
        player2.start();
        // Ball thread already starts in its constructor

        // Timer for game time and periodic repaint
        gameTimer = new Timer(1000, e -> {
            timeLeft--;
            if (timeLeft <= 0) {
                endGame();
            } else {
                repaint();
            }
        });
        gameTimer.start();

        // Ensure threads stop if window is closed
        newFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                stopAll();
            }
            @Override
            public void windowClosing(WindowEvent e) {
                stopAll();
            }
        });
    }

    private void stopAll() {
        if (gameTimer != null) gameTimer.stop();
        if (ball != null) ball.stopLoop();
        if (player1 != null) player1.stopLoop();
        if (player2 != null) player2.stopLoop();
    }

    private void endGame() {
        if (scoreOfPlayer1 > scoreOfPlayer2) new WinPanel(1);
        else if (scoreOfPlayer1 < scoreOfPlayer2) new WinPanel(2);
        else new WinPanel(0);

        stopAll();
        newFrame.dispose();
    }

    public Player1 getPlayer1() { return player1; }
    public Player2 getPlayer2() { return player2; }

    public void goal(int playerID) {
        if (playerID == 1) scoreOfPlayer1++; else scoreOfPlayer2++;
        player1.setGoalLocation();
        player2.setGoalLocation();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        }

        // HUD
        g.setColor(Color.YELLOW);
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 96));
        g.drawString(" " + this.scoreOfPlayer2, 20, 80);
        g.drawString(" " + this.scoreOfPlayer1, 800 - 130, 80);

        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 46));
        g.setColor(Color.WHITE);
        g.drawString("⏱" + timeLeft, 355, 50);
        // שים לב: לא לקרוא repaint() כאן!
    }

    public BufferedImage paintImage(BufferedImage originalImage, int newWidth, int newHeight) {
        Image tmp = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();
        try {
            g2d.drawImage(tmp, 0, 0, null);
        } finally {
            g2d.dispose();
        }
        return resizedImage;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GamePanel::new);
    }
}
