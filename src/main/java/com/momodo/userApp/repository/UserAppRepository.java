package com.momodo.userApp.repository;


import com.momodo.userApp.domain.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAppRepository extends JpaRepository<UserApp, Long>, UserAppRepositoryCustom {

    Optional<UserApp> findByUserId(String userId);

    Optional<UserApp> findOneWithAuthoritiesByUserId(String userId);
}
