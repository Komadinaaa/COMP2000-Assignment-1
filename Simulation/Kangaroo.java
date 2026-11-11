package Simulation;

import java.awt.*;

public class Kangaroo extends Animal {

    public Kangaroo() {
        maxAge = 200;
        breedCooldown = 6000;
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
    }

    @Override
    protected boolean isPrey() {
        return true;
    }

    @Override
    protected boolean isPredator() {
        return false;
    }

    @Override
    protected int getFoodValue() {
        return plantFoodValue;
    }

    public void flee(grid g) {
        g.animals.stream()
                .filter(other -> other.isPredator() && dist(this, other) < SensoryDistance)
                .findFirst()
                .ifPresent(other -> moveAwayFrom(other));
    }

    public void forage(grid g) {
        for (int i = g.vegetation.size() - 1; i >= 0; i--) {
            Vegetation plant = g.vegetation.get(i);
            double Dist = Math.sqrt(Math.pow(plant.x - x, 2) + Math.pow(plant.y - y, 2));
            // if Kangaroo is within forage distance of a plant, the plant is eaten and the
            // kangaroo gains hunger points from plantFoodValue.
            if (Dist < forageDist) {
                eat();
                g.eaten(plant);
                // System.out.println("Forage Successful");

                break;
            }
        }

    }
    public void moveAwayFrom(Animal other) {
        // 1. Get the vector pointing away from the other object

        double dx = this.x - other.x;
        double dy = this.y - other.y;
        
        double distance = Math.hypot(dx, dy);

        // 2. Normalize the direction vector (length of 1)
        double dirX = dx / distance;
        double dirY = dy / distance;

        // Optional: Update this object's vx and vy to reflect the new trajectory
        this.moveX = dirX * this.speed;
        this.moveY = dirY * this.speed;
    }
}
