package ezenweb.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity , Integer> {
    //일종의 dao 역할

    // 1.아이디를 이용한 엔티티 검색
    Optional< MemberEntity> findBymid(String mid ); // select  sql 문법 없이 검색 메소드 생성

    //2. 이메일을 이용한 엔티팅 검색
    Optional<MemberEntity> findBymemail(String email);

    //3. 이름과 이메일이 동일한 엔티티 검색
//1번 방법    @Query(value = "select * from member where mname = ?1 and memail = ?2", nativeQuery = true) //네이티브쿼리
    @Query(value = "select * from member where mname = :mname and memail = :memail", nativeQuery = true) //네이티브쿼리
    Optional<MemberEntity> findId(@Param("mname") String mname, @Param("memail") String memail);

    @Query(value = "select * from member where mid = :mid and memail = :memail", nativeQuery = true)
    Optional<MemberEntity> findPw(@Param("mid") String mid, @Param("memail") String memail);

}
/*
    특정 필드 검색 메소드 만들기 : findByEmail
//JpaRepository [crud]
    1. findAll() : 모든 엔티티 호출
    3. findbyId(pk값) : 해당 pk의 엔티티 호출
    2. save(엔티티) : 해당 엔티티를 DB 레코드 추가
    4. delete(엔티티) : 해당 엔티티를 삭제 처리
     수정은 없음 -> 매핑된 엔티티는 jpa 자동감지 지원이 되므로
        엔티티를 수정하면 자동적으로 db도 수정된다

 */