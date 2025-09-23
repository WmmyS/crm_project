package com.wesley.crm.app.controllers.contato;

import com.wesley.crm.app.models.dtos.contato.ContatoRequestDTO;
import com.wesley.crm.app.models.dtos.contato.ContatoResponseDTO;
import com.wesley.crm.app.services.ContatoService;
import com.wesley.crm.domain.entities.Contato;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/contatos")
@CrossOrigin(origins = "*")
public class ContatoController {

    @Autowired
    private ContatoService contatoService;

    @GetMapping
    public Page<ContatoResponseDTO> listarTodos(
            @PageableDefault(size = 20, sort = "dataContato", direction = Sort.Direction.DESC) Pageable pageable) {
        return contatoService.listarTodos(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContatoResponseDTO> buscarPorId(@PathVariable Long id) {
        ContatoResponseDTO contato = contatoService.buscarPorId(id);
        return ResponseEntity.ok(contato);
    }

    @GetMapping("/cliente/{clienteId}")
    public Page<ContatoResponseDTO> listarPorCliente(
            @PathVariable Long clienteId,
            @PageableDefault(size = 20, sort = "dataContato", direction = Sort.Direction.DESC) Pageable pageable) {
        return contatoService.listarPorCliente(clienteId, pageable);
    }

    @GetMapping("/tipo/{tipo}")
    public List<ContatoResponseDTO> listarPorTipo(@PathVariable Contato.TipoContato tipo) {
        return contatoService.listarPorTipo(tipo);
    }

    @GetMapping("/status/{status}")
    public List<ContatoResponseDTO> listarPorStatus(@PathVariable Contato.StatusContato status) {
        return contatoService.listarPorStatus(status);
    }

    @GetMapping("/periodo")
    public List<ContatoResponseDTO> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim) {
        return contatoService.listarPorPeriodo(dataInicio, dataFim);
    }

    @GetMapping("/buscar")
    public Page<ContatoResponseDTO> buscarPorTermo(
            @RequestParam String termo,
            @PageableDefault(size = 20, sort = "dataContato", direction = Sort.Direction.DESC) Pageable pageable) {
        return contatoService.buscarPorTermo(termo, pageable);
    }

    @PostMapping
    public ResponseEntity<ContatoResponseDTO> criar(@Valid @RequestBody ContatoRequestDTO contatoDTO) {
        ContatoResponseDTO contatoSalvo = contatoService.criar(contatoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(contatoSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContatoResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody ContatoRequestDTO contatoDTO) {
        ContatoResponseDTO contatoAtualizado = contatoService.atualizar(id, contatoDTO);
        return ResponseEntity.ok(contatoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        contatoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoints de relatórios
    @GetMapping("/relatorios/por-tipo")
    public ResponseEntity<List<Object[]>> contatosPorTipo() {
        List<Object[]> resultado = contatoService.contatosPorTipo();
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/relatorios/total-cliente/{clienteId}")
    public ResponseEntity<Long> totalContatosPorCliente(@PathVariable Long clienteId) {
        Long total = contatoService.totalContatosPorCliente(clienteId);
        return ResponseEntity.ok(total);
    }
}