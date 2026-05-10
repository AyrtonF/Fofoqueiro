# **Guia de Padronização e Boas Práticas \- Frontend (Fofoqueiro)**

## Visão Geral

Este documento descreve os padrões, princípios arquiteturais e boas práticas de organização de código e divisão de camadas utilizados em projetos Spring Boot modernos e bem estruturados. Os conceitos aqui apresentados são generalizáveis para qualquer projeto, mas usam como referência a arquitetura implementada no cpo-digital-api, um projeto que combina:

* Clean Architecture (separação clara de responsabilidades)  
* Arquitetura Hexagonal (ports & adapters)  
* Princípios SOLID (Single Responsibility, Open/Closed, Liskov Substitution, Interface Segregation, Dependency Inversion)  
* Domain-Driven Design (DDD) (linguagem ubíqua, bounded contexts)  
* Padrões de Design (Strategy, Adapter, Mediator, Observer)  
* Testes Abrangentes (unitários, integração, cobertura)

---

## Índice

1. [Princípios Fundamentais](https://markdownlivepreview.com/#princ%C3%ADpios-fundamentais)  
2. [Estrutura de Camadas](https://markdownlivepreview.com/#estrutura-de-camadas)  
3. [Divisão de Responsabilidades](https://markdownlivepreview.com/#divis%C3%A3o-de-responsabilidades)  
4. [Padrões e Práticas](https://markdownlivepreview.com/#padr%C3%B5es-e-pr%C3%A1ticas)  
5. [Fluxo de Dados](https://markdownlivepreview.com/#fluxo-de-dados)  
6. [Testes](https://markdownlivepreview.com/#testes)  
7. [Convenções e Boas Práticas](https://markdownlivepreview.com/#conven%C3%A7%C3%B5es-e-boas-pr%C3%A1ticas)

---

## 1\. Princípios Fundamentais

### 1.1 SOLID

Os princípios SOLID garantem código flexível, testável e fácil de manter:

#### S — Single Responsibility Principle (SRP)

Cada classe deve ter uma única razão para mudar.

Exemplo do cpo-digital-api:

CreateFieldValueUseCaseValidator   → Valida campos e acesso

CreateFieldValueUseCase             → Executa lógica de negócio

FieldValueRepositoryAdapter         → Persiste dados

FieldValueMapper                     → Mapeia entre layers

AuditLogSupport                      → Log de auditoria

Cada classe tem uma responsabilidade clara e bem definida.

#### O — Open/Closed Principle (OCP)

Aberto para extensão, fechado para modificação.

Implementação:

* Usar interfaces (ports) para abstrair comportamentos  
* Novos adapters podem ser adicionados sem modificar casos de uso existentes  
* Validators podem ser estendidos sem alterar a base

#### L — Liskov Substitution Principle (LSP)

Subtipos devem poder substituir seus tipos base sem quebrar funcionalidade.

Exemplo:

interface IFieldValueRepositoryPort {

    FieldValue save(FieldValue model);

    Optional\<FieldValue\> findById(Long id);

}

// Qualquer implementação deve cumprir o contrato

class FieldValueRepositoryAdapter implements IFieldValueRepositoryPort { }

#### I — Interface Segregation Principle (ISP)

Interfaces específicas e focadas são melhores que interfaces genéricas.

Bom exemplo:

interface IAuthenticatedUserPort {

    User getCurrentUser();

}

interface IPasswordEncoderPort {

    String encode(String password);

}

Evitar:

// Ruim \- interface muito ampla

interface IUserServicePort {

    User getCurrentUser();

    String encode(String password);

    void sendEmail(String to, String subject);

    // ... mais 20 métodos

}

#### D — Dependency Inversion Principle (DIP)

Depender de abstrações, não de implementações concretas.

Implementação no cpo-digital-api:

@Service

@RequiredArgsConstructor

public class UpdateUserUseCase implements IUseCase\<UserRequestUpdateDTO, Void\> {

    private final IUserRepositoryPort userRepositoryPort;        // Abstração (port)

    private final IPasswordEncoderPort passwordEncoder;          // Abstração (port)

    private final IDomainProfileRepositoryRepositoryPort profileRepositoryPort; // Abstração

    

    // Use case nunca conhece implementações concretas

}

### 1.2 Clean Architecture

A Clean Architecture organiza o código em camadas concêntricas, cada uma com responsabilidades específicas e regras de dependência bem definidas.

Regra de ouro: Camadas externas dependem de camadas internas, nunca o contrário.

┌─────────────────────────────────────┐

│     PRESENTATION (Controllers)      │ ← Camada mais externa

├─────────────────────────────────────┤

│   APPLICATION (Use Cases, Handlers) │

├─────────────────────────────────────┤

│    DOMAIN (Models, DTOs, Ports)     │ ← Camada mais interna

├─────────────────────────────────────┤

│  INFRASTRUCTURE (Adapters, DB, ORM) │ ← Implementação técnica

└─────────────────────────────────────┘

### 1.3 Arquitetura Hexagonal (Ports & Adapters)

Isola o core do negócio (domain) de detalhes técnicos (banco de dados, APIs externas, frameworks).

Conceitos:

* Ports: Interfaces que definem contratos (IUserRepositoryPort, IEmailServicePort)  
* Adapters: Implementações concretas que cumprem os contratos (UserRepositoryAdapter, EmailServiceAdapter)

Benefício: Trocar um adapter (ex: PostgreSQL → MongoDB) sem afetar o domain.

### 1.4 Domain-Driven Design (DDD)

Organiza o código ao redor da lógica de negócio, usando uma linguagem ubíqua.

Exemplo no cpo-digital-api:

Domain: FieldValue (Valor de Campo)

├── Model: FieldValue (POJO puro, sem dependências)

├── Port: IFieldValueRepositoryPort

├── DTO: FieldValueCreateDTO, FieldValueCreateValidatedDTO

├── Validator: CreateFieldValueUseCaseValidator

└── UseCase: CreateFieldValueUseCase

---

## 2\. Estrutura de Camadas

### 2.1 Organização de Pacotes

com.setd.demo

├── presentation/          → Controllers, handlers (entrada HTTP)

│   ├── controllers/

│   ├── handlers/         → Tratamento de exceções

│   └── security/         → Aspectos, filters de segurança

│

├── application/          → Casos de uso, orquestração

│   ├── use\_cases/        → Implementações de negócio

│   ├── common/           → Base classes, handlers, suporte

│   │   ├── IUseCase.java

│   │   ├── UseCaseHandler.java

│   │   └── AuditLogSupport.java

│   └── validators/       → Validadores compartilhados

│

├── domain/              → Core puro (sem dependências externas)

│   ├── models/          → Entidades de negócio (POJOs)

│   ├── ports/           → Interfaces/contratos

│   ├── dtos/            → Objetos de transferência de dados

│   ├── enums/           → Enumerações de domínio

│   └── exceptions/      → Exceções de negócio

│

├── infrastructure/      → Implementação técnica

│   ├── adapters/        → Implementam ports

│   ├── entities/        → Entidades JPA/ORM

│   ├── repositories/    → Spring Data JPAs

│   ├── mappers/         → Conversão entre layers

│   ├── config/          → Configurações Spring

│   └── security/        → Implementações de segurança

│

└── utils/              → Utilitários e helpers

### 2.2 Camada de Apresentação (Presentation)

Responsabilidade: Recepcionar requisições HTTP, validar dados, delegar ao application layer.

Componentes:

1. Controllers (@RestController)  
   * Expõem endpoints REST  
   * Utilizam validação com @Valid  
   * Chamam UseCaseHandler.execute() para executar casos de uso  
   * Não contêm lógica de negócio  
2. Handlers (@ControllerAdvice)  
   * Captura exceções globalmente  
   * Formata respostas de erro padronizadas  
   * Log centralizado de erros  
3. Aspectos de Segurança  
   * @AllowedRoles annotation para verificar permissões  
   * Aspectos AOP para validação de acesso

Exemplo:

@RestController

@RequestMapping("/api/operations/{operationId}/targets/{targetId}/info-entries/{entryId}/field-values")

@RequiredArgsConstructor

public class FieldValueController {

    

    private final UseCaseHandler useCaseHandler;

    private final CreateFieldValueUseCase createFieldValueUseCase;

    

    @PostMapping

    @AllowedRoles({COORDINATOR, ANALYST})

    public ResponseEntity\<Void\> create(

        @PathVariable Long operationId,

        @PathVariable Long targetId,

        @PathVariable Long entryId,

        @Valid @RequestBody FieldValueCreateDTO dto

    ) {

        dto.setOperationId(operationId);

        dto.setTargetId(targetId);

        dto.setEntryId(entryId);

        

        return useCaseHandler.execute(createFieldValueUseCase, dto);

    }

}

Boas Práticas:

* DTOs separados para request e response  
* Anotações de validação (@Valid, @NotNull, @Email, etc.)  
* Sem chamadas diretas a repositories ou services  
* Sempre usar o UseCaseHandler para executar use cases

### 2.3 Camada de Aplicação (Application)

Responsabilidade: Orquestração de negócio, coordenação entre domínio e infraestrutura.

Componentes:

1. Use Cases (@Service implementando IUseCase\<I,O\>)  
   * Implementam lógica de negócio  
   * Dependem apenas de ports (abstrações)  
   * Realizam transações (@Transactional)  
   * Não conhecem detalhes técnicos (SQL, API calls, etc.)  
2. Validators  
   * Validam regras de negócio complexas  
   * Verificam acesso e autorização  
   * Retornam DTOs validadas (imutáveis)  
   * Lançam exceções específicas  
3. UseCaseHandler  
   * Centraliza execução de use cases  
   * Trata exceções e envolve respostas  
   * Gerencia audit logging  
   * Aplica cooldowns (evitar spam de logs)  
4. AuditLogSupport  
   * Log de todas as mudanças de entidades  
   * Registra usuário, timestamp, operação  
   * Armazena diff (antes/depois) em JSON  
   * Integrado com audit repository port

Exemplo de Use Case:

@Service

@Slf4j

@RequiredArgsConstructor

@Transactional

public class UpdateUserUseCase implements IUseCase\<UserRequestUpdateDTO, Void\> {

    private final IUserRepositoryPort userRepositoryPort;

    private final IPasswordEncoderPort passwordEncoder;

    private final AuditLogSupport auditLogSupport;

    

    @Override

    public Void execute(UserRequestUpdateDTO input) {

        // 1\. Validar entrada

        User user \= userRepositoryPort.findById(input.getId())

            .orElseThrow(() \-\> new EntityNotFoundException("User not found"));

        

        // 2\. Capturar estado anterior

        Map\<String, Object\> before \= captureState(user);

        

        // 3\. Aplicar mudanças

        if (input.getUserUpdateDTO().getName() \!= null) {

            user.setName(input.getUserUpdateDTO().getName());

        }

        if (input.getUserUpdateDTO().getEmail() \!= null) {

            user.setEmail(input.getUserUpdateDTO().getEmail());

        }

        

        // 4\. Persistir

        userRepositoryPort.save(user);

        

        // 5\. Registrar auditoria

        auditLogSupport.log("User", user.getId(), "UPDATE", 

            authenticatedUserPort.getCurrentUser().getId(), null, 

            before, user, null);

        

        return null; // Void use cases retornam null

    }

}

Padrão Base para Use Cases:

O cpo-digital-api fornece classes base genéricas:

public abstract class BaseCreateUseCase\<T, P\> implements IUseCase\<T, Void\> {

    // Lógica comum de criação (transação, auditoria, etc.)

}

public abstract class BaseFindByIdUseCase\<T, R, P\> implements IUseCase\<R, T\> {

    // Lógica comum de busca por ID

}

public abstract class BaseUpdateUseCase\<T, P\> implements IUseCase\<T, Void\> {

    // Lógica comum de atualização

}

public abstract class BaseDeleteByIdUseCase\<P\> implements IUseCase\<P, Void\> {

    // Lógica comum de exclusão

}

Benefícios: Reduz duplicação de código, padroniza transações e auditoria.

Boas Práticas:

* Um use case \= um caso de uso específico  
* Injetar apenas ports (abstrações), nunca implementações  
* Usar @Transactional para garantir atomicidade  
* Lançar exceções específicas (não genéricas)  
* Logar ao iniciar/finalizar operação críticas  
* Chamar AuditLogSupport para mudanças de dados

### 2.4 Camada de Domínio (Domain)

Responsabilidade: Conter a lógica e regras de negócio puras, sem dependências de frameworks.

Componentes:

1. Domain Models (domain/models/)  
   * POJOs simples com @Data, @Builder  
   * Sem anotações JPA (@Entity, @Column, etc.)  
   * Sem dependências de Spring  
   * Representam conceitos de negócio  
   * Podem ter métodos de lógica simples

@Data

@Builder

public class FieldValue {

    private Long id;

    private Long entryId;

    private Long templateFieldId;

    private Long customFieldId;

    private String valueContent;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}

2.   
3. Ports (domain/ports/)  
   * Interfaces que definem contratos  
   * Prefixo I para indicar interface  
   * Métodos que o domain precisa (geralmente persistência, busca externa)  
   * Nunca conhecem detalhes de implementação

public interface IFieldValueRepositoryPort {

    FieldValue save(FieldValue model);

    Optional\<FieldValue\> findById(Long id);

    Optional\<FieldValue\> findByEntryIdAndCustomFieldIdAndActiveTrue(Long entryId, Long customFieldId);

    List\<FieldValue\> findByEntryIdAndActiveTrue(Long entryId);

}

public interface IAuthenticatedUserPort {

    User getCurrentUser();

    Long getCurrentUserId();

    boolean hasRole(String role);

}

4.   
5. DTOs (domain/dtos/)  
   * Objetos simples para transferência de dados  
   * Podem ter validações (@NotNull, @Email, etc.)  
   * DTOs de entrada: UserCreateDTO, UserUpdateDTO  
   * DTOs validados: UserCreateValidatedDTO (post-validation)  
   * DTOs de resposta: UserResponseDTO

@Data

public class FieldValueCreateDTO {

    @NotNull(message \= "Entry ID is required")

    private Long entryId;

    

    private Long templateFieldId;

    private Long customFieldId;

    

    @NotBlank

    private String valueContent;

}

// DTO após validação de regras de negócio

@Data

public class FieldValueCreateValidatedDTO {

    private Long entryId;

    private Long templateFieldId;

    private Long customFieldId;

    private String valueContent;

    private Long currentUserId;        // Resolvido durante validação

    private Long operationId;          // Resolvido durante validação

}

6.   
7. Enums (domain/enums/)  
   * Representam estados e tipos  
   * Sem dependências externas

public enum OperationMemberPermission {

    COORDINATOR,

    ANALYST,

    READER;

}

public enum FieldValueType {

    TEXT,

    DATE,

    BOOLEAN,

    NUMERIC;

}

8.   
9. Exceptions (domain/exceptions/)  
   * Exceções de negócio específicas  
   * Herdam de exceções runtime para não forçar try-catch

public class InvalidOperationException extends RuntimeException {

    public InvalidOperationException(String message) {

        super(message);

    }

}

public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException(String message) {

        super(message);

    }

}

10. 

Boas Práticas:

* Domain layer não deve conhecer Spring, JPA, ou qualquer framework  
* Models são imutáveis ou quase-imutáveis  
* Validações de negócio complexas ficam no domain  
* Ports definem interfaces claras e bem focadas  
* DTOs são simples e sem lógica

### 2.5 Camada de Infraestrutura (Infrastructure)

Responsabilidade: Implementar detalhes técnicos e adaptar o domain a tecnologias específicas.

Componentes:

1. Adapters (infrastructure/adapters/)  
   * Implementam interfaces de port  
   * Convertem entre domain models e infrastructure entities  
   * Chamam Spring Data repositories

@Component

@RequiredArgsConstructor

public class FieldValueRepositoryAdapter implements IFieldValueRepositoryPort {

    private final SpringDataFieldValueRepository repository;

    private final FieldValueMapper mapper;

    

    @Override

    public FieldValue save(FieldValue model) {

        FieldValueEntity entity \= mapper.toEntity(model);

        FieldValueEntity saved \= repository.save(entity);

        return mapper.toModel(saved);

    }

    

    @Override

    public Optional\<FieldValue\> findById(Long id) {

        return repository.findById(id)

            .map(mapper::toModel);

    }

}

2.   
3. Entities (infrastructure/entities/)  
   * Anotadas com @Entity, @Table, @Column  
   * Mapeadas para tabelas do banco  
   * Podem ter relacionamentos (@OneToMany, @ManyToOne, etc.)  
   * Hooks de ciclo de vida (@PrePersist, @PreUpdate)  
   * Nunca são usadas fora da camada de infrastructure

@Entity

@Table(name \= "field\_values")

@Data

@NoArgsConstructor

@AllArgsConstructor

@Builder

public class FieldValueEntity {

    @Id

    @GeneratedValue(strategy \= GenerationType.IDENTITY)

    private Long id;

    

    @Column(name \= "entry\_id", nullable \= false)

    private Long entryId;

    

    @Column(name \= "template\_field\_id")

    private Long templateFieldId;

    

    @Column(name \= "custom\_field\_id")

    private Long customFieldId;

    

    @Column(name \= "value\_content", columnDefinition \= "TEXT")

    private String valueContent;

    

    @Column(name \= "is\_active")

    private Boolean isActive;

    

    @Column(name \= "created\_at", updatable \= false)

    private LocalDateTime createdAt;

    

    @Column(name \= "updated\_at")

    private LocalDateTime updatedAt;

    

    @PrePersist

    protected void onCreate() {

        createdAt \= LocalDateTime.now();

        updatedAt \= LocalDateTime.now();

        isActive \= true;

    }

    

    @PreUpdate

    protected void onUpdate() {

        updatedAt \= LocalDateTime.now();

    }

}

4.   
5. Mappers (infrastructure/mappers/)  
   * Convertem entre Domain Models e Entities  
   * Convertem entre Entities e DTOs  
   * Injetáveis como @Component  
   * Usam bibliotecas como MapStruct ou conversão manual

@Component

public class FieldValueMapper {

    

    public FieldValue toModel(FieldValueEntity entity) {

        if (entity \== null) return null;

        return FieldValue.builder()

            .id(entity.getId())

            .entryId(entity.getEntryId())

            .templateFieldId(entity.getTemplateFieldId())

            .customFieldId(entity.getCustomFieldId())

            .valueContent(entity.getValueContent())

            .isActive(entity.getIsActive())

            .createdAt(entity.getCreatedAt())

            .updatedAt(entity.getUpdatedAt())

            .build();

    }

    

    public FieldValueEntity toEntity(FieldValue model) {

        if (model \== null) return null;

        return FieldValueEntity.builder()

            .id(model.getId())

            .entryId(model.getEntryId())

            .templateFieldId(model.getTemplateFieldId())

            .customFieldId(model.getCustomFieldId())

            .valueContent(model.getValueContent())

            .isActive(model.getIsActive())

            .createdAt(model.getCreatedAt())

            .updatedAt(model.getUpdatedAt())

            .build();

    }

    

    public void updateEntityFromModel(FieldValue model, FieldValueEntity entity) {

        entity.setValueContent(model.getValueContent());

        entity.setIsActive(model.getIsActive());

    }

}

6.   
7. Spring Data Repositories (infrastructure/repositories/)  
   * Estendem JpaRepository  
   * Métodos de consulta apenas (sem lógica complexa)  
   * Servem como adaptadores técnicos para bancos de dados  
   * Nunca são chamados diretamente fora de adapters

@Repository

public interface SpringDataFieldValueRepository 

    extends JpaRepository\<FieldValueEntity, Long\> {

    

    Optional\<FieldValueEntity\> findByEntryIdAndCustomFieldIdAndActiveTrue(

        Long entryId, Long customFieldId);

    

    List\<FieldValueEntity\> findByEntryIdAndActiveTrue(Long entryId);

}

8.   
9. Security Adapters (infrastructure/security/)  
   * Implementam ports de segurança  
   * Integram com Spring Security

@Component

public class AuthenticatedUserAdapter implements IAuthenticatedUserPort {

    

    @Override

    public User getCurrentUser() {

        Authentication auth \= SecurityContextHolder.getContext().getAuthentication();

        if (auth \== null || \!auth.isAuthenticated()) {

            throw new AuthException("User not authenticated");

        }

        return (User) auth.getPrincipal();

    }

    

    @Override

    public Long getCurrentUserId() {

        return getCurrentUser().getId();

    }

}

10. 

Boas Práticas:

* Adapters implementam exatamente um port  
* Entities apenas com anotações JPA  
* Mappers são simples e sem lógica complexa  
* Spring Data repos apenas com queries, sem lógica  
* Nunca expor entities fora da camada de infrastructure

---

## 3\. Divisão de Responsabilidades

### 3.1 Padrão Validator

Validadores separam a lógica de validação do use case, permitindo testes focados e reutilização.

Tipos de validação:

1. Validação Básica (DTOs via annotations)  
   * Feita em tempo de request (@Valid)  
   * Verifica tipos, formatos, ranges  
   * Retorna BadRequest automaticamente  
2. Validação de Regras de Negócio (Validators específicos)  
   * Verificam integridade de dados de negócio  
   * Verificam acesso e autorização  
   * Retornam DTOs validadas ou lançam exceções

Estrutura:

@Component

@RequiredArgsConstructor

@Slf4j

public class CreateFieldValueUseCaseValidator {

    private final IFieldValueRepositoryPort fieldValueRepositoryPort;

    private final IInfoEntryRepositoryPort infoEntryRepositoryPort;

    private final InfoEntryAccessValidator accessValidator;

    

    public FieldValueCreateValidatedDTO validate(FieldValueCreateDTO input, Long operationId, Long targetId) {

        log.info("Validating FieldValue creation for entry: {}", input.getEntryId());

        

        // 1\. Validar que exatamente um dos dois campos foi preenchido

        if ((input.getTemplateFieldId() \== null && input.getCustomFieldId() \== null) ||

            (input.getTemplateFieldId() \!= null && input.getCustomFieldId() \!= null)) {

            throw new IllegalArgumentException(

                "Exactly one of templateFieldId or customFieldId must be provided"

            );

        }

        

        // 2\. Validar que a entrada existe

        InfoEntry entry \= infoEntryRepositoryPort.findById(input.getEntryId())

            .orElseThrow(() \-\> new EntityNotFoundException(

                "InfoEntry not found with id: " \+ input.getEntryId()

            ));

        

        // 3\. Validar acesso (lança AccessDeniedException se não autorizado)

        Long currentUserId \= accessValidator.validateWriteAccess(operationId, targetId);

        

        // 4\. Retornar DTO validada (imutável, contém contexto resolvido)

        return FieldValueCreateValidatedDTO.builder()

            .entryId(input.getEntryId())

            .templateFieldId(input.getTemplateFieldId())

            .customFieldId(input.getCustomFieldId())

            .valueContent(input.getValueContent())

            .currentUserId(currentUserId)

            .operationId(operationId)

            .build();

    }

}

Uso no Use Case:

@Service

@RequiredArgsConstructor

public class CreateFieldValueUseCase implements IUseCase\<FieldValueCreateDTO, Void\> {

    private final CreateFieldValueUseCaseValidator validator;

    private final IFieldValueRepositoryPort fieldValueRepositoryPort;

    private final AuditLogSupport auditLogSupport;

    

    @Override

    @Transactional

    public Void execute(FieldValueCreateDTO input) {

        // 1\. Validar

        FieldValueCreateValidatedDTO validated \= validator.validate(

            input, input.getOperationId(), input.getTargetId()

        );

        

        // 2\. Construir modelo de domínio (seguro pois já foi validado)

        FieldValue fieldValue \= FieldValue.builder()

            .entryId(validated.getEntryId())

            .templateFieldId(validated.getTemplateFieldId())

            .customFieldId(validated.getCustomFieldId())

            .valueContent(validated.getValueContent())

            .isActive(true)

            .build();

        

        // 3\. Persistir

        FieldValue saved \= fieldValueRepositoryPort.save(fieldValue);

        

        // 4\. Auditar

        auditLogSupport.log("FieldValue", saved.getId(), "CREATE", 

            validated.getCurrentUserId(), validated.getOperationId(), 

            null, saved, null);

        

        return null;

    }

}

Benefícios:

* Use case limpo e focado  
* Validadores testáveis isoladamente  
* Separação clara de concerns  
* Reutilização de validadores

### 3.2 Padrão Audit

O audit logging registra todas as mudanças de dados importantes, incluindo quem, quando e o quê foi alterado.

Implementação:

@Component

@RequiredArgsConstructor

@Slf4j

public class AuditLogSupport {

    private final IAuditLogRepositoryPort auditLogRepositoryPort;

    private final IAuthenticatedUserPort authenticatedUserPort;

    private final ObjectMapper objectMapper;

    

    public void log(String entityName, Long entityId, String action, 

                    Long userId, Long operationId, 

                    Object beforeState, Object afterState, Object metadata) {

        try {

            JsonNode before \= beforeState \!= null ? 

                objectMapper.valueToTree(beforeState) : null;

            JsonNode after \= afterState \!= null ? 

                objectMapper.valueToTree(afterState) : null;

            

            // Calcular diff

            JsonNode changes \= calculateDiff(before, after);

            

            AuditLog auditLog \= AuditLog.builder()

                .transactionId(UUID.randomUUID())

                .userId(userId)

                .operationId(operationId)

                .entityName(entityName)

                .entityId(entityId)

                .action(action)

                .changes(changes)

                .metadata(metadata)

                .timestamp(LocalDateTime.now())

                .build();

            

            auditLogRepositoryPort.save(auditLog);

            

        } catch (Exception e) {

            log.error("Error logging audit for entity: {} with id: {}", entityName, entityId, e);

            // Não falhar a operação por causa do audit

        }

    }

    

    private JsonNode calculateDiff(JsonNode before, JsonNode after) {

        // Implementar lógica de diff

        // ...

    }

}

Uso:

// Ao atualizar

User userBefore \= captureState(user); // Capturar antes

user.setName(newName);

userRepositoryPort.save(user);

auditLogSupport.log("User", user.getId(), "UPDATE", 

    userId, operationId, userBefore, user, null);

// Ao deletar

auditLogSupport.log("User", user.getId(), "DELETE", 

    userId, operationId, user, null, null);

Benefícios:

* Rastreabilidade completa  
* Conformidade regulatória  
* Facilita debugging  
* Histórico de mudanças

### 3.3 Padrão Access Control

Validadores de acesso verificam permissões antes de permitir operações.

Implementação:

@Component

@RequiredArgsConstructor

@Slf4j

public class InfoEntryAccessValidator {

    private final IOperationRepositoryPort operationRepositoryPort;

    private final IOperationMemberRepositoryPort operationMemberRepositoryPort;

    private final ITargetRepositoryPort targetRepositoryPort;

    private final IAuthenticatedUserPort authenticatedUserPort;

    

    public Long validateWriteAccess(Long operationId, Long targetId) {

        Long currentUserId \= authenticatedUserPort.getCurrentUserId();

        

        // 1\. Verificar se operação existe

        Operation operation \= operationRepositoryPort.findById(operationId)

            .orElseThrow(() \-\> new EntityNotFoundException(

                "Operation not found with id: " \+ operationId

            ));

        

        // 2\. Verificar se usuário é membro da operação

        OperationMember member \= operationMemberRepositoryPort

            .findByOperationIdAndUserIdAndActiveTrue(operationId, currentUserId)

            .orElseThrow(() \-\> new AccessDeniedException(

                "User is not a member of this operation"

            ));

        

        // 3\. Verificar permissões

        if (member.getPermission() \!= OperationMemberPermission.COORDINATOR) {

            throw new AccessDeniedException(

                "User does not have COORDINATOR permission for this operation"

            );

        }

        

        // 4\. Verificar se target pertence à operação

        Target target \= targetRepositoryPort.findById(targetId)

            .orElseThrow(() \-\> new EntityNotFoundException(

                "Target not found with id: " \+ targetId

            ));

        

        if (\!target.getOperationId().equals(operationId)) {

            throw new AccessDeniedException(

                "Target does not belong to this operation"

            );

        }

        

        return currentUserId;

    }

    

    public Long validateReadAccess(Long operationId, Long targetId) {

        // Validação mais permissiva para leitura

        // ...

    }

}

Boas Práticas:

* Validação de acesso centralizada  
* Lançar exceções específicas (AccessDeniedException)  
* Combinar validação de existência com acesso  
* Logar tentativas de acesso negado

---

## 4\. Padrões e Práticas

### 4.1 Padrão Port & Adapter (Hexagonal Architecture)

Objetivo: Isolar o core de negócio de detalhes técnicos.

Componentes:

Domain (Core)

    ↑

    │ implements

    │

Ports (Interfaces)

    ↑

    │ implements

    │

Adapters (Implementações)

    ↓

    │ usa

    │

External Systems (DB, APIs, Filesystems)

Exemplo prático:

// Port \- Define o contrato

public interface IEmailServicePort {

    void sendEmail(String to, String subject, String body);

}

// Adapter \- Implementação concreta

@Component

@RequiredArgsConstructor

public class EmailServiceAdapter implements IEmailServicePort {

    private final JavaMailSender mailSender;

    

    @Override

    public void sendEmail(String to, String subject, String body) {

        SimpleMailMessage message \= new SimpleMailMessage();

        message.setTo(to);

        message.setSubject(subject);

        message.setText(body);

        mailSender.send(message);

    }

}

// Domain \- Usa apenas a interface

@Service

@RequiredArgsConstructor

public class UserRegistrationUseCase implements IUseCase\<UserCreateDTO, Void\> {

    private final IEmailServicePort emailServicePort;

    

    @Override

    public Void execute(UserCreateDTO input) {

        // ... criar usuário ...

        emailServicePort.sendEmail(

            input.getEmail(), 

            "Welcome", 

            "Welcome to our service"

        );

        return null;

    }

}

Benefícios de trocar implementação:

// Para testes

@Component

public class MockEmailServiceAdapter implements IEmailServicePort {

    @Override

    public void sendEmail(String to, String subject, String body) {

        // Mock implementation

    }

}

// Para produção com outro serviço

@Component

@ConditionalOnProperty(name \= "email.provider", havingValue \= "aws-ses")

public class AwsSesEmailAdapter implements IEmailServicePort {

    // Implementação com AWS SES

}

### 4.2 Padrão Estratégia (Strategy)

Permite selecionar comportamento em runtime.

Exemplo:

// Interface de estratégia

public interface IFieldValueValidationStrategy {

    boolean validate(String value, FieldValueType type);

}

// Implementações concretas

@Component

public class DateFieldValidationStrategy implements IFieldValueValidationStrategy {

    @Override

    public boolean validate(String value, FieldValueType type) {

        if (type \!= FieldValueType.DATE) return true;

        try {

            LocalDate.parse(value, DateTimeFormatter.ISO\_LOCAL\_DATE);

            return true;

        } catch (DateTimeParseException e) {

            return false;

        }

    }

}

@Component

public class NumericFieldValidationStrategy implements IFieldValueValidationStrategy {

    @Override

    public boolean validate(String value, FieldValueType type) {

        if (type \!= FieldValueType.NUMERIC) return true;

        try {

            Double.parseDouble(value);

            return true;

        } catch (NumberFormatException e) {

            return false;

        }

    }

}

// Uso

@Component

@RequiredArgsConstructor

public class FieldValueValidator {

    private final List\<IFieldValueValidationStrategy\> strategies;

    

    public boolean validate(String value, FieldValueType type) {

        return strategies.stream()

            .allMatch(strategy \-\> strategy.validate(value, type));

    }

}

### 4.3 Padrão Cadeia de Responsabilidade (Chain of Responsibility)

Permite passar requisições através de uma cadeia de manipuladores.

Exemplo com Filtros de Segurança:

@Component

public class SecurityFilterChain {

    private List\<SecurityFilter\> filters \= new ArrayList\<\>();

    

    public SecurityFilterChain() {

        filters.add(new AuthenticationFilter());

        filters.add(new AuthorizationFilter());

        filters.add(new RateLimitFilter());

    }

    

    public void processRequest(HttpRequest request) {

        for (SecurityFilter filter : filters) {

            if (\!filter.process(request)) {

                throw new SecurityException("Request rejected by " \+ filter.getClass().getSimpleName());

            }

        }

    }

}

### 4.4 Padrão Observer

Notifica múltiplos observadores quando um evento ocorre.

Exemplo com Domain Events:

// Event

public class UserCreatedEvent {

    private Long userId;

    private String email;

    private LocalDateTime createdAt;

}

// Observer

public interface UserEventListener {

    void onUserCreated(UserCreatedEvent event);

}

// Implementações

@Component

public class SendWelcomeEmailListener implements UserEventListener {

    @Override

    public void onUserCreated(UserCreatedEvent event) {

        // Enviar email de boas-vindas

    }

}

// Publisher

@Component

@RequiredArgsConstructor

public class UserEventPublisher {

    private final List\<UserEventListener\> listeners;

    

    public void publishUserCreated(UserCreatedEvent event) {

        listeners.forEach(listener \-\> listener.onUserCreated(event));

    }

}

// Uso em Use Case

@Service

@RequiredArgsConstructor

public class CreateUserUseCase {

    private final UserEventPublisher eventPublisher;

    

    public void execute(UserCreateDTO input) {

        User user \= new User(input);

        // ... persistir ...

        eventPublisher.publishUserCreated(

            new UserCreatedEvent(user.getId(), user.getEmail(), LocalDateTime.now())

        );

    }

}

---

## 5\. Fluxo de Dados

### 5.1 Fluxo Completo de uma Requisição

1\. HTTP REQUEST

   ↓

2\. CONTROLLER

   \- Recebe requisição

   \- Valida DTO com @Valid

   \- Extrai parâmetros de path/query

   \- Cria DTO de entrada

   ↓

3\. USE CASE HANDLER

   \- Executa use case

   \- Captura exceções

   ↓

4\. VALIDATOR

   \- Valida regras de negócio

   \- Verifica acesso

   \- Retorna DTO validada ou lança exceção

   ↓

5\. USE CASE

   \- Aplica lógica de negócio

   \- Chama ports para persistência

   ↓

6\. ADAPTER

   \- Converte entre domain model e entity

   \- Chama Spring Data repository

   ↓

7\. SPRING DATA JPA

   \- Executa query no banco

   \- Retorna entidade (ou lista)

   ↓

8\. ADAPTER (voltar)

   \- Mapeia entity para domain model

   \- Retorna para use case

   ↓

9\. USE CASE (finalizar)

   \- Registra auditoria

   \- Retorna resultado

   ↓

10\. USE CASE HANDLER

    \- Envolve resposta em ResponseEntity

    ↓

11\. CONTROLLER

    \- Retorna ResponseEntity

    ↓

12\. HTTP RESPONSE

    \- Status 200, JSON serializado

### 5.2 Exemplo Prático: Criar um Field Value

Entrada:

POST /api/operations/1/targets/2/info-entries/3/field-values

Content-Type: application/json

{

  "customFieldId": 10,

  "valueContent": "Some value"

}

Passo 1: Controller

@PostMapping

public ResponseEntity\<Void\> create(

    @PathVariable Long operationId,      // 1

    @PathVariable Long targetId,         // 2

    @PathVariable Long entryId,          // 3

    @Valid @RequestBody FieldValueCreateDTO dto  // customFieldId: 10, valueContent: "Some value"

) {

    dto.setOperationId(operationId);

    dto.setTargetId(targetId);

    dto.setEntryId(entryId);

    

    return useCaseHandler.execute(createFieldValueUseCase, dto);

}

Passo 2: UseCaseHandler

public \<I, O\> ResponseEntity\<O\> execute(IUseCase\<I, O\> useCase, I input) {

    try {

        O result \= executeAndReturn(useCase, input);

        return ResponseEntity.ok(result);

    } catch (EntityNotFoundException e) {

        // ...handle error

    }

}

Passo 3: Validator

public FieldValueCreateValidatedDTO validate(FieldValueCreateDTO input, Long operationId, Long targetId) {

    // Validar que customFieldId é válido

    // Validar que entryId existe

    // Validar que usuário tem acesso

    // Retornar DTO validada com currentUserId resolvido

    return FieldValueCreateValidatedDTO.builder()

        .entryId(input.getEntryId())

        .customFieldId(input.getCustomFieldId())

        .valueContent(input.getValueContent())

        .currentUserId(123)  // Resolvido durante validação

        .operationId(1)

        .build();

}

Passo 4: UseCase

public Void execute(FieldValueCreateDTO input) {

    // Validar

    FieldValueCreateValidatedDTO validated \= validator.validate(

        input, input.getOperationId(), input.getTargetId()

    );

    

    // Criar domain model

    FieldValue fieldValue \= FieldValue.builder()

        .entryId(validated.getEntryId())

        .customFieldId(validated.getCustomFieldId())

        .valueContent(validated.getValueContent())

        .isActive(true)

        .build();

    

    // Persistir

    FieldValue saved \= fieldValueRepositoryPort.save(fieldValue);

    

    // Auditar

    auditLogSupport.log("FieldValue", saved.getId(), "CREATE", 

        validated.getCurrentUserId(), validated.getOperationId(), 

        null, saved, null);

    

    return null;

}

Passo 5: Adapter

public FieldValue save(FieldValue model) {

    // Converter domain model para entity

    FieldValueEntity entity \= mapper.toEntity(model);

    

    // Chamar Spring Data JPA

    FieldValueEntity saved \= repository.save(entity);

    

    // Converter de volta para domain model

    return mapper.toModel(saved);

}

Passo 6: Spring Data JPA

// Salvar no banco (JPA handle insert)

FieldValueEntity saved \= repository.save(entity);

// INSERT INTO field\_values (entry\_id, custom\_field\_id, value\_content, is\_active, created\_at, updated\_at)

// VALUES (3, 10, 'Some value', true, now(), now())

// RETURNING id, ...

Resposta:

HTTP/1.1 200 OK

Content-Type: application/json

---

## 6\. Testes

### 6.1 Estrutura de Testes

src/test/java

├── com/setd/demo/

│   ├── presentation/

│   │   ├── controllers/

│   │   │   └── FieldValueControllerTest.java

│   │   └── security/

│   │       └── AllowedRolesAspectTest.java

│   │

│   ├── application/

│   │   ├── use\_cases/

│   │   │   └── field\_value/

│   │   │       ├── CreateFieldValueUseCaseTest.java

│   │   │       ├── UpdateFieldValueUseCaseTest.java

│   │   │       └── DeleteFieldValueUseCaseTest.java

│   │   └── common/

│   │       ├── UseCaseHandlerTest.java

│   │       └── AuditLogSupportTest.java

│   │

│   ├── domain/

│   │   ├── models/

│   │   │   └── FieldValueTest.java

│   │   └── ports/

│   │       └── IFieldValueRepositoryPortTest.java

│   │

│   ├── infrastructure/

│   │   ├── adapters/

│   │   │   └── FieldValueRepositoryAdapterTest.java

│   │   ├── entities/

│   │   │   └── FieldValueEntityTest.java

│   │   └── mappers/

│   │       └── FieldValueMapperTest.java

│   │

│   ├── integration/

│   │   ├── field\_value/

│   │   │   ├── CreateFieldValueIntegrationTest.java

│   │   │   ├── UpdateFieldValueIntegrationTest.java

│   │   │   └── DeleteFieldValueIntegrationTest.java

│   │   └── support/

│   │       └── TestFixtures.java

│   │

│   └── coverage/

│       └── CoreCoverageTest.java

### 6.2 Testes Unitários

Testes rápidos, isolados, sem dependências externas.

Exemplo \- Teste de Validator:

@ExtendWith(MockitoExtension.class)

class CreateFieldValueUseCaseValidatorTest {

    

    @Mock

    private IFieldValueRepositoryPort fieldValueRepositoryPort;

    

    @Mock

    private IInfoEntryRepositoryPort infoEntryRepositoryPort;

    

    @Mock

    private InfoEntryAccessValidator accessValidator;

    

    @InjectMocks

    private CreateFieldValueUseCaseValidator validator;

    

    @Test

    void shouldValidateSuccessfully() {

        // Arrange

        Long operationId \= 1L;

        Long targetId \= 2L;

        Long entryId \= 3L;

        FieldValueCreateDTO dto \= new FieldValueCreateDTO();

        dto.setEntryId(entryId);

        dto.setCustomFieldId(10L);

        dto.setValueContent("Some value");

        

        when(infoEntryRepositoryPort.findById(entryId))

            .thenReturn(Optional.of(new InfoEntry()));

        when(accessValidator.validateWriteAccess(operationId, targetId))

            .thenReturn(100L);

        

        // Act

        FieldValueCreateValidatedDTO result \= validator.validate(dto, operationId, targetId);

        

        // Assert

        assertThat(result).isNotNull();

        assertThat(result.getEntryId()).isEqualTo(entryId);

        assertThat(result.getCurrentUserId()).isEqualTo(100L);

    }

    

    @Test

    void shouldThrowExceptionWhenBothFieldsProvided() {

        // Arrange

        FieldValueCreateDTO dto \= new FieldValueCreateDTO();

        dto.setEntryId(3L);

        dto.setTemplateFieldId(5L);

        dto.setCustomFieldId(10L);  // Ambos preenchidos\!

        dto.setValueContent("Some value");

        

        // Act & Assert

        assertThatThrownBy(() \-\> validator.validate(dto, 1L, 2L))

            .isInstanceOf(IllegalArgumentException.class);

    }

    

    @Test

    void shouldThrowExceptionWhenEntryNotFound() {

        // Arrange

        FieldValueCreateDTO dto \= new FieldValueCreateDTO();

        dto.setEntryId(999L);

        dto.setCustomFieldId(10L);

        dto.setValueContent("Some value");

        

        when(infoEntryRepositoryPort.findById(999L))

            .thenReturn(Optional.empty());

        

        // Act & Assert

        assertThatThrownBy(() \-\> validator.validate(dto, 1L, 2L))

            .isInstanceOf(EntityNotFoundException.class);

    }

}

Exemplo \- Teste de UseCase:

@ExtendWith(MockitoExtension.class)

class CreateFieldValueUseCaseTest {

    

    @Mock

    private IFieldValueRepositoryPort fieldValueRepositoryPort;

    

    @Mock

    private CreateFieldValueUseCaseValidator validator;

    

    @Mock

    private AuditLogSupport auditLogSupport;

    

    @InjectMocks

    private CreateFieldValueUseCase useCase;

    

    @Test

    void shouldCreateFieldValueSuccessfully() {

        // Arrange

        FieldValueCreateDTO input \= new FieldValueCreateDTO();

        input.setEntryId(3L);

        input.setCustomFieldId(10L);

        input.setValueContent("Test value");

        input.setOperationId(1L);

        input.setTargetId(2L);

        

        FieldValueCreateValidatedDTO validated \= FieldValueCreateValidatedDTO.builder()

            .entryId(3L)

            .customFieldId(10L)

            .valueContent("Test value")

            .currentUserId(100L)

            .operationId(1L)

            .build();

        

        FieldValue savedModel \= FieldValue.builder()

            .id(999L)

            .entryId(3L)

            .customFieldId(10L)

            .valueContent("Test value")

            .isActive(true)

            .build();

        

        when(validator.validate(input, input.getOperationId(), input.getTargetId()))

            .thenReturn(validated);

        when(fieldValueRepositoryPort.save(any(FieldValue.class)))

            .thenReturn(savedModel);

        

        // Act

        Void result \= useCase.execute(input);

        

        // Assert

        assertThat(result).isNull();  // Void use cases retornam null

        verify(fieldValueRepositoryPort).save(any(FieldValue.class));

        verify(auditLogSupport).log("FieldValue", 999L, "CREATE", 100L, 1L, null, savedModel, null);

    }

}

Boas Práticas:

* Um teste por comportamento esperado  
* Nomenclatura clara: should\[ExpectedBehavior\]When\[Condition\]  
* AAA pattern: Arrange, Act, Assert  
* Usar mocks para dependências externas  
* Testes rápidos (\< 1ms por teste)  
* Sem I/O real (banco, rede, filesystem)

### 6.3 Testes de Integração

Testes mais lentos que validam fluxos completos com banco de dados real.

Exemplo:

@SpringBootTest(webEnvironment \= WebEnvironment.RANDOM\_PORT)

@ActiveProfiles("test")

@Transactional

class CreateFieldValueIntegrationTest {

    

    @Autowired

    private TestRestTemplate restTemplate;

    

    @Autowired

    private FieldValueRepository fieldValueRepository;

    

    @Autowired

    private TestFixtures testFixtures;

    

    @Test

    void shouldCreateFieldValueEndToEnd() {

        // Arrange

        Operation operation \= testFixtures.createOperation();

        Target target \= testFixtures.createTarget(operation);

        InfoEntry entry \= testFixtures.createInfoEntry(target);

        CustomField customField \= testFixtures.createCustomField();

        User coordinator \= testFixtures.createCoordinator(operation);

        

        FieldValueCreateDTO dto \= FieldValueCreateDTO.builder()

            .customFieldId(customField.getId())

            .valueContent("Integration test value")

            .build();

        

        // Act

        ResponseEntity\<Void\> response \= restTemplate.withBasicAuth(

            coordinator.getEmail(), "password"

        ).postForEntity(

            "/api/operations/{id}/targets/{targetId}/info-entries/{entryId}/field-values",

            dto,

            Void.class,

            operation.getId(),

            target.getId(),

            entry.getId()

        );

        

        // Assert

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        

        Optional\<FieldValue\> saved \= fieldValueRepository.findById(entry.getId());

        assertThat(saved).isPresent();

        assertThat(saved.get().getValueContent()).isEqualTo("Integration test value");

    }

    

    @Test

    void shouldRejectWithoutWriteAccess() {

        // Arrange

        Operation operation \= testFixtures.createOperation();

        Target target \= testFixtures.createTarget(operation);

        InfoEntry entry \= testFixtures.createInfoEntry(target);

        User reader \= testFixtures.createReader(operation);  // Apenas leitura

        

        FieldValueCreateDTO dto \= FieldValueCreateDTO.builder()

            .customFieldId(10L)

            .valueContent("Unauthorized")

            .build();

        

        // Act

        ResponseEntity\<Void\> response \= restTemplate.withBasicAuth(

            reader.getEmail(), "password"

        ).postForEntity(

            "/api/operations/{id}/targets/{targetId}/info-entries/{entryId}/field-values",

            dto,

            Void.class,

            operation.getId(),

            target.getId(),

            entry.getId()

        );

        

        // Assert

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

    }

}

Boas Práticas:

* Usar @SpringBootTest com banco teste (H2 ou TestContainers)  
* Usar TestFixtures para preparar dados consistentes  
* Testar fluxo completo (controller até banco)  
* Testar casos de sucesso e erro  
* Usar @Transactional para rollback automático

### 6.4 Cobertura de Testes

Objetivo: \> 80% de cobertura em código crítico.

\# Gerar relatório

mvn clean test jacoco:report

\# Visualizar em target/site/jacoco/index.html

O que cobrir:

* Lógica de negócio (validators, use cases)  
* Caminhos de erro (exceções)  
* Casos extremos (valores nulos, listas vazias)  
* Integração entre camadas

O que não é crítico:

* Getters/setters automáticos (@Data)  
* Anotações de configuração Spring  
* Conversões triviais

### 6.5 Fixtures e Builders

Use builders para criação consistente de dados de teste.

Exemplo:

@Component

public class TestFixtures {

    

    private final UserRepository userRepository;

    private final OperationRepository operationRepository;

    

    public User createUser(String email, String name) {

        User user \= User.builder()

            .email(email)

            .name(name)

            .password("encoded-password")

            .build();

        return userRepository.save(user);

    }

    

    public Operation createOperation(String name) {

        Operation operation \= Operation.builder()

            .name(name)

            .startDate(LocalDate.now())

            .build();

        return operationRepository.save(operation);

    }

    

    public OperationMember createCoordinator(Operation operation, User user) {

        return OperationMember.builder()

            .operation(operation)

            .user(user)

            .permission(OperationMemberPermission.COORDINATOR)

            .build();

    }

}

---

## 7\. Convenções e Boas Práticas

### 7.1 Nomenclatura

| Elemento | Convenção | Exemplo |
| ----- | ----- | ----- |
| Classes | PascalCase | FieldValueCreateUseCase, UserResponseDTO |
| Métodos | camelCase | validateWriteAccess(), getCurrentUser() |
| Constantes | UPPER\_SNAKE\_CASE | MAX\_RETRY\_ATTEMPTS, DEFAULT\_PAGE\_SIZE |
| Interfaces | Prefixo I \+ PascalCase | IUserRepositoryPort, IEmailServicePort |
| Adapters | NomeDo \+ Adapter | UserRepositoryAdapter, AwsSesEmailAdapter |
| Entities | NomeDo \+ Entity | UserEntity, FieldValueEntity |
| DTOs | NomeDo \+ DTO (+ contexto) | UserCreateDTO, UserResponseDTO, UserCreateValidatedDTO |
| Validators | NomeDo \+ UseCaseValidator | CreateUserUseCaseValidator |
| Repositories JPA | SpringData \+ NomeDo | SpringDataUserRepository |
| Mappers | NomeDo \+ Mapper | UserMapper |

### 7.2 Estrutura de Pacotes

Estrutura hierárquica baseada em funcionalidade:

com.setd.demo

├── application.use\_cases.user.\*          (User-related use cases)

├── application.use\_cases.operation.\*     (Operation-related use cases)

├── application.use\_cases.field\_value.\*   (FieldValue-related use cases)

├── domain.models.user.\*                  (User models)

├── domain.models.operation.\*             (Operation models)

├── infrastructure.entities.user.\*        (User entities)

├── infrastructure.repositories.user.\*    (User repositories)

Benefício: Fácil encontrar código relacionado; organização clara por domínio.

### 7.3 Dependências entre Camadas

PRESENTATION → APPLICATION → DOMAIN ← INFRASTRUCTURE

    ↓            ↓          ↑    ↑

Controllers  UseCases   Models Adapters

             Validators Ports  Entities

             Handlers   DTOs   Repositories

Regra Crítica: Camadas internas NUNCA dependem de camadas externas.

✅ Correto:

* UseCase injeta IUserRepositoryPort (port)  
* Controller injeta UseCaseHandler  
* Adapter injeta SpringDataUserRepository (JPA)

❌ Incorreto:

* Domain model injeta @Autowired UserService  
* UseCase injeta UserEntity diretamente  
* Adapter expõe JpaRepository para controller

### 7.4 Anotações e Decoradores

// Camada de Presentation

@RestController

@RequestMapping("/api/...")

@RequiredArgsConstructor

// Camada de Application

@Service                    // UseCase é um service

@RequiredArgsConstructor

@Transactional             // Casos de uso que modificam dados

@Slf4j                     // Logging

// Camada de Infrastructure

@Component                 // Adapters, mappers

@Repository               // Adapters de repositório

@RequiredArgsConstructor

// Dados

@Data                      // Getters, setters, equals, hashCode

@Builder                   // Builder pattern

@Entity

@Table(name \= "...")

### 7.5 Tratamento de Exceções

Hierarquia de exceções:

Exception (Java padrão)

├── RuntimeException (não verificadas)

│   ├── EntityNotFoundException (entidade não encontrada)

│   ├── AccessDeniedException (sem permissão)

│   ├── IllegalArgumentException (argumento inválido)

│   ├── InvalidOperationException (operação inválida)

│   ├── AuthException (autenticação falhou)

│   └── ... (outras de negócio)

└── IOException (exceções verificadas)

Global Exception Handler:

@ControllerAdvice

@Slf4j

public class GlobalExceptionHandler {

    

    @ExceptionHandler(EntityNotFoundException.class)

    public ResponseEntity\<ErrorResponse\> handleNotFound(EntityNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT\_FOUND)

            .body(new ErrorResponse(ex.getMessage()));

    }

    

    @ExceptionHandler(AccessDeniedException.class)

    public ResponseEntity\<ErrorResponse\> handleAccessDenied(AccessDeniedException ex) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN)

            .body(new ErrorResponse(ex.getMessage()));

    }

    

    @ExceptionHandler(IllegalArgumentException.class)

    public ResponseEntity\<ErrorResponse\> handleBadRequest(IllegalArgumentException ex) {

        return ResponseEntity.status(HttpStatus.BAD\_REQUEST)

            .body(new ErrorResponse(ex.getMessage()));

    }

}

### 7.6 Logging

// Use SLF4J com @Slf4j do Lombok

@Slf4j

public class CreateUserUseCase {

    

    public Void execute(UserCreateDTO input) {

        log.info("Starting user creation for email: {}", input.getEmail());

        

        try {

            // ... lógica ...

            log.info("User created successfully with id: {}", user.getId());

        } catch (Exception e) {

            log.error("Error creating user for email: {}", input.getEmail(), e);

            throw e;

        }

    }

}

Níveis:

* trace() \- Informação muito detalhada (raramente usado)  
* debug() \- Informação de debugging (desenvolvimento)  
* info() \- Informação geral importante (produção)  
* warn() \- Aviso de algo inesperado  
* error() \- Erro que precisa atenção

### 7.7 Documentação

JavaDoc para interfaces e métodos públicos:

/\*\*

 \* Valida regras de negócio para criação de FieldValue.

 \* 

 \* @param input DTO com dados da entrada do usuário

 \* @param operationId ID da operação

 \* @param targetId ID do alvo

 \* @return DTO validada com contexto resolvido (userId, operationId)

 \* @throws IllegalArgumentException se ambos ou nenhum campo foram preenchidos

 \* @throws EntityNotFoundException se entrada, campo ou operação não existem

 \* @throws AccessDeniedException se usuário não tem permissão de escrita

 \*/

public FieldValueCreateValidatedDTO validate(

    FieldValueCreateDTO input, 

    Long operationId, 

    Long targetId

) { }

### 7.8 Performance

Boas Práticas:

Lazy Loading com cuidado:  
// Evitar N+1 queries

@OneToMany(fetch \= FetchType.LAZY)  // ✅ Bom

private List\<FieldValue\> fieldValues;

// Use @EntityGraph para queries específicas

@EntityGraph(attributePaths \= {"fieldValues"})

Optional\<InfoEntry\> findByIdWithFieldValues(Long id);

1. 

Paginação:  
public interface IFieldValueRepositoryPort {

    Page\<FieldValue\> findByEntryIdAndActiveTrue(Long entryId, Pageable pageable);

}

2. 

Índices no banco:  
CREATE INDEX idx\_field\_value\_entry\_active 

ON field\_values(entry\_id, is\_active);

3. 

Connection pooling:  
spring:

  datasource:

    hikari:

      maximum-pool-size: 20

      minimum-idle: 5

4. 

### 7.9 Segurança

Validar entrada sempre:  
@Valid @RequestBody UserCreateDTO dto

1. 

Verificar acesso antes de operação:  
accessValidator.validateWriteAccess(operationId, targetId);

2. 

Não expor IDs sensíveis:  
// ✅ Bom

public UserResponseDTO toResponseDTO(User user) {

    return new UserResponseDTO(user.getName(), user.getEmail());

    // ID e senha não inclusos

}

3. 

Criptografar senhas:  
user.setPassword(passwordEncoderPort.encode(plainPassword));

4. 

HTTPS em produção:  
server:

  port: 443

  ssl:

    key-store: classpath:keystore.jks

5. 

---

## 8\. Checklist para Novos Projetos

Ao iniciar um novo projeto Spring Boot, siga este checklist para manter a qualidade arquitetural:

### Setup Inicial

*  Estrutura de pacotes criada (presentation, application, domain, infrastructure, utils)  
*  pom.xml com dependências essenciais (Spring Boot, JPA, Validation, Logging, Tests)  
*  Profiles de configuração (application.yaml, application-dev.yaml, application-test.yaml)  
*  Lombok configurado (@Data, @Builder, @Slf4j)

### Camada de Domínio

*  Models definidos (POJOs sem framework)  
*  Ports (interfaces) definidas para cada operação técnica  
*  DTOs de entrada, validada e resposta  
*  Exceptions específicas de negócio  
*  Enums de tipos/estados

### Camada de Aplicação

*  IUseCase\<I, O\> implementada por todos os cases de uso  
*  UseCaseHandler centralizado  
*  Validators para regras de negócio  
*  AuditLogSupport integrado  
*  @Transactional em operações de escrita

### Camada de Apresentação

*  Controllers com @RestController  
*  DTOs com validação (@Valid, @NotNull, etc.)  
*  Exception handler global (@ControllerAdvice)  
*  Documentação Swagger/OpenAPI

### Camada de Infraestrutura

*  Adapters implementando ports  
*  JPA Entities com @Entity  
*  Spring Data Repositories simples  
*  Mappers entre layers  
*  Config classes para beans custom

### Testes

*  Testes unitários para validators e use cases  
*  Testes de integração para fluxos completos  
*  TestFixtures para dados consistentes  
*  Coverage \> 80% em código crítico  
*  Testes de acesso negado

### Qualidade

*  Code review checklist  
*  Sonar analysis (se disponível)  
*  Convention sobre configuration  
*  Logging em pontos críticos  
*  Documentação JavaDoc

---

## 9\. Conclusão

A arquitetura bem estruturada com Clean Architecture, ports & adapters, SOLID e testes abrangentes traz benefícios significativos:

✅ Manutenibilidade: Código organizado, fácil de encontrar e modificar ✅ Testabilidade: Isolamento entre camadas permite testes focados ✅ Escalabilidade: Novos recursos podem ser adicionados sem quebrar existentes ✅ Reusabilidade: Validators, adapters e utilities podem ser reutilizados ✅ Rastreabilidade: Audit logging completo de todas as mudanças ✅ Segurança: Validação e controle de acesso em camadas apropriadas

O cpo-digital-api exemplifica esses princípios na prática, oferecendo um modelo sólido para novos projetos.  
