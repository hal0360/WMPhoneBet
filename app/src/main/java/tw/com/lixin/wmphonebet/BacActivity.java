package tw.com.lixin.wmphonebet;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import tw.com.atromoby.rtmplayer.IjkVideoView;
import tw.com.atromoby.utils.Json;
import tw.com.atromoby.widgets.ItemHolder;
import tw.com.atromoby.widgets.ItemsView;
import tw.com.atromoby.widgets.Popup;
import tw.com.atromoby.widgets.RootActivity;
import tw.com.lixin.wmphonebet.Tools.CasinoGrid;
import tw.com.lixin.wmphonebet.Tools.CoinStack;
import tw.com.lixin.wmphonebet.Tools.GoldenButton;
import tw.com.lixin.wmphonebet.Tools.Move;
import tw.com.lixin.wmphonebet.Tools.PayPopup;
import tw.com.lixin.wmphonebet.Tools.SettingPopup;
import tw.com.lixin.wmphonebet.Tools.TableSwitchPopup;
import tw.com.lixin.wmphonebet.Tools.WinLossPopup;
import tw.com.lixin.wmphonebet.global.User;
import tw.com.lixin.wmphonebet.interfaces.BacBridge;
import tw.com.lixin.wmphonebet.jsonData.BacData;
import tw.com.lixin.wmphonebet.jsonData.Client22;
import tw.com.lixin.wmphonebet.models.CoinHolder;
import tw.com.lixin.wmphonebet.models.CostomCoinHolder;
import tw.com.lixin.wmphonebet.websocketSource.BacSource;

public class BacActivity extends WMActivity implements BacBridge {
    private int posX, posY;
    private Animation fadeAnimeB;
    private Move move;
    public ItemsView coinsView;
    private TextView gameStageTxt, pokerBall, playerScreenScore, bankerScreenScore;
    public CoinHolder curCoin;
    private CasinoGrid mainGrid, firstGrid, secGrid, thirdGrid, fourthGrid;
    private View logo;

    private SparseArray<ImageView> pokers = new SparseArray<>();
    //private ImageView[] pokers = new ImageView[6];

    private ConstraintLayout videoContaner, pokerContainer, countdownBox, tableBetContainer, root, tableRight, tableSuper, tableTop, tableLeft;
    private CoinStack stackLeft, stackRight, stackTop, stackBTL, stackBTR, stackSuper;
    private ImageView bankSecondSym, bankThirdSym, bankFourthSym, playerSecondSym, playerThirdSym, playerFourthSym;
    private GoldenButton repeatBtn, cancelBtn, comissionBtn;
    public GoldenButton confirmBtn;
    private boolean viewIsZoomed = false;
    private String bankTableScore;

    private View mainV, firstV, secV, thirdV, fourthV;

    private IjkVideoView video;

    private WinLossPopup popup;
    private BacSource source;

    public void viewZoomOut(View view) {
        if(source.status != 1) return;
        if (viewIsZoomed) {
            move.back(300);
            viewIsZoomed = false;
            logo.bringToFront();

        } else {
            if(view.getId() ==  R.id.first_grid || view.getId() ==  R.id.main_grid || view.getId() ==  R.id.videoContaner ){
                move.toCenter(view,1.5f,300);
            }else{
                move.toCenter(view,1.8f,300);
            }
            viewIsZoomed = true;
        }
    }

