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
        String[] message = event.getMessage().getContentRaw().split(":");
        String[] message2 = event.getMessage().getContentRaw().split(" ");
        super.onGuildMessageReceived(event);
        try {
            conn = DriverManager.getConnection(url, username, password);
            st = conn.createStatement();
            if(message[0].equalsIgnoreCase("!add")){
                if(message.length<2) {
                    event.getChannel().sendMessage("To add a new deadline, follow the form-> !add:courseName:assignmentName:yyyy-mm-dd:dueTime:yourLevel").queue();
                }
                else{
                    try {
                        String sql = "INSERT INTO bot.deadline(course, content, dueDate, dueTime, Author, levels)" +
                                "VALUES (\'"+
                                message[1]+"\',\'"+message[2]+"\',\'"+message[3]+"\',\'"+message[4]+"\',\'"+event.getAuthor().getName()
                                +"\',\'"+message[5]+"\')";
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
            if(message2[0].equalsIgnoreCase("!dead")) {
                if(message2.length<2){
                    event.getChannel().sendMessage("Command !dead supports 'del' and 'all'. EX) !dead all ");
                    event.getChannel().sendMessage("!dead init is not allowed");
                } else {
                    if (message2[1].equalsIgnoreCase("init") && message2.length>2) {
                        event.getChannel().sendMessage("This command is allowed only once").queue();
                        Timer timer = new Timer();
                        timer.scheduleAtFixedRate(new TimerTask() {
                            @Override
                            public void run() {
                                try {
                                    String sql = "DELETE FROM bot.deadline WHERE dueDate < (NOW() - INTERVAL 1 DAY)";

                                    int success = st.executeUpdate(sql);
                                    if (success == 1) {
                                        event.getChannel().sendMessage("Expired due dates successfully deleted!").queue();
                                    }

                                    sql = "SELECT DISTINCT course, content, dueDate, dueTime FROM bot.deadline WHERE dueDate < DATE_SUB(NOW(), INTERVAL -3 DAY) AND levels LIKE \'%" + message2[2] + "%\' ORDER BY dueDate ASC";

                                    ResultSet rs = st.executeQuery(sql);
                                    event.getChannel().sendMessage("DUE SOON!").queue();
                                    while (rs.next()) {
                                        event.getChannel().sendMessage(rs.getString(1) + ", " +
                                                rs.getString(2) + " is due on " + rs.getString(3) + " " + rs.getString(4)).queue();
                                    }

                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }
                            }
                        }, 1 * 1 * 100, 12 * 3600000);
                    } else if (message2[1].equalsIgnoreCase("del")) {
                        if (message2.length < 3) {
                            event.getChannel().sendMessage("To delete a deadline, follow the form-> !dead del yyyy-mm-dd dueTime yourLevel").queue();
                        } else {
                            try {
                                String sql = "DELETE FROM bot.deadline WHERE dueDate = \'" + message2[2] + "\' AND dueTime = \'" + message2[3] + "\' AND levels = \'"+ message2[4]+"\'";
                                int success = st.executeUpdate(sql);
                                if (success == 1) {
                                    event.getChannel().sendMessage("Successfully deleted!").queue();
                                    conn.close();
                                }
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                        }
                    } else if (message2[1].equalsIgnoreCase("all")) {
                        try {
                            String sql =
                                    "SELECT course, content, dueDate, dueTime FROM bot.deadline WHERE levels = \'"+ message2[2] + "\' ORDER BY dueDate ASC LIMIT 10";
                            rs = st.executeQuery(sql);
                            while (rs.next()) {
                                event.getChannel().sendMessage(rs.getString(1) + ", " +
                                        rs.getString(2) + " is due on " + rs.getString(3) + " " + rs.getString(4)).queue();
                            }
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    } else {
                        event.getChannel().sendMessage("Command !dead supports 'del' and 'all'. EX) !dead all yourLevel");
                        event.getChannel().sendMessage("!dead init is not allowed");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
