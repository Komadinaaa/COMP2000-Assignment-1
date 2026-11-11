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
        g.vegetation.stream()
                .filter(plant -> {
                    double dx = plant.x - x;
                    double dy = plant.y - y;
                    return Math.sqrt(dx * dx + dy * dy) < forageDist;
                })
                .findFirst()
                .ifPresent(plant -> {
                    eat();
                    g.eaten(plant);
                });
    }

    public void moveAwayFrom(Animal other) { //move in the opposite direction of the dino
        double dx = this.x - other.x;
        double dy = this.y - other.y;

        double distance = Math.hypot(dx, dy);

        double dirX = dx / distance;
        double dirY = dy / distance;

        this.moveX = dirX * this.speed;
        this.moveY = dirY * this.speed;
    }
}