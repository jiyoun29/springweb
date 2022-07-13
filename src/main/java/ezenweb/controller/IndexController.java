package ezenweb.controller;

import ezenweb.Dto.HelloDto;
import ezenweb.Service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
public class IndexController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/") // "/" -> 최상위 경로
    public String index(){ //model : view와 controll 사이를 이어주는 ui 인터페이스

        // 3. 부동산 시세 크롤링
        boardService.getvalue();
//        //dto 생성
//        HelloDto helloDto = HelloDto.builder()
//                .name("유재석")
//                .amount(10000)
//                .build();
//        //해당 템플릿에 데이터 보내기
//        model.addAttribute("data", helloDto);
//   //     return helloDto; //dto 반환

    return "main"; //메인 페이지를 만들어서 타임리프와 비교해서 보기
        //HTML 파일명
    }

    @GetMapping("/getweather")   // 1. 날씨 크롤링 메소드
    public void getweather( HttpServletResponse response ){
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print(   boardService.getweather()  );
        }catch( Exception e){}
    }

    @GetMapping("/getnews")
    public void getnews( HttpServletResponse response   ){
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print(   boardService.getnews()  );
        }catch( Exception e){}
    }
}

