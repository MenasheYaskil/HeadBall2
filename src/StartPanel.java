import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class StartPanel {
    private AudioPlayer audioPlayer;

    public StartPanel(GameFrame frame) {
        frame.setTitle("Game Start Screen");
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setResizable(false);

        JLabel background = new JLabel(scaledIcon("images/TheBackraund.jpg", 800, 600));
        background.setLayout(null);

        JButton startBtn = new JButton("Start Game");
        startBtn.setBounds(350, 175, 120, 50);
        styleButton(startBtn);
        background.add(startBtn);

        JButton instrBtn = new JButton("Instructions");
        instrBtn.setBounds(335, 275, 150, 50);
        styleButton(instrBtn);
        background.add(instrBtn);

        frame.setContentPane(background);

        audioPlayer = new AudioPlayer("Audio/אייל גולן מלך המגרש Eyal Golan (1).wav");
        audioPlayer.play();

        startBtn.addActionListener((ActionEvent e) -> {
            stopMusic();
            frame.dispose();
            new GamePanel();
            AudioPlayer whistle = new AudioPlayer("Audio/Audio_Voicy_Whistle sound effect.wav");
            whistle.play();
            Timer t = new Timer(2500, ev -> whistle.close());
            t.setRepeats(false);
            t.start();
        });

        JDialog instructionsDialog = new JDialog(frame, "Instructions", true);
        instructionsDialog.setSize(420, 440);
        instructionsDialog.setLayout(new BorderLayout());
        JLabel pic = new JLabel(new ImageIcon("images/istroctions.png"));
        pic.setHorizontalAlignment(SwingConstants.CENTER);
        instructionsDialog.add(pic, BorderLayout.CENTER);
        JButton close = new JButton("סגור");
        styleButton(close);
        instructionsDialog.add(close, BorderLayout.SOUTH);
        close.addActionListener(ev -> instructionsDialog.setVisible(false));

        instrBtn.addActionListener(ev -> {
            instructionsDialog.setLocationRelativeTo(frame);
            instructionsDialog.setVisible(true);
        });

        InputMap im = frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = frame.getRootPane().getActionMap();
        im.put(KeyStroke.getKeyStroke("ENTER"), "start");
        im.put(KeyStroke.getKeyStroke('I'), "instr");
        im.put(KeyStroke.getKeyStroke('M'), "mute");
        am.put("start", new AbstractAction() { @Override public void actionPerformed(ActionEvent e) { startBtn.doClick(); }});
        am.put("instr", new AbstractAction() { @Override public void actionPerformed(ActionEvent e) { instrBtn.doClick(); }});
        am.put("mute", new AbstractAction() { @Override public void actionPerformed(ActionEvent e) { toggleMusic(); }});

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosing(java.awt.event.WindowEvent e) { stopMusic(); }
            @Override public void windowClosed(java.awt.event.WindowEvent e) { stopMusic(); }
        });

        frame.setVisible(true);
    }

    private void styleButton(JButton b) {
        b.setFocusPainted(false);
        b.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        b.setBackground(Color.BLACK);
        b.setForeground(Color.WHITE);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private static ImageIcon scaledIcon(String path, int w, int h) {
        Image img = new ImageIcon(path).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    private void stopMusic() {
        if (audioPlayer != null) {
            audioPlayer.stop();
            audioPlayer.close();
            audioPlayer = null;
        }
    }

    private void toggleMusic() {
        if (audioPlayer == null) return;
        if (audioPlayer.isPlaying()) audioPlayer.pause();
        else audioPlayer.play();
    }
}
