package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;

@Service
public class SenderMessageService {

    private final TelegramBot telegramBot;

    public SenderMessageService(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    // метод по отправке исходящих сообщений
    public void sendMessage(Long idChat, String textMessage) {
        SendMessage message = new SendMessage(idChat, textMessage);
        telegramBot.execute(message);
    }
}
