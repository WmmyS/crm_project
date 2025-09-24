package com.wesley.crm.infra.database;

import com.wesley.crm.domain.entities.Oportunidade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OportunidadeRepository extends JpaRepository<Oportunidade, Long> {

    List<Oportunidade> findByClienteId(Long clienteId);

    @Query("SELECT o FROM Oportunidade o WHERE o.cliente.id = :clienteId ORDER BY o.dataAtualizacao DESC")
    Page<Oportunidade> findByClienteIdOrderByDataAtualizacaoDesc(@Param("clienteId") Long clienteId, Pageable pageable);

    List<Oportunidade> findByStatus(Oportunidade.StatusOportunidade status);

    @Query("SELECT o FROM Oportunidade o WHERE " +
            "LOWER(o.titulo) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
            "LOWER(o.descricao) LIKE LOWER(CONCAT('%', :termo, '%'))")
    Page<Oportunidade> buscarPorTermo(@Param("termo") String termo, Pageable pageable);

    @Query("SELECT o FROM Oportunidade o WHERE o.valorEstimado >= :valorMinimo AND o.valorEstimado <= :valorMaximo")
    List<Oportunidade> findByValorEstimadoBetween(@Param("valorMinimo") BigDecimal valorMinimo,
            @Param("valorMaximo") BigDecimal valorMaximo);

    @Query("SELECT o FROM Oportunidade o WHERE o.probabilidadeFechamento >= :probabilidadeMinima")
    List<Oportunidade> findByProbabilidadeFechamentoGreaterThanEqual(
            @Param("probabilidadeMinima") Integer probabilidadeMinima);

    // Relatórios
    @Query("SELECT o.status, COUNT(o) FROM Oportunidade o GROUP BY o.status")
    List<Object[]> countOportunidadesPorStatus();

    @Query("SELECT SUM(o.valorEstimado) FROM Oportunidade o WHERE o.status = :status")
    BigDecimal somaValorEstimadoPorStatus(@Param("status") Oportunidade.StatusOportunidade status);

    @Query("SELECT COUNT(o) FROM Oportunidade o WHERE o.cliente.id = :clienteId")
    Long countByClienteId(@Param("clienteId") Long clienteId);

    @Query("SELECT AVG(o.probabilidadeFechamento) FROM Oportunidade o WHERE o.status = :status")
    Double mediaProbabilidadePorStatus(@Param("status") Oportunidade.StatusOportunidade status);

    // Métodos adicionais para estatísticas
    Long countByStatus(Oportunidade.StatusOportunidade status);

    @Query("SELECT SUM(o.valorEstimado) FROM Oportunidade o WHERE o.status = :status")
    BigDecimal sumValorEstimadoByStatus(@Param("status") Oportunidade.StatusOportunidade status);

    @Query("SELECT MONTH(o.dataCriacao), SUM(o.valorEstimado) FROM Oportunidade o GROUP BY MONTH(o.dataCriacao)")
    List<Object[]> sumValorEstimadoPorMes();

    @Query("SELECT o FROM Oportunidade o WHERE o.dataFechamentoPrevista BETWEEN :dataInicio AND :dataFim")
    List<Oportunidade> findByDataFechamentoPrevistaBetween(@Param("dataInicio") java.time.LocalDate dataInicio,
            @Param("dataFim") java.time.LocalDate dataFim);
}