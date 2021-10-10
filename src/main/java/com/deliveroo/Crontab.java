package com.deliveroo;

public class Crontab {
    public static void main(String[] args) {
        System.out.println(new Cron(args[0]).getCronDetails());
    }
}
