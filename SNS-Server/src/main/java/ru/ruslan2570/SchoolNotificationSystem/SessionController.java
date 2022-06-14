package ru.ruslan2570.SchoolNotificationSystem;

import ru.ruslan2570.SchoolNotificationSystem.models.Session;
import ru.ruslan2570.SchoolNotificationSystem.models.User;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Random;

// Класс, отвечающий за работу сессий
public class SessionController {

    // поле для хранения экземляра класса
    private static SessionController sessionController;

    // Хэш-карта для хранения сессий
    private final Map<String, Session> sessionMap = new HashMap<>();

    private SessionController(){}

    /*
     * Метод для получения экземляра из поля sessionController
     * Нужен для реализации паттерна singleton
     */
    public static SessionController getInstance(){
        if(sessionController == null)
            sessionController = new SessionController();
        return sessionController;
    }

    // Получение id сессии по экземляру пользователя
    public String getSessionId(User user){
        for(Map.Entry<String, Session> entry: sessionMap.entrySet()){
            String key = entry.getKey();
            Session session = entry.getValue();
            if(user.userId == session.user.userId){
                session.remainingTime+= 5;
                return key;
            }
        }
        return null;
    }

    // Получение экземпляра сессии по экземляру пользователя
    public Session getSession(User user){
        for(Map.Entry<String, Session> entry: sessionMap.entrySet()){
            String key = entry.getKey();
            Session session = entry.getValue();
            if(user.userId == session.user.userId){
                session.remainingTime+= 5;
                return session;
            }
        }
        return null;
    }

    // Получении экземпляра сессии по id сессии
    public Session getSession(String sessionId){
        for(Map.Entry<String, Session> entry: sessionMap.entrySet()){
            String key = entry.getKey();
            Session session = entry.getValue();
            if(session.id.equals(sessionId)){
                session.remainingTime+= 5;
                return session;
            }
        }
        return null;
    }

    /*
     * Метод для уменьшения времени сессии на одну минуту
     * Используется в main
     */
    public void subOneMinute(){
        for(Session session: sessionMap.values()){
            session.remainingTime--;
        }
    }

    /*
     * Метод для удаления истекших сессий
     * Используется в main
     */
    public void deleteExpiredSessions(){
        ArrayList<String> keys = new ArrayList<>();
        for(Map.Entry<String, Session> entry: sessionMap.entrySet()){
            String key = entry.getKey();
            Session session = entry.getValue();
            if(session.remainingTime == 0){
                keys.add(key);
            }
        }
        for(String key: keys){
            sessionMap.remove(key);
        }
    }

    // Добавление сессии при авторизации
    public void addSession(User user){
        if(getSessionId(user) == null){
            String id = generateId();
            Session session = new Session(id, user, 10);
            sessionMap.put(id, session);
        }
    }

    // Метод генерации id сессии
    private static String generateId(){
        return HashUtils.getHash(sessionController.sessionMap.toString() + new Random().nextInt());
    }
}
