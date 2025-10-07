import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class StartPanel {
    private AudioPlayer audioPlayer;

    public StartPanel(GameFrame frame) {
        frame.setTitle("Game Start Screen");
        JLabel background = new JLabel(new ImageIcon("images/TheBackraund.jpg"));
        background.setLayout(new FlowLayout());
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        JButton button = new JButton("Start Game");
        Point p = new Point(350, 275);
        button.setSize(100, 50);
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.setLocation(p.getX(), p.getY()-100);
        background.add(button);
        frame.add(button);

        frame.setResizable(false);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                GamePanel gamePanel = new GamePanel();
                AudioPlayer audioPlayer = new AudioPlayer("Audio/Audio_Voicy_Whistle sound effect.wav");
                audioPlayer.play();
            };
        });
        audioPlayer = new AudioPlayer("Audio/אייל גולן מלך המגרש Eyal Golan (1).wav");
        audioPlayer.play();
        JButton buttonInstructions = new JButton("Instructions");
        buttonInstructions.setSize(100+50, 50);
        buttonInstructions.setLocation(p.getX()-25, p.getY());
        background.add(buttonInstructions);
        frame.add(buttonInstructions);
        frame.add(background, BorderLayout.CENTER);
        // יצירת חלון ההוראות
        JDialog instructionsDialog = new JDialog(frame, "Instructions", true);
        instructionsDialog.setSize(400, 400);
        buttonInstructions.setBackground(Color.BLACK);
        buttonInstructions.setForeground(Color.WHITE);
        instructionsDialog.setLayout(new BorderLayout());
        JLabel instructionsPic = new JLabel(new ImageIcon("images/istroctions.png"));
        instructionsPic.setVisible(true);
        // יצירת התווית עם טקסט ההוראות

        instructionsDialog.add(instructionsPic, BorderLayout.CENTER);

        // יצירת כפתור לסגירת חלון ההוראות
        JButton closeInstructionsButton = new JButton("סגור");
        instructionsDialog.add(closeInstructionsButton, BorderLayout.SOUTH);

        // הוספת מאזין אירועים לכפתור הסגירה
        closeInstructionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                instructionsDialog.setVisible(false);
            }
        });

        // הוספת מאזין אירועים לכפתור הפתיחה
        buttonInstructions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                instructionsDialog.setLocationRelativeTo(frame); // מיקום החלון באמצע הפריים הראשי
                instructionsDialog.setVisible(true);
            }
        });


        frame.setVisible(true);
    }
}