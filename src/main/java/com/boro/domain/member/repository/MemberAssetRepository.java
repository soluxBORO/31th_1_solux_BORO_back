package com.boro.domain.member.repository;

import com.boro.domain.member.entity.MemberAsset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberAssetRepository extends JpaRepository<MemberAsset, Long> {
}
