package com.boro.domain.auth.repository;

import com.boro.domain.auth.entity.Social;
import com.boro.domain.member.entity.Member;
import com.boro.domain.member.entity.enums.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocialRepository extends JpaRepository<Social, Long> {

    Optional<Social> findBySocialTypeAndProviderId(
            SocialType socialType,
            String providerId
    );

    void deleteByMember(Member member);
}
