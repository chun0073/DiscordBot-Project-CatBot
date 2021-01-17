package commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.*;

public class Quiz extends ListenerAdapter implements DbAdapter{

    String question = "";
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        Connection conn;
        Statement st;
        ResultSet rs;

        String[] message = event.getMessage().getContentRaw().split(" ");
        String[] quizSet = event.getMessage().getContentRaw().split(":");
        String[] answer = event.getMessage().getContentRaw().split("@");

        try{
            conn = DriverManager.getConnection(url, username, password);
            st = conn.createStatement();

            if(message[0].equalsIgnoreCase("!quiz")){
                if(message.length>1) {
                    if(message[1].equalsIgnoreCase("help")){
                        event.getChannel().sendMessage("!quiz CourseName will return a random quiz of the course").queue();
                        event.getChannel().sendMessage("!quiz course will show available course names").queue();
                        event.getChannel().sendMessage("How to answer? !@[your answer], " +"Check your point by !quiz point, "+"Check top 10 by !quiz rank").queue();
                        event.getChannel().sendMessage("How to store your quiz? !quizset:[course]:[question]:[answer]").queue();
                    }
                    else if(message[1].equalsIgnoreCase("course")){
                        try{
                            String sql = "SELECT DISTINCT course FROM bot.quiz";
                            rs = st.executeQuery(sql);
                            String res = "";
                            while(rs.next()){
                                res = rs.getString(1)+"  "+res;
                            }
                            event.getChannel().sendMessage("Available course names: "+res).queue();
                        }
                        catch (SQLException throwables){
                            throwables.printStackTrace();
                        }
                    }
                    else if(message[1].equalsIgnoreCase("rank")){
                        try {
                            String sql = "SELECT userName, quizPoint FROM bot.quizuser ORDER BY quizPoint LIMIT 10";
                            rs = st.executeQuery(sql);

                            while(rs.next()) {
                                event.getChannel().sendMessage(rs.getString(1)+" : "+rs.getString(2)).queue();
                            }
                        }
                        catch (SQLException throwables){
                            throwables.printStackTrace();
                        }
                    }
                    else if(message[1].equalsIgnoreCase("point")){
                        try {
                            String sql = "SELECT userName, quizPoint FROM bot.quizuser WHERE userName = \'"+event.getAuthor().getName()+"\'";
                            rs = st.executeQuery(sql);

                            while(rs.next()) {
                                event.getChannel().sendMessage(rs.getString(1)+" : "+rs.getString(2)).queue();
                            }
                        }
                        catch (SQLException throwables){
                            throwables.printStackTrace();
                        }
                    }
                    else {
                        try {
                            String sql = "SELECT id, content FROM bot.quiz WHERE course LIKE \'%"+message[1]+"%\' ORDER BY RAND() LIMIT 1";
                            rs = st.executeQuery(sql);

                            while (rs.next()) {
                                question = rs.getString(1);
                                event.getChannel().sendMessage(rs.getString(2)).queue();
                            }
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                }
                else{
                    try {
                        String sql = "SELECT id, content FROM bot.quiz ORDER BY RAND() LIMIT 1";
                        rs = st.executeQuery(sql);

                        while(rs.next()) {
                            question = rs.getString(1);
                            event.getChannel().sendMessage(rs.getString(2)).queue();
                        }
                    }
                    catch (SQLException throwables){
                        throwables.printStackTrace();
                    }
                }
            }
            if(answer[0].equalsIgnoreCase("!")){
                if(answer.length<2){
                    event.getChannel().sendMessage("What is the answer? ... !$[Answer]").queue();
                }
                else {
                    String ans = answer[1];
                    try {
                        String sql = "SELECT Answer FROM bot.quiz WHERE id ="+question;
                        rs = st.executeQuery(sql);
                        String result = "";
                        while(rs.next()) {
                            result = rs.getString(1);
                        }
                        if(ans.equalsIgnoreCase(result)){
                            event.getChannel().sendMessage("Great! "+ans+ " is correct").queue();
                            CallableStatement cstmt;
                            cstmt = conn.prepareCall("Call bot.QuizPointUpdate(?,?,?);");
                            cstmt.setString(1,event.getAuthor().getName());
                            cstmt.setString(2,event.getAuthor().getId());
                            cstmt.registerOutParameter(3,Types.INTEGER);
                            cstmt.executeUpdate();
                            cstmt.close();
                            conn.close();
                            question = "";
                        }
                        else{
                            event.getChannel().sendMessage("Sorry but the answer was "+result).queue();
                            question = "";
                        }
                    }
                    catch (SQLException throwables){
                        throwables.printStackTrace();
                    }
                }
            }
            if(quizSet[0].equalsIgnoreCase("!quizset")){
                if(message.length<2){
                    event.getChannel().sendMessage("To add a new quiz, follow the form-> !quizset:[course]:[question]:[answer]").queue();
                }
                else{
                    try {
                        String sql = "INSERT INTO bot.quiz(course, content, Answer, Author)" +
                                "VALUES (\'"+
                                quizSet[1]+"\',\'"+quizSet[2]+"\',\'"+quizSet[3]+"\',\'"+event.getAuthor().getName()
                                +"\')";
                        int success = st.executeUpdate(sql);
                        if(success == 1){
                            event.getChannel().sendMessage(quizSet[2]+", "+quizSet[3]+" is successfully stored!").queue();
                            conn.close();
                        }
                    }
                    catch (SQLException throwables){
                        throwables.printStackTrace();
                    }
                }
            }
            if(quizSet[0].equalsIgnoreCase("!quizdel")){
                if(quizSet.length<2){
                    event.getChannel().sendMessage("!quizdel:[username]").queue();
                }
                else{
                    try {
                        String sql = "DELETE FROM bot.quiz WHERE Author = \'" + quizSet[1]+"\'";
                        int success = st.executeUpdate(sql);
                        if(success == 1){
                            event.getChannel().sendMessage("All quizzes from "+quizSet[1]+" successfully deleted!").queue();
                            conn.close();
                        }
                    }
                    catch (SQLException throwables){
                        throwables.printStackTrace();
                    }
                }
            }
            if(quizSet[0].equalsIgnoreCase("!checkAuthor")){
                if(quizSet.length<2){
                    event.getChannel().sendMessage("!checkAuthor:[username]").queue();
                }
                else{
                    try {
                        String sql = "SELECT COUNT(*) FROM bot.quiz WHERE Author = \'" + quizSet[1]+"\'";
                        rs = st.executeQuery(sql);
                        while (rs.next()){
                            event.getChannel().sendMessage(quizSet[1]+" made "+rs.getString(1) +" quizzes").queue();
                        }
                    }
                    catch (SQLException throwables){
                        throwables.printStackTrace();
                    }
                }
            }
        }
        catch(SQLException throwables){
            throwables.printStackTrace();
            event.getChannel().sendMessage("I am sorry but my database is under the maintenance.");
        }
    }
}
