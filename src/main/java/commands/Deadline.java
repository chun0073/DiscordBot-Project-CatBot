package commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.Timer;
import java.util.TimerTask;

public class Deadline extends ListenerAdapter implements DbAdapter{
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        Connection conn;
        Statement st;
        ResultSet rs;
        String[] message = event.getMessage().getContentRaw().split(" ");
        super.onGuildMessageReceived(event);
        try {
            conn = DriverManager.getConnection(url, username, password);
            st = conn.createStatement();
            if(message[0].equalsIgnoreCase("!add")){
                if(message.length<2) {
                    event.getChannel().sendMessage("To add a new deadline, follow the form-> !add courseName assignmentName yyyy-mm-dd dueTime").queue();
                }
                else{
                    try {
                        String sql = "INSERT INTO bot.deadline(course, content, dueDate, dueTime, Author)" +
                                "VALUES (\'"+
                                message[1]+"\',\'"+message[2]+"\',\'"+message[3]+"\',\'"+message[4]+"\',\'"+event.getAuthor().getName()
                                +"\')";
                        int success = st.executeUpdate(sql);
                        if(success == 1){
                            event.getChannel().sendMessage(message[2]+", "+message[3]+" is successfully stored!").queue();
                            conn.close();
                        }
                    }
                    catch (SQLException throwables){
                        throwables.printStackTrace();
                    }
                }
            }
            if(message[0].equalsIgnoreCase("!deadline")){
                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        try{
                            String sql =
                                    "SELECT DISTINCT course, content, dueDate, dueTime FROM bot.deadline WHERE dueDate < DATE_SUB(NOW(), INTERVAL -3 DAY) ORDER BY dueDate ASC";
                            ResultSet rs = st.executeQuery(sql);
                            event.getChannel().sendMessage("DUE SOON!").queue();
                            while(rs.next()){
                                event.getChannel().sendMessage(rs.getString(1)+", "+
                                rs.getString(2)+" is due on "+rs.getString(3)+" "+rs.getString(4)).queue();
                            }
                            sql = "DELETE FROM bot.deadline WHERE dueDate < now() - interval 1 day";
                            int success = st.executeUpdate(sql);
                            if(success==1){
                                event.getChannel().sendMessage("Expired due dates deleted!");
                            }
                        }
                        catch (SQLException throwables){
                            throwables.printStackTrace();
                        }
                    }
                }, 1 * 1 * 100, 24 * 3600000);
            }
            if(message[0].equalsIgnoreCase("!deaddel")){
                if(message.length<2) {
                    event.getChannel().sendMessage("To delete a deadline, follow the form-> !deaddel yyyy-mm-dd dueTime").queue();
                }
                else{
                    try {
                        String sql = "DELETE FROM bot.deadline WHERE dueDate = \'" + message[1]+"\' AND dueTime = \'" + message[2]+"\'";
                        int success = st.executeUpdate(sql);
                        if(success == 1){
                            event.getChannel().sendMessage("Successfully deleted!").queue();
                            conn.close();
                        }
                    }
                    catch (SQLException throwables){
                        throwables.printStackTrace();
                    }
                }
            }
            if(message[0].equalsIgnoreCase("!deadall")){
                try{
                    String sql =
                            "SELECT course, content, dueDate, dueTime FROM bot.deadline ORDER BY dueDate ASC LIMIT 10";
                    rs = st.executeQuery(sql);
                    while(rs.next()){
                        event.getChannel().sendMessage(rs.getString(1)+", "+
                                rs.getString(2)+" is due on "+rs.getString(3)+" "+rs.getString(4)).queue();
                    }
                }
                catch (SQLException throwables){
                    throwables.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
