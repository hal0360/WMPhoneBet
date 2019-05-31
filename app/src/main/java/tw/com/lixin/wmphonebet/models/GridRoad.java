package tw.com.lixin.wmphonebet.models;

import java.util.List;

import tw.com.lixin.wmphonebet.global.Road;


public class GridRoad {

    public int posX = -1;
    public int posY = -1;
    public int next = -1;
    public int preWin = 0;

  //  private int next;
    public int[][] road;

    public boolean blueWillWin = true;

    public GridRoad(){
        road = new int[80][6];
    }


    public int posXX = -1;
    public int posYY = -1;
    public int nextX = -1;
    public int resX;

    public void setFirst(List<List<Integer>> arrs){

        for(List<Integer> arr : arrs){
            next++;
            posX = next;
            posY = -1;
            for(int res: arr){
                posY++;
                if(posY > 5 || road[posX][posY] != 0) posY--;
                while (road[posX][posY] != 0) posX++;
                road[posX][posY] = res;
            }
        }

    }

    public void setSec(List<List<Integer>> arrs){
        blueWillWin = true;
        for(int i = 0; i < arrs.size() - 1; i++ ){
            List<Integer> curLine = arrs.get(i+1);
            List<Integer> preLine = arrs.get(i);
            if(i > 0){
                if(blueWillWin) drawReal(Road.Bank);
                else drawReal(Road.Play);
            }
            for(int k = 2; k <= curLine.size(); k++ ){
                if(k - preLine.size() == 1) drawReal(Road.Play);
                else drawReal(Road.Bank);
            }
            int lastK = curLine.size() + 1;
            blueWillWin = lastK - preLine.size() == 1;
        }
    }

    public void setThird(List<List<Integer>> arrs){
        blueWillWin = true;
        for(int i = 0; i < arrs.size() - 2; i++ ){
            List<Integer> curLine = arrs.get(i+2);
            List<Integer> preLine = arrs.get(i);
            if(i > 0){
                if(blueWillWin) drawReal(Road.Bank_S);
                else drawReal(Road.Play_S);
            }
            for(int k = 2; k <= curLine.size(); k++ ){
                if(k - preLine.size() == 1) drawReal(Road.Play_S);
                else drawReal(Road.Bank_S);
            }
            int lastK = curLine.size() + 1;
            blueWillWin = lastK - preLine.size() == 1;
        }
    }

    public void setFourth(List<List<Integer>> arrs){
        blueWillWin = true;
        for(int i = 0; i < arrs.size() - 3; i++ ){
            List<Integer> curLine = arrs.get(i+3);
            List<Integer> preLine = arrs.get(i);
            if(i > 0){
                if(blueWillWin) drawReal(Road.Bank_I);
                else drawReal(Road.Play_I);
            }
            for(int k = 2; k <= curLine.size(); k++ ){
                if(k - preLine.size() == 1) drawReal(Road.Play_I);
                else drawReal(Road.Bank_I);
            }
            int lastK = curLine.size() + 1;
            blueWillWin = lastK - preLine.size() == 1;
        }
    }

    public void drawRealAskFirst(int tPreWin, int tAskWin, int rid){

        posYY = posY;
        posXX = posX;
        nextX = next;

        if(tPreWin != tAskWin){
            nextX++;
            posXX = nextX;
            posYY = -1;
        }

        posYY++;
        if(posYY > 5 || road[posXX][posYY] != 0 && posYY > 0) posYY--;
        while (road[posXX][posYY] != 0) posXX++;
        resX = rid;

    }

    public void drawRealAsk(int rid){

        posYY = posY;
        posXX = posX;
        nextX = next;

        if(preWin != rid){
            nextX++;
            posXX = nextX;
            posYY = -1;
        }

        posYY++;
        if(posYY > 5 || road[posXX][posYY] != 0 && posYY > 0) posYY--;
        while (road[posXX][posYY] != 0) posXX++;
        resX = rid;

    }

    private void drawReal(int rid){

        if(preWin != rid){
            next++;
            posX = next;
            posY = -1;
        }

        posY++;
        if(posY > 5 || road[posX][posY] != 0 && posY > 0) posY--;
        while (road[posX][posY] != 0) posX++;

        road[posX][posY] = rid;
        preWin = rid;
    }
}
