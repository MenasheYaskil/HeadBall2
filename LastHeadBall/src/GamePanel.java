//import javax.imageio.ImageIO;
//import javax.swing.*;
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//
//public class GamePanel extends JPanel {
//    private Player player1;
//    private Player player2;
//    private BufferedImage backgroundImage;
//    private JFrame newFrame;
//
//    public GamePanel() {
//        newFrame = new JFrame("Game Start Screen");
//        newFrame.setSize(800, 381);
//        newFrame.setLocationRelativeTo(null);
//        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        newFrame.setResizable(false);
//
//        // Load background image
//        try {
//            backgroundImage = ImageIO.read(new File("images/backraundAfterPressed.jpg"));
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.exit(1);
//        }
//
//        // Set up the player
//        player1 = new Player(this);
//        this.setLayout(null); // Use absolute positioning
//        player1.setBounds(0, 0, 800, 381);
//        this.add(player1);
//        player2 = new Player(this);
//        this.setLayout(null);
//        player2.setBounds(0,0,880,381);
//        this.add(player2);
//        // Add GamePanel to the frame
//        newFrame.add(this);
//        newFrame.setVisible(true);
//        this.setFocusable(true);
//        this.requestFocusInWindow();
//
//        // Start the player thread
//        player1.start();
//        player2.start();
//    }
//
//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        // Draw the background image
//        if (backgroundImage != null) {
//            g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);
//        }
//        // Draw the player
//        player1.paintComponent(g);
//        player2.setX(60);
//        BufferedImage image2 = null;
//        try {
//            image2 = ImageIO.read(new File("images/playerClean.png"));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        image2 = this.paintImage(image2, 100, 60);
//        player2.setImage(image2);
//        player2.paintComponent(g);
//    }
//
//    public BufferedImage paintImage(BufferedImage originalImage, int newWidth, int newHeight) {
//        Image tmp = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
//        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
//
//        Graphics2D g2d = resizedImage.createGraphics();
//        g2d.drawImage(tmp, 0, 0, null);
//        g2d.dispose();
//
//        return resizedImage;
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(GamePanel::new);
//    }
//}
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GamePanel extends JPanel {
    private Player1 player1;
    private Player2 player2;
    private Ball ball;
    private BufferedImage backgroundImage;
    private JFrame newFrame;

    public GamePanel() {
        newFrame = new JFrame("Game Start Screen");
        newFrame.setSize(800, 381);
        newFrame.setLocationRelativeTo(null);
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newFrame.setResizable(false);

        // Load background image
        try {
            backgroundImage = ImageIO.read(new File("images/backraundAfterPressed.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }


        // Set up the player
        player1 = new Player1(this);
        this.setLayout(null); // Use absolute positioning
        player1.setBounds(0, 0, 800, 381);
        this.add(player1);
        player2 = new Player2(this);
        this.setLayout(null); // Use absolute positioning
        player2.setBounds(0, 0, 800, 381);
        this.add(player2);

        ball = new Ball(this);
        this.setLayout(null);
        ball.setBounds(0,0,800,381);
        this.add(ball);

        // Add GamePanel to the frame
        newFrame.add(this);
        newFrame.setVisible(true);
        this.setFocusable(true);
        this.requestFocusInWindow();

        // Start the player thread
        player1.start();
        player2.start();
        //ball.run();
    }
    public Player1 getPlayer1() {
        return player1;
    }

    public Player2 getPlayer2() {
        return player2;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the background image
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);
        }
        // Draw the player
        player1.paintComponent(g);
        player2.paintComponent(g);
        ball.paintComponent(g);

    }

    public BufferedImage paintImage(BufferedImage originalImage, int newWidth, int newHeight) {
        Image tmp = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return resizedImage;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GamePanel::new);
    }
}
