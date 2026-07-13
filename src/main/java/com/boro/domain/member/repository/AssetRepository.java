package com.boro.domain.member.repository;

import com.boro.domain.member.entity.Asset;
import com.boro.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, Long> {

    List<Asset>  findByMember(Member member);
}
