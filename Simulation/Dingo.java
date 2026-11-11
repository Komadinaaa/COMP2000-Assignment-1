package Simulation;

import java.awt.Color;

public class Dingo extends Animal {

    public Dingo() {
        maxAge = 300;
        breedCooldown = 13000;
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
        g.animals.stream()
                .filter(other -> other.isPrey())
                .filter(other -> {
                    if (this.energy < maxEnergy / 3) {
                        return dist(this, other) < SensoryDistance;
                    } else {
                        return dist(this, other) < 15;
                    }
                })
                .findFirst()
                .ifPresent(other -> {
                    eat();
                    if (this.energy < maxEnergy / 3) {
                        this.x = other.x;
                        this.y = other.y;
                        this.changeDirection();
                    } else {
                    }
                    g.death(other, "eaten");
                });
    }
}