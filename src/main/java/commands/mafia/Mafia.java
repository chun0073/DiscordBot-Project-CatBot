package commands.mafia;

import java.util.*;

public class Mafia {

    private ArrayList<String> roles = new ArrayList<>();
    private Map<String, Person> players = new HashMap<String, Person>();
    private Person player;
    private static int count = 0;
    public Mafia() {
        roles.add("commands.mafia.Hacker"); roles.add("commands.mafia.Doctor"); roles.add("commands.mafia.Priest");
        roles.add("commands.mafia.Programmer"); roles.add("commands.mafia.Architect"); roles.add("commands.mafia.Lawyer");
        Collections.shuffle(roles);

    }
    public String join(String name, String channel, String id){
        String alter;
        if(roles.size()>0) {
            if (roles.get(0) == "commands.mafia.Hacker") {
                player = new Hacker(name, channel, id);
                players.put(roles.get(0), player);
                alter = "Wait for "+(roles.size()-1)+" more players";
                roles.remove(0);
            } else if (roles.get(0) == "Laywer") {
                player = new Lawyer(name, channel, id);
                players.put(roles.get(0), player);
                alter = "Wait for "+(roles.size()-1)+" more players";
                roles.remove(0);
            } else if (roles.get(0) == "commands.mafia.Architect") {
                player = new Architect(name, channel, id);
                players.put(roles.get(0), player);
                alter = "Wait for "+(roles.size()-1)+" more players";
                roles.remove(0);
            } else if (roles.get(0) == "commands.mafia.Doctor") {
                player = new Doctor(name, channel, id);
                players.put(roles.get(0), player);
                alter = "Wait for "+(roles.size()-1)+" more players";
                roles.remove(0);
            } else if (roles.get(0) == "commands.mafia.Programmer") {
                player = new Programmer(name, channel, id);
                players.put(roles.get(0), player);
                alter = "Wait for "+(roles.size()-1)+" more players";
                roles.remove(0);
            } else {
                player = new Priest(name, channel, id);
                players.put(roles.get(0), player);
                alter = "Wait for "+(roles.size()-1)+" more players";
                roles.remove(0);
            }
        }
        else{
            alter = "But no vacancy";
        }
        return alter;
    }
    public Map getPlayers(){
        return players;
    }
    public Person getPlayer(String key){
        return players.get(key);
    }
    public List getRoles(){
        return roles;
    }

}
