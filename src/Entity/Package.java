package Entity;

import java.util.*;
class Package{
    private ArrayList<Integer> index;
    private Data data;
    private String state;   //index、onepeople、index onepeople
    public Package(){}
    public Package(ArrayList<Integer> index,Data data,String state){
        this.index=index;
        this.data=data;
        this.state=state;
    }

    public ArrayList<Integer> getIndex(){
        return this.index;
    }
    public Data getData(){
        return data;
    }
    public String getState(){
        return state;
    }

}