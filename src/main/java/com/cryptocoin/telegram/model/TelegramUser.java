package com.cryptocoin.telegram.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.Id;

/**
 * Created by Alexey Podlubnyy on 03.03.2022
 */
@Getter
@Setter
@Table(name = "telegram_users")
@Entity
public class TelegramUser {

    @Id
    private Long chatId;

//    needs to be completed
//    @OneToMany(mappedBy = "user")
//    private Set<Coin> coin;

    private String firstName;
    private String lastName;
    private String userName;
    private boolean repeatStatus;

    public TelegramUser(Long chatId, String firstName, String lastName, String userName, boolean repeatStatus) {
        this.chatId = chatId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.repeatStatus = repeatStatus;
    }

    public TelegramUser() {

    }


}
