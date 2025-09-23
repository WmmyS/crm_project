package com.wesley.crm.infra.database;

import com.wesley.crm.domain.entities.Contato;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ContatoRepository extends JpaRepository<Contato, Long> {

    List<Contato> findByClienteId(Long clienteId);

    @Query("SELECT c FROM Contato c WHERE c.cliente.id = :clienteId ORDER BY c.dataContato DESC")
    Page<Contato> findByClienteIdOrderByDataContatoDesc(@Param("clienteId") Long clienteId, Pageable pageable);

    List<Contato> findByTipo(Contato.TipoContato tipo);

    List<Contato> findByStatus(Contato.StatusContato status);

    @Query("SELECT c FROM Contato c WHERE c.dataContato BETWEEN :dataInicio AND :dataFim")
    List<Contato> findByDataContatoBetween(@Param("dataInicio") LocalDateTime dataInicio, 
                                          @Param("dataFim") LocalDateTime dataFim);

    @Query("SELECT c FROM Contato c WHERE " +
           "LOWER(c.assunto) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(c.descricao) LIKE LOWER(CONCAT('%', :termo, '%'))")
    Page<Contato> buscarPorTermo(@Param("termo") String termo, Pageable pageable);

    // Relatórios
    @Query("SELECT c.tipo, COUNT(c) FROM Contato c GROUP BY c.tipo")
    List<Object[]> countContatosPorTipo();

    @Query("SELECT COUNT(c) FROM Contato c WHERE c.cliente.id = :clienteId")
    Long countByClienteId(@Param("clienteId") Long clienteId);
}