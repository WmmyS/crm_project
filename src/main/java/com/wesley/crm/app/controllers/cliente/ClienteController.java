package com.wesley.crm.app.controllers.cliente;

import com.wesley.crm.domain.entities.Cliente;
import com.wesley.crm.app.models.dtos.cliente.ClienteDTO;
import com.wesley.crm.app.services.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
@Tag(name = "👥 Clientes", description = "Endpoints para gerenciamento de clientes - Requer autenticação via JWT Token ou API Key")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    @Operation(summary = "📋 Listar clientes", description = "🔐 **Requer Autenticação Tripla** - JWT + API Key + Application Token.")
    @SecurityRequirement(name = "bearerAuth")
    @SecurityRequirement(name = "apiKey")
    @SecurityRequirement(name = "applicationToken")
    public Page<ClienteDTO> listarTodos(
            @PageableDefault(size = 20, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        return clienteService.listarTodos(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "🔍 Buscar cliente por ID", description = "🔐 **Requer Autenticação Tripla** - JWT + API Key + Application Token.")
    @SecurityRequirement(name = "bearerAuth")
    @SecurityRequirement(name = "apiKey")
    @SecurityRequirement(name = "applicationToken")
    public ResponseEntity<ClienteDTO> buscarPorId(@PathVariable Long id) {
        Optional<ClienteDTO> cliente = clienteService.buscarPorId(id);
        return cliente.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ClienteDTO> buscarPorEmail(@PathVariable String email) {
        Optional<ClienteDTO> cliente = clienteService.buscarPorEmail(email);
        return cliente.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar")
    public Page<ClienteDTO> buscarPorTermo(
            @RequestParam String termo,
            @PageableDefault(size = 20, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        return clienteService.buscarPorTermo(termo, pageable);
    }

    @GetMapping("/status/{status}")
    public List<ClienteDTO> listarPorStatus(@PathVariable Cliente.StatusCliente status) {
        return clienteService.listarPorStatus(status);
    }

    @GetMapping("/empresa/{empresaId}")
    public List<ClienteDTO> listarPorEmpresa(@PathVariable Long empresaId) {
        return clienteService.listarPorEmpresa(empresaId);
    }

    @PostMapping
    @Operation(summary = "➕ Criar cliente", description = "🔐 **Requer Autenticação Tripla** - JWT + API Key + Application Token.")
    @SecurityRequirement(name = "bearerAuth")
    @SecurityRequirement(name = "apiKey")
    @SecurityRequirement(name = "applicationToken")
    public ResponseEntity<Cliente> criar(@Valid @RequestBody Cliente cliente) {
        try {
            Cliente clienteSalvo = clienteService.criar(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(clienteSalvo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "✏️ Atualizar cliente", description = "🔐 **Requer Autenticação Tripla** - JWT + API Key + Application Token.")
    @SecurityRequirement(name = "bearerAuth")
    @SecurityRequirement(name = "apiKey")
    @SecurityRequirement(name = "applicationToken")
    public ResponseEntity<Cliente> atualizar(@PathVariable Long id, @Valid @RequestBody Cliente clienteAtualizado) {
        try {
            Cliente cliente = clienteService.atualizar(id, clienteAtualizado);
            return ResponseEntity.ok(cliente);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "🗑️ Deletar cliente", description = "🔐 **Requer Autenticação Tripla** - JWT + API Key + Application Token.")
    @SecurityRequirement(name = "bearerAuth")
    @SecurityRequirement(name = "apiKey")
    @SecurityRequirement(name = "applicationToken")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            clienteService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoints de relatórios
    @GetMapping("/relatorios/total-por-status")
    public ResponseEntity<Long> contarPorStatus(@RequestParam Cliente.StatusCliente status) {
        Long total = clienteService.contarPorStatus(status);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/relatorios/por-estado")
    public ResponseEntity<List<Object[]>> clientesPorEstado() {
        List<Object[]> resultado = clienteService.clientesPorEstado();
        return ResponseEntity.ok(resultado);
    }
}