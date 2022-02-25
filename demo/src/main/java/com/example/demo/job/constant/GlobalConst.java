package com.example.demo.job.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GlobalConst {

    //max number of pages
    public static int MAX_PAGE = 100;

    // how many in a page - fixed
    public static int PAGE_SIZE_MAX_20 = 20;
    public static int PAGE_SIZE_MAX_50 = 50;
    public static int PAGE_SIZE_MAX_100 = 100;

    public final static List<String> TOP5_COMPANIES=new ArrayList<String>(Arrays.asList(
            "Amazon",
            "Google",
            "Shopify",
            "Microsoft"
    ));
    public final static List<String> TOP5_CITIES=new ArrayList<String>(Arrays.asList(
            "Vancouver",
            "Toronto",
            "Waterloo",
            "Montreal",
            "Ottawa"));
    /**
     * 登录session key
     */
    public final static String SESSION_KEY = "user";

}
