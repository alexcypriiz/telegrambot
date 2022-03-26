package com.cryptocoin.telegram.bot;

import com.cryptocoin.telegram.model.Coin;
import com.cryptocoin.telegram.model.TelegramUser;
import com.cryptocoin.telegram.service.GeckoCoinService;
import com.cryptocoin.telegram.service.MainButtonsMenu;
import com.cryptocoin.telegram.service.CoinService;
import com.cryptocoin.telegram.service.UserService;
import com.cryptocoin.telegram.stickers.Stickers;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.groupadministration.LeaveChat;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.*;
import java.util.stream.Collectors;

import static com.cryptocoin.telegram.constants.Constants.*;

/**
 * Created by Alexey Podlubnyy on 19.02.2022
 */

@EnableScheduling
@Component
public class JamCryptoCoinBot extends TelegramLongPollingBot {
    @Value("${bot.name}")
    private String botUsername;

    @Value("${bot.token}")
    private String botToken;

    final
    MainButtonsMenu mainButtonsMenu;

    final
    UserService userService;

    final
    CoinService topCoinService;

    final
    GeckoCoinService geckoCoinService;

    public JamCryptoCoinBot(GeckoCoinService geckoCoinService, UserService userService, MainButtonsMenu mainButtonsMenu, CoinService topCoinService) {
        this.geckoCoinService = geckoCoinService;
        this.userService = userService;
        this.mainButtonsMenu = mainButtonsMenu;
        this.topCoinService = topCoinService;
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
        String[] nameCoinText = callbackQuery.getMessage().getText().split("\\s");

        if (topCoinService.findByUserIdAndChatIdAndCoin(callbackQuery.getMessage().getChatId(), nameCoinText[0].toLowerCase(Locale.ROOT).trim()) == null) {
            topCoinService.saveTopCoin(new Coin(callbackQuery.getMessage().getChatId(), nameCoinText[0].toLowerCase(Locale.ROOT).trim()));
        } else {
            topCoinService.deleteByChatIdAndCoin(callbackQuery.getMessage().getChatId(), nameCoinText[0].toLowerCase(Locale.ROOT).trim());
        }
        execute(EditMessageReplyMarkup.builder()
                .chatId(callbackQuery.getMessage().getChatId().toString())
                .messageId(callbackQuery.getMessage().getMessageId())
                .replyMarkup(null)
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
                        if (userService.findUserByChatId(message.getChatId()) == null) {
                            //отвравка об регистрации новых пользователей разработчику бота
                            execute(SendMessage.builder().text("Новый пользователь:\nNickname: " + message.getFrom().getUserName()
                                            + "\nLast name: " + message.getFrom().getLastName()
                                            + "\nFirst name: " + message.getFrom().getFirstName()
                                            + "\nUser id: " + message.getFrom().getId()
                                            + "\nChat id: " + message.getChatId())
                                    .chatId("229847206")
                                    .build());

                            userService.saveUser(new TelegramUser(message.getChatId(), message.getFrom().getFirstName(), message.getFrom().getLastName(), message.getFrom().getUserName(), false));

                            execute(Stickers.JAM_HELLO.getSendSticker(message.getChatId().toString()));

                            execute(SendMessage.builder().text("Привет, " + message.getFrom().getFirstName() + ". Я Джемка, бот, который подскажет курс криптовалюты.")
                                    .chatId(message.getChatId().toString())
                                    .replyMarkup(mainButtonsMenu.getMainMenuTopAndEnableRepeat())
                                    .build());

                            execute(Stickers.JAM_SPY.getSendSticker(message.getChatId().toString()));

                            execute(SendMessage.builder().text("Я готов отслеживать избранные тобой монеты.\nБуду информировать тебя каждый час об их изменении.")
                                    .chatId(message.getChatId().toString())
                                    .build());

                            execute(SendMessage.builder().text("Какую выберем монету? Напиши ее название полностью в английской расскладке.")
                                    .chatId(message.getChatId().toString())
                                    .build());
                        } else {
                            execute(SendMessage.builder().text("Пользователь перезапустил бота:\nNickname: " + message.getFrom().getUserName()
                                            + "\nLast name: " + message.getFrom().getLastName()
                                            + "\nFirst name: " + message.getFrom().getFirstName()
                                            + "\nUser id: " + message.getFrom().getId()
                                            + "\nChat id: " + message.getChatId())
                                    .chatId("229847206")
                                    .build());

                            execute(Stickers.JAM_HAPPY.getSendSticker(message.getChatId().toString()));
                            execute(SendMessage.builder().text("Ураа! С возвращением!) Продолжим искать монету? Напиши ее название полностью в английской расскладке.")
                                    .replyMarkup(mainButtonsMenu.getMainMenuTopAndEnableRepeat())
                                    .chatId(message.getChatId().toString())
                                    .build());
                        }
                        return;

                    case "/help":
                        execute(SendMessage.builder().text("/start - запуск бота;\n"
                                        + "\n-После запуска бота введите название криптовалюты в английской раскладке. Например: bitcoin;\n"
                                        + "\n-Если бот не найдет монету, то перепроверьте правильность ввода и введите заново;\n"
                                        + "\n-Под статистикой найденной монеты выведена кнопка для добавления монеты в избранное;\n"
                                        + "\n-Чтобы убрать избранную монету, нужно повторно отправить в чат название монеты. Нажмите кнопку под выведенной статистикой монеты;\n"
                                        + "\n-В меню имеется кнопка для вывода избранных. При ее нажатии Вам будут отправлены Ваши избранные монеты;\n"
                                        + "\n-В меню имеется кнопка для рассылки избранных монет. При ее нажатии будет включена рассылка избранных монет каждый час;\n"
                                        + "\n-Для отключения рассылки, нажмите на кнопку в меню;\n"
                                        + "\nВопросы или предложения по улучшению: https://t.me/alexcypriiz, Алексей")
                                .chatId(message.getChatId().toString())
                                .build());
                        return;
                }
            }
        } else if (message.getText().equals(TOP_COIN)) {
            if (topCoinService.findByChatId(message.getChatId()) == null) {
                execute(Stickers.JAM_WINK.getSendSticker(message.getChatId().toString()));
                execute(SendMessage.builder().text("Пусто.. Я не нашел монеты в избранном. Может добавим?)" +
                                "\nСНОСКА: Пропишите название монеты в чат, если монета была найдена, то нажмите кнопку под текстом")
                        .chatId(message.getChatId().toString())
                        .build());

            } else {
                String allTopCoin = topCoinService.findByChatId(message.getChatId()).stream().map(s -> s.getCoin()).collect(Collectors.joining("\n"));
                execute(SendMessage.builder().text(allTopCoin)
                        .chatId(message.getChatId().toString())
                        .build());
            }
        } else if (message.getText().equals(ENABLE_REPEAT_STATUS)) {
            userService.setFixedRepeatStatusFor(true, message.getChatId());

            execute(Stickers.JAM_RIOTOUS.getSendSticker(message.getChatId().toString()));
            execute(SendMessage.builder().text("Уууу, да! Я буду оповещать каждый новый час о твоих избранных монетах.")
                    .chatId(message.getChatId().toString())
                    .replyMarkup(mainButtonsMenu.getMainMenuTopAndDisableRepeat())
                    .build());
        } else if (message.getText().equals(DISABLE_REPEAT_STATUS)) {
            userService.setFixedRepeatStatusFor(false, message.getChatId());

            execute(Stickers.JAM_SLEEP.getSendSticker(message.getChatId().toString()));
            execute(SendMessage.builder().text("Ладно, ладно.. Не буду я спамить тебе каждый новый час. Нооо.. ты знаешь, где меня найти)")
                    .chatId(message.getChatId().toString())
                    .replyMarkup(mainButtonsMenu.getMainMenuTopAndEnableRepeat())
                    .build());
        } else {
            try {
                execute(Stickers.JAM_CUNNING.getSendSticker(message.getChatId().toString()));

                execute(SendMessage.builder().text("Хммм, а вот это интересненько. Сейчас поищем..")
                        .chatId(message.getChatId().toString())
                        .build());
                try {
                    String coinOutPut = geckoCoinService.getCoin(message.getText().toLowerCase(Locale.ROOT));

                    if (topCoinService.findByUserIdAndChatIdAndCoin(message.getChatId(), message.getText().toLowerCase(Locale.ROOT)) == null) {
                        List<List<InlineKeyboardButton>> buttonAddCoin = new ArrayList<>();
                        buttonAddCoin.add(Arrays.asList(InlineKeyboardButton.builder().text("Добавить монету в избранное").callbackData("Хорошо, монетка отложена").build()));
                        execute(SendMessage.builder().text(coinOutPut)
                                .chatId(message.getChatId().toString())
                                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttonAddCoin).build())
                                .build());
                    } else {
                        List<List<InlineKeyboardButton>> buttonDeleteCoin = new ArrayList<>();
                        buttonDeleteCoin.add(Arrays.asList(InlineKeyboardButton.builder().text("Убрать монету из избранного").callbackData("Сделано, я забуду про нее").build()));
                        execute(SendMessage.builder().text(coinOutPut)
                                .chatId(message.getChatId().toString())
                                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttonDeleteCoin).build())
                                .build());
                    }

                } catch (ArrayIndexOutOfBoundsException e) {
                    execute(Stickers.JAM_SADLY.getSendSticker(message.getChatId().toString()));

                    execute(SendMessage.builder().text("Такой монеты нету( Перепроверь название и введи, пожалуйста, еще раз.")
                            .chatId(message.getChatId().toString())
                            .build());
                }
            }
            catch (TelegramApiRequestException e) {
                System.out.println("Группа: " + message.getChatId() + ", от \"" + message.getFrom().getFirstName()+ " " + message.getFrom().getLastName() + " " + message.getFrom().getUserName() + "\" не обработано сообщение, бот вышел из беседы");
                execute(LeaveChat.builder().chatId(message.getChatId().toString()).build());
            }
        }
    }

    public void sendMessageScheduled(Long chatId, String coin) {
        try {

            execute(SendMessage.builder().text(geckoCoinService.getCoinPriceAndRating(coin))
                    .chatId(String.valueOf(chatId))
                    .build());
        } catch (TelegramApiException e) {
            userService.setFixedRepeatStatusFor(false, chatId);
        }
    }

    @SneakyThrows
//    @Scheduled(cron = "0 * * * * ?") // тестовая строка
    @Scheduled(cron = "0 0 * * * ?")
    public void startSendMessage() {
        for (Coin coin : topCoinService.findCoinBy()) {
            if (userService.findRepeatStatusIdByChatId(coin.getChatId())) {
                sendMessageScheduled(coin.getChatId(), coin.getCoin());
            }
        }
    }
}