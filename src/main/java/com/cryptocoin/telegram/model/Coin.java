package com.cryptocoin.telegram.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.Id;
import java.util.Set;

@Getter
@Setter
@Table(name = "top_coins")
@Entity
public class Coin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //    needs to be completed
//    @ManyToOne
//    @JoinColumn(name = "user")
//    private User user;
    private Long chatId;
    private String coin;

    public Coin(Long chatId, String coin) {
        this.chatId = chatId;
        this.coin = coin;
    }

    public Coin() {

    }
}
