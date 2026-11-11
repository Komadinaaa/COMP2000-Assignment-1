package Simulation;



import java.util.ArrayList;
import java.util.List;

public class grid {

    Container<Animal> animals;

    Container<Vegetation> vegetation;

    java.awt.Container simArea;

    private final List<AnimalListener> listeners = new ArrayList<>();


    public <T> void set(Container<Animal> i, Container<Vegetation> v, java.awt.Container simArea) {

        animals = i;

        vegetation = v;

        this.simArea = simArea;

    }

    public void addListener(AnimalListener l) {
        listeners.add(l);
    }

    public void removeListener(AnimalListener l) {
        listeners.remove(l);
    }

    private void fireAnimalBorn(Animal a) {
        listeners.forEach(l -> l.onAnimalBorn(a));
    }

    private void fireAnimalDied(Animal a, String cause) {
        listeners.forEach(l -> l.onAnimalDied(a, cause));
    }

    public void newDingo(double x, double y) {
        Animal d = AnimalFactory.create(AnimalFactory.AnimalType.DINGO, x, y);
        this.simArea.add(d);
        this.animals.add(d);
        fireAnimalBorn(d);
    }

    public void newKangaroo(double x, double y) {
        Animal k = AnimalFactory.create(AnimalFactory.AnimalType.KANGAROO, x, y);
        this.simArea.add(k);
        this.animals.add(k);
        fireAnimalBorn(k);
    }

    public void death(Animal a) {
        death(a, "unknown");
    }

    public void death(Animal a, String cause) {
        // Animal dies and is removed
        animals.remove(a);
        simArea.remove(a);
        fireAnimalDied(a, cause);
    }

    public void checkBounds (Animal a) {
        int maxX = simArea.getWidth() - a.Animal_Size;
        int maxY = simArea.getHeight() - a.Animal_Size;

        if (a.x < 0) {
            a.x = 0;
            a.moveX = Math.abs(a.moveX);
        }
        else if (a.x > maxX) {
            a.x = maxX;
            a.moveX = -Math.abs(a.moveX);
        }

        if (a.y < 0) {
            a.y = 0;
            a.moveY = Math.abs(a.moveY);
        }
        else if (a.y > maxY) {
            a.y = maxY;
            a.moveY = -Math.abs(a.moveY);
        }
    }

    public void eaten(Vegetation v) {

        // vegetation is eaten and is removed

        vegetation.remove(v);

        simArea.remove(v);

    }

    public void growVegetation() {

        // vegetation is regenerated
        double x = Math.random() * 800 + 5;
        double y = Math.random() * 600 + 5;

        newVegetation(x, y);

    }

    public void newVegetation(double x, double y) {

        // create new vegetation

        Vegetation v = new Vegetation(x, y);

        this.simArea.add(v);

        this.vegetation.add(v);

    }

} 

