import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class StartPanel {

    public StartPanel(GameFrame frame) {
        frame.setTitle("Game Start Screen");
        JLabel background = new JLabel(new ImageIcon("images/startbackground.jpg"));
        background.setLayout(new FlowLayout());
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        JButton button = new JButton("Start Game");
        Point p = new Point(350, 275);
        button.setSize(100, 50);
        button.setLocation(p.getX(), p.getY());
        background.add(button);
        frame.add(button);
        frame.add(background, BorderLayout.CENTER);
        frame.setResizable(false);


        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                //new GamePanel().displayNewFrame();
                GamePanel gamePanel = new GamePanel();
            };
        });
        frame.setVisible(true);
    }
}