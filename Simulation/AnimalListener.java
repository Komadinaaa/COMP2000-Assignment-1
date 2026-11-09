package Simulation;

public interface AnimalListener {
    void onAnimalBorn(Animal a);
    void onAnimalDied(Animal a, String cause);
}
