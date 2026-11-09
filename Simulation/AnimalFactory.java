package Simulation;

public class AnimalFactory {
    public enum AnimalType {
        DINGO,
        KANGAROO
    }

    public static Animal create(AnimalType type, double x, double y) {
        Animal animal;
        if (type == AnimalType.DINGO) {
            animal = new Dingo();
        } else if (type == AnimalType.KANGAROO) {
            animal = new Kangaroo();
        } else {
            throw new IllegalArgumentException("Unknown animal type: " + type);
        }
        animal.create(x, y);
        return animal;
    }
}
