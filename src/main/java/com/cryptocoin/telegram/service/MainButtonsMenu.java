package com.cryptocoin.telegram.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

import static com.cryptocoin.telegram.constants.Constants.*;

@Component
public class MainButtonsMenu {

    public ReplyKeyboardMarkup getMainMenuTopAndEnableRepeat() {
        final ReplyKeyboardMarkup replyKeyboardMarkup;
        replyKeyboardMarkup = ReplyKeyboardMarkup.builder()
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
//        KeyboardRow row3 = new KeyboardRow();
        row1.add(new KeyboardButton(TOP_COIN));
        row2.add(new KeyboardButton(ENABLE_REPEAT_STATUS));
//        row3.add(new KeyboardButton(HELP));
        keyboard.add(row1);
        keyboard.add(row2);
//        keyboard.add(row3);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup getMainMenuTopAndDisableRepeat() {
        final ReplyKeyboardMarkup replyKeyboardMarkup;
        replyKeyboardMarkup = ReplyKeyboardMarkup.builder()
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        row1.add(new KeyboardButton(TOP_COIN));
        row2.add(new KeyboardButton(DISABLE_REPEAT_STATUS));
//        row3.add(new KeyboardButton(HELP));
        keyboard.add(row1);
        keyboard.add(row2);
//        keyboard.add(row3);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }
}