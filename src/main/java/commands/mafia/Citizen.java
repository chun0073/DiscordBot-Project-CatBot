package commands.mafia;

public abstract class Citizen extends Person {
    private String description = "You become a kind innocent citizen. Citizens have no special functions";
    public Citizen(String name, String channel, String id){
        super(name, channel, id);
    }

    public String getDescription(){
        return this.description;
    }

}
