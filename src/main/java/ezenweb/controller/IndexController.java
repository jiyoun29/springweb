package ezenweb.controller;

import ezenweb.Dto.HelloDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/") // "/" -> 최상위 경로
    public String index(Model model){ //model : view와 controll 사이를 이어주는 ui 인터페이스

        //dto 생성
        HelloDto helloDto = HelloDto.builder()
                .name("유재석")
                .amount(10000)
                .build();

        //해당 템플릿에 데이터 보내기
        model.addAttribute("data", helloDto);

   //     return helloDto; //dto 반환

    return "main"; //메인 페이지를 만들어서 타임리프와 비교해서 보기
        //HTML 파일명
    }
}

