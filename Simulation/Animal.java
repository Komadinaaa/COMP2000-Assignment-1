package Simulation;

import java.awt.*;

public abstract class Animal extends Panel {

    double moveX;
    double moveY;

    double x;
    double y;

    int Animal_Size = 25;

    double energy = 100;
    double restEnergy = 50;
    double maxEnergy = 150;

    double hunger = 75;
    double maxHunger = 100;

    double speed = energy / 18;
    double maxSpeed = maxEnergy / 18;

    int age;

    int maxAge;
    int update;
    int SensoryDistance = 30;

    double breedEnergy = 70;
    int breedAge = 30;
    long breedCooldown;
    long lastBreedTime = 0;

    int plantFoodValue = 10;
    int kangarooFoodValue = 50;

    int forageDist = 50;

    public Animal() {
        setBounds((int) x, (int) y, 10, 10);

    }

    public void create(double initX, double initY) {
        // create new object at initial X position and initial Y Position
        x = initX;
        y = initY;

        changeDirection();
    }

    public void wander(grid a) {
        if (energy <= restEnergy) {
            rest(a);
            return;
        }
        preMove(a);


        x += moveX;
        y += moveY;
        a.checkBounds(this);

        setBounds((int) x, (int) y, Animal_Size, Animal_Size);

        update();
        ageAnimal();
        hungry();
        breed(a);

        // Hooks
        consumeEnergy();
        checkDeath(a);

       
        if (isAlive()) {
            handleFeeding(a);
        }
    }

        // Hooks with default implementations
    protected void preMove(grid g) {
    }

    protected abstract void consumeEnergy();

    protected abstract void reproduce(grid g);

    protected abstract boolean isPrey();

    protected abstract boolean isPredator();

    protected abstract int getFoodValue();

    protected void handleFeeding(grid g) {
        if (hunger < maxHunger) {
            seekFood(g);
        }
    }
        protected void seekFood(grid g) {
    }

    protected void checkDeath(grid g) {
        if (energy <= 0) {
            g.death(this, "exhaustion");
            return;
        }
        if (age > maxAge) {
            g.death(this, "old age");
            return;
        }
        if (hunger <= 0) {
            g.death(this, "hunger");
            return;
        }
    }

    protected boolean isAlive() {
        return energy > 0 && age <= maxAge && hunger > 0;
    }

    public double dist(Animal A, Animal B) {
        double dx = B.x - A.x;
        double dy = B.y - A.y;

        return Math.sqrt(dx * dx + dy * dy);
    }

    public void rest(grid a) {
        if (energy <= maxEnergy) {
            energy = maxEnergy;
        }
    }

    public int ageAnimal() {
        age += 1;
        return age;
    }

    public int update() {
        update += 1;
        return update;
    }

    public final void breed(grid g) {
        if (canBreed()) {
            reproduce(g);
            energy -= 20;
            lastBreedTime = System.currentTimeMillis();
        }
    }

    public boolean canBreed() {
        long currentTime = System.currentTimeMillis();
        return age >= breedAge &&
                energy >= breedEnergy &&
                currentTime - lastBreedTime >= breedCooldown &&
                hunger > 30;

    }

    public void hungry() {
        hunger -= 1;
    }

    public void eat() {
        hunger = Math.min(maxHunger, hunger + getFoodValue());
    }

    public void changeDirection() {
        
        moveX = Math.random() * speed;
        moveY = Math.random() * speed;
        int direction = (int) Math.ceil(Math.random() * 4);
        if (direction == 1 || direction == 2) {
            moveX *= -1;
        }
        if (direction == 2 || direction == 4) {
            moveY *= -1;
        }
    }
}