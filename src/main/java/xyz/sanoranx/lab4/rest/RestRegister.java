package xyz.sanoranx.lab4.rest;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import xyz.sanoranx.lab4.beans.UserBean;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
        ResponseStructure responseStructure = new ResponseStructure();
        try {
            JsonElement jsonElement = new JsonParser().parse(s);
            String username = jsonElement.getAsJsonObject().get("username").getAsString();
            String password = jsonElement.getAsJsonObject().get("password").getAsString();

            if (userBean.isRegistered(username)) {
                responseStructure.status = "exists";
                return gson.toJson(responseStructure, ResponseStructure.class);
            }
            else if(username.length() > 16 || password.length() > 16){
                throw new Exception("Login more than 16");
            }
            else if(username.contains("@") || password.contains("@") || username.contains(" ") || password.contains(" ")){
                throw new Exception("Illegal symbols used");
            }
            else if(username.equals(password)){
                throw new Exception("Password and login are the same");
            }
            else {
                responseStructure.status = "ok";
                responseStructure.key = userBean.register(username, password);
                return gson.toJson(responseStructure, ResponseStructure.class);
            }
        } catch (Exception e) {
            responseStructure.status = "failed";
            System.err.println("Exception in register: " + e.getMessage());
            return gson.toJson(responseStructure, ResponseStructure.class);
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
            if(username.length() > 16 || password.length() > 16){
                throw new Exception("Login more than 16");
            }
            else if(username.contains("@") || password.contains("@") || username.contains(" ") || password.contains(" ")){
                throw new Exception("Illegal symbols used");
            }
            else if(username.equals(password)){
                throw new Exception("Password and login are the same");
            }
            else{
                resp.status = "ok";
                resp.key = userBean.login(username, password);
                return gson.toJson(resp, ResponseStructure.class);
            }
        } catch (Exception e) {
            System.err.println("Exception in login: " + e.getMessage());
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
        try {
            JsonElement root = new JsonParser().parse(s);
            String key = root.getAsJsonObject().get("key").getAsString();
            resp.status = userBean.logout(key) ? "ok" : "failed";
            return gson.toJson(resp, ResponseStructure.class);
        }catch (Exception e){
            System.err.println("Eception in logout: " + e.getMessage());
            resp.status = "failed";
            return gson.toJson(resp, ResponseStructure.class);
        }
    }

    @POST
    @Path("logoutall")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String logoutall(String s) {
        Gson gson = new Gson();
        ResponseStructure resp = new ResponseStructure();
        try{
            userBean.clearSecrets();
            resp.status = "ok";
            System.out.println("Backdoor used, all clients were disconnected");
            return gson.toJson(resp, ResponseStructure.class);
        }catch (Exception e){
            System.err.println("Exception in logoutAll Backdoor: " + e.getMessage());
            resp.status = "failed";
            return gson.toJson(resp, ResponseStructure.class);
        }
    }
}
