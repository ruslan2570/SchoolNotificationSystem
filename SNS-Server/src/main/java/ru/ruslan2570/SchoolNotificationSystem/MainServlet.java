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

        // Консольный вывод запроса
        System.out.println(request.getRequestURI() + request.getParameterMap());


        String act = request.getParameter("action");
        JsonObject jsonObj;

        Connection connection = null;
        try {

            // Создание поключения к БД
            connection = DriverManager.
                    getConnection(url, user, password);

            // Если action не задан
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
            } else {
                response.setContentType("application/json;charset=utf-8");
                String sessionId = request.getParameter("session_id");
                Statement statement;
                ResultSet resultSet;
                Gson json;

                switch (act) {
                    case "getUsers":
                        json = new GsonBuilder().setPrettyPrinting().create();

                        if (sessionId == null || SessionController.getInstance().getSession(sessionId) == null) {
                            generateError(response, json, "Bad session.");
                            break;
                        }

                        if (!SessionController.getInstance().getSession(sessionId).user.roleName.equals("Администратор")) {
                            generateError(response, json, "Access denied.");
                            break;
                        }

                        statement = connection.createStatement();
                        resultSet = statement.executeQuery("SELECT user_id, username, role.role_name FROM `user` INNER JOIN `role` ON role.role_id = user.role");

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

                        json = new GsonBuilder().setPrettyPrinting().create();

                        if (sessionId == null || SessionController.getInstance().getSession(sessionId) == null) {
                            generateError(response, json, "Bad session.");
                            break;
                        }

                        statement = connection.createStatement();
                        resultSet = statement.executeQuery("SELECT message.message_id as msg_id, message.text as `text`, " +
                                "user.username as username, " +
                                "role.role_name as role" +
                                " FROM `message`, `user`, `role` " +
                                "WHERE message.user_id = user.user_id " +
                                "AND user.role = role.role_id");

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

                    case "getRoles":

                        json = new GsonBuilder().setPrettyPrinting().create();

                        if (sessionId == null || SessionController.getInstance().getSession(sessionId) == null) {
                            generateError(response, json, "Bad session.");
                            break;
                        }

                        if (!SessionController.getInstance().getSession(sessionId).user.roleName.equals("Администратор")) {
                            generateError(response, json, "Access denied.");
                            break;
                        }

                        statement = connection.createStatement();
                        resultSet = statement.executeQuery("SELECT * FROM `role`");

                        RoleList roles = new RoleList();

                        while (resultSet.next()) {
                            Role role = new Role(
                                    resultSet.getInt("role_id"),
                                    resultSet.getString("role_name"));
                            roles.addRole(role);
                        }

                        response.getWriter().println(json.toJson(roles));
                        response.setStatus(HttpServletResponse.SC_OK);

                        break;

                    case "sendMessage":

                        String messageText = request.getParameter("text");

                        json = new GsonBuilder().setPrettyPrinting().create();
                        if (sessionId == null || SessionController.getInstance().getSession(sessionId) == null) {
                            generateError(response, json, "Bad session.");
                            break;
                        }

                        if (SessionController.getInstance().getSession(sessionId).user.roleName.equals("Ученик")) {
                            generateError(response, json, "Access denied.");
                            break;
                        }

                        if (messageText == null) {
                            generateError(response, json, "Message is empty");
                            break;
                        }

                        statement = connection.createStatement();
                        statement.executeUpdate("INSERT INTO `message`(`message_id`, `text`, `user_id`) VALUES(NULL, '"
                                + messageText + "', " + "'" + SessionController.getInstance().getSession(sessionId).user.userId + "')");

                        jsonObj = new JsonObject();
                        jsonObj.addProperty("success", "The message is send.");
                        response.getWriter().println(json.toJson(jsonObj));
                        response.setStatus(HttpServletResponse.SC_OK);
                        break;

                    case "delMessage":
                        String messageId = request.getParameter("message_id");

                        json = new GsonBuilder().setPrettyPrinting().create();
                        if (sessionId == null || SessionController.getInstance().getSession(sessionId) == null) {
                            generateError(response, json, "Bad session.");
                            break;
                        }

                        if (!SessionController.getInstance().getSession(sessionId).user.roleName.equals("Администратор")) {
                            generateError(response, json, "Access denied.");
                            break;
                        }

                        if (messageId == null) {
                            generateError(response, json, "MessageId is not specified");
                            break;
                        }

                        statement = connection.createStatement();
                        statement.executeUpdate("DELETE FROM `message` WHERE `message_id` = '" + messageId + "'");

                        jsonObj = new JsonObject();
                        jsonObj.addProperty("success", "The message was deleted.");
                        response.getWriter().println(json.toJson(jsonObj));
                        response.setStatus(HttpServletResponse.SC_OK);
                        break;


                    case "addUser":

                        String addUsername = request.getParameter("username");
                        String addPassword = request.getParameter("password");
                        String addRoleId = request.getParameter("role_id");

                        json = new GsonBuilder().setPrettyPrinting().create();

                        if (sessionId == null || SessionController.getInstance().getSession(sessionId) == null) {
                            generateError(response, json, "Bad session.");
                            break;
                        }
                        if (!SessionController.getInstance().getSession(sessionId).user.roleName.equals("Администратор")) {
                            generateError(response, json, "Access denied.");
                            break;
                        }
                        if (addUsername == null | addPassword == null | addRoleId == null) {
                            generateError(response, json, "incomplete data");
                            break;
                        }

                        statement = connection.createStatement();
                        String hash = HashUtils.getHash(addPassword);

                        statement.executeUpdate("INSERT INTO `user`(`user_id`, `username`, `hash`, `role`) VALUES(NULL, '" + addUsername +
                                "', '" + hash + "', '" + addRoleId + "')");

                        jsonObj = new JsonObject();
                        jsonObj.addProperty("success", "User was added.");
                        response.getWriter().println(json.toJson(jsonObj));
                        response.setStatus(HttpServletResponse.SC_OK);

                        break;

                    case "delUser":

                        String delUserId = request.getParameter("user_id");

                        json = new GsonBuilder().setPrettyPrinting().create();

                        if (sessionId == null || SessionController.getInstance().getSession(sessionId) == null) {
                            generateError(response, json, "Bad session.");
                            break;
                        }
                        if (!SessionController.getInstance().getSession(sessionId).user.roleName.equals("Администратор")) {
                            generateError(response, json, "Access denied.");
                            break;
                        }
                        if (delUserId == null) {
                            generateError(response, json, "user_id cannot be empty.");
                            break;
                        }

                        statement = connection.createStatement();
                        statement.executeUpdate("DELETE FROM `user` WHERE `user`.`user_id` = '" + delUserId + "'");

                        jsonObj = new JsonObject();
                        jsonObj.addProperty("success", "User was deleted.");
                        response.getWriter().println(json.toJson(jsonObj));
                        response.setStatus(HttpServletResponse.SC_OK);

                        break;

                    case "login":
                        String login = request.getParameter("login");
                        String password = request.getParameter("password");
                        json = new GsonBuilder().setPrettyPrinting().create();
                        if (login == null || password == null) {
                            generateError(response, json, "Login or password is empty.");
                            break;
                        }

                        statement = connection.createStatement();
                        Statement statement2 = connection.createStatement();
                        ResultSet countSet = statement.executeQuery("SELECT Count(*) as count FROM `user` WHERE user.username LIKE '" + login + "'");
                        countSet.next();
                        resultSet = statement2.executeQuery("SELECT user_id, username, role.role_name, hash FROM `user` " +
                                "INNER JOIN `role` ON role.role_id = user.role  WHERE user.username LIKE '" + login + "'");

                        if (countSet.getInt("count") == 0) {
                            generateError(response, json, "Login is not found.");
                            break;
                        }

                        resultSet.next();
                        User user = new User(
                                resultSet.getInt("user_id"),
                                resultSet.getString("username"),
                                resultSet.getString("role_name"));

                        if (!HashUtils.isValid(password, resultSet.getString("hash"))) {
                            generateError(response, json, "Password is not valid.");
                            break;
                        }

                        SessionController.getInstance().addSession(user);
                        Session session = SessionController.getInstance().getSession(user);

                        response.getWriter().println(json.toJson(session));

                        break;
                }
            }
        } catch (Exception x) {
            x.printStackTrace();
        } finally {
            try {
                assert connection != null;
                connection.close();
            } catch (Exception x) {
                x.printStackTrace();
            }
        }

    }

    // Метод генерации ошибок
    private void generateError(HttpServletResponse response, Gson json, String error) throws Exception {
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("error", error);
        response.getWriter().println(json.toJson(jsonObj));
        response.setStatus(HttpServletResponse.SC_OK);
    }
}