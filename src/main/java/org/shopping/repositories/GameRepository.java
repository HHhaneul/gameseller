package org.shopping.repositories;

import org.shopping.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface GameRepository extends JpaRepository<Game, Long>, QuerydslPredicateExecutor<Game> {



}