package ezenweb;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing //? //jpa가 매핑된 엔티티(테이블)의 변화 감지 **
@SpringBootApplication //스프링부트의 자동 설정과 스프링 빈(클래스 메모리를 의미) 읽기와 생성을 모두 자동설정
public class application {
    public static void main(String[] args) { //메인 스레드(코드를 읽어주는 역할)

        SpringApplication.run(application.class); //내장 서버(톰캣) 스프링 시작
    }
    
    
    
}
