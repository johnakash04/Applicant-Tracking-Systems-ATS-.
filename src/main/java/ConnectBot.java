import lombok.Getter;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Activity;


import javax.security.auth.login.LoginException;


public class ConnectBot{

    @Getter
    private static JDA jda;
    private static String TOKEN;

    public ConnectBot() {
        TOKEN = "MTA5Mjk5OTAwMDkzNzk4ODEyNg.G24ijz.tGRrbTX1D-wDEThDHhhhE2l9q74gs_MWK5Oyvk";
        start();
    }


    private void start() {
        try {
            jda = JDABuilder.createDefault(TOKEN)
                    .setActivity(Activity.watching("Direct Messages!"))
                    .addEventListeners(new BotListener())
                    .addEventListeners(new MessageListener())
                    
                    .build();



            jda.awaitReady();
            System.out.println("Bot has successfully built");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

  


}