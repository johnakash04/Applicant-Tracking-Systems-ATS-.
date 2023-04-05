import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.apache.poi.extractor.POITextExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class MessageListener extends ListenerAdapter{

    String help = "Welcome to your personal resume advisor bot, ResuScan!"
        + "\n--------------------------------"
        + "\nBoost your chances of getting hired with just one command:"
        + "\n!scan [DM job posting] [ATTACHMENT document.docx]"
        + "\n--------------------------------"
        + "\nReceive instant feedback on your resume's performance and stand out in today's competitive job market."
        + "\nLet's get you hired!";


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.isFromType(ChannelType.PRIVATE)) {

            //System.out.printf("[PM] %s: %s\n", event.getAuthor().getName(),event.getMessage().getContentDisplay());

            String[] command = event.getMessage().getContentRaw().split(" ");
            System.out.println("command: " + command[0]);

            //SCAN COMMAND
            if(command[0].equals("!scan") && !event.getMessage().getAttachments().isEmpty() ){
                downloadAttachment(event.getMessage());
                //analyse document alongside keywords
            }

            //HELP COMMAND
            if(command[0].equals("!help")){
                sendPrivateMessage(event.getMessage().getAuthor(),help);
            }

        }
    }

    public void downloadAttachment(Message msg){
            try{
                Message.Attachment attachment = msg.getAttachments().get(0);
                if(attachment!= null) {

                    if(attachment.getFileExtension().equals("docx")){
                        File f;
                        String fName = attachment.getFileName();
                        attachment.getProxy().downloadToFile(f = new File(fName)).thenAccept(file -> {
                            //System.out.println("Wrote attachment to file " + file.getAbsolutePath());
                        });
                        
                        sendPrivateMessage(msg.getAuthor(),"Analyzed " + attachment.getFileName() +analyzeDocument(f,msg.getContentRaw()));
                    }
                    else{
                        sendPrivateMessage(msg.getAuthor(),"Error ." + attachment.getFileExtension() + " is not supported. \nPlease provide a .docx document.");

                    }
                }
            }
            catch (IndexOutOfBoundsException e){}
    }

    public void sendPrivateMessage(User user, String content){
        try {
            user.openPrivateChannel()
                    .flatMap(channel -> channel.sendMessage(content))
                    .queue();
        }
        catch (Exception e){}

    }

    public String analyzeDocument(File f , String content){

        String[] str = content.split(" ");
        List<String> keywords = new ArrayList<String>();
        keywords = Arrays.asList(str);

        double score = 0;
        double denom = 0;
        String category = "null";
       

        try {
            InputStream fStream = new FileInputStream(f);
            XWPFDocument doc = new XWPFDocument(fStream);
            POITextExtractor extractor = new XWPFWordExtractor(doc);
            String resume = extractor.getText();
            extractor.close();

            String[] resumeSplit = resume.split(" ");
            denom = resumeSplit.length;

            
            /* JOB DESC & CV KEYWORD SCAN */
            for(int i = 0; i < keywords.size(); i++){
                if(resume.contains(keywords.get(i))){
                    score++;
                }
            }

            /* CV KEYWORD & CATEGORY SCAN */
    

        } catch (IOException e) {}

            
        return "\nResume Keyword Performance: " + new DecimalFormat("#.00%").format(score/denom)  + 
                "\nIdentified Category: " + category;
        
    }

}
