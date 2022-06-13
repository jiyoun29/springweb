package ezenweb.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController //컨트롤러를 json을 반환하는 컨트롤러 설정 [타임리프x]
public class HelloController {

    @GetMapping("/hello")
    public String hello(){  return "hello"; }

}
