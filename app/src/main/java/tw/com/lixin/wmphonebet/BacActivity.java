package tw.com.lixin.wmphonebet;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import tw.com.lixin.wmphonebet.global.User;
import tw.com.lixin.wmphonebet.interfaces.BacBridge;
import tw.com.lixin.wmphonebet.jsonData.Client22;
import tw.com.lixin.wmphonebet.models.CoinHolder;
import tw.com.lixin.wmphonebet.models.CostomCoinHolder;
import tw.com.lixin.wmphonebet.websocketSource.BacSource;

public class BacActivity extends RootActivity implements BacBridge {
    private int posX, posY;
    private Animation fadeAnimeB;
    private Move move;
    public ItemsView coinsView;
    private Popup winPopup;
    private TextView gameStageTxt, pokerBall, playerScreenScore, bankerScreenScore;
    public CoinHolder curCoin;
    private CasinoGrid mainGrid, firstGrid, secGrid, thirdGrid, fourthGrid;
    private View logo;

    private ImageView[] pokers = new ImageView[6];

    private ConstraintLayout videoContaner, pokerContainer, countdownBox, tableBetContainer, root, tableRight, tableSuper, tableTop, tableLeft;
    private CoinStack stackLeft, stackRight, stackTop, stackBTL, stackBTR, stackSuper;
    private ImageView bankSecondSym, bankThirdSym, bankFourthSym, playerSecondSym, playerThirdSym, playerFourthSym;
    private GoldenButton repeatBtn, cancelBtn, comissionBtn;
    public GoldenButton confirmBtn;
    private boolean viewIsZoomed = false;
    private String bankTableScore;

    private View mainV, firstV, secV, thirdV, fourthV;

    private IjkVideoView video;

    private BacSource source;

    public void viewZoomOut(View view) {
        if(!source.isBettingNow) return;
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

        String path = "rtmp://wmvdo.c2h6.cn/ytb" + String.format(Locale.US, "%02d", source.groupID) + "-1/stream1";
        video = findViewById(R.id.player);
        video.setVideoPath(path);
        video.start();

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
        winPopup = new Popup(this, R.layout.win_loss_popup);
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

        pokers[0] = findViewById(R.id.player_poker1);
        pokers[1] = findViewById(R.id.player_poker2);
        pokers[2] = findViewById(R.id.player_poker3);
        pokers[3] = findViewById(R.id.banker_poker1);
        pokers[4] = findViewById(R.id.banker_poker2);
        pokers[5] = findViewById(R.id.banker_poker3);
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

                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        Log.e("SCROLL", "stopped.");
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        Log.e("SCROLL", "drag.");
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        Log.e("SCROLL", "settling.");
                        break;
                }
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    final int curPos = coinsView.findScroll();
                    delay(200, () -> coinsView.scrollTo(curPos));
                }
            }
        });

        clicked(R.id.cash_btn,v-> new PayPopup(this).show());

        stackLeft.setUp(source.stackLeft);
        stackRight.setUp(source.stackRight);
        stackTop.setUp(source.stackTop);
        stackBTL.setUp(source.stackBTL);
        stackBTR.setUp(source.stackBTR);
        stackSuper.setUp(source.stackSuper);
        CardStatus();

        clicked(R.id.table_left, v -> {
            stackLeft.add(curCoin);
            alert("added");
            checkStackEmpty();
        });
        clicked(tableRight, v -> {
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

        clicked(R.id.switch_table_btn, v -> new TableSwitchPopup(this).show());

        confirmBtn.clicked(v -> {
            Client22 client22 = new Client22(source.groupID, source.areaID);
            if (source.comission) {
                client22.data.commission = 1;
                stackSuper.addCoinToClient(client22, 8);
            }
            stackBTR.addCoinToClient(client22, 4);
            stackTop.addCoinToClient(client22, 3);
            stackRight.addCoinToClient(client22, 1);
            stackBTL.addCoinToClient(client22, 5);
            stackLeft.addCoinToClient(client22, 2);
            if (client22.data.betArr.size() > 0) source.send(Json.to(client22));
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
        if (stackLeft.isEmpty() && stackBTL.isEmpty() && stackRight.isEmpty() && stackTop.isEmpty() && stackSuper.isEmpty() && stackBTR.isEmpty()) {
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

    @Override
    public void onPause() {
        super.onPause();
        // onBackPressed();
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

        for(int i = 0; i < 6; i++) {
            if(App.group.pokers[i] != 0){
                pokers[i].setImageResource(App.group.pokers[i]);
                pokers[i].setVisibility(View.VISIBLE);
            }else{
                pokers[i].setVisibility(View.INVISIBLE);
            }
        }

        if(App.group.displayCard){
            pokerContainer.bringToFront();
            pokerContainer.setVisibility(View.VISIBLE);
        }else{
            pokerContainer.setVisibility(View.INVISIBLE);
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
    public void cardStatusUpdate() {
        if (App.group.cardStatus == 0) {
            gameStageTxt.setText("洗牌中");
        } else if (App.group.cardStatus == 1) {
            gameStageTxt.setText("請下注");
            winPopup.dismiss();
            resetPokers();
            confirmBtn.disable(false);
        } else if (App.group.cardStatus == 2) {
            confirmBtn.disable(true);
            cancelBtn.disable(true);
            repeatBtn.disable(true);
            if(viewIsZoomed){
                move.back(0);
                viewIsZoomed = false;
            }
            resetPokers();
            gameStageTxt.setText("開牌中");
        } else if (App.group.cardStatus == 3) {
            gameStageTxt.setText("結算中");
        } else {

        }
    }

    @Override
    public void cardAreaUpadte() {
        for(int i = 0; i < 6; i++) {
            if(source.pokers[i] != 0){
                pokers[i].setImageResource(source.pokers[i]);
                pokers[i].setVisibility(View.VISIBLE);
            }else{
                pokers[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void balanceUpdate(float value) {

    }

    @Override
    public void betOK() {

    }

    @Override
    public void betFail() {

    }

    @Override
    public void gridUpdate() {

    }

    @Override
    public void betCountdown(int sec) {

    }

    @Override
    public void groupLogOK() {

    }

    @Override
    public void groupLogFail() {

    }
}
