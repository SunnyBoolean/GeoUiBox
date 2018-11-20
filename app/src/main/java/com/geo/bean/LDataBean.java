package com.geo.bean;

import java.io.Serializable;

/**
 * Created by liwei on 2018/10/17.
 */

public class LDataBean implements Serializable{
    public int type;
    public  String title;
    public String content;
    public LDataBean(int type,String title,String content){
        this.type=type;
        this.title=title;
        this.content=content;
    }
}
