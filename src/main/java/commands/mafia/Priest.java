package commands.mafia;

import commands.mafia.Citizen;

public class Priest extends Citizen {
    private String description = "You are a priest. You can pray god for yourself.";
    public Priest(String name, String channel, String id) {
        super(name,channel,id);
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
