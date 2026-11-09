package Simulation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        g.addListener(new AnimalListener() {
            @Override
            public void onAnimalBorn(Animal a) {
                System.out.println("New " + a.getClass().getSimpleName());
            }

            @Override
            public void onAnimalDied(Animal a, String cause) {
                if ("eaten".equals(cause)) {
                    System.out.println(a.getClass().getSimpleName() + " was eaten");
                } else {
                    System.out.println(a.getClass().getSimpleName() + " died of " + cause);
                }
            }
        });

        for (int i = 0; i < 300; i++) {
            double x = (int) (Math.random() * 800);
            double y = (int) (Math.random() * 600);
            g.newVegetation(x, y);
        }
        for (int i = 0; i < 2; i++) {
            double x = (int) (Math.random() * 750) + 25;
            double y = (int) (Math.random() * 550) + 25;
            g.newDingo(x, y);
        }
        for (int i = 0; i < 10; i++) {
            double x = (int) (Math.random() * 750) + 25;
            double y = (int) (Math.random() * 550) + 25;
            g.newKangaroo(x, y);
        }

        Timer timer = new Timer(30, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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
            }
        });

        f.setVisible(true);
        timer.start();
    }
}
