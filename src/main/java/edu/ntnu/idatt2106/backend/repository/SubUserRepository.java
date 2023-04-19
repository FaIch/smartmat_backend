package edu.ntnu.idatt2106.backend.repository;

import edu.ntnu.idatt2106.backend.model.user.SubUser;
import edu.ntnu.idatt2106.backend.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubUserRepository extends JpaRepository<SubUser, Long> {
    List<SubUser> findSubUserByMainUser(User mainUser);
}
