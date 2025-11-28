package com.auth.repository;



import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.auth.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    //Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByEmail(String email);
   // Optional<UserEntity> findById(Long id);
   // List<UserEntity> findAll();
    
}

