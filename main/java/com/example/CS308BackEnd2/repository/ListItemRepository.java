package com.example.CS308BackEnd2.repository;


import com.example.CS308BackEnd2.model.ListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListItemRepository extends JpaRepository<ListItem, Long> {


}
