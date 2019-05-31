package tw.com.lixin.wmphonebet.jsonData;

public class Server24 {
    public Data data;
    // public int protocol;

    public interface CmdData {
        void exec(Data data);
    }
public class Data{
    public int gameID;
    public int groupID;
    public int areaID;
    public int areaType;
    public int cardArea;
    public int cardID;
}
}