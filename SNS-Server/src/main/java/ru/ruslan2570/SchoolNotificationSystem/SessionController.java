package ru.ruslan2570.SchoolNotificationSystem;

import ru.ruslan2570.SchoolNotificationSystem.models.Session;
import ru.ruslan2570.SchoolNotificationSystem.models.User;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Random;

public class SessionController {

    private static SessionController sessionController;

    private final Map<String, Session> sessionMap = new HashMap<>();

    private SessionController(){}

    public static SessionController getInstance(){
        if(sessionController == null)
            sessionController = new SessionController();
        return sessionController;
    }

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

    public void subOneMinute(){
        for(Session session: sessionMap.values()){
            session.remainingTime--;
        }
    }

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

    public void addSession(User user){
        if(getSessionId(user) == null){
            String id = generateId();
            Session session = new Session(id, user, 10);
            sessionMap.put(id, session);
        }
    }

    private static String generateId(){
        return HashUtils.getHash(sessionController.toString() + new Random().nextInt());
    }
}
