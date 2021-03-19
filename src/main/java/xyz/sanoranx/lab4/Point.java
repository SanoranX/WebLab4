package com.example.lab4;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "result_table")
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Double x;
    private Double y;
    private Double r;
    private Boolean result;
    private String creator;

    public Point(Double x, Double y, Double r, Boolean result, String creator) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.result = result;
        this.creator = creator;
    }

    public String getX() {
        return String.format("%.2f", x);
    }

    public void setX(Double x) {
        this.x = x;
    }

    public String getY() {
        return String.format("%.2f", y);
    }

    public void setY(Double y) {
        this.y = y;
    }

    public String getR() {
        return String.format("%.2f", r);
    }

    public void setR(Double r) {
        this.r = r;
    }

    public String getResult() {
        return result ? "Yes" : "No";
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCreator(String creator) { this.creator = creator; }

    public String getCreator() { return creator; }

    public Point() {
        x = 0.;
        y = 0.;
        r = 0.;
        result = true;
    }

    public static Boolean calculate(Double x, Double y, Double r) {
        if (x <= 0 && y <= 0 && Math.sqrt(x * x + y * y) <= Math.abs(r))
            return true;
        else if (x >= 0 && y <= 0 && x <= r / 2 && y >= -r)
            return true;
        else if (x >= 0 && y >= 0 && r - x >= y)
            return true;
        else
            return false;
    }
}
