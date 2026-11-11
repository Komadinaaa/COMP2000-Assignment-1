package Simulation;

import javax.swing.*;
import java.awt.*;
import java.util.Deque;

public class PopulationGraph extends JPanel {
    private final Deque<Integer> dingoHistory;
    private final Deque<Integer> kangarooHistory;

    public PopulationGraph(Deque<Integer> dingoHistory, Deque<Integer> kangarooHistory) {
        this.dingoHistory = dingoHistory;
        this.kangarooHistory = kangarooHistory;
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int pad = 35;

        g.setColor(Color.BLACK);
        g.drawLine(pad, h - pad, w - pad, h - pad);
        g.drawLine(pad, pad, pad, h - pad);
        g.drawString("Time", w / 2 - 15, h - 10);
        g.drawString("Pop", 5, pad - 10);

        int n = Math.max(dingoHistory.size(), kangarooHistory.size());
        if (n < 2) {
            drawLegend(g, w, pad);
            return;
        }

        int max = 1;
        for (int v : dingoHistory) {
            max = Math.max(max, v);
        }
        for (int v : kangarooHistory) {
            max = Math.max(max, v);
        }

        g.setColor(Color.LIGHT_GRAY);
        g.drawString(String.valueOf(max), 5, pad + 5);
        g.drawString("0", 15, h - pad + 5);

        drawSeries(g, dingoHistory, n, max, w, h, pad, new Color(240, 126, 38));
        drawSeries(g, kangarooHistory, n, max, w, h, pad, new Color(150, 118, 108));
        drawLegend(g, w, pad);
    }

    private void drawSeries(Graphics2D g, Deque<Integer> data, int n, int max, int w, int h, int pad, Color c) {
        g.setColor(c);
        int i = 0;
        int prevX = 0;
        int prevY = 0;
        for (int v : data) {
            int x = pad + (n == 1 ? 0 : i * (w - 2 * pad) / (n - 1));
            int y = h - pad - (max == 0 ? 0 : v * (h - 2 * pad) / max);
            if (i > 0) {
                g.drawLine(prevX, prevY, x, y);
            }
            prevX = x;
            prevY = y;
            i++;
        }
    }

    private void drawLegend(Graphics2D g, int w, int pad) {
        g.setColor(new Color(240, 126, 38));
        g.fillRect(w - 110, pad, 10, 10);
        g.setColor(Color.BLACK);
        g.drawString("Dingos", w - 95, pad + 10);

        g.setColor(new Color(150, 118, 108));
        g.fillRect(w - 110, pad + 15, 10, 10);
        g.setColor(Color.BLACK);
        g.drawString("Kangaroos", w - 95, pad + 25);
    }
}
