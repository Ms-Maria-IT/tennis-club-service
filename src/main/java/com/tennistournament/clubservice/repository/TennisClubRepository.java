package com.tennistournament.clubservice.repository;

import com.tennistournament.clubservice.model.TennisClub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TennisClubRepository extends JpaRepository<TennisClub, Long> {
}
