# VagaBounds

---

## Visão Geral

VagaBounds é uma API RESTful desenvolvida em Java com Spring Boot, cujo objetivo é servir como plataforma central de cadastro e gestão de vagas de tecnologia para empresas e candidatos. Através desta API, diversas filiais de um mesmo grupo empresarial podem consumir e gerenciar de forma unificada:

* Cadastro de **Company**
* Cadastro e gerenciamento de **Job**
* Cadastro e gerenciamento de **Candidate**
* Processo de **Application** às vagas
* Relatórios de vagas e candidatos inscritos

---

## Domínio e Regras de Negócio

Entidades principais (nomes em Inglês) com `id: Long` auto-increment:

* **Company**

    * `id: Long`
    * `name: String`
    * `location: String` (inclui remote/hybrid)
    * `email: String`
    * `password: String` (armazenada com hash)

* **Job**

    * `id: Long`
    * `companyId: Long` (FK para Company)
    * `title: String`
    * `description: String`
    * `jobType: Enum {INTERNSHIP, TRAINEE, FULL_TIME, PART_TIME}`
    * `requirements: List<String>`
    * `desiredSkills: List<String>`
    * `deadline: LocalDate` (data limite de inscrição)
    * **Regras específicas:**

        * **INTERNSHIP:** exige `semester` (int) e `course` (String)
        * **TRAINEE:** exige `graduationYear` (int)
    * Empresas podem **prorrogar** o `deadline` antes de expirar.

* **Candidate**

    * `id: Long`
    * `name: String`
    * `email: String`
    * `password: String` (hash)
    * `education: Enum {NONE, UNDERGRAD, GRADUATE}`
    * `course: String` (opcional)
    * `graduationYear: Integer` (opcional)
    * `resumeUrl: String`

* **Application**

    * `id: Long`
    * `candidateId: Long` (FK)
    * `jobId: Long` (FK)
    * `appliedAt: LocalDateTime`
    * **Regras de Consistência:**

        * `UNDERGRAD` pode se candidatar apenas a **INTERNSHIP**
        * `GRADUATE` pode se candidatar a **TRAINEE** ou **FULL\_TIME**
        * Após `deadline`, novas candidaturas são rejeitadas.

---

## Requisitos Funcionais

1. **Autenticação e Autorização** via JWT para Company e Candidate.
2. **Endpoints CRUD** para todas as entidades: `/companies`, `/jobs`, `/candidates`, `/applications`.
3. **Filtros e Paginação** em listagens (ex.: listar vagas abertas).
4. **Upload de Currículo** para Candidate (armazenar em AWS S3 ou local).
5. **Relatórios de Vagas**: por empresa e global.
6. **Notificações por E-mail**: ao prorrogar deadline ou quando candidato aplicar.

---

## Funcionalidades Principais

### Autenticação e Registro
- Login para empresas e candidatos
- Registro de empresas
- Registro de candidatos

### Gestão de Vagas
- Cadastro de vagas (estágio, trainee, emprego)
- Consulta de vagas ativas
- Atualização de informações da vaga
- Prorrogação do prazo de inscrição
- Relatórios de vagas por empresa e grupo empresarial

### Candidaturas
- Aplicação para vagas com envio de currículo
- Verificação automática de requisitos
- Gestão do status das candidaturas
- Visualização de candidatos por vaga

### Relatórios
- Relatório de vagas por empresa
- Relatório de vagas por grupo empresarial
- Estatísticas gerais do sistema

---
## Tecnologias Utilizadas

- Java 21
- Maven
- Spring Boot 3.5.0
- Spring Web
- Spring Security
- Spring Data JPA
- PostgreSQL Driver
- Lombok
- Spring Boot DevTools

**Iniciando o Projeto Spring Boot:**

```bash
# Criar projeto via Spring Initializr (start.spring.io)
# Dependencies: Spring Web, Spring Data JPA, PostgreSQL Driver,
# Spring Security, Jakarta Validation, Lombok, Springdoc OpenAPI

git clone git@github.com:devenzonascimento/vagabounds-api.git
cd vagabounds-api

# Configurar aplicação (application.properties ou .env)
# SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/vagabounds
# SPRING_DATASOURCE_USERNAME=postgres
# SPRING_DATASOURCE_PASSWORD=senha
# JWT_SECRET=secreto

./mvnw clean install
./mvnw spring-boot:run
```

---

## Organização de Código e Padrões

```
src
 └── main
     ├── java
     │   └── com.vagabounds
     │       ├── controllers       # Controladores REST
     │       ├── dtos              # Data Transfer Objects
     │       ├── exceptions        # Exception Handlers
     │       ├── models            # JPA Entities
     │       ├── repositories      # Spring Data Repositories
     │       ├── security          # JWT & Security Config
     │       ├── services          # Business Logic
     │       └── VagaBoundsApplication.java # Classe principal
     └── resources
         ├── application.properties
```

---

## To-Do List de Features

* [x] Projeto inicial via Spring Initializr
* [ ] Configurar Docker Compose para PostgreSQL
* [ ] **AuthenticationService** (JWT)
* [ ] CRUD de **Company**
* [ ] CRUD de **Job** com regras de deadline
* [ ] CRUD de **Candidate**, upload de resume
* [ ] Endpoint de **Application** com validações
* [ ] Relatórios de vagas por Company e geral
* [ ] Documentação Swagger/OpenAPI
* [ ] Testes unitários e de integração
* [ ] Configurar Checkstyle e Spotless
* [ ] GitHub Actions CI/CD
* [ ] Envio de e-mails de notificação
* [ ] Logs estruturados

---

## Contribuições

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Faça commit de suas alterações (`git commit -m 'Adiciona nova feature'`)
4. Envie para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request
