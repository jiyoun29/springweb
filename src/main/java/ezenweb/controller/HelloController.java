package ezenweb.controller;

import ezenweb.Dto.HelloDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController //컨트롤러를 json을 반환하는 컨트롤러 설정 [타임리프 반환 x]
public class HelloController {
    @GetMapping("/hello")
    public HelloDto hello(){ //dto로 변경

        //Dto 생성
        HelloDto helloDto = HelloDto.builder()
            .name("유재석")
            .amount(10000)
            .build();

        return helloDto; //dto 반환
    
    }

}
