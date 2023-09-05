package yoon.cloud.board.gatewayService.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yoon.cloud.board.gatewayService.jwt.JwtUtils;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class MainController {

    private final JwtUtils jwtUtils;

    @PostMapping("/")
    public String getToken(Map<String, Object> map, String subject){
        return jwtUtils.createToken(map, subject);
    }
}
