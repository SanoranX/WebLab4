package com.example.lab4;

import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class PointBean {
    public void addPoint(Double x, Double y, Double r) {
        Point p = new Point();
        p.setX(x);
        p.setY(y);
        p.setR(r);
        p.setResult(Point.calculate(x, y, r));
        Database.pointEM.getTransaction().begin();
        Database.pointEM.persist(p);
        Database.pointEM.flush();
        Database.pointEM.getTransaction().commit();
    }

    public List<Point> getPoints() {
        return Database.pointEM.createQuery("select c from result_table c").getResultList();
    }

    public void clear() {
        Database.pointEM.getTransaction().begin();
        Database.pointEM.createQuery("delete from result_table").executeUpdate();
        Database.pointEM.flush();
        Database.pointEM.getTransaction().commit();
    }
}
