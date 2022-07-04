package package1.Processors;

public class Processor{
    private Message stopMessage;
    private int wLen;

    public static Message process(Message message) {
        try {
            System.out.println("mess from proc : " + message);
            message.doAction();
            Goods OK = new Goods(1,1,1, "OK");
            return new Message(message.getcType(), message.getbUserId(), OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
