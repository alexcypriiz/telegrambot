package com.cryptocoin.telegram.bot;

import com.cryptocoin.telegram.service.GeckoCoinService;
import com.cryptocoin.telegram.stickers.Stickers;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;

import java.util.*;

/**
 * Created by Alexey Podlubnyy on 19.02.2022
 */

@EnableScheduling
@Component
public class DaniilCryptoCoinBot extends TelegramLongPollingBot {
    @Value("${bot.name}")
    private String botUsername;

    @Value("${bot.token}")
    private String botToken;

    private Boolean button = false;

    private Boolean searchCoin = true;

    private Message message;

    final
    GeckoCoinService geckoCoinService;

    public DaniilCryptoCoinBot(GeckoCoinService geckoCoinService) {
        this.geckoCoinService = geckoCoinService;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            handleCallback(update.getCallbackQuery());
        } else if (update.hasMessage()) {
            handleMessage(update.getMessage());
        }

    }

    @SneakyThrows
    private void handleCallback(CallbackQuery callbackQuery) {
        execute(SendMessage.builder().text(callbackQuery.getData())
                .chatId(callbackQuery.getMessage().getChatId().toString())
                .build());
    }

    @SneakyThrows
    private void handleMessage(Message message) {
        if (message.hasText() && message.hasEntities()) {
            Optional<MessageEntity> commandEntity =
                    message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
            if (commandEntity.isPresent()) {
                String command = message.getText().substring(commandEntity.get().getOffset(), commandEntity.get().getLength());
                switch (command) {
                    case "/start":
                        execute(Stickers.JAM_HELLO.getSendSticker(message.getChatId().toString()));

//                        execute(SendMessage.builder().text("Chat id: " + message.getChatId().toString() + "\nFirst name: " + message.getFrom().getFirstName() + "\nLast name: " + message.getFrom().getLastName() + "\nUser name: " + message.getFrom().getUserName() + "\nUnique id"+ message.getFrom().getId())
//                                .chatId(message.getChatId().toString())
//                                .build());
                        execute(SendMessage.builder().text("Привет, " + message.getFrom().getUserName() + ". Я Данила, бот, который подскажет курс криптовалюты.")
                                .chatId(message.getChatId().toString())
                                .build());

                        execute(Stickers.JAM_SPY.getSendSticker(message.getChatId().toString()));

                        execute(SendMessage.builder().text("Я готов отслеживать выбранную тобой криптовалюту.\nБуду информировать тебя каждые 3 секунды об ее изменении.")
                                .chatId(message.getChatId().toString())
                                .build());

                        execute(SendMessage.builder().text("Какую выберем монету? Напиши ее название полностью в английской расскладке.")
                                .chatId(message.getChatId().toString())
                                .build());
                        return;
                    case "/help":
                        execute(SendMessage.builder().text("/start - запуск бота\n"
                                + "\n-После запуска бота введите название криптовалюты в английской раскладке. Например: bitcoin\n"
                                + "\n-Если бот не найдет монету, то перепроверьте правильность ввода и введите заново.\n"
                                + "\n-Чтобы остановить ряд сообщений о бирже, напишете stop\n"
                                + "\n-После остановки Вы так же можете продолжить шпионить для монетой, просто введите имя монеты.")
                                .chatId(message.getChatId().toString())
                                .build());
                        return;
//                    case "/maquette":
//                        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
//                        buttons.add(Arrays.asList(InlineKeyboardButton.builder().text("Кнопка для условия 1").callbackData("Я вернул фидбэк для кнопки №1").build(),
//                                InlineKeyboardButton.builder().text("Кнопка для условия 2").callbackData("Я вернул фидбэк для кнопки №2").build()));
//                        buttons.add(Arrays.asList(InlineKeyboardButton.builder().text("Кнопка для условия 3").callbackData("Я вернул фидбэк для кнопки №3").build(),
//                                InlineKeyboardButton.builder().text("Кнопка для условия 4").callbackData("Я вернул фидбэк для кнопки №4").build()));
//
//                        execute(SendMessage.builder().text("Выберете, пожалуйста, желаемый Вами шаблон.")
//                                .chatId(message.getChatId().toString())
//                                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
//                                .build());
//                        return;

                }
            }
        } else if (message.getText().toLowerCase().equals("стоп") | message.getText().toLowerCase().equals("stop")) {
            this.button = false;
            execute(Stickers.JAM_SLEEP.getSendSticker(message.getChatId().toString()));

            execute(SendMessage.builder().text("Хорошо, я отключил оповещение каждые 3 секунды.")
                    .chatId(message.getChatId().toString())
                    .build());
        } else {
            execute(Stickers.JAM_CUNNING.getSendSticker(message.getChatId().toString()));

            execute(SendMessage.builder().text("Хммм, а вот это интересненько. Сейчас поищем..")
                    .chatId(message.getChatId().toString())
                    .build());
            this.button = true;
            this.message = message;
            startSendMessage();
        }
    }

    @SneakyThrows
    @Scheduled(fixedDelay = 3000)
    public void startSendMessage() {
        if (!button) {
        } else if (button) {
            try {
                String coinOutPut = geckoCoinService.getCoin(message.getText().toLowerCase(Locale.ROOT));

                if (!searchCoin) {
                    execute(Stickers.JAM_RIOTOUS.getSendSticker(message.getChatId().toString()));

                    execute(SendMessage.builder().text("Нашел! \nДля отключения напиши \"stop\" без ковычек.")
                            .chatId(message.getChatId().toString())
                            .build());
                    this.searchCoin = true;
                }
                execute(SendMessage.builder().text(coinOutPut)
                        .chatId(message.getChatId().toString())
                        .build());
            } catch (ArrayIndexOutOfBoundsException e) {
                this.searchCoin = false;
                this.button = false;
                execute(Stickers.JAM_SADLY.getSendSticker(message.getChatId().toString()));

                execute(SendMessage.builder().text("Такой монеты нету( Перепроверь название и введи, пожалуйста, еще раз.")
                        .chatId(message.getChatId().toString())
                        .build());
            }
        }
    }


}
