package com.boro.domain.member.repository;

import com.boro.domain.member.entity.Member;
import com.boro.domain.member.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
    List<PointHistory> findByMemberOrderByCreatedAtDesc(Member member);
}
