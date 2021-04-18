package client;

/**
 * Created by Niloofar on 4/25/2020.
 */
public class Message {
    String sender;
    String text;

    public Message(String input) {
        String tokens[] = input.trim().split(" ");
        sender = tokens[0];
        text = "";
        for(int  i = 1 ; i < tokens.length ; i++){
            text += tokens[i];
            text += " ";
        }
        text.trim();
    }

    public String getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }

    public void show(){
        System.out.println("from " + sender + ":");
        System.out.println(text + '\n');
    }
}
