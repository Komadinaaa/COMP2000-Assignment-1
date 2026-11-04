package Simulation;

import java.awt.*;

public class Kangaroo extends Animal {

    public Kangaroo() {
        setBackground(new Color(150, 118, 108));
    }

    @Override
    protected void preMove(grid g) {
        flee(g);
    }

    @Override
    protected void consumeEnergy() {
        if (energy > 0) {
            energy -= 10;
        }
    }

    @Override
    protected void seekFood(grid g) {
        forage(g);
    }

    @Override
    protected void reproduce(grid g) {
        g.newKangaroo(x, y);
        System.out.println("New Kangaroo");
    }

    public void flee(grid g) {
        // if kangaroo is within sensoryDistance of a dingo it changes directions to
        // flee.
        Container<Animal> animals = g.animals;
        for (int i = animals.size() - 1; i >= 0; i--) {
            Animal other = animals.get(i);
            if (other instanceof Dingo) {
                if (dist(this, other) < SensoryDistance) {
                    changeDirection();
                    break;
                }
            }
        }
    }

    public void forage(grid g) {
        for (int i = g.vegetation.size() - 1; i >= 0; i--) {
            Vegetation plant = g.vegetation.get(i);
            double dx = plant.x - x;
            double dy = plant.y - y;
            double Dist = Math.sqrt(dx * dx + dy * dy);
            // if Kangaroo is within forage distance of a plant, the plant is eaten and the
            // kangaroo gains hunger points from plantFoodValue.
            if (Dist < forageDist) {
                eat();
                g.eaten(plant);
                System.out.println("Forage Successful");
                break;
            }
        }
    }
}
