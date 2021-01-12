package commands.mafia;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MafiaOperator extends ListenerAdapter {
    Mafia mafia;
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();

        if (msg.getContentRaw().equals("!ping")) {
            MessageChannel channel = event.getChannel();
            long time = System.currentTimeMillis();
            channel.sendMessage("pong!").queue(response -> {
                response.editMessageFormat("Pong: %d ms", System.currentTimeMillis() - time).queue();
            });
        }

        if (msg.getContentRaw().equals("!mafia")) {
            mafia = new Mafia();
            event.getChannel().sendMessage("commands.mafia.Mafia game begins by " + event.getAuthor().getName()).queue();
        }

        if (msg.getContentRaw().equals("!m.join")) {
            if (mafia.getClass() != null) {
                event.getChannel().sendMessage("Welcome, " + event.getAuthor().getName() + "! " + mafia.join(event.getAuthor().getName(), event.getChannel().getId().toString(), event.getAuthor().getId().toString())).queue();
            } else {
                event.getChannel().sendMessage("!mafia should be called before this.").queue();
            }
        }
        if (msg.getContentRaw().equals("!mt.players")){
            if (mafia.getClass() != null) {
                event.getChannel().sendMessage("There are " + mafia.getPlayers().size() + " players, " + mafia.getPlayers().toString()).queue();
            }
            else{
                event.getChannel().sendMessage("!mafia should be called before this.").queue();
            }
        }
    }
}
/*
        api.addMessageCreateListener(event -> {

            if (event.getMessageContent().equalsIgnoreCase("!m.start")) {
                if (mafia.getPlayers().size() > 5) {
                    event.getChannel().sendMessage("commands.mafia.Mafia game will be started soon!");
                    CompletableFuture<User> doctor = api.getUserById(mafia.getPlayer("commands.mafia.Doctor").getId());
                    MessageBuilder messageDoctor = new MessageBuilder();
                    messageDoctor.append(mafia.getPlayer("commands.mafia.Doctor").getDescription());
                    CompletableFuture<User> commands.mafia.Programmer = api.getUserById(mafia.getPlayer("commands.mafia.Programmer").getId());
                    MessageBuilder messageProgrammer = new MessageBuilder();
                    messageProgrammer.append(mafia.getPlayer("commands.mafia.Programmer").getDescription());
                    try {
                        messageDoctor.send(doctor.get());
                        messageProgrammer.send(commands.mafia.Programmer.get());
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Timer timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            count++;
                            event.getChannel().sendMessage("The sun rises. "+"Here are "+mafia.getPlayers().size()+"players. Find mafia to save your life");

                        }
                    }, 1 * 1 * 1000, 200 * 1000 / 2);
                    Timer timer1 = new Timer();
                    timer1.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            event.getChannel().sendMessage("The night has come, commands.mafia.Mafia can now do something");

                        }
                    },180*1000 / 2,180*1000 / 2);
                } else {
                    event.getChannel().sendMessage((6 - mafia.getPlayers().size()) + " more players needed");
                }
            }
            if (event.getMessageContent().equalsIgnoreCase("!kill.")){
                if(count > 0){
                    // kill method should be placed here.
                }
                else{

                }
            }
        });

        */