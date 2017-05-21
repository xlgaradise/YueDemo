package com.hmxl.yuedemo.bean;

import java.util.Date;

/**
 * Created by HPC on 2017/5/20.
 */

public class RadarRequsetOption {
    public enum SearchType{
        all,
        run,
        movie,
        eat,
        other
    }
    public SearchType searchType;
    public String searchSex;
    public String message;
    public String date;

    public RadarRequsetOption(){
        searchType = SearchType.all;
        searchSex = "all";
        message = "";
        date = "";
    }

}
