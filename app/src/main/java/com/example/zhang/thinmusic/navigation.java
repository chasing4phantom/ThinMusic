package com.example.zhang.thinmusic;

/**
 * Created by zhang on 2018/1/22.
 */

public class navigation{
    private String name;
    private  int imageId;
    public  navigation(String name, int imageId){
        this.name = name;
        this.imageId = imageId;
    }
    public  String getName(){
        return  name;
    }
    public  int getImageId(){
        return  imageId;
    }
}