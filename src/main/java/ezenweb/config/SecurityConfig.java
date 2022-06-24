package ezenweb.config;

import ezenweb.Service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration //해당 클래스를 설정 클래스로 주입
public class SecurityConfig extends WebSecurityConfigurerAdapter {
                                //웹 서큐리티 설정 상속 클래스


    //암호화 제공 [ 특정 필드 암호화 ]

    @Override //재정의
    protected void configure(HttpSecurity http) throws Exception {
                        //http(링크) 관련된 시큐리티를 보안(and재정의)
        http //http에 접근할 수 있는 권한
            .authorizeHttpRequests() //인증된 요청들
                .antMatchers("/admin/**").hasRole("ADMIN") //어드민 뒤로부터는 어드민만 들어갈 수 있게 롤을 부여
                .antMatchers("/MEMBER/INFO").hasRole("MEMBER")
            .antMatchers("/board/save") // 인증할 url (글쓰기 페이지를 로그인한 사람에게만 주기)
                .hasRole("MEMBER") // 해당 인증 권한이 있을 경우 = 세션에 role 필드가 있어야 한다.
            .antMatchers("/**") //인증할 url
            .permitAll() //인증이 없어도 요청 가능한 상태 = 모든 접근 허용
            .and()
                .formLogin() //로그인 페이지 보안 설정
                .loginPage("/member/login") //아이디/비밀번호를 입력받을 페이지 url
                .loginProcessingUrl("/member/logincontroller") //로그인 처리할 url
                .defaultSuccessUrl("/") //로그인 성공시, 이동할 url
                .usernameParameter("mid") //로그인시 아이디로 입력 받을 변수명[기본값 : user->mid]
                .passwordParameter("mpw")//로그인시 비밀번호로 입력 받을 변수명[기본값 : password->mpw]
            .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout")) //로그인 처리할 url 정의
                .logoutSuccessUrl("/") //로그인 성공시
                .invalidateHttpSession(true) //세션 초기화
            .and()
            .csrf() // csrf : 사이트간 요청 위조 [ 해킹 공격 방법 중 하나 ] = 서버에게 요청할 수 있는 페이지 제한
            .ignoringAntMatchers("/member/logincontroller") //로그인 풀어주기(무시)
            .ignoringAntMatchers("/member/signup")
                .and().exceptionHandling() //오류 페이지 발생시 시큐리티가 가지고 옴
                .accessDeniedPage("/error");


//        super.configure(http); //슈퍼클래스의 기본 설정값으로 사용하는 것
    } //config.메소드 정의


    //1.로그인 보안 서비스
    @Autowired
    private MemberService memberService; //멤버 관련 서비스

    //2.
    @Override //인증(로그인) 관리 메소드
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //패스워드 인증
        auth.userDetailsService(memberService).passwordEncoder(new BCryptPasswordEncoder());
                //인증할 서비스 객체() -> 패스워드 인코더(BCrypt 객체로)


//        super.configure(auth); //기본값 사용x
    }









}