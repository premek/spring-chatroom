package net.vyhnal.demo.room;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Integer> {
    @Cacheable("rooms")
    Optional<RoomEntity> findByName(String name);

    @Override
    @Cacheable("rooms")
    List<RoomEntity> findAll();
}