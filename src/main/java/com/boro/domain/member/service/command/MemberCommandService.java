package com.boro.domain.member.service.command;

import com.boro.domain.member.converter.AssetConverter;
import com.boro.domain.member.converter.MemberConverter;
import com.boro.domain.member.dto.request.MemberRequestDTO;
import com.boro.domain.member.dto.response.MemberResponseDTO;
import com.boro.domain.member.entity.Asset;
import com.boro.domain.member.entity.Member;
import com.boro.domain.member.entity.MemberAsset;
import com.boro.domain.member.entity.PointHistory;
import com.boro.domain.member.entity.enums.PointReason;
import com.boro.domain.member.repository.AssetRepository;
import com.boro.domain.member.repository.MemberAssetRepository;
import com.boro.domain.member.repository.MemberRepository;
import com.boro.global.error.code.status.MemberErrorCode;
import com.boro.global.error.exception.handler.MemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberCommandService {

    private final MemberRepository memberRepository;
    private final AssetRepository assetRepository;
    private final MemberAssetRepository memberAssetRepository;

    public void changeMemberInfo(Long memberId, MemberRequestDTO.ChangeMemberInfo request){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        member.changeMemberInfo(request);
    }

    public void applyPoint(Long memberId, PointReason reason) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        member.applyPoint(reason.getPoint());
        PointHistory pointHistory = MemberConverter.toPointHistory(reason);
        member.addPointHistory(pointHistory);
        log.info("포인트 이벤트 발행 완료!");
    }

    public MemberResponseDTO.CreatedAsset purchaseStoreAssets(Long memberId, Long assetId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.ASSET_NOT_FOUND));

        int price = asset.getPrice();
        if (member.getPoint() < price){
            throw new MemberException(MemberErrorCode.INSUFFICIENT_POINT);
        }
        member.applyPoint(-price);
        PointHistory pointHistory = MemberConverter.toPointHistory(PointReason.ITEM_PURCHASE);
        member.addPointHistory(pointHistory);

        MemberAsset memberAsset = AssetConverter.toMemberAsset(member, asset);
        MemberAsset savedMemberAsset = memberAssetRepository.save(memberAsset);
        return AssetConverter.toCreatedAsset(savedMemberAsset.getAsset());
    }

}
