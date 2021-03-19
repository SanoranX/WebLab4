package xyz.sanoranx.lab4.beans;

import xyz.sanoranx.lab4.entity.User;
import xyz.sanoranx.lab4.dao.Database;

import javax.ejb.Singleton;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

@Singleton
public class UserBean {

    private static Set<String> loggedInSecretKeys = new HashSet<>();

    private List<User> getRegistered() {
        return Database.userEM.createQuery("select c from user_table c").getResultList();
    }

    public String register(String username, String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        String encodedPass = Base64.getEncoder().encodeToString(hash);

        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(encodedPass);

        String secKey = new BigInteger(130, new SecureRandom()).toString(32);

        loggedInSecretKeys.add(secKey);

        Database.userEM.getTransaction().begin();
        Database.userEM.persist(user);
        Database.userEM.flush();
        Database.userEM.getTransaction().commit();
        return secKey;
    }

    public Boolean isValidUser(String secKey) {
        return loggedInSecretKeys.contains(secKey);
    }

    public String login(String username, String password) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        String encodedPass = Base64.getEncoder().encodeToString(hash);

        List<User> users = getRegistered();
        for (User user : users) {
            if (username.equals(user.getUsername()) &&
                encodedPass.equals(user.getPasswordHash())) {
                String secKey = new BigInteger(130, new SecureRandom()).toString(32);
                loggedInSecretKeys.add(secKey);
                return secKey;
            }
        }
        throw new Exception("Invalid username or pass");
    }

    public Boolean isRegistered(String username) {
        List<User> users = getRegistered();
        for (User user : users) {
            if (username.equals(user.getUsername()))
                return true;
        }
        return false;
    }

    public Boolean logout(String secretKey) {
        return loggedInSecretKeys.remove(secretKey);
    }
}
