package jpabook.jpashop.dto;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class MemberResponse {

    private Long id;

    private String name;

    private Address address;

    @Builder
    private MemberResponse(Long id, String name, Address address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public static MemberResponse of(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .address(member.getAddress())
                .build();
    }
}
