package com.cryptocoin.telegram.repository;

import com.cryptocoin.telegram.model.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Alexey Podlubnyy on 03.03.2022
 */
public interface UserRepository extends JpaRepository<TelegramUser, Long> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE TelegramUser set repeatStatus = ?1 WHERE chatId = ?2")
    void setFixedRepeatStatusFor(Boolean repeatStatus, Long chatId);


    @Query("SELECT chatId FROM TelegramUser WHERE repeatStatus = true")
    List<Long> findByRepeatStatus();

    @Query("SELECT repeatStatus FROM TelegramUser WHERE chatId = ?1")
    Boolean findRepeatStatusIdByChatId(Long chatId);

    TelegramUser findUserByChatId(Long chatId);
//    needs to be completed
//    @Query("SELECT u.chatId FROM User u JOIN u.coin")
//    List<String> findAllUser();
}
