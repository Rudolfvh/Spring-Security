package org.example.database.repository;

import org.example.database.entity.User;
import org.example.dto.UserFilter;

import java.util.List;

public interface FilterUserRepository {

    List<User> findAllByFilter(UserFilter filter);

}
