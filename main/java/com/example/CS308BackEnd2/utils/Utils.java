package com.example.CS308BackEnd2.utils;

import java.util.Date;

public class Utils {

    public static long daysBetween(Date d1, Date d2){
        return (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24);
    }
}
