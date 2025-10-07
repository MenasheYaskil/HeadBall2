import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class StartPanel {
    private AudioPlayer audioPlayer;

    public StartPanel(GameFrame frame) {
        frame.setTitle("Game Start Screen");
        JLabel background = new JLabel(new ImageIcon("images/TheBackraund.jpg"));
        background.setLayout(null); // Absolute for precise placement
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setResizable(false);

        // Buttons
        JButton button = new JButton("Start Game");
        button.setBounds(350, 175, 100, 50);
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        background.add(button);

        JButton buttonInstructions = new JButton("Instructions");
        buttonInstructions.setBounds(325, 275, 150, 50);
        buttonInstructions.setBackground(Color.BLACK);
        buttonInstructions.setForeground(Color.WHITE);
        background.add(buttonInstructions);

        frame.setContentPane(background);

        // Sounds
        audioPlayer = new AudioPlayer("Audio/אייל גולן מלך המגרש Eyal Golan (1).wav");
        audioPlayer.play();

        // Start button
        button.addActionListener((ActionEvent e) -> {
            if (audioPlayer != null) audioPlayer.stop();
            if (audioPlayer != null) audioPlayer.close();
            frame.dispose();
            new GamePanel();
            AudioPlayer whistle = new AudioPlayer("Audio/Audio_Voicy_Whistle sound effect.wav");
            whistle.play();
        });

        // Instructions dialog
        JDialog instructionsDialog = new JDialog(frame, "Instructions", true);
        instructionsDialog.setSize(400, 400);
        instructionsDialog.setLayout(new BorderLayout());
        JLabel instructionsPic = new JLabel(new ImageIcon("images/istroctions.png"));
        instructionsDialog.add(instructionsPic, BorderLayout.CENTER);
        JButton closeInstructionsButton = new JButton("סגור");
        instructionsDialog.add(closeInstructionsButton, BorderLayout.SOUTH);

        closeInstructionsButton.addActionListener(ev -> instructionsDialog.setVisible(false));
        buttonInstructions.addActionListener(ev -> {
            instructionsDialog.setLocationRelativeTo(frame);
            instructionsDialog.setVisible(true);
        });

        frame.setVisible(true);
    }
}
