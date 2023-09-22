package com.sulleong.recommend.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sulleong.member.QMember;
import com.sulleong.preference.QPreference;
import com.sulleong.recommend.QRecommend;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RecommendRepositoryImpl implements RecommendRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QRecommend qRecommend = QRecommend.recommend;
    private final QPreference qPreference = QPreference.preference;
    private final QMember qMember = QMember.member;

    @Override
    public List<Long> recommendBeersByMyFavoriteBeers(List<Long> beerIds) {
        return queryFactory
                .select(qRecommend.beer2)
                .from(qRecommend)
                .where(qRecommend.beer1.in(beerIds))
                .where(qRecommend.beer2.notIn(beerIds))
                .orderBy(qRecommend.distance.asc())
                .limit(4)
                .fetch();
    }

    @Override
    public List<Long> recommendBeersByFavoriteBeers() {
        return queryFactory
                .select(qPreference.beer.id)
                .from(qPreference)
                .groupBy(qPreference.beer.id)
                .orderBy(qPreference.count().desc())
                .limit(10)
                .fetch();
    }

    @Override
    public List<Long> recommendBeersByAgeAndGender(Integer age, String gender) {
        return queryFactory
                .select(qPreference.beer.id)
                .from(qPreference)
                .join(qMember)
                .on(qPreference.member.id.eq(qMember.id))
                .where(age != null ? qMember.age.eq(age) : null)
                .where(gender != null ? qMember.gender.eq(gender) : null)
                .groupBy(qPreference.beer.id)
                .orderBy(qPreference.count().desc())
                .limit(10)
                .fetch();
    }

}
