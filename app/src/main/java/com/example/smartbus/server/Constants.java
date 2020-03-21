package com.example.smartbus.server;

public class Constants {

    //--------general------
    // public static final String url = "https://www.ali0.sa/bus/android_files/";
    private static final String url = "http://192.168.43.126/";
    public static final String loginUrl = url + "login.php";
    //---Driver----------
    public static final String infoDriverTag = "diverInfo";
    public static final String getStudentUrl = url + "getStudent.php";
    public static final String updateDriverProfile = url + "insertDriverData.php";
    public static final String getStudentInfoUrl = url + "getStudentInfo.php";

    //---Student-----------
    public static final String infoTag = "info";
    public static final String rateTag = "rate";

    public static final String getChildrenUrl = url + "getChildren.php";
    public static final String updateStudentProfile = url + "insertStudentData.php";
    public static final String driverRateUrl = url + "insertDriverRate.php";

}
