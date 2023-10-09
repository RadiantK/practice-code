package jpabook.jpashop.dto;

import jpabook.jpashop.domain.Address;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class MemberRequest {

    @NotEmpty(message = "회원 이름은 필수 입니다.")
    private String name;

    private String zipcode;
    private String city;
    private String street;

    @Builder
    private MemberRequest(@NotEmpty String name, String zipcode, String city, String street) {
        this.name = name;
        this.zipcode = zipcode;
        this.city = city;
        this.street = street;
    }

    public MemberDto toMemberDto() {
        return MemberDto.builder()
                .name(name)
                .address(new Address(zipcode, city, street))
                .build();
    }
}
