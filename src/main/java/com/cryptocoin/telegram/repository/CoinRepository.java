package com.cryptocoin.telegram.repository;

import com.cryptocoin.telegram.model.Coin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CoinRepository extends JpaRepository<Coin, Long> {

    //    @Query("SELECT CHAT_ID, TOP_COIN FROM TOPCOUINSUSER WHERE CHAT_ID = :chatId AND TOP_COIN = :topCoin")
    Coin findByChatIdAndCoin(Long chatId, String coin);

    List<Coin> findByChatId(Long chatId);

    @Query("SELECT chatId FROM Coin WHERE chatId = ?1 AND coin = ?2")
    Long findChatIdByChatIdAndCoin(Long chatId, String coin);

    @Query("SELECT DISTINCT coin FROM Coin")
    List<String> findCoinDistinctBy();

    //    @Transactional
//    @Query("DELETE FROM TOPCOUINSUSER WHERE CHAT_ID = :chatId AND TOP_COIN = :topCoin")
    void deleteByChatIdAndCoin(Long chatId, String coin);

    List<Coin> findCoinBy();

}

