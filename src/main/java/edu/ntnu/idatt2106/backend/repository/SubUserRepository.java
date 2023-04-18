package edu.ntnu.idatt2106.backend.repository;

import edu.ntnu.idatt2106.backend.model.user.SubUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubUserRepository extends JpaRepository<SubUser, Long> {
}
