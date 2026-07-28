package com.boro.domain.member.converter;

import com.boro.domain.member.dto.response.MemberResponseDTO;
import com.boro.domain.member.entity.Asset;
import com.boro.domain.member.entity.Member;
import com.boro.domain.member.entity.MemberAsset;

import java.util.List;
import java.util.Set;

public class AssetConverter {

    public static List<MemberResponseDTO.StoreAsset> toStoreAssetList(
            List<Asset> assetList, Set<Long> ownedAssetIds
    ) {
        return assetList.stream()
                .map(asset -> AssetConverter.toStoreAsset(asset, ownedAssetIds.contains(asset.getId())))
                .toList();
    }

    public static MemberResponseDTO.StoreAsset toStoreAsset(Asset asset, boolean owned) {
        return MemberResponseDTO.StoreAsset.builder()
                .itemId(asset.getId())
                .itemName(asset.getName())
                .itemCategory(asset.getAssetCategory())
                .itemPrice(asset.getPrice())
                .owned(owned)
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
