package ru.ruslan2570.SchoolNotificationSystem;

import com.sun.net.httpserver.HttpsServer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) {

        try {
            // Создание обработчика сервлетов и добавление сервлетов
            ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
            contextHandler.addServlet(new ServletHolder(new IndexServlet()), "/index.html");
            contextHandler.addServlet(new ServletHolder(new MainServlet(args[0], args[1], args[2])), "/request");

            // Создание сервера
            Server server = new Server(4242);
            server.setHandler(contextHandler);

            // Поток для уменьшения длительности сессий и удаления истекших сессий
            new Thread(() -> {
                try {
                    while(true) {
                        SessionController.getInstance().deleteExpiredSessions();
                        SessionController.getInstance().subOneMinute();
                        Thread.sleep(60000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

            // Запуск сервера
            server.start();
            server.join();

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
