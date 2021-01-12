package commands.mafia;

public class Doctor extends Citizen{
    public String description = "You are a doctor who can save someone's life";
    public Doctor(String name, String channel, String id){
        super(name, channel, id);
    }

    public String getDescription(){
        return this.description;
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
