package tw.com.lixin.wmphonebet.jsonData;

import java.util.List;

import tw.com.lixin.wmphonebet.jsonData.data.Game;

public class LobbyData {
    public int protocol;
    public Data data;

    public class Data {
        public List<Game> gameArr;
        public float balance;
        public int gameID;
        public int onlinePeople;

    }
}
