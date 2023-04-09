import lombok.Getter;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Activity;


public class ConnectBot{

    @Getter
    private static JDA jda;
    private static String TOKEN;

    public ConnectBot() {
        TOKEN = "ENTER TOKEN";
        start();
    }


    private void start() {
        try {
            jda = JDABuilder.createDefault(TOKEN)
                    .setActivity(Activity.watching("Direct Messages!"))
                    .addEventListeners(new MessageListener())
                    .build();

            jda.awaitReady();
            System.out.println("Bot has successfully built");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

 
}