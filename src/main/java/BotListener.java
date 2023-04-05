import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;

public class BotListener implements EventListener {
    @Override
    public void onEvent(GenericEvent e){
        /** READY **/
        if(e instanceof ReadyEvent){
            System.out.println("Bot is ready to listen");
        }

    }

}
