package ceat.game.entity;

import java.util.ArrayList;

public class EntityQuery <E extends BoardEntity, T extends BoardEntity> {
    private ArrayList<E> a;
    private ArrayList<T> b;

    public EntityQuery<E, T> overlap(ArrayList<E> entities, ArrayList<T> otherEntities) {
        ArrayList<E> overlapA = new ArrayList<>();
        ArrayList<T> overlapB = new ArrayList<>();
        for (E thing : entities) {
            for (T otherThing : otherEntities) {
                if (thing.overlaps(otherThing) || otherThing.overlaps(thing)) {
                    if (!overlapA.contains(thing))
                        overlapA.add(thing);
                    if (!overlapB.contains(otherThing))
                        overlapB.add(otherThing);
                }
            }
        }
        a = overlapA;
        b = overlapB;
        return this;
    }

    public ArrayList<E> getA() {
        return a;
    }

    public ArrayList<T> getB() {
        return b;
    }

    public String toString() {
        return "ENTITY QUERY";
    }
    public boolean equals() {
        return false;
    }
}
