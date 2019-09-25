package com.example.demo.dao;

import com.example.demo.vo.Tutorials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TutorialsDao extends JpaRepository<Tutorials, Long> {

        List<Tutorials> findByTitle(String title);

        Tutorials testFindByTitle(String title);

        @Query("select t from Tutorials t where t.author like %:author")
        Tutorials testFindByAuthor(@Param("author") String name);
}


