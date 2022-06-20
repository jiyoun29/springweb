package ezenweb.controller;

import ezenweb.Dto.BoardDto;
import ezenweb.Service.BoardService;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private HttpServletRequest request;
       //1. 세션 호출을 위한 request 객체 생성

    @Autowired
    private BoardService boardService;
        //2. 서비스 호출을 위한 boardService 객체 생성

    /////////////////view 열기[템플릿 열기] 매핑//////////////////
    //1. 게시판 페이지 열기
    @GetMapping("/list") //페이지 여는 것들 : get
    public String list(){
        return "board/list";
    }

//    int selectbno = 0; // view 메소드와 getboard 메소드에서 사용된 변수

    //2. 게시판 개별 조회 페이지
    @GetMapping("/view/{bno}") //url 경로에 변수 = path경로.  {변수명}
    public String view(@PathVariable("bno") int bno ) { //경로의 값을 빼옴 PathVariable("변수명")
        //{} 안에서 선언된 변수는 밖에서 사용불가
        
//               HttpServletResponse response , Model model){ //경로의 값을 빼옴 PathVariable("변수명")
//        System.out.println("선택한 게시물 번호"+bno);
        //Model 인터페이스 : Controller -> HTML 데이터 전송
        // model.addAttribute("data", boardService.getboard(bno));

//        try {response.setCharacterEncoding("UTF-8");
//            response.setContentType("application/json");
//            response.getWriter().print(boardService.getboard(bno));
//        } catch (Exception e) {System.out.println(e);}

        request.getSession().setAttribute("bno", bno);

//        selectbno = bno;
      return "board/view"; //템플릿으로 ajax 통신
    }


    //3. 게시물 수정 페이지
    @GetMapping("/update")
    public String update(){
        return "board/update";
    }
    
    //4. 게시물 쓰기 페이지
    @GetMapping("/save")
    public String save(){
        return "board/save";
    }

    /////////////////service 호출(처리) 매핑//////////////////
    //1. C : 게시물 저장 메소드
    @PostMapping("/save") //스크립트로 한다는 가정 하
    @ResponseBody //템플릿 아닌 객체 반환
    public boolean save(BoardDto boardDto){
        //테스트
//        boardDto.setBtitle("안녕"); boardDto.setBcontent("하하하");
        
        return boardService.save(boardDto);
        //보드 서비스에서 디티오를 꺼내고 결과물을 리턴
    }

    //2. R 모든 게시물 출력 처리 메소드
    @GetMapping("/getboardlist")
    public void getboardlist(HttpServletResponse response){
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().println(boardService.getboardlist());
        } catch (Exception e) {System.out.println(e);}
    }



    //2. 개별 조회 출력 메소드
    @GetMapping("/getboard")
    public void getboard(HttpServletResponse response){

        int bno =  (Integer) request.getSession().getAttribute("bno");
        try {response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print(boardService.getboard(  bno   ));
        } catch (Exception e) {System.out.println(e);}

    }

    //3. U
    @PutMapping("/update")
    @ResponseBody
    public boolean update(BoardDto boardDto){
        int bno = (Integer) request.getSession().getAttribute("bno");
        boardDto.setBno(bno); //bno을 가져옴(내가 보고 있는 게시물의 번호)
        return boardService.update(boardDto);
    }

    //4. D
    @DeleteMapping("/delete")
    @ResponseBody
    public boolean delete(@RequestParam("bno") int bno){
        return boardService.delete(bno);
    }

}

/////////////////////////
/*
    url 변수 이동
        1. <a href="URL/데이터"> <a>
            @Getmapping("/view/{변수명}")
            @PathVariable("bno")
        2. ajax : url : "/board/view/"+bno;

* */