    @SuppressLint("FindViewByIdCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bac);
        source = BacSource.getInstance();
        fadeAnimeB = AnimationUtils.loadAnimation(this, R.anim.prediction_fade);
        source.bind(this);

        String path = "rtmp://wmvdo.nicejj.cn/live" + source.groupID + "/stream1";
        video = findViewById(R.id.player);
        video.setVideoPath(path);
        video.start();
        popup = new WinLossPopup(this);

        root = findViewById(R.id.root);
        confirmBtn = findViewById(R.id.confirm_bet_btn);
        cancelBtn = findViewById(R.id.cancel_bet_btn);
        repeatBtn = findViewById(R.id.repeat_bet_btn);
        comissionBtn = findViewById(R.id.comission_btn);
        tableBetContainer = findViewById(R.id.table_bet_container);
        setTextView(R.id.table_num, source.groupID + "");
        bankFourthSym = findViewById(R.id.bank_fourth_sym);
        bankThirdSym = findViewById(R.id.bank_third_sym);
        bankSecondSym = findViewById(R.id.bank_second_sym);
        playerSecondSym = findViewById(R.id.player_second_sym);
        playerThirdSym = findViewById(R.id.player_third_sym);
        playerFourthSym = findViewById(R.id.player_fourth_sym);
        tableRight = findViewById(R.id.table_right);
        tableSuper = findViewById(R.id.table_super);
        tableTop = findViewById(R.id.table_top);
        tableLeft = findViewById(R.id.table_left);

        countdownBox = findViewById(R.id.countdown);
        playerScreenScore = findViewById(R.id.player_screen_score);
        bankerScreenScore = findViewById(R.id.banker_screen_score);
        gameStageTxt = findViewById(R.id.stage_info_txt);
        coinsView = findViewById(R.id.coinsView);
        addAllCoins();
        logo = findViewById(R.id.lobby_logo);
        videoContaner = findViewById(R.id.videoContaner);
        mainGrid = findViewById(R.id.main_grid);
        firstGrid = findViewById(R.id.first_grid);
        secGrid = findViewById(R.id.second_grid);
        thirdGrid = findViewById(R.id.third_grid);
        fourthGrid = findViewById(R.id.fourth_grid);
        stackBTL = findViewById(R.id.table_bt_l_stack);
        stackBTR = findViewById(R.id.table_bt_r_stack);
        stackTop = findViewById(R.id.table_top_stack);
        stackLeft = findViewById(R.id.table_left_stack);
        stackRight = findViewById(R.id.table_right_stack);
        stackSuper = findViewById(R.id.table_bt_super_stack);
        pokerContainer = findViewById(R.id.poker_layout);

        pokers.put(1,findViewById(R.id.player_poker1));
        pokers.put(3,findViewById(R.id.player_poker2));
        pokers.put(5,findViewById(R.id.player_poker3));
        pokers.put(2,findViewById(R.id.banker_poker1));
        pokers.put(4,findViewById(R.id.banker_poker2));
        pokers.put(6,findViewById(R.id.banker_poker3));
        pokerBall = findViewById(R.id.poker_ball);
        resetPokers();

        setTextView(R.id.gyu_shu, getString(R.string.table_number) + " " + source.table.number + " -- " + source.table.round);
        setTextView(R.id.banker_count, source.table.bankCount + "");
        setTextView(R.id.player_count, source.table.playCount + "");
        setTextView(R.id.tie_count, source.table.tieCount + "");
        setTextView(R.id.bank_pair_count, source.table.bankPairCount + "");
        setTextView(R.id.play_pair_count, source.table.playPairCount + "");

        treeObserve(root, v -> move = new Move(this, root));
        treeObserve(coinsView, v->{
            int coinWidth = coinsView.getHeight();
            int coinListWidth = coinsView.getWidth();
            coinsView.getLayoutParams().width = coinListWidth - (coinListWidth % coinWidth);
        });
        coinsView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    final int curPos = coinsView.findScroll();
                    delay(200, () -> coinsView.scrollTo(curPos));
                }
            }
        });

        if(!isPortrait()) clicked(R.id.cash_btn,v-> new PayPopup(this).show());

        stackLeft.setUp(source.stackLeft);
        stackRight.setUp(source.stackRight);
        stackTop.setUp(source.stackTop);
        stackBTL.setUp(source.stackBTL);
        stackBTR.setUp(source.stackBTR);
        stackSuper.setUp(source.stackSuper);
        statusUpdate();

        clicked(R.id.table_left, v -> {
            stackLeft.add(curCoin);
            checkStackEmpty();
        });
        clicked(R.id.table_right, v -> {
            stackRight.add(curCoin);
            checkStackEmpty();
        });
        clicked(R.id.table_top, v -> {
            stackTop.add(curCoin);
            checkStackEmpty();
        });
        clicked(R.id.table_bt_l, v -> {
            stackBTL.add(curCoin);
            checkStackEmpty();
        });
        clicked(R.id.table_bt_r, v -> {
            stackBTR.add(curCoin);
            checkStackEmpty();
        });
        clicked(tableSuper, v -> {
            if (source.comission) {
                stackSuper.add(curCoin);
                checkStackEmpty();
            }
        });

        clicked(R.id.fullscreen_btn, v ->{
            videoContaner.removeView(video);
            delay(3000,()-> videoContaner.addView(video));
        });
        clicked(R.id.scroll_left_btn, v -> coinsView.smoothScrollToPosition(0));
        clicked(R.id.scroll_right_btn, v -> coinsView.smoothScrollToPosition(18));
        clicked(R.id.back_btn, v -> onBackPressed());

        comissionBtn.clicked(v -> {
            if (source.comission) {
                source.comission = false;
                tableBetContainer.setBackgroundResource(R.drawable.table_bt);
                comissionBtn.setImageResource(R.drawable.casino_item_btn_super6);
                tableRight.getLayoutParams().height = tableLeft.getHeight();
                tableSuper.setVisibility(View.GONE);
                stackSuper.cancelBet();
                setTextView(R.id.table_right_score, bankTableScore);
            } else {
                source.comission = true;
                tableBetContainer.setBackgroundResource(R.drawable.table_bt_super6);
                tableRight.getLayoutParams().height = tableTop.getHeight();
                comissionBtn.setImageResource(R.drawable.casino_item_btn_super6_a);
                tableSuper.setVisibility(View.VISIBLE);
                setTextView(R.id.table_right_score, "1(0.5)");
            }
            checkStackEmpty();
        });

     //   if(!isPortrait()) clicked(R.id.switch_table_btn, v -> new TableSwitchPopup(this).show());

        confirmBtn.clicked(v -> {

            Client22 client22 = new Client22();
            if (source.comission) {
                client22.data.commission = 1;
                source.stackSuper.addCoinToClient(client22, 8);
            }
            source.stackBTR.addCoinToClient(client22, 4);
            source.stackTop.addCoinToClient(client22, 3);
            source.stackRight.addCoinToClient(client22, 1);
            source.stackBTL.addCoinToClient(client22, 5);
            source.stackLeft.addCoinToClient(client22, 2);

            if (client22.data.betArr.size() > 0) {
                //alert(Json.to(client22));
                source.send(Json.to(client22));
            }
            else alert("You haven't put any money!");
        });

        cancelBtn.clicked(v -> {
            stackSuper.cancelBet();
            stackBTR.cancelBet();
            stackTop.cancelBet();
            stackRight.cancelBet();
            stackBTL.cancelBet();
            stackLeft.cancelBet();
            checkStackEmpty();
        });

        repeatBtn.clicked(v -> {
            stackSuper.repeatBet();
            stackBTR.repeatBet();
            stackTop.repeatBet();
            stackRight.repeatBet();
            stackBTL.repeatBet();
            stackLeft.repeatBet();
        });

        clicked(R.id.bankBtn, v -> {
            askRoad(1);
            bankSecondSym.setImageResource(source.table.secGrid.resX);
            bankThirdSym.setImageResource(source.table.thirdGrid.resX);
            bankFourthSym.setImageResource(source.table.fourthGrid.resX);
        });

        clicked(R.id.playBtn, v -> {
            askRoad(2);
            playerSecondSym.setImageResource(source.table.secGrid.resX);
            playerThirdSym.setImageResource(source.table.thirdGrid.resX);
            playerFourthSym.setImageResource(source.table.fourthGrid.resX);
        });

        clicked(R.id.setting_btn, v -> new SettingPopup(this).show());

        setTextView(R.id.table_left_score, source.tableLeftScore);
        setTextView(R.id.table_right_score, source.tableRightScore);
        setTextView(R.id.table_bt_l_score, source.tableBtlScore);
        setTextView(R.id.table_bt_r_score, source.tableBtrScore);
        setTextView(R.id.table_top_score, source.tableTopScore);
        bankTableScore = source.tableRightScore;
        setTextView(R.id.table_bt_super_score, "12");
        setTextView(R.id.gyu_shu2, "1 - " + source.maxBetVal);
        setTextView(R.id.player_money, User.balance() + "");
        comissionBtn.disable(false);

        treeObserve(fourthGrid, v->{
            double dim = mainGrid.getHeight() / 6;
            mainGrid.getLayoutParams().width = (int) Math.round(dim * 14);
            mainGrid.setGrid(14, 6);
            double width = thirdGrid.getWidth();
            double dim2 = thirdGrid.getHeight() / 3;
            int wGrid = (int) Math.round(width / dim2);
            double width2 = firstGrid.getWidth();
            double dim3 = firstGrid.getHeight() / 6;
            int wGrid2 = (int) Math.round(width2 / dim3);
            firstGrid.setGrid(wGrid2, 6);
            secGrid.setGridDouble(wGrid * 2, 3);
            thirdGrid.setGridDouble(wGrid, 3);
            fourthGrid.setGridDouble(wGrid, 3);
            setMainGrid();
        });
    }

    private void checkStackEmpty() {
        if (source.stackLeft.isEmpty() && source.stackBTL.isEmpty() && source.stackRight.isEmpty() && source.stackTop.isEmpty() && source.stackSuper.isEmpty() && source.stackBTR.isEmpty()) {
            cancelBtn.disable(true);
            repeatBtn.disable(true);
        } else {
            cancelBtn.disable(false);
            repeatBtn.disable(false);
        }
    }

    private void clearAskViews(){
        if(mainV == null) return;
        mainV.clearAnimation();
        mainV.setBackgroundResource(0);
        firstV.clearAnimation();
        firstV.setBackgroundResource(0);
        secV.clearAnimation();
        secV.setBackgroundResource(0);
        thirdV.clearAnimation();
        thirdV.setBackgroundResource(0);
        fourthV.clearAnimation();
        fourthV.setBackgroundResource(0);
    }

    private void askRoad(int win) {
        clearAskViews();
        source.table.askRoadThird(win);
        source.table.askRoadSec(win);
        source.table.askRoadFirst(win);
        source.table.askRoadFourth(win);
        if (firstGrid.width > source.table.firstGrid.posXX) {
            firstV = firstGrid.insertImage(source.table.firstGrid.posXX, source.table.firstGrid.posYY, source.table.firstGrid.resX);
            firstV.startAnimation(fadeAnimeB);
        }
        if (secGrid.width > source.table.secGrid.posXX) {
            secV = secGrid.insertImage(source.table.secGrid.posXX, source.table.secGrid.posYY, source.table.secGrid.resX);
            secV.startAnimation(fadeAnimeB);
        }
        if (thirdGrid.width > source.table.thirdGrid.posXX) {
            thirdV = thirdGrid.insertImage(source.table.thirdGrid.posXX, source.table.thirdGrid.posYY, source.table.thirdGrid.resX);
            thirdV.startAnimation(fadeAnimeB);
        }
        if (fourthGrid.width > source.table.fourthGrid.posXX) {
            fourthV = fourthGrid.insertImage(source.table.fourthGrid.posXX, source.table.fourthGrid.posYY, source.table.fourthGrid.resX);
            fourthV.startAnimation(fadeAnimeB);
        }
        if(win == 1){
            if(posY < 5){
                mainV = mainGrid.insertImage(posX, posY + 1, R.drawable.casino_roadbank);
                mainV.startAnimation(fadeAnimeB);
            }else{
                mainV =  mainGrid.insertImage(posX+1, 0, R.drawable.casino_roadbank);
                mainV.startAnimation(fadeAnimeB);
            }
        }else{
            if(posY < 5){
                mainV =  mainGrid.insertImage(posX, posY + 1, R.drawable.casino_roadplay);
                mainV.startAnimation(fadeAnimeB);
            }else{
                mainV =   mainGrid.insertImage(posX+1, 0, R.drawable.casino_roadplay);
                mainV.startAnimation(fadeAnimeB);
            }
        }
    }



    private void setMainGrid() {
        int indexx = 0;
        firstGrid.drawRoad(source.table.firstGrid);
        secGrid.drawRoad(source.table.secGrid);
        thirdGrid.drawRoad(source.table.thirdGrid);
        fourthGrid.drawRoad(source.table.fourthGrid);
        for (int x = 0; x < mainGrid.width; x++) {
            for (int y = 0; y < mainGrid.height; y++) {
                if (indexx >= source.table.mainRoad.size()) return;
                mainGrid.insertImage(x, y, source.table.mainRoad.get(indexx));
                indexx++;
                posX = x;
                posY = y;
            }
        }
    }

    private void resetCoinStacks() {
        stackTop.clearCoin();
        stackBTR.clearCoin();
        stackRight.clearCoin();
        stackBTL.clearCoin();
        stackLeft.clearCoin();
        stackSuper.clearCoin();
        checkStackEmpty();
    }

    public void resetPokers() {
        if(source.status == 2){pokerContainer.setVisibility(View.VISIBLE);
        }else{ pokerContainer.setVisibility(View.INVISIBLE); }
        for(int y = 0; y < pokers.size(); y++) pokers.valueAt(y).setVisibility(View.INVISIBLE);
        for(int i = 0; i < source.pokers.size(); i++) {
            int key = source.pokers.keyAt(i);
            cardUpdate(key, source.pokers.get(key));
        }
    }

    private void addAllCoins() {
        List<ItemHolder> coins = new ArrayList<>();
        coins.add(new CoinHolder(R.drawable.casino_item_chip_1, 1));
        coins.add(new CoinHolder(R.drawable.casino_item_chip_5, 5));
        curCoin = new CoinHolder(R.drawable.casino_item_chip_10, 10);
        curCoin.selected = true;
        coins.add(curCoin);
        coins.add(new CoinHolder(R.drawable.casino_item_chip_20, 20));
        coins.add(new CoinHolder(R.drawable.casino_item_chip_50, 50));
        coins.add(new CoinHolder(R.drawable.casino_item_chip_100, 100));
        coins.add(new CoinHolder(R.drawable.casino_item_chip_500, 500));
        coins.add(new CoinHolder(R.drawable.casino_item_chip_1k, 1000));
        coins.add(new CoinHolder(R.drawable.casino_item_chip_5k, 5000));
        coins.add(new CoinHolder(R.drawable.casino_item_chip_10k, 10000));
        coins.add(new CoinHolder(R.drawable.casino_item_chip_20k, 20000));
        coins.add(new CoinHolder(R.drawable.casino_item_chip_50k, 50000));
        coins.add(new CoinHolder(R.drawable.casino_item_chip_100k, 100000));
        coins.add(new CoinHolder(R.drawable.casino_item_chip_200k, 200000));
        coins.add(new CoinHolder(R.drawable.casino_item_chip_1m, 1000000));
        coins.add(new CoinHolder(R.drawable.casino_item_chip_5m, 5000000));
        coins.add(new CoinHolder(R.drawable.casino_item_chip_10m, 10000000));
        coins.add(new CoinHolder(R.drawable.casino_item_chip_50m, 50000000));
        coins.add(new CoinHolder(R.drawable.casino_item_chip_100m, 100000000));
        coins.add(new CoinHolder(R.drawable.casino_item_chip_200m, 200000000));
        coins.add(new CostomCoinHolder());
        coinsView.add(coins);
    }

    @Override
    public void statusUpdate() {
        pokerContainer.setVisibility(View.VISIBLE);
        if (source.status == 0) {
            gameStageTxt.setText("洗牌中");
        } else if (source.status == 1) {
            stackLeft.disable(false);
            stackRight.disable(false);
            stackTop.disable(false);
            stackBTL.disable(false);
            stackBTR.disable(false);
            stackSuper.disable(false);
            popup.dismiss();
            gameStageTxt.setText("請下注");
            pokerContainer.setVisibility(View.INVISIBLE);
            resultUpadte();

            confirmBtn.disable(false);
        } else if (source.status == 2) {
            stackLeft.disable(true);
            stackRight.disable(true);
            stackTop.disable(true);
            stackBTL.disable(true);
            stackBTR.disable(true);
            stackSuper.disable(true);
            confirmBtn.disable(true);
            cancelBtn.disable(true);
            repeatBtn.disable(true);
            if(viewIsZoomed){
                move.back(0);
                viewIsZoomed = false;
            }

            gameStageTxt.setText("開牌中");
        } else if (source.status == 3) {
            gameStageTxt.setText("結算中");
            resetCoinStacks();
        } else {

        }
    }

    @Override
    public void cardUpdate(int area, int img) {
        ImageView pokerImg = pokers.get(area);
        pokerImg.setVisibility(View.VISIBLE);
        pokerImg.setImageResource(img);
        /*
        for(int i = 0; i < 6; i++) {
            if(source.pokers[i] != 0){
                pokers[i].setImageResource(source.pokers[i]);
                pokers[i].setVisibility(View.VISIBLE);
            }else{
                pokers[i].setVisibility(View.INVISIBLE);
            }
        }*/
    }

    @Override
    public void resultUpadte() {
        if (source.pokerWin == 1) {
            pokerBall.setText(getString(R.string.banker_score));
            pokerBall.setBackgroundResource(R.drawable.casino_item_bt_bank);
        } else if (source.pokerWin == 2) {
            pokerBall.setText(getString(R.string.player_score));
            pokerBall.setBackgroundResource(R.drawable.casino_item_bt_player);
        } else if (source.pokerWin == 4) {
            pokerBall.setText(getString(R.string.tie_score));
            pokerBall.setBackgroundResource(R.drawable.casino_item_bt_tie);
        }
        if(source.pokerWin == -1){
            playerScreenScore.setText("");
            bankerScreenScore.setText("");
            pokerBall.setVisibility(View.INVISIBLE);
        }else{
            playerScreenScore.setText(getString(R.string.player_score) + source.playerScore);
            bankerScreenScore.setText(getString(R.string.banker_score) + source.bankerScore);
            pokerBall.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void balanceUpdate(float value) {
       setTextView(R.id.player_money, value + "");
    }

    @Override
    public void betUpdate(boolean betOK) {
        if(betOK){
            alert("bet succ!");
        }else{
            alert("fail!");
        }

    }


    @Override
    public void gridUpdate() {
        setMainGrid();
    }

    @Override
    public void betCountdown(int sec) {
        gameStageTxt.setText("請下注" + sec);
        if (sec <= 5) countdownBox.setBackgroundResource(R.drawable.casino_countdown2);
    }

    @Override
    public void winLossUpdate(BacData data) {
        popup.setUp(source, data);
        popup.show();
    }

}
