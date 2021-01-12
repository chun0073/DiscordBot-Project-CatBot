package commands.mafia;

public interface Operation {
    public abstract String vote(String target);
    public abstract boolean solve();

}
