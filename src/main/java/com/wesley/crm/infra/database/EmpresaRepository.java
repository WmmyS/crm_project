package com.wesley.crm.infra.database;

import com.wesley.crm.domain.entities.Empresa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    Optional<Empresa> findByCnpj(String cnpj);

    @Query("SELECT e FROM Empresa e WHERE " +
           "LOWER(e.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(e.email) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(e.cnpj) LIKE LOWER(CONCAT('%', :termo, '%'))")
    Page<Empresa> buscarPorTermo(@Param("termo") String termo, Pageable pageable);

    List<Empresa> findByCidadeAndEstado(String cidade, String estado);

    @Query("SELECT e FROM Empresa e WHERE e.estado = :estado")
    List<Empresa> findByEstado(@Param("estado") String estado);
}