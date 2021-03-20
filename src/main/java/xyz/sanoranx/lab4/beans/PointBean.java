package xyz.sanoranx.lab4.beans;

import xyz.sanoranx.lab4.dao.Database;
import xyz.sanoranx.lab4.entity.Point;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
public class PointBean {
    public void addPoint(Double x, Double y, Double r, String creator) {
        EntityManager pointEM = Database.pointEntityManagerFactory.createEntityManager();
        Point p = new Point();
        p.setX(x);
        p.setY(y);
        p.setR(r);
        p.setResult(Point.calculate(x, y, r));
        p.setCreator(creator);
        pointEM.getTransaction().begin();
        pointEM.persist(p);
        pointEM.flush();
        pointEM.getTransaction().commit();
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
