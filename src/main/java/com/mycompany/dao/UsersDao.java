
package com.mycompany.dao;
import com.mycompany.models.User;

import java.util.List;

public interface UsersDao extends CrudDao<User> {
    List<User> findAllByFirstName(String firstName);
}