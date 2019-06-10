package tw.com.lixin.wmphonebet.Tools;

import tw.com.atromoby.utils.CountDown;
import tw.com.atromoby.utils.Json;
import tw.com.lixin.wmphonebet.global.Url;
import tw.com.lixin.wmphonebet.jsonData.BacData;
import tw.com.lixin.wmphonebet.jsonData.TableData;

public class BacSocket extends CasinoSocket {

    private BacBridge bridge;


    public boolean comission = false;
    public boolean cardIsOpening = true;
    public boolean isBettingNow = true;
    public int groupID = -1;
    public int areaID;
    public CountDown countDownTimer;
    public CoinStackBack leftBack, rightBack, topBack, lowRightbBack, lowLeftBack, superBack;
    public TableData tableData;
    public int[] pokers;
    public int cardStatus = 0;
    public boolean displayCard = false;



    public BacSocket(){
        webUrl = Url.Bac;
    }

    public void bind(BacBridge bridge){
        this.bridge = bridge;
    }

    public void unbind(){
        this.bridge = null;
    }

    @Override
    public void onReceive(String text) {
        BacData bacData = Json.from(text, BacData.class);

        switch(bacData.protocol) {
            case 10:
                Game bacGame = null;
                for(Game game: lobbyData.data.gameArr){
                    if (game.gameID == 101)
                        bacGame = game;
                }
                if(bacGame == null) return;
                for(TableStage tableStage: bacGame.groupArr){
                    if ( tableStage.gameStage != 4){
                        Table table = new Table();
                        table.setUp(tableStage.historyArr);
                        table.stage = tableStage.gameStage;
                        table.groupID = tableStage.groupID;
                        table.groupType = tableStage.groupType;
                        table.score = tableStage.bankerScore;
                        table.round = tableStage.gameNoRound;
                        table.number = tableStage.gameNo;
                        App.tables.add(table);
                    }
                }
                if(bridge != null) handler.post(() -> bridge.wholeDataUpdated());
                break;
            case 20:
                User.balance(lobbyData.data.balance);
                if(bridge != null) handler.post(() -> bridge.balanceUpdated());
                break;
            case 22:
                if(bridge != null) handler.post(() -> bridge.peopleOnlineUpdate(lobbyData.data.onlinePeople));
                break;
            default:
    }
}
