package tw.com.lixin.wmphonebet.jsonData;

public class LoginResData {
    public int protocol;
    public Data data;

    public class Data {
        public Boolean bOk;
        public int gameID;
        public String account;
        public int memberID;
        public String userName;
        public String sid;
    }

}
