package com.cryptocoin.telegram.service;

import com.cryptocoin.telegram.model.Coin;
import com.cryptocoin.telegram.repository.CoinRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CoinService {

    final
    CoinRepository topCoinRepository;


    public CoinService(CoinRepository topCoinRepository) {
        this.topCoinRepository = topCoinRepository;
    }

    public Coin saveTopCoin(Coin topCoin) {
        return topCoinRepository.save(topCoin);
    }

    public List<Coin> findByChatId(Long chatId) {
        return topCoinRepository.findByChatId(chatId);
    }

    public List<String> findDistinctBy() {
        return topCoinRepository.findCoinDistinctBy();
    }

    public Coin findByUserIdAndChatIdAndCoin(Long chatId, String coin) {
        return topCoinRepository.findByChatIdAndCoin(chatId, coin);
    }

    public Long findChatIdByChatIdAndCoin(Long chatId, String coin) {
        return topCoinRepository.findChatIdByChatIdAndCoin(chatId, coin);
    }

    public void deleteById(Long id) {
        topCoinRepository.deleteById(id);
    }

    @Transactional
    public void deleteByChatIdAndCoin(Long chatId, String coin) {
        topCoinRepository.deleteByChatIdAndCoin(chatId, coin);
    }

    public List<Coin> findCoinBy() {
        return topCoinRepository.findCoinBy();
    }
}
