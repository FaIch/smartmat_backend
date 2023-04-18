package edu.ntnu.idatt2106.backend.repository;

import edu.ntnu.idatt2106.backend.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}
