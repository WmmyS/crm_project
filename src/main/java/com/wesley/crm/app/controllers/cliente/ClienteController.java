package com.wesley.crm.app.controllers.cliente;

import com.wesley.crm.domain.entities.Cliente;
import com.wesley.crm.app.models.dtos.cliente.ClienteDTO;
import com.wesley.crm.app.models.dtos.cliente.ClienteRequestDTO;
import com.wesley.crm.app.services.ClienteService;
import com.wesley.crm.app.services.FileUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
@Tag(name = "👥 Clientes", description = "Endpoints para gerenciamento de clientes - Requer autenticação dupla")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private FileUploadService fileUploadService;

    @GetMapping
    @Operation(summary = "📋 Listar clientes", description = "🔐 **Requer Autenticação Dupla** - JWT + Application Token.")
    public Page<ClienteDTO> listarTodos(
            @PageableDefault(size = 20, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        return clienteService.listarTodos(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "🔍 Buscar cliente por ID", description = "🔐 **Requer Autenticação Dupla** - JWT + Application Token.")
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
    @Operation(summary = "➕ Criar cliente", description = "🔐 **Requer Autenticação Dupla** - JWT + Application Token.")
    public ResponseEntity<ClienteDTO> criar(@Valid @RequestBody ClienteRequestDTO clienteRequest) {
        try {
            ClienteDTO clienteSalvo = clienteService.criar(clienteRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(clienteSalvo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value = "/com-imagem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "➕ Criar cliente com imagem", description = "🔐 **Requer Autenticação Dupla** - Cria cliente e faz upload da imagem em uma única requisição.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"id\": 1, \"nome\": \"João Silva\", \"email\": \"joao@email.com\", \"imagemUrl\": \"/uploads/clientes/abc123.jpg\"}"))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou arquivo muito grande")
    })
    public ResponseEntity<ClienteDTO> criarComImagem(
            @RequestParam("nome") String nome,
            @RequestParam("email") String email,
            @RequestParam(value = "telefone", required = false) String telefone,
            @RequestParam(value = "endereco", required = false) String endereco,
            @RequestParam(value = "cidade", required = false) String cidade,
            @RequestParam(value = "estado", required = false) String estado,
            @RequestParam(value = "cep", required = false) String cep,
            @RequestParam(value = "empresaId", required = false) Long empresaId,
            @RequestParam(value = "imagem", required = false) MultipartFile imagem) {
        try {
            // Criar DTO com os dados recebidos
            ClienteRequestDTO clienteRequest = new ClienteRequestDTO();
            clienteRequest.setNome(nome);
            clienteRequest.setEmail(email);
            clienteRequest.setTelefone(telefone);
            clienteRequest.setEndereco(endereco);
            clienteRequest.setCidade(cidade);
            clienteRequest.setEstado(estado);
            clienteRequest.setCep(cep);
            clienteRequest.setEmpresaId(empresaId);

            ClienteDTO clienteSalvo = clienteService.criarComImagem(clienteRequest, imagem);
            return ResponseEntity.status(HttpStatus.CREATED).body(clienteSalvo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "✏️ Atualizar cliente", description = "🔐 **Requer Autenticação Dupla** - JWT + Application Token.")
    public ResponseEntity<ClienteDTO> atualizar(@PathVariable Long id,
            @Valid @RequestBody ClienteRequestDTO clienteRequest) {
        try {
            ClienteDTO cliente = clienteService.atualizar(id, clienteRequest);
            return ResponseEntity.ok(cliente);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "🗑️ Deletar cliente", description = "🔐 **Requer Autenticação Dupla** - JWT + Application Token.")
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

    @PostMapping(value = "/{id}/imagem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "🖼️ Upload de imagem", description = "🔐 **Requer Autenticação Dupla** - Faz upload da imagem do cliente (JPG, PNG, GIF - máx 5MB).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imagem enviada com sucesso", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"id\": 1, \"nome\": \"João Silva\", \"email\": \"joao@email.com\", \"imagemUrl\": \"/uploads/clientes/abc123.jpg\"}"))),
            @ApiResponse(responseCode = "400", description = "Arquivo inválido ou muito grande"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<ClienteDTO> uploadImagem(@PathVariable Long id, @RequestParam("imagem") MultipartFile file) {
        try {
            ClienteDTO cliente = clienteService.uploadImagem(id, file);
            return ResponseEntity.ok(cliente);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}/imagem")
    @Operation(summary = "🗑️ Remover imagem", description = "🔐 **Requer Autenticação Dupla** - Remove a imagem do cliente.")
    public ResponseEntity<ClienteDTO> removerImagem(@PathVariable Long id) {
        try {
            ClienteDTO cliente = clienteService.removerImagem(id);
            return ResponseEntity.ok(cliente);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/imagem/{filename}")
    @Operation(summary = "🖼️ Visualizar imagem", description = "🆓 **Endpoint PÚBLICO** - Retorna a imagem do cliente. Use o nome do arquivo da URL retornada no upload.", security = {})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imagem encontrada", content = @Content(mediaType = "image/jpeg")),
            @ApiResponse(responseCode = "404", description = "Imagem não encontrada")
    })
    public ResponseEntity<Resource> visualizarImagem(@PathVariable String filename) {
        try {
            Resource resource = fileUploadService.getImage(filename);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}