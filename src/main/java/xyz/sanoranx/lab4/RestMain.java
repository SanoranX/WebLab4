package com.example.lab4;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Stateless
@Path("/api")
public class RestMain {
    @EJB
    private PointBean pointBean;

    @EJB
    private UserBean userBean;

    @POST
    @Path("points/clear")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String clear(String s) {
        Gson gson = new Gson();
        ResponseStructure responseStructure = new ResponseStructure();
        JsonElement jsonElement = new JsonParser().parse(s);
        String userLogonKey = jsonElement.getAsJsonObject().get("key").getAsString();
        if (userBean.isValidUser(userLogonKey)) {
            try {
                pointBean.clear();
                responseStructure.status = "ok";
            } catch (Exception e) {
                responseStructure.status = "failed";
            }
            return gson.toJson(responseStructure, ResponseStructure.class);
        } else {
            responseStructure.status = "failed";
            return gson.toJson(responseStructure, ResponseStructure.class);
        }
    }

    @POST
    @Path("points/get")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getPoints(String s) {
        Gson gson = new Gson();
        ResponseStructure responseStructure = new ResponseStructure();

        JsonElement jsonElement = new JsonParser().parse(s);
        String userLogonKey = jsonElement.getAsJsonObject().get("key").getAsString();

        if (userBean.isValidUser(userLogonKey)) {
            try {
                responseStructure.data = pointBean.getPoints();
            } catch (Exception e) {
                responseStructure.data = new ArrayList<Point>();
            }
            return gson.toJson(responseStructure, ResponseStructure.class);
        } else {
            responseStructure.status = "failed";
            return gson.toJson(responseStructure, ResponseStructure.class);
        }
    }

    @POST
    @Path("points/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String addPoint(String s) {
        Gson gson = new Gson();
        ResponseStructure resp = new ResponseStructure();
        resp.status = "failed";

        JsonElement jsonElement = new JsonParser().parse(s);
        String key = jsonElement.getAsJsonObject().get("key").getAsString();
        String username = jsonElement.getAsJsonObject().get("username").getAsString();
        if (userBean.isValidUser(key)) {
            try {
                String x = jsonElement.getAsJsonObject().get("x").getAsString();
                String y = jsonElement.getAsJsonObject().get("y").getAsString();
                String r = jsonElement.getAsJsonObject().get("r").getAsString();

                if (!Arrays.asList(1., 2., 3., 4.).contains(Double.parseDouble(r))) {
                    throw new Exception("Invalid R value");
                }

                pointBean.addPoint(
                        Double.parseDouble(x),
                        Double.parseDouble(y),
                        Double.parseDouble(r),
                        username
                );
                resp.last_point = pointBean.getPoints().get(pointBean.getPoints().size() - 1);
                resp.status = "ok";
            } catch (Exception e) {
                resp.status = "failed";
            }
        } else {
            resp.status = "failed";
        }
        return gson.toJson(resp, ResponseStructure.class);
    }
}