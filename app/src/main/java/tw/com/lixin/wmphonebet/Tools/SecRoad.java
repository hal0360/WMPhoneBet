package tw.com.lixin.wmphonebet.Tools;

import java.util.List;

import tw.com.lixin.wmphonebet.global.Road;

public class SecRoad {

    private List<List<Integer>> sortedRoad;
    public int preWWin = 0;
    public int posXX = 0;
    public int posYY = -1;
    private int nextt = -1;
    public int[][] smallRoad;

 public SecRoad(List<List<Integer>> road){
     sortedRoad = road;
     smallRoad = new int[80][7];
     for(int i=0; i<80; i++)smallRoad[i][6] = 999;
     drawGrid();
 }

    private void drawGrid(){

        boolean blueWillWin = true;

        for(int i = 0; i < sortedRoad.size() - 1; i++ ){

            List<Integer> curLine = sortedRoad.get(i+1);
            List<Integer> preLine = sortedRoad.get(i);

            if(i > 0){

                if(blueWillWin){
                    drawReal(Road.Bank);
                }else{
                    drawReal(Road.Play);
                }

            }

            for(int k = 2; k <= curLine.size(); k++ ){

                if(k - preLine.size() == 1){

                    drawReal(Road.Play);

                } else {

                    drawReal(Road.Bank);
                }

            }

            int lastK = curLine.size() + 1;
            if(lastK - preLine.size() == 1){
                blueWillWin = true;
            }else{
                blueWillWin = false;
            }

        }

    }

    public int getLastRid(){
        if(posYY < 0) return 0;
        return smallRoad[posXX][posYY];
    }

    private void drawReal(int rid){
        if(preWWin != rid){
            nextt++;
            posXX = nextt;
            posYY = -1;
        }
        posYY++;
        if(smallRoad[posXX][posYY] != 0 && posYY > 0) posYY--;
        while (smallRoad[posXX][posYY] != 0) posXX++;
        smallRoad[posXX][posYY] = rid;
        preWWin = rid;
    }

}
