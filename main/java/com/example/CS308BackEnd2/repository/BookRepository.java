package com.example.CS308BackEnd2.repository;




import com.example.CS308BackEnd2.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    public Book findBookByPrice(int price);

    public List<Book> findBooksByDiscountRateNot(double discountRate);

    public List<Book> findBooksByCategoryName(String category);
}

