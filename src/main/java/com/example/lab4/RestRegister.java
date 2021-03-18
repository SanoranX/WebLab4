package com.example.lab4;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("/lk")
public class RestRegister {

    @EJB
    private UserBean userBean;

    @POST
    @Path("register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String register(String s) {
        Gson gson = new Gson();
        ResponseStructure resp = new ResponseStructure();
        try {
            JsonElement root = new JsonParser().parse(s);
            String username = root.getAsJsonObject().get("username").getAsString();

            if (userBean.isRegistered(username)) {
                resp.status = "exists";
                return gson.toJson(resp, ResponseStructure.class);
            }
            else {
                String password = root.getAsJsonObject().get("password").getAsString();
                resp.status = "ok";
                resp.key = userBean.register(username, password);
                return gson.toJson(resp, ResponseStructure.class);
            }
        } catch (Exception e) {
            resp.status = "failed";
            return gson.toJson(resp, ResponseStructure.class);
        }
    }

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String login(String s) {
        ResponseStructure resp = new ResponseStructure();
        Gson gson = new Gson();

        try {
            JsonElement root = new JsonParser().parse(s);
            String username = root.getAsJsonObject().get("username").getAsString();
            String password = root.getAsJsonObject().get("password").getAsString();
            resp.status = "ok";
            resp.key = userBean.login(username, password);
            return gson.toJson(resp, ResponseStructure.class);

        } catch (Exception e) {
            resp.status = "failed";
            return gson.toJson(resp, ResponseStructure.class);
        }
    }

    @POST
    @Path("logout")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String logout(String s) {
        Gson gson = new Gson();
        ResponseStructure resp = new ResponseStructure();

        JsonElement root = new JsonParser().parse(s);
        String key = root.getAsJsonObject().get("key").getAsString();
        resp.status = userBean.logout(key) ? "ok" : "failed";
        return gson.toJson(resp, ResponseStructure.class);
    }
}
