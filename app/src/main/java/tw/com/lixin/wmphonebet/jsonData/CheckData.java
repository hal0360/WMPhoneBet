package tw.com.lixin.wmphonebet.jsonData;

public class CheckData {
    public Data data;
    public int protocol = 1;

    public CheckData(String sid) {
        data = new Data();
        data.sid = sid;
    }

    private class Data{
        String sid = "";
    }
}
