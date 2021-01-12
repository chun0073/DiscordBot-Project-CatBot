package commands.mafia;

public class Architect extends Citizen{
    public Architect(String name, String channel, String id) {
        super(name, channel, id);
    }

    @Override
    public String vote(String target) {
        return null;
    }

    @Override
    public boolean solve() {
        return false;
    }
}
