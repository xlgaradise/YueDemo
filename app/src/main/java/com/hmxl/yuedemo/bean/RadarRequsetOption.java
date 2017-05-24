package com.hmxl.yuedemo.bean;

import java.util.Date;

/**
 * Created by HPC on 2017/5/20.
 */

public class RadarRequsetOption {
    public enum SearchType{
        all(0), run(1), movie(2), eat(3), climb(4), travel(5), other(6);
        private int index;
        private SearchType(int index){
            this.index = index;
        }
        public static SearchType getSearchType(int index){
            SearchType type = SearchType.all;
            for(SearchType s: SearchType.values()){
                if(s.index == index)
                    type = s;
            }
            return type;
        }
    }
    public SearchType searchType;
    public String searchSex;
    public String message;
    public String date;

    public RadarRequsetOption(){
        searchType = SearchType.all;
        searchSex = "all";
        message = "nullmessage";
        date = "nulldate";
    }

}
