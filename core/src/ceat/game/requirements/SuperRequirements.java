package ceat.game.requirements;

public class SuperRequirements extends Requirements {
    public SuperRequirements() {
        super();
    }
    public SuperRequirements(int n) {
        super(n);
    }
    public void doThing() {
        super.doThing();
        System.out.println("b");
    }
    public String toString() {
        return "SUPER REQUIREMENTS";
    }
    public boolean equals() {
        return false;
    }
}
