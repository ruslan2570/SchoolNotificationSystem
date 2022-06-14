package ru.ruslan2570.SchoolNotificationSystem;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// Страница index.html для проверки работы сервера
public class IndexServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws IOException {

        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println("<h1>The quick brown fox jumps over the lazy dog</h1>");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
