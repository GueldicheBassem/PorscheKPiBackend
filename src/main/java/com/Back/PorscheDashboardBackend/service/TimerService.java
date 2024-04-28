package com.Back.PorscheDashboardBackend.service;

import com.Back.PorscheDashboardBackend.dao.HourlyKosuDao;
import com.Back.PorscheDashboardBackend.dao.UserDao;
import com.Back.PorscheDashboardBackend.entity.HourlyKosu;
import com.Back.PorscheDashboardBackend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Service
public class TimerService {
    @Autowired
    private EmailSenderService emailService;
    private Map<String, LocalDateTime> userTimers = new HashMap<>();
    @Autowired
    private UserDao userDao;
    public void startTimer(String username) {
        userTimers.put(username, LocalDateTime.now().plusMinutes(1));
    }
    @Autowired
    HourlyKosuDao hourlyKosuDao;

    public boolean isTimerExpired(String username) {
        LocalDateTime timerExpiration = userTimers.get(username);
        if (timerExpiration != null) {
            return LocalDateTime.now().isAfter(timerExpiration);
        }
        return false; // Timer expired if no timer is set
    }

    public void resetTimer(String username) {
        userTimers.put(username, LocalDateTime.now().plusMinutes(1));
    }

    public void penalizeUser(String username) {

        User user = userDao.findByUserName(username);
        user.setPenaltypoints(user.getPenaltypoints()+1);
        userDao.save(user);
        String superiorEmail = user.getSuperiorEmail();
        if (superiorEmail != null && !superiorEmail.isEmpty()) {
            String subject = "Escalation Kosu : "+user.getName()+" "+user.getLastname() ;
            String text = "L'utilisateur " + user.getName() +" "+user.getLastname()+" n'a pas rempli son entrée Kosu Horaire à temps et a été pénalisé en conséquence." +
                    " Veuillez prendre les mesures nécessaires.";
            emailService.sendEmail(superiorEmail, subject, text);
        }
    }

    public void logUserTimers() {

System.out.println("Checking list");
        for (Map.Entry<String, LocalDateTime> entry : userTimers.entrySet()) {
            System.out.println("Username: " + entry.getKey() + ", Expiration Time: " + entry.getValue());
        }
    }
    public LocalDateTime getExpirationTime(String username) {
        return userTimers.get(username);
    }


    public void clearUserTimers(){
        userTimers.clear();

    }
    @Scheduled(fixedRate = 1000) // Runs every minute
    public void checkExpiredTimers() {

        // Iterate through all users and penalize those with expired timers
        for (Map.Entry<String, LocalDateTime> entry : userTimers.entrySet()) {
            String username = entry.getKey();



            if (isTimerExpired(username)) {
                // Timer expired, penalize the user and reset the timer
                resetTimer(username);
                penalizeUser(username);
                System.out.println("User:" + entry.getKey()+"Has been penlized for missing his entry "+entry.getValue()+".");
            }
        }
    }

}
