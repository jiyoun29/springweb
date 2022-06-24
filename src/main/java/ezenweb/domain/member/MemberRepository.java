package ezenweb.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity , Integer> {
    //일종의 dao 역할

    // 1.아이디를 이용한 엔티티 검색
    Optional< MemberEntity> findBymid(String mid ); // select  sql 문법 없이 검색 메소드 생성
}

//JpaRepository [crud]
    //1. findAll() : 모든 엔티티 호출
    //3. findbyId(pk값) : 해당 pk의 엔티티 호출
    //2. save(엔티티) : 해당 엔티티를 DB 레코드 추가
    //4. delete(엔티티) : 해당 엔티티를 삭제 처리
    // 수정은 없음 -> 매핑된 엔티티는 jpa 자동감지 지원이 되므로
        //엔티티를 수정하면 자동적으로 db도 수정된다