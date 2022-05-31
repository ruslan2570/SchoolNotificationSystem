package ru.ruslan2570.SchoolNotificationSystem;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;

public class MainServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Connection connection = DriverManager.

            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery("SELECT user_id, username, role.role_name FROM `user` INNER JOIN role on role.role_id = user.role");

            while(set.next()){
                response.getWriter().println("<h2>" + set.getString("username") + "role.role_name" + "</h2>");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
