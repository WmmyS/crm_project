package com.wesley.crm.app.controllers.oportunidade;

import com.wesley.crm.app.models.dtos.oportunidade.OportunidadeRequestDTO;
import com.wesley.crm.app.models.dtos.oportunidade.OportunidadeResponseDTO;
import com.wesley.crm.app.services.OportunidadeService;
import com.wesley.crm.domain.entities.Oportunidade;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/oportunidades")
@CrossOrigin(origins = "*")
public class OportunidadeController {

    @Autowired
    private OportunidadeService oportunidadeService;

    @GetMapping
    public Page<OportunidadeResponseDTO> listarTodas(
            @PageableDefault(size = 20, sort = "dataAtualizacao", direction = Sort.Direction.DESC) Pageable pageable) {
        return oportunidadeService.listarTodas(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OportunidadeResponseDTO> buscarPorId(@PathVariable Long id) {
        OportunidadeResponseDTO oportunidade = oportunidadeService.buscarPorId(id);
        return ResponseEntity.ok(oportunidade);
    }

    @GetMapping("/cliente/{clienteId}")
    public Page<OportunidadeResponseDTO> listarPorCliente(
            @PathVariable Long clienteId,
            @PageableDefault(size = 20, sort = "dataAtualizacao", direction = Sort.Direction.DESC) Pageable pageable) {
        return oportunidadeService.listarPorCliente(clienteId, pageable);
    }

    @GetMapping("/status/{status}")
    public List<OportunidadeResponseDTO> listarPorStatus(@PathVariable Oportunidade.StatusOportunidade status) {
        return oportunidadeService.listarPorStatus(status);
    }

    @GetMapping("/buscar")
    public Page<OportunidadeResponseDTO> buscarPorTermo(
            @RequestParam String termo,
            @PageableDefault(size = 20, sort = "dataAtualizacao", direction = Sort.Direction.DESC) Pageable pageable) {
        return oportunidadeService.buscarPorTermo(termo, pageable);
    }

    @GetMapping("/valor")
    public List<OportunidadeResponseDTO> listarPorFaixaValor(
            @RequestParam BigDecimal valorMinimo,
            @RequestParam BigDecimal valorMaximo) {
        return oportunidadeService.listarPorFaixaValor(valorMinimo, valorMaximo);
    }

    @PostMapping
    public ResponseEntity<OportunidadeResponseDTO> criar(@Valid @RequestBody OportunidadeRequestDTO oportunidadeDTO) {
        OportunidadeResponseDTO oportunidadeSalva = oportunidadeService.criar(oportunidadeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(oportunidadeSalva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OportunidadeResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody OportunidadeRequestDTO oportunidadeDTO) {
        OportunidadeResponseDTO oportunidadeAtualizada = oportunidadeService.atualizar(id, oportunidadeDTO);
        return ResponseEntity.ok(oportunidadeAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        oportunidadeService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoints de relatórios
    @GetMapping("/relatorios/por-status")
    public ResponseEntity<List<Object[]>> oportunidadesPorStatus() {
        List<Object[]> resultado = oportunidadeService.oportunidadesPorStatus();
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/relatorios/valor-total/{status}")
    public ResponseEntity<BigDecimal> valorTotalPorStatus(@PathVariable Oportunidade.StatusOportunidade status) {
        BigDecimal valorTotal = oportunidadeService.valorTotalPorStatus(status);
        return ResponseEntity.ok(valorTotal);
    }

    @GetMapping("/relatorios/probabilidade-media/{status}")
    public ResponseEntity<Double> probabilidadeMediaPorStatus(@PathVariable Oportunidade.StatusOportunidade status) {
        Double probabilidadeMedia = oportunidadeService.probabilidadeMediaPorStatus(status);
        return ResponseEntity.ok(probabilidadeMedia);
    }
}