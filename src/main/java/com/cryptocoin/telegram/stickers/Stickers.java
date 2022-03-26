package com.cryptocoin.telegram.stickers;

import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;

/**
 * Created by Alexey Podlubnyy on 19.02.2022
 */
public enum Stickers {
    JAM_HELLO(new InputFile(new File("/opt/HelloSticker.tgs"))),
    JAM_SLEEP(new InputFile(new File("/opt/SleepSticker.tgs"))),
    JAM_SPY(new InputFile(new File("/opt/SpySticker.tgs"))),
    JAM_CUNNING(new InputFile(new File("/opt/СunningSticker.tgs"))),
    JAM_SADLY(new InputFile(new File("/opt/SadlySticker.tgs"))),
    JAM_RIOTOUS(new InputFile(new File("/opt/RiotousSticker.tgs"))),
    JAM_WINK(new InputFile(new File("/opt/WinkSticker.tgs"))),
    JAM_HAPPY(new InputFile(new File("/opt/HappySticker.tgs")));

    InputFile stickerId;

    Stickers(InputFile stickerId) {
        this.stickerId = stickerId;
    }

    public SendSticker getSendSticker(String chatId) {
        if ("".equals(chatId)) throw new IllegalArgumentException("ChatId cant be null");
        SendSticker sendSticker = getSendSticker();
        sendSticker.setChatId(chatId);
        return sendSticker;
    }

    public SendSticker getSendSticker() {
        SendSticker sendSticker = new SendSticker();
        sendSticker.setSticker(stickerId);
        return sendSticker;
    }
}
