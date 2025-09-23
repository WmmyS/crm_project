package com.wesley.crm.app.controllers.empresa;package com.wesley.crm.app.controllers.empresa;package com.wesley.crm.app.controllers.empresa;package com.wesley.crm.app.controllers.empresa;package com.wesley.crm.app.controllers.empresa;package com.wesley.crm.app.controllers.empresa;package com.wesley.crm.app.controllers.empresa;



import com.wesley.crm.domain.entities.Empresa;

import com.wesley.crm.app.models.dtos.empresa.EmpresaRequestDTO;

import com.wesley.crm.app.models.dtos.empresa.EmpresaResponseDTO;import com.wesley.crm.domain.entities.Empresa;

import com.wesley.crm.app.services.EmpresaService;

import io.swagger.v3.oas.annotations.Operation;import com.wesley.crm.app.models.dtos.empresa.EmpresaRequestDTO;

import io.swagger.v3.oas.annotations.Parameter;

import io.swagger.v3.oas.annotations.media.Content;import com.wesley.crm.app.models.dtos.empresa.EmpresaResponseDTO;import com.wesley.crm.domain.entities.Empresa;

import io.swagger.v3.oas.annotations.media.ExampleObject;

import io.swagger.v3.oas.annotations.media.Schema;import com.wesley.crm.app.services.EmpresaService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

import io.swagger.v3.oas.annotations.responses.ApiResponses;import io.swagger.v3.oas.annotations.Operation;import com.wesley.crm.app.models.dtos.empresa.EmpresaRequestDTO;

import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;import jakarta.validation.Valid;import com.wesley.crm.app.models.dtos.empresa.EmpresaResponseDTO;import com.wesley.crm.domain.entities.Empresa;

import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Sort;import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.web.PageableDefault;

import org.springframework.http.HttpStatus;import org.springframework.data.domain.Page;import com.wesley.crm.app.services.EmpresaService;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;import org.springframework.data.domain.Pageable;



import java.util.List;import org.springframework.data.domain.Sort;import io.swagger.v3.oas.annotations.Operation;import com.wesley.crm.app.models.dtos.empresa.EmpresaRequestDTO;

import java.util.Optional;

import org.springframework.data.web.PageableDefault;

@RestController

@RequestMapping("/api/empresas")import org.springframework.http.HttpStatus;import io.swagger.v3.oas.annotations.Parameter;

@CrossOrigin(origins = "*")

@Tag(name = "Empresas", description = "API para gerenciamento de empresas")import org.springframework.http.ResponseEntity;

public class EmpresaController {

import org.springframework.web.bind.annotation.*;import io.swagger.v3.oas.annotations.tags.Tag;import com.wesley.crm.app.models.dtos.empresa.EmpresaResponseDTO;import com.wesley.crm.domain.entities.Empresa;

    @Autowired

    private EmpresaService empresaService;



    @GetMappingimport java.util.List;import jakarta.validation.Valid;

