package com.Unemployment.JournalApplication.repository;

import com.Unemployment.JournalApplication.entity.Users;

import java.util.List;

public interface UserRepository {
    List<Users> getUserForSA();
}
