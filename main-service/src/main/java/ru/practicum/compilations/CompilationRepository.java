package ru.practicum.compilations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.compilations.model.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    @Query(value = "SELECT c FROM Compilation c WHERE c.pinned IS TRUE")
    Page<Compilation> getCompilationWithTruePinned(Pageable page);

    @Query(value = "SELECT c FROM Compilation c WHERE c.pinned IS FALSE")
    Page<Compilation> getCompilationWithFalsePinned(Pageable page);
}
