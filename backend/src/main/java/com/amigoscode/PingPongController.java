package com.amigoscode;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingPongController {
    record PingPong(String result){}
    @GetMapping("/ping")
    public PingPong getPingPong(){
        return new PingPong("pong but no ping and pong and no ping test josie");
    }
}
