package ru.ruslan2570.SchoolNotificationSystem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ruslan2570.SchoolNotificationSystem.models.Message;
import ru.ruslan2570.SchoolNotificationSystem.models.MessageList;
import ru.ruslan2570.SchoolNotificationSystem.models.User;
import ru.ruslan2570.SchoolNotificationSystem.models.UserList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainServlet extends HttpServlet {

    private final String url;
    private final String user;
    private final String password;

    public MainServlet(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        String act = request.getParameter("action");

        try {

            Connection connection = DriverManager.
                    getConnection(url, user, password);

            if (act == null) {

                response.setContentType("text/html;charset=utf-8");
                Statement statement = connection.createStatement();
                ResultSet set = statement.executeQuery("SELECT message.message_id as msg_id, message.text as `text`, " +
                        "user.username as username, " +
                        "role.role_name as role" +
                        " FROM `message`, `user`, `role` " +
                        "WHERE message.user_id = user.user_id " +
                        "AND user.role = role.role_id");

                response.getWriter().println("<!DOCTYPE html><html><head><title>Список сообщений</title><style> " +
                        "table{font-family:Lucida Sans Unicode,Lucida Grande,Sans-Serif;font-size:14px;" +
                        "border-radius:10px;border-spacing:0;text-align:center}th{background:#BCEBDD;color:white;text-shadow:0" +
                        " 1px 1px #2D2020;padding:10px 20px}th,td{border-style:solid;border-width:0 1px 1px 0;border-color:white}th:first-child," +
                        "td:first-child{text-align:left}th:first-child{border-top-left-radius:10px}th:last-child{border-top-right-radius:10px;border-right:none}" +
                        "td{padding:10px 20px;background:#F8E391}tr:last-child td:first-child{border-radius:0 0 0 10px}" +
                        "tr:last-child td:last-child{border-radius:0 0 10px 0}" +
                        "tr td:last-child{border-right:none}" +
                        "</style></head><body><table><tr><th>№</th><th>Текст</th><th>Автор</th><th>Роль автора</th>");
                while (set.next()) {
                    response.getWriter().println("<tr><td>" + set.getString("msg_id") + "</td><td>" + set.getString("text")
                            + "</td><td>" + set.getString("username") + "</td><td>" + set.getString("role") + "</td></tr>");
                }
                response.getWriter().println("</table></body></html>");
                response.setStatus(HttpServletResponse.SC_OK);
            }

            else {
                response.setContentType("application/json;charset=utf-8");
                Statement statement;
                ResultSet set;
                Gson json;

                switch (act) {
                    case "getUsers":
                        statement = connection.createStatement();
                        set = statement.executeQuery("SELECT user_id, username, role.role_name FROM `user` INNER JOIN `role` ON role.role_id = user.role");

                        json = new GsonBuilder().setPrettyPrinting().create();
                        UserList users = new UserList();

                        while (set.next()) {
                            User user = new User(
                                    set.getInt("user_id"),
                                    set.getString("username"),
                                    set.getString("role_name"));
                            users.addUser(user);
                        }

                        response.getWriter().println(json.toJson(users));

                        response.setStatus(HttpServletResponse.SC_OK);
                        break;

                    case "getMessages":
                        statement = connection.createStatement();
                        set = statement.executeQuery("SELECT message.message_id as msg_id, message.text as `text`, " +
                                "user.username as username, " +
                                "role.role_name as role" +
                                " FROM `message`, `user`, `role` " +
                                "WHERE message.user_id = user.user_id " +
                                "AND user.role = role.role_id");


                        json = new GsonBuilder().setPrettyPrinting().create();
                        MessageList messages = new MessageList();

                        while (set.next()) {
                            Message msg = new Message(
                                    set.getInt("msg_id"),
                                    set.getString("text"),
                                    set.getString("username"),
                                    set.getString("role"));
                            messages.addMessage(msg);
                        }

                        response.getWriter().println(json.toJson(messages));

                        response.setStatus(HttpServletResponse.SC_OK);



                        break;

                    case "login":
                        String login = request.getParameter("login");
                        String password = request.getParameter("password");

                        if(login == null || password == null){
                            json = new GsonBuilder().setPrettyPrinting().create();

                            JsonObject jsonObj = new JsonObject();
                            jsonObj.addProperty("error", "Login or password is empty.");
                            response.getWriter().println(json.toJson(jsonObj));
                            response.setStatus(HttpServletResponse.SC_OK);
                            break;
                        }

                        statement = connection.createStatement();
                        set = statement.executeQuery("SELECT * FROM `user` WHERE user.username LIKE '" + login + "'");

                        System.out.println(set.isLast());
                        while(set.next()){
                            System.out.println(set.getString("username"));
                        }

                        break;
                }
            }


    } catch( Exception x)
    {
        x.printStackTrace();
    }
}
}


