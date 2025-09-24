package com.wesley.crm.app.controllers.empresa;

import com.wesley.crm.app.models.dtos.empresa.EmpresaRequestDTO;
import com.wesley.crm.app.models.dtos.empresa.EmpresaResponseDTO;
import com.wesley.crm.app.services.EmpresaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/empresas")
@CrossOrigin(origins = "*")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @GetMapping
    public Page<EmpresaResponseDTO> listarTodas(
            @PageableDefault(size = 20, sort = "razaoSocial", direction = Sort.Direction.ASC) Pageable pageable) {
        return empresaService.listarTodas(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpresaResponseDTO> buscarPorId(@PathVariable Long id) {
        Optional<EmpresaResponseDTO> empresa = empresaService.buscarPorId(id);
        return empresa.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EmpresaResponseDTO> criar(@Valid @RequestBody EmpresaRequestDTO empresaRequest) {
        try {
            EmpresaResponseDTO empresaCriada = empresaService.criar(empresaRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(empresaCriada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpresaResponseDTO> atualizar(@PathVariable Long id,
            @Valid @RequestBody EmpresaRequestDTO empresaRequest) {
        try {
            Optional<EmpresaResponseDTO> empresaAtualizada = empresaService.atualizar(id, empresaRequest);
            return empresaAtualizada.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            empresaService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
