package com.minsu.minsu.common.bean;

import java.util.List;

/**
 * Created by hpc on 2018/1/8.
 */

public class OrderBean {
    public String msg;
    public int code;
    public List<Data> data;

    public class Data {
        public int type;
        public String name;
        public String time;
        public String title;
        public String check_time;
        public String days;
        public String total_price;
        public String province;
        public String city;
        public String district;
        public String town;
        public String fd_mobile;
    }
}