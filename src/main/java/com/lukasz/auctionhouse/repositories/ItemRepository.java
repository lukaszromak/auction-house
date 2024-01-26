package com.lukasz.auctionhouse.repositories;

import com.lukasz.auctionhouse.domain.Item;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {

    @Query("SELECT i FROM Item i WHERE lower(i.name) LIKE lower(concat('%', ?1, '%'))")
    List<Item> findAllByName(String name);
    @Query("SELECT i FROM Item i JOIN i.itemCategory c WHERE c.id = ?1")
    List<Item> findAllByCategoryId(Long categoryId);
    @Query("SELECT i FROM Item i JOIN i.itemProducers p WHERE p.id = ?1")
    List<Item> findAllByProducerId(Long producerId);
    @Query("SELECT i FROM Item i WHERE lower(i.name) LIKE lower(concat('%', ?1, '%')) and lower(i.description) LIKE lower(CONCAT('%', ?2, '%'))")
    List<Item> findAllByNameAndDescription(String name, String description);
    List<Item> findAllByExpirationDateAfterAndExpirationDateBefore(Date dateAfter, Date dateBefore);
    @Query("SELECT DISTINCT i FROM Item i JOIN i.itemProducers p WHERE p.name IN ?1")
    List<Item> findAllByItemProducerName(Set<String> producerNames);
    List<Item> findAll(Specification<Item> specification);
    List<Item> findAllByCategoryPhrase(String phrase);
    @Query("select i from Item i join i.itemCategory where lower(i.itemCategory.name) like lower(concat('%', ?1, '%'))")
    List<Item> findAllByCategoryPhraseQuery(@Param("phrase") String phrase);
}
