package com.cabin.ter.controller;

import com.cabin.ter.util.AsserUtil;
import com.cabin.ter.util.TemporaryLinkUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ThymeleafPageRendering {
    @GetMapping("/emailBindingPage")
    public String showTestPage(@RequestParam("token") String token,
                               @RequestParam("openId") String openId,
                               Model model) {
        boolean tokenExpired = TemporaryLinkUtil.isTokenExpired(token);
        AsserUtil.isTrue(tokenExpired, "链接已失效");
        model.addAttribute("openId", openId);
        return "EmailBinding";
    }
}
