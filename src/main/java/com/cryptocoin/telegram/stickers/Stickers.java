package com.cryptocoin.telegram.stickers;

import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;

/**
 * Created by Alexey Podlubnyy on 19.02.2022
 */
public enum Stickers {
    JAM_HELLO(new InputFile(new File("StickerFiles/HelloSticker.tgs"))),
    JAM_SLEEP(new InputFile(new File("StickerFiles/SleepSticker.tgs"))),
    JAM_SPY(new InputFile(new File("StickerFiles/SpySticker.tgs"))),
    JAM_CUNNING(new InputFile(new File("StickerFiles/СunningSticker.tgs"))),
    JAM_SADLY(new InputFile(new File("StickerFiles/SadlySticker.tgs"))),
    JAM_RIOTOUS(new InputFile(new File("StickerFiles/RiotousSticker.tgs"))),
    JAM_WINK(new InputFile(new File("StickerFiles/WinkSticker.tgs"))),
    JAM_HAPPY(new InputFile(new File("StickerFiles/HappySticker.tgs")));

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
