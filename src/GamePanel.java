import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GamePanel extends JPanel {
    public static final int LOGICAL_W = 800;
    public static final int LOGICAL_H = 381;

    // דיפולט חלון "בינוני"
    private static final int WINDOWED_W = 1280;
    private static final int WINDOWED_H = 720;

    private Player1 player1;
    private Player2 player2;
    private Ball ball;

    private BufferedImage backgroundImage;
    private JFrame frame;
    private int scoreP1, scoreP2;
    private Timer secondTick, repaintTimer;
    private int timeLeft = 90;

    // מסך מלא (Borderless – ללא FSEM כדי למנוע קפיאות)
    private boolean fullscreen = false;
    private Rectangle windowedBoundsBackup;

    // פרמטרי סקייל לציור (Letterbox)
    private volatile double renderScale = 1.0;
    private volatile int offsetX = 0, offsetY = 0;

    public GamePanel() {
        setDoubleBuffered(true);
        setLayout(null);
        setFocusable(true);

        frame = new JFrame("HeadBall");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(true); // מאפשר כפתור הגדלה
        frame.setSize(WINDOWED_W, WINDOWED_H);
        frame.setLocationRelativeTo(null);

        try {
            backgroundImage = ImageIO.read(new File("images/backraundAfterPressed.jpg"));
        } catch (IOException e) {
            backgroundImage = null;
        }

        // ישויות – פיזיקה במרחב לוגי קבוע (800x381)
        player1 = new Player1(this);
        player1.setBounds(0, 0, LOGICAL_W, LOGICAL_H);
        add(player1);

        player2 = new Player2(this);
        player2.setBounds(0, 0, LOGICAL_W, LOGICAL_H);
        add(player2);

        ball = new Ball(this);
        ball.setBounds(0, 0, LOGICAL_W, LOGICAL_H);
        add(ball);

        frame.setContentPane(this);
        frame.setVisible(true);

        player1.start();
        player2.start(); // הכדור מתחיל לבד

        // טיימרים
        secondTick = new Timer(1000, e -> {
            timeLeft--;
            if (timeLeft <= 0) endGame();
        });
        secondTick.start();

        repaintTimer = new Timer(1000 / 60, e -> repaint());
        repaintTimer.start();

        frame.addWindowListener(new WindowAdapter() {
            @Override public void windowClosed(WindowEvent e) { stopAll(); }
            @Override public void windowClosing(WindowEvent e) { stopAll(); }
        });

        // קיצורי מקשים למסך מלא
        InputMap im = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0), "toggleFS");
        am.put("toggleFS", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { toggleFullscreen(); }
        });
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "exitFS");
        am.put("exitFS", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { if (fullscreen) toggleFullscreen(); }
        });

        recalcScale();
        frame.addComponentListener(new ComponentAdapter() {
            @Override public void componentResized(ComponentEvent e) { recalcScale(); }
            @Override public void componentShown(ComponentEvent e)    { recalcScale(); }
        });
    }

    private void recalcScale() {
        int w = getWidth()  > 0 ? getWidth()  : frame.getWidth();
        int h = getHeight() > 0 ? getHeight() : frame.getHeight();

        double sx = w / (double) LOGICAL_W;
        double sy = h / (double) LOGICAL_H;
        renderScale = Math.min(sx, sy);

        int drawW = (int) Math.round(LOGICAL_W * renderScale);
        int drawH = (int) Math.round(LOGICAL_H * renderScale);
        offsetX = (w - drawW) / 2;
        offsetY = (h - drawH) / 2;

        // מעדכנים רכיבי ציור כך שיכסו את הפאנל ויציירו לפי הסקייל
        if (ball != null)    ball.setRenderScale(renderScale, offsetX, offsetY);
        if (player1 != null) player1.setRenderScale(renderScale, offsetX, offsetY);
        if (player2 != null) player2.setRenderScale(renderScale, offsetX, offsetY);

        if (player1 != null) player1.setBounds(0, 0, w, h);
        if (player2 != null) player2.setBounds(0, 0, w, h);
        if (ball != null)    ball.setBounds(0, 0, w, h);
    }

    // Borderless Fullscreen – בלי GraphicsDevice.setFullScreenWindow (מונע קפיאות)
    private void toggleFullscreen() {
        fullscreen = !fullscreen;

        // שינוי קישוטי חלון דורש dispose/visible – אבל בלי לגעת בת’רדים/טיימרים
        Rectangle current = frame.getBounds();
        frame.dispose();

        if (fullscreen) {
            windowedBoundsBackup = current;
            frame.setUndecorated(true);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            frame.setUndecorated(false);
            if (windowedBoundsBackup != null) frame.setBounds(windowedBoundsBackup);
            else { frame.setSize(WINDOWED_W, WINDOWED_H); frame.setLocationRelativeTo(null); }
        }

        frame.setContentPane(this);
        frame.setVisible(true);
        requestFocusInWindow();
        recalcScale();
    }

    private void stopAll() {
        if (secondTick != null)  secondTick.stop();
        if (repaintTimer != null) repaintTimer.stop();
        if (ball != null)    ball.stopLoop();
        if (player1 != null) player1.stopLoop();
        if (player2 != null) player2.stopLoop();
    }

    private void endGame() {
        stopAll();
        if (scoreP1 > scoreP2) new WinPanel(1);
        else if (scoreP2 > scoreP1) new WinPanel(2);
        else new WinPanel(0);
        frame.dispose();
    }

    public Player1 getPlayer1() { return player1; }
    public Player2 getPlayer2() { return player2; }

    public void goal(int playerID) {
        if (playerID == 1) scoreP1++; else scoreP2++;
        player1.setGoalLocation();
        player2.setGoalLocation();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Letterbox שחור בשוליים
        if (offsetX > 0 || offsetY > 0) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.translate(offsetX, offsetY);
            g2.scale(renderScale, renderScale);

            if (backgroundImage != null) {
                g2.drawImage(backgroundImage, 0, 0, LOGICAL_W, LOGICAL_H, null);
            }

            g2.setColor(Color.YELLOW);
            g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 96));
            g2.drawString(String.valueOf(scoreP2), 20, 90);
            g2.drawString(String.valueOf(scoreP1), LOGICAL_W - 120, 90);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 46));
            g2.drawString("⏱" + timeLeft, 355, 60);
        } finally {
            g2.dispose();
            Toolkit.getDefaultToolkit().sync();
        }
    }

    // נשמר לשחקנים (resize תמונות)
    public BufferedImage paintImage(BufferedImage originalImage, int newWidth, int newHeight) {
        Image tmp = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();
        try {
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(tmp, 0, 0, null);
        } finally { g2d.dispose(); }
        return resizedImage;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GamePanel::new);
    }
}
