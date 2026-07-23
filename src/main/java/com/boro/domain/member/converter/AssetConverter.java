package com.boro.domain.member.converter;

import com.boro.domain.member.dto.response.MemberResponseDTO;
import com.boro.domain.member.entity.Asset;
import com.boro.domain.member.entity.Member;
import com.boro.domain.member.entity.MemberAsset;

import java.util.List;

public class AssetConverter {

    public static List<MemberResponseDTO.StoreAsset> toStoreAssetList(List<Asset> assetList) {
        return assetList.stream()
                .map(AssetConverter::toStoreAsset)
                .toList();
    }

    public static MemberResponseDTO.StoreAsset toStoreAsset(Asset asset) {
        return MemberResponseDTO.StoreAsset.builder()
                .itemId(asset.getId())
                .itemName(asset.getName())
                .itemCategory(asset.getAssetCategory())
                .itemPrice(asset.getPrice())
                .build();
    }

    public static MemberAsset toMemberAsset(Member member, Asset asset) {
        return MemberAsset.builder()
                .member(member)
                .asset(asset)
                .build();
    }

    public static MemberResponseDTO.CreatedAsset toCreatedAsset(Asset asset) {
        return MemberResponseDTO.CreatedAsset.builder()
                .assetId(asset.getId())
                .build();
    }

}
