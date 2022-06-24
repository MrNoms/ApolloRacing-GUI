package com.revature.apolloracing.ui;

import java.io.PrintStream;
import java.util.Scanner;

public abstract class IMenu {
    PrintStream cout = System.out;
    Scanner cin = new Scanner(System.in);
    abstract void start();

    void prompt() {
        prompt("");
    }
    void prompt(String str) {
        cout.print(str.concat("> "));
    }
}
