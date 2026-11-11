package Simulation;

import javax.swing.*;
import java.awt.*;
import java.util.stream.IntStream;

public class Simulation {
    public static void main(String[] args) {
        JFrame f = new JFrame("Ecosystem Simulation");
        f.setSize(800, 600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(null);
        f.getContentPane().setBackground(new Color(163, 46, 7));

        Container<Animal> animals = new Container<>();
        Container<Vegetation> vegetation = new Container<>();

        grid g = new grid();
        g.set(animals, vegetation, f);

        JLabel statsLabel = new JLabel();
        statsLabel.setBounds(10, 5, 300, 20);
        statsLabel.setForeground(Color.WHITE);
        statsLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        f.add(statsLabel);

        g.addListener(new AnimalListener() {
            private int dingos = 0;
            private int kangaroos = 0;

            private void update() {
                statsLabel.setText("Dingos: " + dingos + " | Kangaroos: " + kangaroos);
            }

            @Override
            public void onAnimalBorn(Animal a) {
                if (a instanceof Dingo) {
                    dingos++;
                } else if (a instanceof Kangaroo) {
                    kangaroos++;
                }
                System.out.println("New " + a.getClass().getSimpleName());
                update();
            }

            @Override
            public void onAnimalDied(Animal a, String cause) {
                if (a instanceof Dingo) {
                    dingos--;
                } else if (a instanceof Kangaroo) {
                    kangaroos--;
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
        IntStream.range(0, 2).forEach(i -> {
            double x = (int) (Math.random() * 750) + 25;
            double y = (int) (Math.random() * 550) + 25;
            g.newDingo(x, y);
        });
        IntStream.range(0, 10).forEach(i -> {
            double x = (int) (Math.random() * 750) + 25;
            double y = (int) (Math.random() * 550) + 25;
            g.newKangaroo(x, y);
        });

        Timer timer = new Timer(30, e -> {
            try {
                int i = animals.size();
                while (i > 0) {
                    i--;
                    animals.get(i).wander(g);
                }
                g.growVegetation();
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
