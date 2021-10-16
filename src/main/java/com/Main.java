package com;

import com.DataBase.DBInterface;

import java.util.Scanner;

public class Main {
    static String path = "C:\\Users\\START\\Documents\\DataBase";
    public static void main(String[] args) {
        DBInterface dbInterface = new DBInterface(new Scanner(System.in), path);
        dbInterface.startUp();
    }
}
