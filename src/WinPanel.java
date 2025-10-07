import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WinPanel extends JPanel {
    private BufferedImage bg;
    private final JFrame frame;
    private final Timer anim;
    private final int[] cx, cy, cs;
    private final float[] cv;
    private float fade = 0f;
    private final int w = 800, h = 381;

    public WinPanel(int playerID) {
        frame = new JFrame("Winner");
        frame.setSize(w, h);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        try {
            String p = playerID == 2 ? "images/red wins.jpg" : playerID == 1 ? "images/green wins.jpg" : "images/draw.jpg";
            bg = ImageIO.read(new File(p));
        } catch (IOException e) {
            bg = null;
        }
        int n = playerID == 0 ? 80 : 160;
        cx = new int[n]; cy = new int[n]; cs = new int[n]; cv = new float[n];
        for (int i = 0; i < n; i++) {
            cx[i] = (int) (Math.random() * w);
            cy[i] = (int) (Math.random() * -h);
            cs[i] = 4 + (int) (Math.random() * 8);
            cv[i] = 1.2f + (float) (Math.random() * 1.8f);
        }

        InputMap im = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "close");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "close");
        am.put("close", new AbstractAction() { @Override public void actionPerformed(ActionEvent e) { frame.dispose(); }});

        anim = new Timer(1000 / 60, e -> {
            for (int i = 0; i < cy.length; i++) {
                cy[i] += cv[i];
                if (cy[i] > h + 10) { cy[i] = -20; cx[i] = (int) (Math.random() * w); cs[i] = 4 + (int) (Math.random() * 8); }
            }
            fade = Math.min(1f, fade + 0.05f);
            repaint();
        });

        frame.setContentPane(this);
        frame.addWindowListener(new WindowAdapter() {
            @Override public void windowClosed(WindowEvent e) { anim.stop(); }
            @Override public void windowClosing(WindowEvent e) { anim.stop(); }
        });
        frame.setVisible(true);
        anim.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawBackground(g2);
        drawVignette(g2);
        drawConfetti(g2);
        g2.dispose();
        Toolkit.getDefaultToolkit().sync();
    }

    private void drawBackground(Graphics2D g2) {
        if (bg == null) {
            GradientPaint gp = new GradientPaint(0, 0, new Color(20, 22, 30), 0, getHeight(), new Color(8, 9, 12));
            g2.setPaint(gp);
            g2.fillRect(0, 0, getWidth(), getHeight());
            return;
        }
        int panelW = getWidth(), panelH = getHeight();
        double sx = panelW / (double) bg.getWidth();
        double sy = panelH / (double) bg.getHeight();
        double s = Math.min(sx, sy);
        int drawW = (int) Math.round(bg.getWidth() * s);
        int drawH = (int) Math.round(bg.getHeight() * s);
        int ox = (panelW - drawW) / 2;
        int oy = (panelH - drawH) / 2;
        Object old = g2.getRenderingHint(RenderingHints.KEY_INTERPOLATION);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(bg, ox, oy, drawW, drawH, null);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, old);
    }

    private void drawVignette(Graphics2D g2) {
        float a = 0.25f * fade;
        g2.setPaint(new Color(0f, 0f, 0f, a));
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    private void drawConfetti(Graphics2D g2) {
        for (int i = 0; i < cx.length; i++) {
            float hue = (i * 0.037f) % 1f;
            g2.setColor(Color.getHSBColor(hue, 0.8f, 1f));
            g2.fillRoundRect(cx[i], cy[i], cs[i], cs[i], 2, 2);
        }
    }
}
