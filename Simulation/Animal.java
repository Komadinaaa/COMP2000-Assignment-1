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
    double maxEnergy = 100;

    double hunger = 75;
    double maxHunger = 100;

    double speed = energy / 18;
    double maxSpeed = maxEnergy / 18;

    int age;

    int maxAge = 200;
    int update;
    int SensoryDistance = 50;

    double breedEnergy = 70;
    int breedAge = 30;
    long breedCooldown = 8000;
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
        // animal wanders around, direction determined in create

        // if energy < restEnergy, forces animal to rest to regain energy.
        if (energy <= restEnergy) {
            rest(a);
            return;
        }
        preMove(a);

        a.checkBounds(this);

        x += moveX;
        y += moveY;

        setBounds((int) x, (int) y, Animal_Size, Animal_Size);

        update();
        ageAnimal();
        hungry();
        breed(a);

        // Hooks for species-specific behavior
        consumeEnergy();
        checkDeath(a);

        // If still alive and hungry, seek food
        if (isAlive()) {
            handleFeeding(a);
        }
    }

        // Hooks with default implementations
    protected void preMove(grid g) {
        // Default: do nothing
    }


    protected abstract void consumeEnergy();

    protected void handleFeeding(grid g) {
        if (hunger < maxHunger) {
            seekFood(g);
        }
    }
        protected void seekFood(grid g) {
        // Default: nothing
    }

    protected void checkDeath(grid g) {
        if (energy <= 0) {
            g.death(this);
            System.out.println(this.getClass().getSimpleName() + " died of exhaustion");
            return;
        }
        if (age > maxAge) {
            g.death(this);
            System.out.println(this.getClass().getSimpleName() + " died of old age");
            return;
        }
        if (hunger <= 0) {
            g.death(this);
            System.out.println(this.getClass().getSimpleName() + " died of hunger");
            return;
        }
    }

    protected boolean isAlive() {
        // Simple check - grid.death removes from containers, but useful as a guard
        return energy > 0 && age <= maxAge && hunger > 0;
    }

    public double dist(Animal A, Animal B) {
        // returns distance between animals A and B.
        double dx = B.x - A.x;
        double dy = B.y - A.y;

        return Math.sqrt(dx * dx + dy * dy);
    }

    public void rest(grid a) {
        // if energy <= maxEnergy animal rests to restore energy.
        if (energy <= maxEnergy) {
            energy = maxEnergy;
        }
    }

    public int ageAnimal() {
        // Ages animal by 1 when called.
        age += 1;
        return age;
    }

    public int update() {
        // update by 1 every call
        update += 1;
        return update;
    }

    public void breed(grid g) {
        // if eligible kangaroo, create new kangaroo
        // if eligible dingo, create new dingo

        if (canBreed()) {

            if (this instanceof Kangaroo) {
                g.newKangaroo(x, y);
                System.out.println("New Kangaroo");
            }
            if (this instanceof Dingo) {
                g.newDingo(x, y);
                System.out.println("New Dingo");
            }
            // breeding costs 20 points of energy
            energy -= 20;
            // takes current time when breeding occurs.
            lastBreedTime = System.currentTimeMillis();
        }
    }

    public boolean canBreed() {
        // checks breeding eligibility
        long currentTime = System.currentTimeMillis();
        // if age > minimum breeding, energy > minimum energy requirement
        // Hunger > 30 and after breed cooldown, CanBreed = true
        return age >= breedAge &&
                energy >= breedEnergy &&
                currentTime - lastBreedTime >= breedCooldown &&
                hunger > 30;

    }

    public void hungry() {
        // hunger reduced when 1 when called.
        hunger -= 1;
    }

    public void eat() {
        // if kangaroo eats: hunger = hunger + food value from plants
        if (this instanceof Kangaroo) {
            hunger += plantFoodValue;
        }
        // if dingo eats: hunger = hunger + food value from kangaroos.
        if (this instanceof Dingo) {
            hunger += kangarooFoodValue;
        }
    }

    public void changeDirection() {
        // Four different directions, if math.random returns a 1 or a 2, moveX is
        // negated
        // if math.random returns 2 or 4, moveY is negated
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
