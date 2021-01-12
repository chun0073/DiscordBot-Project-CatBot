package commands.mafia;

import commands.mafia.Citizen;

public class Programmer extends Citizen {
    private String description = "You are a programmer. You can make anything you want if you can survive here.";
    public Programmer(String name, String channel, String id){
        super(name,channel, id);
    }
    public String getDescription(){
        return this.description;
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
