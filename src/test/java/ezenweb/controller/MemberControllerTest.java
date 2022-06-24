package ezenweb.controller;

import ezenweb.Dto.LoginDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {

    //mvc 테스트 사용 클래스
    @Autowired
    MockMvc mvc;


    //로그인 열기
    @Test
    void testlogin() throws Exception {
        mvc.perform(get("/member/login")).andDo(print());
    }

    //로그아웃
    @Test
    void testlogout() throws Exception {
        mvc.perform(get("/member/logout")).andDo(print());
    }

    //회원 수정 페이지
    @Test
    void testmemberupdate() throws Exception {
        mvc.perform(get("/member/update")).andDo(print());
    }

    //회원 삭제 페이지
    @Test
    void testmemberdelete() throws Exception {
        mvc.perform(get("/member/delete")).andDo(print());
    }

    //회원 가입 페이지
    @Test
    void testsignup() throws Exception {
        mvc.perform(get("/member/signup")).andDo(print());
    }

    //////////////////////////////////////////////////////////////

    //변경 가능한 페이지
//    void testloginService() throws Exception {
//        LoginDto loginDto = LoginDto.builder()
//                .mno(1)
//                .build();
//    }


}
