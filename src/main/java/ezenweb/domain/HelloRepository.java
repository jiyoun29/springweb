package ezenweb.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HelloRepository extends JpaRepository<HelloEntity, Long> {


}

//repository < ------------ > Dao 역할