    @Operation(

        summary = "Listar todas as empresas",import java.util.Optional;

        description = "Retorna uma lista paginada de empresas. Use campos válidos para ordenação.",

        parameters = {import org.springframework.beans.factory.annotation.Autowired;import com.wesley.crm.app.services.EmpresaService;

            @Parameter(

                name = "page",@RestController

                description = "Número da página (começando em 0)",

                example = "0"@RequestMapping("/api/empresas")import org.springframework.data.domain.Page;

            ),

            @Parameter(@CrossOrigin(origins = "*")

                name = "size", 

                description = "Tamanho da página",@Tag(name = "Empresas", description = "API para gerenciamento de empresas")import org.springframework.data.domain.PageRequest;import io.swagger.v3.oas.annotations.Operation;import com.wesley.crm.app.models.dtos.empresa.EmpresaRequestDTO;

                example = "10"

            ),public class EmpresaController {

            @Parameter(

                name = "sort",import org.springframework.data.domain.Pageable;

                description = "Campo para ordenação. Campos válidos: id, nome, cnpj, email, dataCriacao",

                examples = {    @Autowired

                    @ExampleObject(name = "Por ID", value = "id"),

                    @ExampleObject(name = "Por Nome", value = "nome"),    private EmpresaService empresaService;import org.springframework.data.domain.Sort;import io.swagger.v3.oas.annotations.Parameter;

                    @ExampleObject(name = "Por CNPJ", value = "cnpj"),

                    @ExampleObject(name = "Por Email", value = "email"),

                    @ExampleObject(name = "Por Data de Criação", value = "dataCriacao")

                }    @GetMappingimport org.springframework.http.HttpStatus;

            )

        }    @Operation(summary = "Listar todas as empresas")

    )

    @ApiResponses(value = {    public Page<EmpresaResponseDTO> listarTodas(import org.springframework.http.ResponseEntity;import io.swagger.v3.oas.annotations.tags.Tag;import com.wesley.crm.app.models.dtos.empresa.EmpresaResponseDTO;import com.wesley.crm.domain.entities.Empresa;import com.wesley.crm.domain.entities.Empresa;

        @ApiResponse(responseCode = "200", description = "Lista de empresas retornada com sucesso"),

        @ApiResponse(responseCode = "400", description = "Parâmetros inválidos")            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

    })

    public Page<EmpresaResponseDTO> listarTodas(        return empresaService.listarTodas(pageable);import org.springframework.web.bind.annotation.*;

            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        return empresaService.listarTodas(pageable);    }

    }

import jakarta.validation.Valid;

    @GetMapping("/{id}")

    @Operation(    @GetMapping("/{id}")

        summary = "Buscar empresa por ID",

        description = "Retorna uma empresa específica pelo seu ID"    @Operation(summary = "Buscar empresa por ID")import java.util.List;

    )

    @ApiResponses(value = {    public ResponseEntity<EmpresaResponseDTO> buscarPorId(@PathVariable Long id) {

        @ApiResponse(responseCode = "200", description = "Empresa encontrada"),

        @ApiResponse(responseCode = "404", description = "Empresa não encontrada")        Optional<EmpresaResponseDTO> empresa = empresaService.buscarPorId(id);import java.util.Optional;import org.springframework.beans.factory.annotation.Autowired;import com.wesley.crm.app.services.EmpresaService;

    })

    public ResponseEntity<EmpresaResponseDTO> buscarPorId(        return empresa.map(ResponseEntity::ok)

            @Parameter(description = "ID da empresa", example = "1") @PathVariable Long id) {

        Optional<EmpresaResponseDTO> empresa = empresaService.buscarPorId(id);                .orElse(ResponseEntity.notFound().build());

        return empresa.map(ResponseEntity::ok)

                .orElse(ResponseEntity.notFound().build());    }

    }

@RestControllerimport org.springframework.data.domain.Page;

    @GetMapping("/cnpj/{cnpj}")

    @Operation(    @GetMapping("/cnpj/{cnpj}")

        summary = "Buscar empresa por CNPJ",

        description = "Retorna uma empresa específica pelo seu CNPJ"    @Operation(summary = "Buscar empresa por CNPJ")@RequestMapping("/api/empresas")

    )

    @ApiResponses(value = {    public ResponseEntity<EmpresaResponseDTO> buscarPorCnpj(@PathVariable String cnpj) {

        @ApiResponse(responseCode = "200", description = "Empresa encontrada"),

        @ApiResponse(responseCode = "404", description = "Empresa não encontrada")        Optional<EmpresaResponseDTO> empresa = empresaService.buscarPorCnpj(cnpj);@CrossOrigin(origins = "*")import org.springframework.data.domain.Pageable;import jakarta.validation.Valid;import com.wesley.crm.app.models.dtos.empresa.EmpresaDTO;import com.wesley.crm.app.models.dtos.empresa.EmpresaDTO;

    })

    public ResponseEntity<EmpresaResponseDTO> buscarPorCnpj(        return empresa.map(ResponseEntity::ok)

            @Parameter(description = "CNPJ da empresa", example = "12.345.678/0001-90") @PathVariable String cnpj) {

        Optional<EmpresaResponseDTO> empresa = empresaService.buscarPorCnpj(cnpj);                .orElse(ResponseEntity.notFound().build());@Tag(name = "Empresas", description = "API para gerenciamento de empresas")

        return empresa.map(ResponseEntity::ok)

                .orElse(ResponseEntity.notFound().build());    }

    }

public class EmpresaController {import org.springframework.data.domain.Sort;

    @GetMapping("/buscar")

    @Operation(    @GetMapping("/buscar")

        summary = "Buscar empresas por termo",

        description = "Busca empresas por nome, email ou outros campos"    @Operation(summary = "Buscar empresas por termo")

    )

    public Page<EmpresaResponseDTO> buscarPorTermo(    public Page<EmpresaResponseDTO> buscarPorTermo(

            @Parameter(description = "Termo de busca", example = "Tech Solutions") @RequestParam String termo,

            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {            @RequestParam String termo,    @Autowiredimport org.springframework.data.web.PageableDefault;import org.springframework.beans.factory.annotation.Autowired;

        return empresaService.buscarPorTermo(termo, pageable);

    }            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {



    @GetMapping("/estado/{estado}")        return empresaService.buscarPorTermo(termo, pageable);    private EmpresaService empresaService;

    @Operation(

        summary = "Listar empresas por estado",    }

        description = "Retorna empresas de um estado específico"

    )import org.springframework.http.HttpStatus;

    public List<Empresa> listarPorEstado(

            @Parameter(description = "Estado (sigla)", example = "SP") @PathVariable String estado) {    @GetMapping("/estado/{estado}")

        return empresaService.listarPorEstado(estado);

    }    @Operation(summary = "Listar empresas por estado")    @GetMapping



    @PostMapping    public List<Empresa> listarPorEstado(@PathVariable String estado) {

    @Operation(

        summary = "Criar nova empresa",        return empresaService.listarPorEstado(estado);    @Operation(summary = "Listar todas as empresas", import org.springframework.http.ResponseEntity;import org.springframework.data.domain.Page;import com.wesley.crm.app.services.EmpresaService;import com.wesley.crm.app.services.EmpresaService;

        description = "Cria uma nova empresa no sistema"

    )    }

    @ApiResponses(value = {

        @ApiResponse(responseCode = "201", description = "Empresa criada com sucesso"),               description = "Retorna uma lista paginada de empresas. Campos disponíveis para ordenação: id, nome, cnpj, email")

        @ApiResponse(responseCode = "400", description = "Dados inválidos")

    })    @PostMapping

    public ResponseEntity<Empresa> criar(

            @io.swagger.v3.oas.annotations.parameters.RequestBody(    @Operation(summary = "Criar nova empresa")    public Page<EmpresaResponseDTO> listarTodas(import org.springframework.web.bind.annotation.*;

                description = "Dados da empresa",

                content = @Content(    public ResponseEntity<Empresa> criar(@Valid @RequestBody EmpresaRequestDTO empresaDTO) {

                    schema = @Schema(implementation = EmpresaRequestDTO.class),

                    examples = @ExampleObject(        Empresa novaEmpresa = empresaService.criar(empresaDTO);            @Parameter(description = "Número da página") @RequestParam(defaultValue = "0") int page,

                        name = "Exemplo de Empresa",

                        value = """        return ResponseEntity.status(HttpStatus.CREATED).body(novaEmpresa);

                        {

                          "nome": "Exemplo Tecnologia Ltda",    }            @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size,import org.springframework.data.domain.Pageable;

                          "cnpj": "11.222.333/0001-44",

                          "email": "contato@exemplo.com.br",

                          "telefone": "(11) 1234-5678",

                          "endereco": "Rua das Flores, 123",    @PutMapping("/{id}")            @Parameter(description = "Campo para ordenação (id, nome, cnpj, email)") @RequestParam(defaultValue = "id") String sort,

                          "cidade": "São Paulo",

                          "estado": "SP",    @Operation(summary = "Atualizar empresa")

                          "cep": "01234-567"

                        }    public ResponseEntity<Empresa> atualizar(@PathVariable Long id, @Valid @RequestBody EmpresaRequestDTO empresaDTO) {            @Parameter(description = "Direção da ordenação") @RequestParam(defaultValue = "ASC") String direction) {import java.util.List;

                        """

                    )        try {

                )

            )            Empresa empresaAtualizada = empresaService.atualizar(id, empresaDTO);        

            @Valid @RequestBody EmpresaRequestDTO empresaDTO) {

        Empresa novaEmpresa = empresaService.criar(empresaDTO);            return ResponseEntity.ok(empresaAtualizada);

        return ResponseEntity.status(HttpStatus.CREATED).body(novaEmpresa);

    }        } catch (Exception e) {        // Validar campo de ordenaçãoimport java.util.Optional;import org.springframework.data.domain.Sort;import jakarta.validation.Valid;import jakarta.validation.Valid;



    @PutMapping("/{id}")            return ResponseEntity.notFound().build();

    @Operation(

        summary = "Atualizar empresa",        }        String[] camposValidos = {"id", "nome", "cnpj", "email", "dataCriacao", "dataAtualizacao"};

        description = "Atualiza os dados de uma empresa existente"

    )    }

    @ApiResponses(value = {

        @ApiResponse(responseCode = "200", description = "Empresa atualizada com sucesso"),        String campoSort = sort;

        @ApiResponse(responseCode = "404", description = "Empresa não encontrada"),

        @ApiResponse(responseCode = "400", description = "Dados inválidos")    @DeleteMapping("/{id}")

    })

    public ResponseEntity<Empresa> atualizar(    @Operation(summary = "Deletar empresa")        boolean campoValido = false;

            @Parameter(description = "ID da empresa", example = "1") @PathVariable Long id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(    public ResponseEntity<Void> deletar(@PathVariable Long id) {

                description = "Dados atualizados da empresa",

                content = @Content(        try {        for (String campo : camposValidos) {@RestControllerimport org.springframework.data.web.PageableDefault;

                    schema = @Schema(implementation = EmpresaRequestDTO.class),

                    examples = @ExampleObject(            empresaService.deletar(id);

                        name = "Exemplo de Atualização",

                        value = """            return ResponseEntity.noContent().build();            if (campo.equals(sort)) {

                        {

                          "nome": "Empresa Atualizada Ltda",        } catch (Exception e) {

                          "cnpj": "11.222.333/0001-44",

                          "email": "novo@exemplo.com.br",            return ResponseEntity.notFound().build();                campoValido = true;@RequestMapping("/api/empresas")

                          "telefone": "(11) 9999-8888",

                          "endereco": "Nova Rua, 456",        }

                          "cidade": "São Paulo",

                          "estado": "SP",    }                break;

                          "cep": "01234-999"

                        }}

                        """            }@CrossOrigin(origins = "*")import org.springframework.http.HttpStatus;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.beans.factory.annotation.Autowired;

                    )

                )        }

            )

            @Valid @RequestBody EmpresaRequestDTO empresaDTO) {        if (!campoValido) {@Tag(name = "Empresas", description = "API para gerenciamento de empresas")

        try {

            Empresa empresaAtualizada = empresaService.atualizar(id, empresaDTO);            campoSort = "id"; // fallback para campo padrão

            return ResponseEntity.ok(empresaAtualizada);

        } catch (Exception e) {        }public class EmpresaController {import org.springframework.http.ResponseEntity;

            return ResponseEntity.notFound().build();

        }        

    }

        Sort.Direction sortDirection = direction.equalsIgnoreCase("DESC") ? 

    @DeleteMapping("/{id}")

    @Operation(            Sort.Direction.DESC : Sort.Direction.ASC;

        summary = "Deletar empresa",

        description = "Remove uma empresa do sistema"            @Autowiredimport org.springframework.web.bind.annotation.*;import org.springframework.data.domain.Page;import org.springframework.data.domain.Page;

    )

    @ApiResponses(value = {        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, campoSort));

        @ApiResponse(responseCode = "204", description = "Empresa deletada com sucesso"),

        @ApiResponse(responseCode = "404", description = "Empresa não encontrada")        return empresaService.listarTodas(pageable);    private EmpresaService empresaService;

    })

    public ResponseEntity<Void> deletar(    }

            @Parameter(description = "ID da empresa", example = "1") @PathVariable Long id) {

        try {

            empresaService.deletar(id);

            return ResponseEntity.noContent().build();    @GetMapping("/{id}")

        } catch (Exception e) {

            return ResponseEntity.notFound().build();    @Operation(summary = "Buscar empresa por ID")    @GetMapping

        }

    }    public ResponseEntity<EmpresaResponseDTO> buscarPorId(@PathVariable Long id) {

}
        Optional<EmpresaResponseDTO> empresa = empresaService.buscarPorId(id);    @Operation(summary = "Listar todas as empresas", description = "Retorna uma lista paginada de empresas")import java.util.List;import org.springframework.data.domain.Pageable;import org.springframework.data.domain.Pageable;

        return empresa.map(ResponseEntity::ok)

                .orElse(ResponseEntity.notFound().build());    public Page<EmpresaResponseDTO> listarTodas(

    }

            @Parameter(description = "Parâmetros de paginação")import java.util.Optional;

    @GetMapping("/cnpj/{cnpj}")

    @Operation(summary = "Buscar empresa por CNPJ")            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

    public ResponseEntity<EmpresaResponseDTO> buscarPorCnpj(@PathVariable String cnpj) {

        Optional<EmpresaResponseDTO> empresa = empresaService.buscarPorCnpj(cnpj);        return empresaService.listarTodas(pageable);import org.springframework.data.domain.Sort;import org.springframework.data.domain.Sort;

        return empresa.map(ResponseEntity::ok)

                .orElse(ResponseEntity.notFound().build());    }

    }

@RestController

    @GetMapping("/buscar")

    @Operation(summary = "Buscar empresas por termo")    @GetMapping("/{id}")

    public Page<EmpresaResponseDTO> buscarPorTermo(

            @RequestParam String termo,    @Operation(summary = "Buscar empresa por ID", description = "Retorna uma empresa específica pelo seu ID")@RequestMapping("/api/empresas")import org.springframework.data.web.PageableDefault;import org.springframework.data.web.PageableDefault;

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size) {    public ResponseEntity<EmpresaResponseDTO> buscarPorId(

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));

        return empresaService.buscarPorTermo(termo, pageable);            @Parameter(description = "ID da empresa") @PathVariable Long id) {@CrossOrigin(origins = "*")

    }

        Optional<EmpresaResponseDTO> empresa = empresaService.buscarPorId(id);

    @GetMapping("/estado/{estado}")

    @Operation(summary = "Listar empresas por estado")        return empresa.map(ResponseEntity::ok)public class EmpresaController {import org.springframework.http.HttpStatus;import org.springframework.http.HttpStatus;

    public List<Empresa> listarPorEstado(@PathVariable String estado) {

        return empresaService.listarPorEstado(estado);                .orElse(ResponseEntity.notFound().build());

    }

    }

    @PostMapping

    @Operation(summary = "Criar nova empresa")

    public ResponseEntity<Empresa> criar(@Valid @RequestBody EmpresaRequestDTO empresaDTO) {

        Empresa novaEmpresa = empresaService.criar(empresaDTO);    @GetMapping("/cnpj/{cnpj}")    @Autowiredimport org.springframework.http.ResponseEntity;import org.springframework.http.ResponseEntity;

        return ResponseEntity.status(HttpStatus.CREATED).body(novaEmpresa);

    }    @Operation(summary = "Buscar empresa por CNPJ", description = "Retorna uma empresa específica pelo seu CNPJ")



    @PutMapping("/{id}")    public ResponseEntity<EmpresaResponseDTO> buscarPorCnpj(    private EmpresaService empresaService;

    @Operation(summary = "Atualizar empresa")

    public ResponseEntity<Empresa> atualizar(@PathVariable Long id, @Valid @RequestBody EmpresaRequestDTO empresaDTO) {            @Parameter(description = "CNPJ da empresa") @PathVariable String cnpj) {

        try {

            Empresa empresaAtualizada = empresaService.atualizar(id, empresaDTO);        Optional<EmpresaResponseDTO> empresa = empresaService.buscarPorCnpj(cnpj);import org.springframework.web.bind.annotation.*;import org.springframework.web.bind.annotation.*;

            return ResponseEntity.ok(empresaAtualizada);

        } catch (Exception e) {        return empresa.map(ResponseEntity::ok)

            return ResponseEntity.notFound().build();

        }                .orElse(ResponseEntity.notFound().build());    @GetMapping

    }

    }

    @DeleteMapping("/{id}")

    @Operation(summary = "Deletar empresa")    public Page<EmpresaResponseDTO> listarTodas(

    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        try {    @GetMapping("/buscar")

            empresaService.deletar(id);

            return ResponseEntity.noContent().build();    @Operation(summary = "Buscar empresas por termo", description = "Busca empresas por nome, email ou outros campos")            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        } catch (Exception e) {

            return ResponseEntity.notFound().build();    public Page<EmpresaResponseDTO> buscarPorTermo(

        }

    }            @Parameter(description = "Termo de busca") @RequestParam String termo,        return empresaService.listarTodas(pageable);import java.util.List;import java.util.List;

}
            @Parameter(description = "Parâmetros de paginação")

            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {    }

        return empresaService.buscarPorTermo(termo, pageable);

    }import java.util.Optional;import java.util.Optional;



    @GetMapping("/estado/{estado}")    @GetMapping("/{id}")

    @Operation(summary = "Listar empresas por estado", description = "Retorna empresas de um estado específico")

    public List<Empresa> listarPorEstado(    public ResponseEntity<EmpresaResponseDTO> buscarPorId(@PathVariable Long id) {

            @Parameter(description = "Estado") @PathVariable String estado) {

        return empresaService.listarPorEstado(estado);        Optional<EmpresaResponseDTO> empresa = empresaService.buscarPorId(id);

    }

        return empresa.map(ResponseEntity::ok)@RestController@RestController

    @PostMapping

    @Operation(summary = "Criar nova empresa", description = "Cria uma nova empresa no sistema")                .orElse(ResponseEntity.notFound().build());

    public ResponseEntity<Empresa> criar(

            @Parameter(description = "Dados da empresa") @Valid @RequestBody EmpresaRequestDTO empresaDTO) {    }@RequestMapping("/api/empresas")@RequestMapping("/api/empresas")

        Empresa novaEmpresa = empresaService.criar(empresaDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(novaEmpresa);

    }

    @GetMapping("/cnpj/{cnpj}")@CrossOrigin(origins = "*")@CrossOrigin(origins = "*")

    @PutMapping("/{id}")

    @Operation(summary = "Atualizar empresa", description = "Atualiza os dados de uma empresa existente")    public ResponseEntity<EmpresaResponseDTO> buscarPorCnpj(@PathVariable String cnpj) {

    public ResponseEntity<Empresa> atualizar(

            @Parameter(description = "ID da empresa") @PathVariable Long id,         Optional<EmpresaResponseDTO> empresa = empresaService.buscarPorCnpj(cnpj);public class EmpresaController {public class EmpresaController {

            @Parameter(description = "Dados atualizados da empresa") @Valid @RequestBody EmpresaRequestDTO empresaDTO) {

        try {        return empresa.map(ResponseEntity::ok)

            Empresa empresaAtualizada = empresaService.atualizar(id, empresaDTO);

            return ResponseEntity.ok(empresaAtualizada);                .orElse(ResponseEntity.notFound().build());

        } catch (Exception e) {

            return ResponseEntity.notFound().build();    }

        }

    }    @Autowired    @Autowired



    @DeleteMapping("/{id}")    @GetMapping("/buscar")

    @Operation(summary = "Deletar empresa", description = "Remove uma empresa do sistema")

    public ResponseEntity<Void> deletar(    public Page<EmpresaResponseDTO> buscarPorTermo(    private EmpresaService empresaService;    private EmpresaService empresaService;

            @Parameter(description = "ID da empresa") @PathVariable Long id) {

        try {            @RequestParam String termo,

            empresaService.deletar(id);

            return ResponseEntity.noContent().build();            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        } catch (Exception e) {

            return ResponseEntity.notFound().build();        return empresaService.buscarPorTermo(termo, pageable);

        }

    }    }    @GetMapping    @GetMapping

}


    @GetMapping("/estado/{estado}")    public Page<EmpresaDTO> listarTodas(    public Page<EmpresaDTO> listarTodas(

    public List<Empresa> listarPorEstado(@PathVariable String estado) {

        return empresaService.listarPorEstado(estado);            @PageableDefault(size = 20, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {            @PageableDefault(size = 20, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {

    }

        return empresaService.listarTodas(pageable);        return empresaService.listarTodas(pageable);

    @PostMapping

    public ResponseEntity<Empresa> criar(@Valid @RequestBody EmpresaRequestDTO empresaDTO) {    }    }

        Empresa novaEmpresa = empresaService.criar(empresaDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(novaEmpresa);

    }

    @GetMapping("/{id}")    @GetMapping("/{id}")

    @PutMapping("/{id}")

    public ResponseEntity<Empresa> atualizar(@PathVariable Long id, @Valid @RequestBody EmpresaRequestDTO empresaDTO) {    public ResponseEntity<EmpresaDTO> buscarPorId(@PathVariable Long id) {    public ResponseEntity<EmpresaDTO> buscarPorId(@PathVariable Long id) {

        try {

            Empresa empresaAtualizada = empresaService.atualizar(id, empresaDTO);        Optional<EmpresaDTO> empresa = empresaService.buscarPorId(id);        Optional<EmpresaDTO> empresa = empresaService.buscarPorId(id);

            return ResponseEntity.ok(empresaAtualizada);

        } catch (Exception e) {        return empresa.map(ResponseEntity::ok)        return empresa.map(ResponseEntity::ok)

            return ResponseEntity.notFound().build();

        }                     .orElse(ResponseEntity.notFound().build());                     .orElse(ResponseEntity.notFound().build());

    }

    }    }

    @DeleteMapping("/{id}")

    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        try {

            empresaService.deletar(id);    @GetMapping("/cnpj/{cnpj}")    @GetMapping("/cnpj/{cnpj}")

            return ResponseEntity.noContent().build();

        } catch (Exception e) {    public ResponseEntity<EmpresaDTO> buscarPorCnpj(@PathVariable String cnpj) {    public ResponseEntity<EmpresaDTO> buscarPorCnpj(@PathVariable String cnpj) {

            return ResponseEntity.notFound().build();

        }        Optional<EmpresaDTO> empresa = empresaService.buscarPorCnpj(cnpj);        Optional<EmpresaDTO> empresa = empresaService.buscarPorCnpj(cnpj);

    }

}        return empresa.map(ResponseEntity::ok)        return empresa.map(ResponseEntity::ok)

                     .orElse(ResponseEntity.notFound().build());                     .orElse(ResponseEntity.notFound().build());

    }    }



    @GetMapping("/buscar")    @GetMapping("/buscar")

    public Page<EmpresaDTO> buscarPorTermo(    public Page<EmpresaDTO> buscarPorTermo(

            @RequestParam String termo,            @RequestParam String termo,

            @PageableDefault(size = 20, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {            @PageableDefault(size = 20, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {

        return empresaService.buscarPorTermo(termo, pageable);        return empresaService.buscarPorTermo(termo, pageable);

    }    }



    @GetMapping("/estado/{estado}")    @GetMapping("/estado/{estado}")

    public List<Empresa> listarPorEstado(@PathVariable String estado) {    public List<Empresa> listarPorEstado(@PathVariable String estado) {

        return empresaService.listarPorEstado(estado);        return empresaService.listarPorEstado(estado);

    }    }



    @PostMapping    @PostMapping

    public ResponseEntity<Empresa> criar(@Valid @RequestBody Empresa empresa) {    public ResponseEntity<Empresa> criar(@Valid @RequestBody Empresa empresa) {

        try {        try {

            Empresa empresaSalva = empresaService.criar(empresa);            Empresa empresaSalva = empresaService.criar(empresa);

            return ResponseEntity.status(HttpStatus.CREATED).body(empresaSalva);            return ResponseEntity.status(HttpStatus.CREATED).body(empresaSalva);

        } catch (Exception e) {        } catch (Exception e) {

            return ResponseEntity.badRequest().build();            return ResponseEntity.badRequest().build();

        }        }

    }    }



    @PutMapping("/{id}")    @PutMapping("/{id}")

    public ResponseEntity<Empresa> atualizar(@PathVariable Long id, @Valid @RequestBody Empresa empresaAtualizada) {    public ResponseEntity<Empresa> atualizar(@PathVariable Long id, @Valid @RequestBody Empresa empresaAtualizada) {

        try {        try {

            Empresa empresa = empresaService.atualizar(id, empresaAtualizada);            Empresa empresa = empresaService.atualizar(id, empresaAtualizada);

            return ResponseEntity.ok(empresa);            return ResponseEntity.ok(empresa);

        } catch (Exception e) {        } catch (Exception e) {

            return ResponseEntity.notFound().build();            return ResponseEntity.notFound().build();

        }        }

    }    }



    @DeleteMapping("/{id}")    @DeleteMapping("/{id}")

    public ResponseEntity<Void> deletar(@PathVariable Long id) {    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        try {        try {

            empresaService.deletar(id);            empresaService.deletar(id);

            return ResponseEntity.noContent().build();            return ResponseEntity.noContent().build();

        } catch (Exception e) {        } catch (Exception e) {

            return ResponseEntity.notFound().build();            return ResponseEntity.notFound().build();

        }        }

    }    }



    // Endpoints de relatórios    // Endpoints de relatórios

    @GetMapping("/relatorios/total-por-estado")    @GetMapping("/relatorios/total-por-estado")

    public ResponseEntity<Long> contarPorEstado(@RequestParam String estado) {    public ResponseEntity<Long> contarPorEstado(@RequestParam String estado) {

        Long total = empresaService.contarPorEstado(estado);        Long total = empresaService.contarPorEstado(estado);

        return ResponseEntity.ok(total);        return ResponseEntity.ok(total);

    }    }



    @GetMapping("/relatorios/por-estado")    @GetMapping("/relatorios/por-estado")

    public ResponseEntity<List<Object[]>> empresasPorEstado() {    public ResponseEntity<List<Object[]>> empresasPorEstado() {

        List<Object[]> resultado = empresaService.empresasPorEstado();        List<Object[]> resultado = empresaService.empresasPorEstado();

        return ResponseEntity.ok(resultado);        return ResponseEntity.ok(resultado);

    }    }

}import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/empresas")
@CrossOrigin(origins = "*")
public class EmpresaController {

    @Autowired
    private EmpresaRepository empresaRepository;

    @GetMapping
    public Page<EmpresaDTO> listarTodas(
            @PageableDefault(size = 20, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Empresa> empresas = empresaRepository.findAll(pageable);
        List<EmpresaDTO> empresaDTOs = empresas.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(empresaDTOs, pageable, empresas.getTotalElements());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpresaDTO> buscarPorId(@PathVariable Long id) {
        Optional<Empresa> empresa = empresaRepository.findById(id);
        return empresa.map(e -> ResponseEntity.ok(convertToDTO(e)))
                     .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<EmpresaDTO> buscarPorCnpj(@PathVariable String cnpj) {
        Optional<Empresa> empresa = empresaRepository.findByCnpj(cnpj);
        return empresa.map(e -> ResponseEntity.ok(convertToDTO(e)))
                     .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar")
    public Page<EmpresaDTO> buscarPorTermo(
            @RequestParam String termo,
            @PageableDefault(size = 20, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Empresa> empresas = empresaRepository.buscarPorTermo(termo, pageable);
        List<EmpresaDTO> empresaDTOs = empresas.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(empresaDTOs, pageable, empresas.getTotalElements());
    }

    @GetMapping("/estado/{estado}")
    public List<Empresa> listarPorEstado(@PathVariable String estado) {
        return empresaRepository.findByEstado(estado);
    }

    @PostMapping
    public ResponseEntity<Empresa> criar(@Valid @RequestBody Empresa empresa) {
        try {
            Empresa empresaSalva = empresaRepository.save(empresa);
            return ResponseEntity.status(HttpStatus.CREATED).body(empresaSalva);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empresa> atualizar(@PathVariable Long id, @Valid @RequestBody Empresa empresaAtualizada) {
        return empresaRepository.findById(id)
                .map(empresa -> {
                    empresa.setNome(empresaAtualizada.getNome());
                    empresa.setCnpj(empresaAtualizada.getCnpj());
                    empresa.setEmail(empresaAtualizada.getEmail());
                    empresa.setTelefone(empresaAtualizada.getTelefone());
                    empresa.setEndereco(empresaAtualizada.getEndereco());
                    empresa.setCidade(empresaAtualizada.getCidade());
                    empresa.setEstado(empresaAtualizada.getEstado());
                    empresa.setCep(empresaAtualizada.getCep());

                    return ResponseEntity.ok(empresaRepository.save(empresa));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (empresaRepository.existsById(id)) {
            empresaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private EmpresaDTO convertToDTO(Empresa empresa) {
        List<ClienteResumoDTO> clientesResumo = empresa.getClientes().stream()
                .map(this::convertClienteToResumoDTO)
                .collect(Collectors.toList());

        return new EmpresaDTO(
            empresa.getId(),
            empresa.getNome(),
            empresa.getCnpj(),
            empresa.getEmail(),
            empresa.getTelefone(),
            empresa.getEndereco(),
            empresa.getCidade(),
            empresa.getEstado(),
            empresa.getCep(),
            empresa.getDataCriacao(),
            empresa.getDataAtualizacao(),
            clientesResumo
        );
    }

    private ClienteResumoDTO convertClienteToResumoDTO(Cliente cliente) {
        return new ClienteResumoDTO(
            cliente.getId(),
            cliente.getNome(),
            cliente.getEmail(),
            cliente.getTelefone(),
            cliente.getStatus() != null ? cliente.getStatus().toString() : null,
            cliente.getDataCriacao()
        );
    }
}