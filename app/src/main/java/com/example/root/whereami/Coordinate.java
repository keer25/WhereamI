package com.example.root.whereami;

/**
 * Created by root on 12/4/17.
 */

public class Coordinate {
    int x;
    int y;

    public Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }

    public boolean eq(Coordinate co){
        if (co.x == x && co.y == y) return true;
        return false;
    }


}
