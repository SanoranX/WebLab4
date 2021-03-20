package xyz.sanoranx.lab4.rest;

import xyz.sanoranx.lab4.entity.Point;

import java.util.List;

public class ResponseStructure {
    public String status;
    public String key;
    public List<Point> data;
    public Point last_point;

    public ResponseStructure() {
    }
}

