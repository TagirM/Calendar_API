package ru.tomsknipineft.controllers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

public class MainM {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input work days:");
        int workDays = scanner.nextInt();
        while (workDays!=0){
            int calendar = workDays + (workDays/5)*2;
            System.out.println(calendar);
            System.out.println("Input work days:");
            workDays = scanner.nextInt();
        }
    }

}
