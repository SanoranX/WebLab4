package xyz.sanoranx.lab4.rest;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import xyz.sanoranx.lab4.beans.PointBean;
import xyz.sanoranx.lab4.beans.UserBean;
import xyz.sanoranx.lab4.entity.Point;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Arrays;

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
        try{
            JsonElement jsonElement = new JsonParser().parse(s);
            String userLogonKey = jsonElement.getAsJsonObject().get("key").getAsString();
            if (userBean.isValidUser("unknown", userLogonKey)) {
                try {
                    pointBean.clear();
                    responseStructure.status = "ok";
                } catch (Exception e) {
                    responseStructure.status = "failed";
                }
                return gson.toJson(responseStructure, ResponseStructure.class);
            } else {
                responseStructure.status = "outdated";
                return gson.toJson(responseStructure, ResponseStructure.class);
            }
        }catch (Exception e){
            responseStructure.status = "failed";
            System.err.println("Exception in Clear: " + e.getMessage());
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

        try{
            JsonElement jsonElement = new JsonParser().parse(s);
            String userLogonKey = jsonElement.getAsJsonObject().get("key").getAsString();

            if (userBean.isValidUser("unknown", userLogonKey)) {
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
        }catch (Exception e){
            System.err.println("Exception in GetPoints: " + e.getMessage());
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
        try{

            JsonElement jsonElement = new JsonParser().parse(s);
            String key = jsonElement.getAsJsonObject().get("key").getAsString();
            String username = jsonElement.getAsJsonObject().get("username").getAsString();

            if (userBean.isValidUser(username, key)) {
                try {
                    Double x = Double.parseDouble(jsonElement.getAsJsonObject().get("x").getAsString());
                    Double y = Double.parseDouble(jsonElement.getAsJsonObject().get("y").getAsString());
                    Double r = Double.parseDouble(jsonElement.getAsJsonObject().get("r").getAsString());

                    if (!Arrays.asList(1., 2., 3., 4.).contains(r)) {
                        throw new Exception("Invalid R value [" + r + "]");
                    }
                    else if(x > 4 || x < -4) {
                        throw new Exception("Invalid X value [" + x + "]");
                    }
                    else if(y > 4 || y < -4) {
                        throw new Exception("Invalid Y value [" + y + "]");
                    }
                    pointBean.addPoint(x, y, r, username);
                    resp.last_point = pointBean.getPoints().get(pointBean.getPoints().size() - 1);
                    resp.status = "ok";
                } catch (Exception e) {
                    System.err.println("Exception in AddPoitn: " + e.getMessage());
                    resp.status = "failed";
                }
            } else {
                resp.status = "outdated";
                return gson.toJson(resp, ResponseStructure.class);
            }
            return gson.toJson(resp, ResponseStructure.class);
        }catch (Exception e){
            System.err.println("Exception in AddPoitn: " + e.getMessage());
            return gson.toJson(resp, ResponseStructure.class);
        }
    }
}