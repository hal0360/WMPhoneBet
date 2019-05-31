package tw.com.lixin.wmphonebet.jsonData;

import java.util.List;

import tw.com.lixin.wmphonebet.jsonData.data.Game;

public class LobbyData {
    public int protocol;
    public Data data;

    public class Data {
        public Boolean bOk;
        public int gameID;
        public String account;
        public int memberID;
        public String userName;
        public String sid;

        public List<Game> gameArr;
        public float balance;

    }
}
