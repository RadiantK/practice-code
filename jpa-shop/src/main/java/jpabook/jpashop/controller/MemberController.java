package jpabook.jpashop.controller;

import jpabook.jpashop.dto.MemberRequest;
import jpabook.jpashop.dto.MemberResponse;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("memberRequest", new MemberRequest());

        return "members/createMemberForm";
    }

    @PostMapping("/new")
    public String create(@Valid MemberRequest memberRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "members/createMemberForm";
        }

        memberService.join(memberRequest.toMemberDto());

        return "redirect:/";
    }

    @GetMapping
    public String list(Model model) {
        List<MemberResponse> members = memberService.findMembers();
        model.addAttribute("members", members);

        return "members/memberList";
    }
}
