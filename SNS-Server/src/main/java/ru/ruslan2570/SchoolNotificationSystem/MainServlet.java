package ru.ruslan2570.SchoolNotificationSystem;


import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.Gson;

import java.sql.*;
import java.util.ArrayList;

public class MainServlet extends HttpServlet {

    private final String url;
    private final String user;
    private final String password;

    public MainServlet(String url, String user, String password){
        this.url = url;
        this.user = user;
        this.password = password;
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        response.setContentType("application/json;charset=utf-8");
        String act = request.getParameter("action");

        try {

            Connection connection = DriverManager.
                    getConnection(url, user, password);

            if (act == null) {

                Statement statement = connection.createStatement();
                ResultSet set = statement.executeQuery("SELECT message.message_id as msg_id, message.text as `text`, " +
                        "user.username as username, " +
                        "role.role_name as role" +
                        " FROM `message`, `user`, `role` " +
                        "WHERE message.user_id = user.user_id " +
                        "AND user.role = role.role_id");

                response.getWriter().println("<!DOCTYPE html><html><head><title>Список сообщений</title><style> " +
                        "table { border: 1px solid grey; } th { border: 1px solid grey; } td { border: 1px solid grey; }" +
                        "</style></head><body><table><tr><th>№</th><th>Текст</th><th>Автор</th><th>Роль автора</th>");
                while (set.next()) {
                    response.getWriter().println("<tr><td>" + set.getString("msg_id") + "</td><td>" + set.getString("text")
                            + "</td><td>" + set.getString("username") + "</td><td>" + set.getString("role") + "</td></tr>");
                }
                response.getWriter().println("</table></body></html>");
                response.setStatus(HttpServletResponse.SC_OK);
            } else switch(act) {
                case "getUsers":
                    Statement statement = connection.createStatement();
                    ResultSet set = statement.executeQuery("SELECT user_id, username, role.role_name FROM `user` INNER JOIN `role` ON role.role_id = user.role");

                    //Gson json = new Gson();
                    Gson json = new GsonBuilder().setPrettyPrinting().create();
                    StringBuilder s = new StringBuilder();

                    ArrayList<User> users = new ArrayList<>();

                    while(set.next()){
                        User user = new User(set.getInt("user_id"), set.getString("username"), set.getString("role_name"));
                        response.getWriter().println(json.toJson(user));
                        s.append(json.toJson(user));
                    }

                    users.add(json.fromJson(s.toString(), User.class));
                    System.out.println(usr.username);



                    response.setStatus(HttpServletResponse.SC_OK);
                    break;
            }

        } catch (Exception x) {
            x.printStackTrace();
        }
    }
}


