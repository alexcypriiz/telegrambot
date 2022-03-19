package com.cryptocoin.telegram.service;

import com.cryptocoin.telegram.model.TelegramUser;
import com.cryptocoin.telegram.repository.UserRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Alexey Podlubnyy on 20.02.2022
 */
@Service
public class UserService {
    final
    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public TelegramUser findUser(Long id) {
        return userRepository.getById(id);
    }

    public TelegramUser saveUser(TelegramUser user) {
        return userRepository.save(user);
    }

//    needs to be completed
//    public List<String> findAllUser() {
//        return userRepository.findAllUser();
//    }

    @Modifying
    public void setFixedRepeatStatusFor(Boolean repeatStatus, Long userId) {
        userRepository.setFixedRepeatStatusFor(repeatStatus, userId);
    }

    public List<Long> findByRepeatStatus() {
        return userRepository.findByRepeatStatus();
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public Boolean findRepeatStatusIdByChatId(Long chatId) {
        return userRepository.findRepeatStatusIdByChatId(chatId);
    }

    public TelegramUser findUserByChatId(Long chatId) {
        return userRepository.findUserByChatId(chatId);
    }
}
