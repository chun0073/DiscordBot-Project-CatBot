package commands.mafia;

public class Hacker extends Killer{
    private String description = "You are a kind of killer. You can kill someone when the night comes";
    public Hacker(String name, String channel, String id){
        super(name, channel, id);

    }
    public void kill(String target){

    }
    @Override
    public String vote(String target) {
        return target;
    }
    @Override
    public boolean solve() {
        return true;
    }
}
