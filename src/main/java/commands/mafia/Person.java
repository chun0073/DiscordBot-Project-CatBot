package commands.mafia;

public abstract class Person implements Operation{
    private String name, channel;
    private String id;
    private String description = "entity";
    public Person(String name, String channel, String id){
        setName(name);
        this.channel = channel;
        this.id = id;
    }
    public String getDescription(){
        return description;
    }
    public void setName(String name){
        if(name.length()>0){
            this.name = name;
        }
        else{
            this.name = null;
        }
    }
    public String getId(){return this.id;}
    public String getName(){
        return this.name;
    }
    public String getChannel() {
        return this.channel;
    }
}
