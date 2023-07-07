package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        UserService userService = new UserServiceImpl();
        userService.createUsersTable();
        userService.saveUser("Anton", "Ivanov", (byte) 20);
        userService.saveUser("Evgeniy", "Smirnov", (byte) 25);
        userService.saveUser("Jon", "Jonson", (byte) 30);
        userService.saveUser("Antony", "Mc.David", (byte) 35);

        List<User> users = userService.getAllUsers();
        for (User u: users) {
            System.out.println(u);
        }

        userService.cleanUsersTable();
        userService.dropUsersTable();
    }
}