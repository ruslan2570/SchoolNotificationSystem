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
            response.setContentType("text/html;charset=utf-8");
            Connection connection = DriverManager.

            getConnection("jdbc:mysql://", "user", "password");

            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery("SELECT message.message_id as msg_id, message.text as `text`, user.username as username, " +
                    "role.role_name as role" +
                    " FROM `message`, `user`, `role` " +
                    "WHERE message.user_id = user.user_id " +
                    "AND user.role = role.role_id");

            response.getWriter().println("<!DOCTYPE html>");
            response.getWriter().println("<html>");
            response.getWriter().println("<head>");
            response.getWriter().println("<style>");
            response.getWriter().println("table { border: 1px solid grey; } th { border: 1px solid grey; } td { border: 1px solid grey; }");
            response.getWriter().println("</style></head><body><table>");
            response.getWriter().println("<tr><th>№</th><th>Текст</th><th>Автор</th><th>Роль автора</th>");
            while(set.next()){
                response.getWriter().println("<tr><td>" + set.getString("msg_id") + "</td><td>" + set.getString("text")
                        + "</td><td>" + set.getString("username") + "</td><td>"  +  set.getString("role") + "</td></tr>");
            }
            response.getWriter().println("</table></body></html>");
            response.setStatus(HttpServletResponse.SC_OK);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
