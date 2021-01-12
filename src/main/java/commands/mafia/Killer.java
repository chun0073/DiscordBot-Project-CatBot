package commands.mafia;

public abstract class Killer extends Person {

    public Killer(String name, String channel, String id) {
        super(name, channel, id);
    }
    public abstract void kill(String name);
}
