package Simulation;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.IntStream;

public class Simulation {
    public static void main(String[] args) {
        JFrame f = new JFrame("Ecosystem Simulation");
        f.setSize(800, 800);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(null);

        JPanel simPanel = new JPanel(null);
        simPanel.setBounds(0, 0, 800, 600);
        simPanel.setBackground(new Color(163, 46, 7));
        f.add(simPanel);

        Container<Animal> animals = new Container<>();
        Container<Vegetation> vegetation = new Container<>();

        grid g = new grid();
        g.set(animals, vegetation, simPanel);

        JLabel statsLabel = new JLabel();
        statsLabel.setBounds(10, 5, 300, 20);
        statsLabel.setForeground(Color.WHITE);
        statsLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        simPanel.add(statsLabel);

        int[] dingoCount = {0};
        int[] kangarooCount = {0};
        Deque<Integer> dingoHistory = new ArrayDeque<>();
        Deque<Integer> kangarooHistory = new ArrayDeque<>();
        int maxSamples = 1000;

        PopulationGraph graphPanel = new PopulationGraph(dingoHistory, kangarooHistory);
        graphPanel.setBounds(0, 600, 800, 200);
        f.add(graphPanel);

        g.addListener(new AnimalListener() {
            private void update() {
                statsLabel.setText("Dingos: " + dingoCount[0] + " | Kangaroos: " + kangarooCount[0]);
            }

            @Override
            public void onAnimalBorn(Animal a) {
                if (a instanceof Dingo) {
                    dingoCount[0]++;
                } else if (a instanceof Kangaroo) {
                    kangarooCount[0]++;
                }
                System.out.println("New " + a.getClass().getSimpleName());
                update();
            }

            @Override
            public void onAnimalDied(Animal a, String cause) {
                if (a instanceof Dingo) {
                    dingoCount[0]--;
                } else if (a instanceof Kangaroo) {
                    kangarooCount[0]--;
                }
                if ("eaten".equals(cause)) {
                    System.out.println(a.getClass().getSimpleName() + " was eaten");
                } else {
                    System.out.println(a.getClass().getSimpleName() + " died of " + cause);
                }
                update();
            }
        });

        IntStream.range(0, 300).forEach(i -> {
            double x = (int) (Math.random() * 800);
            double y = (int) (Math.random() * 600);
            g.newVegetation(x, y);
        });
        IntStream.range(0, 3).forEach(i -> {
            double x = (int) (Math.random() * 750) + 25;
            double y = (int) (Math.random() * 550) + 25;
            g.newDingo(x, y);
        });
        IntStream.range(0, 10).forEach(i -> {
            double x = (int) (Math.random() * 750) + 25;
            double y = (int) (Math.random() * 550) + 25;
            g.newKangaroo(x, y);
        });

        Timer timer = new Timer(20, e -> {
            try {
                int i = animals.size();
                while (i > 0) {
                    i--;
                    animals.get(i).wander(g);
                }
                g.growVegetation();
                g.growVegetation();
                g.growVegetation();

                dingoHistory.addLast(dingoCount[0]);
                kangarooHistory.addLast(kangarooCount[0]);
                while (dingoHistory.size() > maxSamples) {
                    dingoHistory.removeFirst();
                }
                while (kangarooHistory.size() > maxSamples) {
                    kangarooHistory.removeFirst();
                }

                graphPanel.repaint();
                f.repaint();
            } catch (IndexOutOfBoundsException ex) {
                System.out.println(ex);
                System.out.println(animals.size());
            }
        });

        f.setVisible(true);
        timer.start();
    }
}
