package usecase;

import model.user;
import repository.usersRepo;
import helper.currentUser;
import helper.validasi;

import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

public class userUc {
    private final usersRepo userRepo;

    public userUc(usersRepo userRepo) {
        this.userRepo = userRepo;
    }

    public boolean register(user u) {
        try {
            //validasi format email
            if (!validasi.isValidGmail(u.getEmail())) {
                System.out.println("Email must use the @gmail.com domain!");
                return false;
            }

            //validasi format no hp
            if (!validasi.isValidPhoneNumber(u.getPhoneNumber())) {
                System.out.println("Phone number must be at least 11 digits!");
                return false;
            }

            //validasi email uniq
            if (userRepo.isEmailExist(u.getEmail())) {
                System.out.println("Email already registered!");
                return false;
            }

            //validasi no hp uniq
            if (userRepo.isPhoneNumberExist(u.getPhoneNumber())) {
                System.out.println("Phone number already registered!");
                return false;
            }

            // Hash password
            String hashedPassword = BCrypt.hashpw(u.getPassword(), BCrypt.gensalt());
            u.setPassword(hashedPassword);

            return userRepo.insertUser(u);

        } catch (SQLException e) {
            System.err.println("Register error: " + e.getMessage());
            return false;
        }
    }

    public user login(String email, String password) {
        try {
            user u = userRepo.findUserByEmail(email);
            if (u != null && BCrypt.checkpw(password, u.getPassword())) {
                currentUser.set(u.getId(), u.getUserName());
                return u;
            } else {
                System.out.println("Email or password doesn't match!");
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Login error: " + e.getMessage());
            return null;
        }
    }
}
