package jpabook.jpashop.dto;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberDto {

    private String name;

    private Address address;

    @Builder
    public MemberDto(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    public Member toEntity() {
        return Member.builder()
                .name(name)
                .address(address)
                .build();
    }
}
