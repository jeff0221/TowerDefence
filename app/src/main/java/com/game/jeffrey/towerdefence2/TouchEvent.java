package com.game.jeffrey.towerdefence2;

public class TouchEvent
{
    public enum TouchEventType{
        Down,
        Up,
        Dragged
    }
    public TouchEventType type;
    public int x;
    public int y;
    public int pointer;

    public String toString() {
        return "pointer: " + pointer;
    }
}
