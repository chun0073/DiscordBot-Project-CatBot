import commands.Deadline;
import commands.Quiz;
import commands.mafia.Mafia;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import javax.security.auth.login.LoginException;

public class Main extends ListenerAdapter {
    static String token = "NzYwMTE1MDkyODQyMzQ4NTY0.X3HWWA.HKMmxhc1PbbGoErR95tzOrj6ZDo";
    static Mafia mafia;
    static int count = 0;

    public static void main(String[] args) {
        try {
            JDABuilder.createLight(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
                    .addEventListeners(new Main()).addEventListeners(new Quiz()).addEventListeners(new Deadline()).setActivity(Activity.listening("!help")).build();

        } catch (LoginException e) {
            e.printStackTrace();

        }
    }
    // onMessageReceived for main.
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        MessageChannel channel = event.getChannel();
        long time = System.currentTimeMillis();

        if (msg.getContentRaw().equalsIgnoreCase("!ping")) {

            channel.sendMessage("pong!").queue(response -> {
                response.editMessageFormat("Pong: %d ms", System.currentTimeMillis() - time).queue();
            });
        }
        if(msg.getContentRaw().equalsIgnoreCase("!help")){
            channel.sendMessage("This bot provides quiz and deadline functions, check by !quiz help or !commands").queue();
        }
        if(msg.getContentRaw().equalsIgnoreCase("!info")){
            channel.sendMessage("This bot has been developed by PiggyCat and Jin. The bot is designated to encourage students to study courses with multiple functions").queue();
        }
        if(msg.getContentRaw().equalsIgnoreCase("!hire")){
            channel.sendMessage("Hire the bot to your channel: "+"https://discord.com/api/oauth2/authorize?client_id=760115092842348564&permissions=269585488&scope=bot").queue();
        }
        if(msg.getContentRaw().equalsIgnoreCase("!commands")){
            channel.sendMessage("!ping, !help, !info, !hire, !quiz, !checkAuthor, !@, !quizset, !quizdel, !add, !dead init, !dead del, !dead all").queue();
        }
    }
}

