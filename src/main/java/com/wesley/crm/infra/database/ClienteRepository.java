package com.wesley.crm.infra.database;

import com.wesley.crm.domain.entities.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByEmail(String email);

    Optional<Cliente> findByCpf(String cpf);

    List<Cliente> findByStatus(Cliente.StatusCliente status);

    @Query("SELECT c FROM Cliente c WHERE " +
           "LOWER(c.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(c.email) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(c.telefone) LIKE LOWER(CONCAT('%', :termo, '%'))")
    Page<Cliente> buscarPorTermo(@Param("termo") String termo, Pageable pageable);

    @Query("SELECT c FROM Cliente c WHERE c.empresa.id = :empresaId")
    List<Cliente> findByEmpresaId(@Param("empresaId") Long empresaId);

    @Query("SELECT c FROM Cliente c WHERE c.cidade = :cidade AND c.estado = :estado")
    List<Cliente> findByCidadeAndEstado(@Param("cidade") String cidade, @Param("estado") String estado);

    // Relatórios
    @Query("SELECT COUNT(c) FROM Cliente c WHERE c.status = :status")
    Long countByStatus(@Param("status") Cliente.StatusCliente status);

    @Query("SELECT c.estado, COUNT(c) FROM Cliente c GROUP BY c.estado ORDER BY COUNT(c) DESC")
    List<Object[]> countClientesPorEstado();
}