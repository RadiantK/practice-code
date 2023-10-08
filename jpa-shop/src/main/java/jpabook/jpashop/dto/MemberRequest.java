package jpabook.jpashop.dto;

import jpabook.jpashop.domain.Address;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@NoArgsConstructor
public class MemberRequest {

    @NotEmpty
    private String name;

    private Address address;

    @Builder
    public MemberRequest(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    public MemberDto toMemberDto() {
        return MemberDto.builder()
                .name(name)
                .address(address)
                .build();
    }
}
