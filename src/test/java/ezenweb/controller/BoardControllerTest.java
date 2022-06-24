package ezenweb.controller;

import ezenweb.Dto.LoginDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
//스프링 테스트를 위한 mockmvcrequest 메소드 호출
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc //mvc 테스트 중 c,s,m 까지 가능/ @WebMvcTest //mvc 테스트 중 C만 가능
public class BoardControllerTest { //public class TestController/ 테스트 할 이름 추가
    //프론트 view가 없다는 가정 하에 HTTP를 테스트 할 수 있는 메소드 = perform
    //.perform(http요청 메소드(URL)); -> 자바에서 url 확인 가능 : post,get,update,delete
    // 1.  perform().param("변수명" , "데이터")   :   데이터 전송
    // 2.   perform().session( 세션명 ) : 세션 전송
    // 2. MockHttpSession : 테스트 세션 클래스
    // 3. .andDo(print() )  : 테스트 결과를 console 출력
    //   Status = 200 성공 Status = 404 경로문제 Status = 5x 서버내부문제(java코드)
    // MockHttpServletRequest:
    // MockHttpServletResponse:


    @Autowired
    MockMvc mvc; //mvc 테스트에 사용 되는 클래스

    //1. 게시판 열기 테스트
    @Test
    void testlist() throws Exception { //예외처리 던지기
        mvc.perform(get("/board/list")).andDo(print()); } //.andExpect(status().isOk())

    //2. 게시판 개별 조회 페이지 테스트
    @Test
    void testview() throws Exception {
        mvc.perform(get("/board/view/1")).andDo(print()); } //.andExpect(status().isOk())

    //3. 게시판 수정 페이지 테스트
    @Test
    void testupdate() throws Exception {
        mvc.perform(get("/board/update")).andDo(print()); }

    //4. 게시판 쓰기 페이지 테스트
    @Test
    void testsave() throws Exception {
        mvc.perform(get("/board/save")).andDo(print());
    }

    /////////////////////////////////////////////////////////
    //변수 전달 테스트
        //http 요청 메소드("url").param("필드명",데이터)
    //세션 전달 테스트
        //mockHttpSession 클래스
        //http 요청 메소드("url")


    void testsaveService() throws Exception {

//        LoginDto loginDto = LoginDto.builder()
//            .mno(1)
//            .mid("qweqwe")
//            .mname("qweqwe")
//            .build();
//        MockHttpSession mockHttpSession = new MockHttpSession();
//        mockHttpSession.setAttribute("login",loginDto);


//        mvc.perform(post("/board/save")
//            .param("btitle","테스트 제목")
//            .param("bcontent","테스트 내용")
//            .param("category","자유게시판"))
//            .andDo(print());
    }


    //모든 게시물 호출 테스트
    @Test
    void testgetBoardlist() throws Exception {
        mvc.perform(get("/board/getboardlist")
            .param("cno","1")
            .param("key","")
            .param("keyword","")
            .param("page","0")
            ).andDo(print());
    }


    //게시물 검색 테스트
    @Test
    void getBoardlist() throws Exception {
        mvc.perform(get("/board/getboardlist")
            .param("cno","1")
            .param("key","btitle")
            .param("keyword","asd")
            .param("page","0")
            ).andDo(print());
    }

    //게시물 개별 조회 테스트
    @Test
    void testgetBoard() throws Exception {
        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("bno",5);
                //섹션 불러오기
        mvc.perform(get("/board/getboard")
            .session(mockHttpSession))
            .andDo(print());
    }

    //특정 게시물 수정 테스트
    @Test
    void testupdateService() throws Exception {
        //1번 게시물 수정 테스트
        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("bno",5);

        mvc.perform(put("/board/update")
            .param("btitle","수정 테스트 제목")
            .param("bcontent", "수정 테스트 내용")
            .session(mockHttpSession))
            .andDo(print());
    }

    //특정 게시물 삭제 테스트
    @Test
    void testdeleteService() throws Exception {
        //1번 게시물 삭제 테스트
        mvc.perform(delete("/board/delete")
            .param("bno","5"))
            .andDo(print());
    }

    // 카테고리 출력 테스트
    @Test
    void testgetcategorylist() throws Exception{
        mvc.perform( get("/board/getcategorylist") )
                .andDo( print() );
    }
    
}
