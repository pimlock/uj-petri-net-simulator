package petrieditor.visual.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

/**
 * @author wiktor
 * @author Shannon Hickey
 */
public class CoolButton extends JToggleButton {
    public static final int ALONE = 0;
    public static final int LEFT = 1;
    public static final int MIDDLE = 2;
    public static final int RIGHT = 3;

    private static final Color HOVER_COLOR = Color.WHITE;
    private static final Color FOREGROUND_COLOR = new Color(91, 118, 173);

    private int style;

    private final MouseAdapter mouseListner = new MouseAdapter() {
        public void mouseEntered(MouseEvent me) {
            setForeground(HOVER_COLOR);
        }

        public void mouseExited(MouseEvent me) {
            if (!isSelected())
                setForeground(FOREGROUND_COLOR);
        }
    };

    public CoolButton(int style) {
        super();
        this.style = style;
        setContentAreaFilled(false);
        setBorderPainted(false);
        setForeground(FOREGROUND_COLOR);
        addMouseListener(mouseListner);
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        int h = getHeight();
        int w = getWidth();

        GradientPaint gpUp = new GradientPaint(0, 0, new Color(0xF6F8FA), 0, h / 2, new Color(0xA3B8CB));
        GradientPaint gpDown = new GradientPaint(0, h / 2, new Color(0xA3B8CB), 0, h, new Color(0xD5EAFB));

        GradientPaint gpSelUp = new GradientPaint(0, 0, new Color(0x8FA9C0), 0, h / 2, new Color(0x33628C));
        GradientPaint gpSelDown = new GradientPaint(0, h / 2, new Color(0x33628C), 0, h, new Color(0x76A4CE));

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint p1;
        GradientPaint p2;

        if (getModel().isPressed()) {
            p1 = new GradientPaint(0, 0, new Color(0x22374A), 0, h - 1, new Color(0x62778A));
            p2 = new GradientPaint(0, 1, new Color(0, 0, 0, 50), 0, h - 3, new Color(255, 255, 255, 100));
        } else {
            p1 = new GradientPaint(0, 0, new Color(0x62778A), 0, h - 1, new Color(0x22374A));
            p2 = new GradientPaint(0, 1, new Color(255, 255, 255, 100), 0, h - 3, new Color(0, 0, 0, 50));
        }

        if (style == ALONE) {
            _fillBackground(g2d, w, h, gpUp, gpDown, gpSelUp, gpSelDown, new RoundRectangle2D.Float(0, 0, w - 1, h - 1, 20, 20));

            g2d.setPaint(p1);
            g2d.drawRoundRect(0, 0, w - 1, h - 1, 20, 20);
            g2d.setPaint(p2);
            g2d.drawRoundRect(1, 1, w - 3, h - 3, 18, 18);
        } else if (style == LEFT) {
            _fillBackground(g2d, w, h, gpUp, gpDown, gpSelUp, gpSelDown, new RoundRectangle2D.Float(0, 0, w - 1 + 20, h - 1, 20, 20));

            g2d.setPaint(p1);
            g2d.drawRoundRect(0, 0, w - 1 + 20, h - 1, 20, 20);
            g2d.setPaint(p2);
            g2d.drawRoundRect(1, 1, w - 3 + 20, h - 3, 18, 18);
            g2d.setPaint(p2);
            g2d.drawLine(w - 1, 2, w - 1, h - 1);
        } else if (style == MIDDLE) {
            _fillBackground(g2d, w, h, gpUp, gpDown, gpSelUp, gpSelDown, new Rectangle2D.Float(-20, 0, w - 1 + 20, h - 1));

            g2d.setPaint(p1);
            g2d.drawRect(-5, 0, w - 1 + 10, h - 1);
            g2d.setPaint(p2);
            g2d.drawRect(-5, 1, w - 3 + 10, h - 3);
            g2d.setPaint(p1);
            g2d.drawLine(0, 0, 0, h);
            g2d.drawLine(w - 1, 1, w - 1, h);
            g2d.setPaint(p2);
            g2d.drawLine(1, 2, 1, h);
            g2d.drawLine(w - 2, 2, w - 2, h - 1);
        } else if (style == RIGHT) {
            _fillBackground(g2d, w, h, gpUp, gpDown, gpSelUp, gpSelDown, new RoundRectangle2D.Float(-20, 0, w - 1 + 20, h - 1, 20, 20));

            g2d.setPaint(p1);
            g2d.drawRoundRect(-20, 0, w - 1 + 20, h - 1, 20, 20);
            g2d.setPaint(p2);
            g2d.drawRoundRect(1 - 20, 1, w - 3 + 20, h - 3, 18, 18);
            g2d.setPaint(p2);
            g2d.drawLine(0, 2, 0, h - 1);
        }

        g2d.dispose();
        super.paintComponent(g);
    }

    private void _fillBackground(Graphics2D g2d, int w, int h, GradientPaint gpUp, GradientPaint gpDown,
                                 GradientPaint gpSelUp, GradientPaint gpSelDown, RectangularShape r) {
        Shape clip = g2d.getClip();
        g2d.clip(r);
        g2d.setPaint(isSelected() ? gpSelUp : gpUp);
        g2d.fillRect(0, 0, w, h / 2);
        g2d.setPaint(isSelected() ? gpSelDown : gpDown);
        g2d.fillRect(0, h / 2, w, h);
        g2d.setClip(clip);
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame();

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new FlowLayout());

        CoolButton c1 = new CoolButton(1);
        c1.setText("1");
        CoolButton c2 = new CoolButton(0);
        c2.setText("2");
        CoolButton c3 = new CoolButton(3);
        c3.setText("3");

        CoolButton c4 = new CoolButton(2);
        c4.setText("0");

        panel.add(c1);
        panel.add(c2);
        panel.add(c3);
        panel.add(c4);

        frame.add(panel);

        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
