package Simulation;

public class AnimalFactory {
    public enum AnimalType {
        DINGO,
        KANGAROO
    }

    public static Animal create(AnimalType type, double x, double y) {
        Animal animal = switch (type) {
            case DINGO -> new Dingo();
            case KANGAROO -> new Kangaroo();
        };
        animal.create(x, y);
        return animal;
    }
}
