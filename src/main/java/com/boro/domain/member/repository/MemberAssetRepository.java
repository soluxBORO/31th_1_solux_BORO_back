package com.boro.domain.member.repository;

import com.boro.domain.member.entity.Asset;
import com.boro.domain.member.entity.Member;
import com.boro.domain.member.entity.MemberAsset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberAssetRepository extends JpaRepository<MemberAsset, Long> {
    List<MemberAsset> findByMember(Member member);
    Optional<MemberAsset>  findByMemberAndAsset(Member member, Asset asset);
}
