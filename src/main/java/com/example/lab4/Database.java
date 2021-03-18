package com.example.lab4;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Database {
    public static EntityManagerFactory userEntityManagerFactory = Persistence.createEntityManagerFactory("UserUnit");
    public static EntityManager userEM = userEntityManagerFactory.createEntityManager();

    public static EntityManagerFactory pointEntityManagerFactory = Persistence.createEntityManagerFactory("PointUnit");
    public static EntityManager pointEM = pointEntityManagerFactory.createEntityManager();
}
