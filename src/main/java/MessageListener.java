import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.poi.extractor.POITextExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class MessageListener extends ListenerAdapter{

    String help = "**Welcome to your personal resume advisor bot, ResuScan!**"
        + "\n--------------------------------"
        + "\n**Boost your chances of getting hired with just one __command__**:"
        + "\n`!scan [ATTACHMENT document.docx]`"
        + "\n--------------------------------"
        + "\n**Result Analysis** :satellite_orbital:"
        + "\n--------------------------------"
        + "\n**Action Performance**: Measure the candidates performance by targetting action verbs. \n\t\t\tGreat: 70% +\n\t\t\tOk: 50% + \n\t\t\tBad: Less "
        + "\n--------------------------------"
        + "\n**Quantitive Performance**: Measure the candidates performance by targetting their quantitive acheivments. \n\t\t\tGreat: 70% + \n\t\t\tOk: 50% +  \n\t\t\tBad: Less"
        + "\n--------------------------------"
        + "\n**Identified Category**: Scanner determined category: Scan a variety of feild keywords to identify the cv's category. Wrong categories likely lack proper vocabulary"
        + "\n--------------------------------"
        + "\n**Styling**: Measure the resumes styling by comparing the scanner word count and the accurate result. Best formats have high accuracy."    
        + "\n--------------------------------"  
        + "\nReceive instant feedback on your resume's performance and stand out in today's competitive job market."
        + "\nLet's get you hired!";

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        try {
            if (event.isFromType(ChannelType.PRIVATE)) {

                String[] command = event.getMessage().getContentRaw().split(" ");
            
                //SCAN COMMAND
                if(command[0].equals("!scan") && !event.getMessage().getAttachments().isEmpty() ){
                    downloadAttachment(event.getMessage());
                }

                //HELP COMMAND
                if(command[0].equals("!help")){
                    sendPrivateMessage(event.getMessage().getAuthor(),help);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }        
    }

    public void downloadAttachment(Message msg) throws Exception{
        Message.Attachment attachment = msg.getAttachments().get(0);
            if(attachment!= null) {
                if(attachment.getFileExtension().equals("docx")){
                    File f;
                    String fName = attachment.getFileName();
                    attachment.getProxy().downloadToFile(f = new File(fName)).thenAccept(file -> {
                    }); 
                    sendPrivateMessage(msg.getAuthor(),"Analyzed " + attachment.getFileName() +analyzeResume(f));
                }
                else{
                    sendPrivateMessage(msg.getAuthor(),"Error ." + attachment.getFileExtension() + " is not supported. \nPlease provide a .docx document.");
                }
            }

    }

    public void sendPrivateMessage(User user, String content) throws Exception{
        user.openPrivateChannel()
                .flatMap(channel -> channel.sendMessage(content))
                .queue();
    }

    public String analyzeResume(File f) throws Exception{

        double actionVerbPerf = 0;
        double quantativePerf = 0;
        int wordCount =0;

        /** LOADING  ACTION VERBS */
        BufferedReader br = new BufferedReader(new FileReader("src/main/resources/actionVerbs.txt"));
        String actionLine = br.readLine().toLowerCase();

        List<String> actionVerbs = new ArrayList<>();
        String[] tmp = actionLine.split(",");
        for(String s : tmp){
            actionVerbs.add(s);
        }
        
        /** LOADING RESUME */
        InputStream fStream = new FileInputStream(f);
        XWPFDocument doc = new XWPFDocument(fStream);
        POITextExtractor extractor = new XWPFWordExtractor(doc);

        String r = extractor.getText().toLowerCase();
        r = r.replaceAll("\\\t+", " ");
        r = r.replaceAll(",", "");
        
        String[] rSplit = r.split(" ");
        List<String> resume = Arrays.asList(rSplit);
        wordCount = resume.size();
        
        /* CLOSING STREAMS */            
        extractor.close();
        br.close();

        /* Action Verb Performance */
        actionVerbs.retainAll(resume);  
        actionVerbPerf = actionVerbs.size();
        
        /* QUANTATIVE PERF */
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        
        for(String str : resume){
            if( pattern.matcher(str).matches()){
                quantativePerf++;
            }
        }
        
        return "\n-----" +
               "\n**Action Performance**: " + new DecimalFormat("#.0%").format(actionVerbPerf/7)  + 
               "\n**Quantative Performance**: " + new DecimalFormat("#.0%").format(quantativePerf/7)  +
               "\n**Identified Category: " + "NaN" +
               "\n**Word Count**: " + wordCount;
    }

}
