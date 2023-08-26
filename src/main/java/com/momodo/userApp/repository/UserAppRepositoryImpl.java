package com.momodo.userApp.repository;

import com.momodo.userApp.domain.Tier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.momodo.userApp.domain.QUserApp.userApp;

@Repository
@RequiredArgsConstructor
public class UserAppRepositoryImpl implements UserAppRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public void resetAllUserTiers() {
        queryFactory.update(userApp)
                .set(userApp.tier, Tier.RED.name())
                .execute();
    }
}
