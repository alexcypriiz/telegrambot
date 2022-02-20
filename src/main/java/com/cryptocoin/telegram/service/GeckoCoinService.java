package com.cryptocoin.telegram.service;

import com.cryptocoin.telegram.model.Id;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Alexey Podlubnyy on 19.02.2022
 */
@Service
public class GeckoCoinService {
    private final RestTemplate restTemplate;

    public GeckoCoinService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public String getCoin(String nameCoin) {
        ResponseEntity<Id[]> coin = this.restTemplate.getForEntity(
                "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&ids=" + nameCoin, Id[].class);
        Id[] coins = coin.getBody();
        return coins[0].getName() + " (" + coins[0].getSymbol() + ")\n"
                + "Позиция в рейтинге: " + coins[0].getMarket_cap_rank() + "\n"
                + "Цена: " + coins[0].getCurrent_price() + " usd" + "\n"
                + "Капитализация: " + coins[0].getMarket_cap() + " usd" + "\n"
                ;
    }
}

