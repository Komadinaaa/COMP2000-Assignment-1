package Simulation;

import java.awt.Color;

public class Dingo extends Animal {

    public Dingo() {
        setBackground(new Color(240, 126, 38));
    }

    @Override
    protected void consumeEnergy() {
        if (energy > 0) {
            energy -= 35;
        }
    }

    @Override
    protected void seekFood(grid g) {
        hunt(g);
    }

    @Override
    protected void reproduce(grid g) {
        g.newDingo(x, y);
        System.out.println("New Dingo");
    }

    @Override
    protected boolean isPrey() {
        return false;
    }

    @Override
    protected boolean isPredator() {
        return true;
    }

    @Override
    protected int getFoodValue() {
        return kangarooFoodValue;
    }

    public void hunt(grid g) {
        // if Dingo is within sensoryDistance of kangaroo, hunt is successful, kangaroo
        // dies and
        // Dingo eats, gaining hunger points from KangarooFoodValue.
        Container<Animal> animals = g.animals;
        for (int i = animals.size() - 1; i >= 0; i--) {
            Animal other = animals.get(i);
            if (other.isPrey()) {
                if (dist(this, other) < SensoryDistance) {
                    eat();
                    System.out.println("Hunt Successful");
                    System.out.println("Kangaroo was eaten");
                    g.death(other);
                    break;
                }
            }
        }
    }
}
