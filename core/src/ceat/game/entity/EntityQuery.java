package ceat.game.entity;

import java.util.ArrayList;

public class EntityQuery<E extends BoardEntity, T extends BoardEntity> {
    public ArrayList<E> a;
    public ArrayList<T> b;

    public EntityQuery<E, T> overlap(ArrayList<E> entities, ArrayList<T> otherEntities) {
        ArrayList<E> overlapA = new ArrayList<>();
        ArrayList<T> overlapB = new ArrayList<>();
        for (E thing : entities) {
            for (T otherThing : otherEntities) {
                if (thing.gridX == otherThing.gridX && thing.gridY == otherThing.gridY) {
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
}
