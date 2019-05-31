package tw.com.lixin.wmphonebet.Tools;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class Move {

    public View view;
    private int statusBarOffset;
    private int originalPos[];
    private DisplayMetrics dm;


    public Move(Activity context, View root){
        dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        statusBarOffset = dm.heightPixels - root.getMeasuredHeight();
        originalPos = new int[2];
    }


    public void toCenter(View view, float scale, int speed ){
        view.getLocationOnScreen( originalPos );
        int xDest = dm.widthPixels/2;
        xDest -= (view.getMeasuredWidth()/2);
        int yDest = dm.heightPixels/2 - (view.getMeasuredHeight()/2) - statusBarOffset;
        view.bringToFront();
        view.animate().scaleX(scale).scaleY(scale).translationX(xDest - originalPos[0]).translationY(yDest - originalPos[1]).setDuration(speed).start();
        this.view = view;
    }

    public void back(int speed){
        view.animate().scaleX(1.0f).scaleY(1.0f).translationX(0).translationY(0).setDuration(speed).start();
        view.invalidate();
    }


    public static void disableClipOnParents(View v) {

        if (v == null) {
            return;
        }
        if (v instanceof ViewGroup) {
            ((ViewGroup) v).setClipChildren(false);
        }
        disableClipOnParents((View) v.getParent());

    }

    public static int divide(int rawVal){

        List<Integer> powers = new ArrayList<>();
        for(int i = 8; i >= 0; i-- ){
            int boss = (int) Math.pow(2,i);
            if(rawVal >= boss){
                powers.add(0,boss);
                rawVal = rawVal - boss;
                if(rawVal <= 0){
                    break;
                }
            }
        }
        return powers.get(0);
    }


    public static Bitmap loadBitmapFromView(Context context, View v) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        v.measure(View.MeasureSpec.makeMeasureSpec(dm.widthPixels, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(dm.heightPixels, View.MeasureSpec.EXACTLY));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Bitmap returnedBitmap = Bitmap.createBitmap(v.getMeasuredWidth(),
                v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(returnedBitmap);
        v.draw(c);
        return returnedBitmap;
    }


    /*
    private void setUpRobots(String mss){

        Server20 server20 = Json.from(mss,Server20.class);
        if(server20.data.groupID == groupID && server20.data.gameID == App.gameID && server20.protocol == 20){
            canBet = false;
            resetCoinStacks();
            if(server20.data.gameStage == 0){
                gameStageTxt.setText("洗牌中");
                Log.e("kknd", "洗牌中");
                resetPokers();
            }else if(server20.data.gameStage == 1){
                gameStageTxt.setText("請下注");
                Log.e("kknd", "請下注");
                winPopup.dismiss();
                resetPokers();
                canBet = true;
            }else if(server20.data.gameStage == 2){
                gameStageTxt.setText("開牌中");
                Log.e("kknd", "開牌中");
                pokerContainer.setVisibility(View.VISIBLE);
            }else if(server20.data.gameStage == 3){
                gameStageTxt.setText("結算中");
                Log.e("kknd", "結算中");
                resetPokers();
            }else{
                gameStageTxt.setText("已關桌");
                finish();
            }
            return;
        }

        Server24 server24 = Json.from(mss,Server24.class);
        if(server24.data.groupID == groupID && server24.data.gameID == App.gameID && server20.protocol == 24){
            if(server24.data.cardArea == 1){
                playerPoker1.setImageResource(Poker.NUM(server24.data.cardID));
                playerPoker1.setVisibility(View.VISIBLE);
            }else if(server24.data.cardArea == 2){
                bankerPoker1.setImageResource(Poker.NUM(server24.data.cardID));
                bankerPoker1.setVisibility(View.VISIBLE);
            }else if(server24.data.cardArea == 4){
                bankerPoker2.setImageResource(Poker.NUM(server24.data.cardID));
                bankerPoker2.setVisibility(View.VISIBLE);
            }else if(server24.data.cardArea == 6){
                bankerPoker3.setImageResource(Poker.NUM(server24.data.cardID));
                bankerPoker3.setVisibility(View.VISIBLE);
            }else if(server24.data.cardArea == 3){
                playerPoker2.setImageResource(Poker.NUM(server24.data.cardID));
                playerPoker2.setVisibility(View.VISIBLE);
            }else if(server24.data.cardArea == 5){
                playerPoker3.setImageResource(Poker.NUM(server24.data.cardID));
                playerPoker3.setVisibility(View.VISIBLE);
            }
            return;
        }

        Server22 server22 = Json.from(mss,Server22.class);
        if(server22.data.groupID == groupID && server22.data.gameID == App.gameID && server22.protocol == 22){
            if(server22.data.bOk){
                alert("Bet successful");
                canBet = false;
            }else{
                alert("Error occurred when betting, try again");
            }
            return;
        }

        Server26 server26 = Json.from(mss,Server26.class);
        if(server26.data.groupID == groupID && server26.data.gameID == App.gameID && server26.protocol == 26){
            table.casinoRoad = new CasinoRoad(server26.data.historyArr);
            setMainGrid(table.casinoRoad);
            return;
        }

        Server31 server31 = Json.from(mss,Server31.class);
        if(server31.data.groupID == groupID && server31.data.gameID == App.gameID && server31.protocol == 31){
            TextView mText = winPopup.findViewById(R.id.player_bet);
            mText.setText(stackLeft.value+ "");
            mText = winPopup.findViewById(R.id.banker_bet);
            mText.setText(stackRight.value+ "");
            mText = winPopup.findViewById(R.id.player_pair_bet);
            mText.setText(stackBTL.value+ "");
            mText = winPopup.findViewById(R.id.banker_pair_bet);
            mText.setText(stackBTR.value+ "");
            mText = winPopup.findViewById(R.id.tie_bet);
            mText.setText(stackTop.value+ "");
            mText = winPopup.findViewById(R.id.player_win);
            mText.setText(server31.data.dtMoneyWin.get(2));
            mText = winPopup.findViewById(R.id.banker_win);
            mText.setText(server31.data.dtMoneyWin.get(1));
            mText = winPopup.findViewById(R.id.player_pair_win);
            mText.setText(server31.data.dtMoneyWin.get(5));
            mText = winPopup.findViewById(R.id.banker_pair_win);
            mText.setText(server31.data.dtMoneyWin.get(4));
            mText = winPopup.findViewById(R.id.tie_win);
            mText.setText(server31.data.dtMoneyWin.get(3));
            mText = winPopup.findViewById(R.id.total_win_money);
            mText.setText(server31.data.moneyWin);
            winPopup.show();
            return;
        }

        Server25 server25 = Json.from(mss,Server25.class);
        if(server25.data.groupID == groupID && server25.data.gameID == App.gameID && server25.protocol == 25){
            int pokerWin = Move.divide(server25.data.result);
            if(pokerWin == 1){
                pokerBall.setText(getString(R.string.banker_score));
                pokerBall.setBackgroundResource(R.drawable.casino_item_bt_bank);
            }else if(pokerWin == 2){
                pokerBall.setText(getString(R.string.player_score));
                pokerBall.setBackgroundResource(R.drawable.casino_item_bt_player);
            }else{
                pokerBall.setText(getString(R.string.tie_score));
                pokerBall.setBackgroundResource(R.drawable.casino_item_bt_bank);
            }
            playerScreenScore.setText(getString(R.string.player_score) + server25.data.playerScore);
            bankerScreenScore.setText(getString(R.string.banker_score) + server25.data.bankerScore);
            pokerBall.setVisibility(View.VISIBLE);
            return;
        }

        Server10 server10 = Json.from(mss,Server10.class);
        if(server10.protocol == 10){
            if(server10.data.bOk ){
                loggedIn = true;
                setTextView(R.id.table_left_score, server10.data.dtOdds.get(2));
                setTextView(R.id.table_right_score, server10.data.dtOdds.get(1));
                setTextView(R.id.table_bt_l_score, server10.data.dtOdds.get(5));
                setTextView(R.id.table_bt_r_score, server10.data.dtOdds.get(4));
                setTextView(R.id.table_top_score, server10.data.dtOdds.get(3));
                stackLeft.maxValue = server10.data.maxBet02;
                stackBTL.maxValue = server10.data.maxBet04;
                stackRight.maxValue = server10.data.maxBet01;
                stackBTR.maxValue = server10.data.maxBet04;
                stackTop.maxValue = server10.data.maxBet03;
                areaID = server10.data.areaID;
                setTextView(R.id.player_money, server10.data.balance + "");
            }else{
                alert("Access denied");
                finish();
            }
        }

    }*/


}
