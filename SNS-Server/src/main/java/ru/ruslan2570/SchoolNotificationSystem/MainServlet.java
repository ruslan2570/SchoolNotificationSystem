package ru.ruslan2570.SchoolNotificationSystem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ruslan2570.SchoolNotificationSystem.models.*;

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
                ResultSet resultSet = statement.executeQuery("SELECT message.message_id as msg_id, message.text as `text`, " +
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
                while (resultSet.next()) {
                    response.getWriter().println("<tr><td>" + resultSet.getString("msg_id") + "</td><td>" + resultSet.getString("text")
                            + "</td><td>" + resultSet.getString("username") + "</td><td>" + resultSet.getString("role") + "</td></tr>");
                }
                response.getWriter().println("</table></body></html>");
                response.setStatus(HttpServletResponse.SC_OK);
            }

            else {
                response.setContentType("application/json;charset=utf-8");
                String sessionId = request.getParameter("session_id");
                Statement statement;
                ResultSet resultSet;
                Gson json;

                switch (act) {
                    case "getUsers":



                        statement = connection.createStatement();
                        resultSet = statement.executeQuery("SELECT user_id, username, role.role_name FROM `user` INNER JOIN `role` ON role.role_id = user.role");

                        json = new GsonBuilder().setPrettyPrinting().create();
                        UserList users = new UserList();

                        while (resultSet.next()) {
                            User user = new User(
                                    resultSet.getInt("user_id"),
                                    resultSet.getString("username"),
                                    resultSet.getString("role_name"));
                            users.addUser(user);
                        }

                        response.getWriter().println(json.toJson(users));

                        response.setStatus(HttpServletResponse.SC_OK);
                        break;

                    case "getMessages":
                        statement = connection.createStatement();
                        resultSet = statement.executeQuery("SELECT message.message_id as msg_id, message.text as `text`, " +
                                "user.username as username, " +
                                "role.role_name as role" +
                                " FROM `message`, `user`, `role` " +
                                "WHERE message.user_id = user.user_id " +
                                "AND user.role = role.role_id");

                        json = new GsonBuilder().setPrettyPrinting().create();
                        MessageList messages = new MessageList();

                        while (resultSet.next()) {
                            Message msg = new Message(
                                    resultSet.getInt("msg_id"),
                                    resultSet.getString("text"),
                                    resultSet.getString("username"),
                                    resultSet.getString("role"));
                            messages.addMessage(msg);
                        }

                        response.getWriter().println(json.toJson(messages));
                        response.setStatus(HttpServletResponse.SC_OK);

                        break;

                    case "login":
                        String login = request.getParameter("login");
                        String password = request.getParameter("password");
                        json = new GsonBuilder().setPrettyPrinting().create();
                        if(login == null || password == null){
                            JsonObject jsonObj = new JsonObject();
                            jsonObj.addProperty("error", "Login or password is empty.");
                            response.getWriter().println(json.toJson(jsonObj));
                            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            break;
                        }

                        statement = connection.createStatement();
                        Statement statement2 = connection.createStatement();
                        ResultSet countSet = statement.executeQuery("SELECT Count(*) as count FROM `user` WHERE user.username LIKE '" + login + "'");
                        countSet.next();
                        resultSet = statement2.executeQuery("SELECT user_id, username, role.role_name, hash FROM `user` " +
                                "INNER JOIN `role` ON role.role_id = user.role  WHERE user.username LIKE '" + login + "'");

                        if(countSet.getInt("count") == 0){
                            JsonObject jsonObj = new JsonObject();
                            jsonObj.addProperty("error", "Login is not found.");
                            response.getWriter().println(json.toJson(jsonObj));
                            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            break;
                        }

                        resultSet.next();
                        User user = new User(
                                resultSet.getInt("user_id"),
                                resultSet.getString("username"),
                                resultSet.getString("role_name"));

                        if(!HashUtils.isValid(password, resultSet.getString("hash"))){
                            JsonObject jsonObj = new JsonObject();
                            jsonObj.addProperty("error", "Password is not valid.");
                            response.getWriter().println(json.toJson(jsonObj));
                            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            break;
                        }
                        Session session = SessionController.getInstance().getSession(user);
                        if(session == null){
                            SessionController.getInstance().addSession(user);
                            session = SessionController.getInstance().getSession(user);
                        }

                        response.getWriter().println(json.toJson(session));

                        break;
                }
            }
    } catch(Exception x)
    {
        x.printStackTrace();
    }
}
}


