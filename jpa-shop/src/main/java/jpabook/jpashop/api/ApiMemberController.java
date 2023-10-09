package jpabook.jpashop.api;

import jpabook.jpashop.dto.MemberRequest;
import jpabook.jpashop.dto.MemberResponse;
import jpabook.jpashop.service.MemberService;
import lombok.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ApiMemberController {

    private final MemberService memberService;

    @GetMapping("/members")
    public ApiResponse<MemberDataResponse> members() {
        List<MemberResponse> findMembers = memberService.findMembers();

        return ApiResponse.of(new MemberDataResponse(findMembers.size(), findMembers));
    }

    @PostMapping("/members")
    public ApiResponse<Long> saveMemberV1(@Valid @RequestBody MemberRequest memberRequest) {
        Long savedId = memberService.join(memberRequest.toMemberDto());

        return ApiResponse.of(savedId);
    }

    @PutMapping("/members/{id}")
    public ApiResponse<MemberEditResponse> updateMember(
            @PathVariable("id") Long id,
            @Valid @RequestBody MemberEditRequest request
    ) {
        memberService.update(id, request.getName());
        MemberResponse findMember = memberService.findMember(id);

        return ApiResponse.of(MemberEditResponse.of(findMember.getId(), findMember.getName()));
    }

    @Getter
    @Setter
    @AllArgsConstructor
    static class MemberDataResponse {
        private int count;
        private List<MemberResponse> list;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    static class MemberEditRequest {
        @NotEmpty(message = "회원 이름을 입력하세요.")
        private String name;
    }

    @Getter
    @Setter
    static class MemberEditResponse {
        private Long id;
        private String name;

        private MemberEditResponse(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public static MemberEditResponse of(Long id, String name) {
            return new MemberEditResponse(id, name);
        }
    }
}
