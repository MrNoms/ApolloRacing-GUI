package com.revature.apolloracing.ui;

import java.io.PrintStream;
import java.util.Scanner;

public interface IMenu {
    PrintStream cout = System.out;
    Scanner cin = new Scanner(System.in);
    void start();
}
