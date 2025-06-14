# Domain Model: Empresas e Grupos - VagaBounds

Este documento foca no **domínio de empresas** (companhias, filiais, grupos) e suas **relações** e **features**.

---

## 1. Entidades Principais

| Entidade        | Descrição                                                                         | Atributos-chave                                                          | Relacionamentos                                            |
| --------------- | --------------------------------------------------------------------------------- | ------------------------------------------------------------------------ | ---------------------------------------------------------- |
| **Group**       | Agrupamento de várias empresas/filiais pertencentes a um mesmo grupo corporativo. | id: Long<br/>name: String                                                | 1 Group → \* Companies                                     |
| **Company**     | Empresa ou filial cadastrada na plataforma, responsável por criar vagas.          | id: Long<br/>name: String<br/>location: String<br/>email: String<br/>... | \* Company ↔ 1 Group<br/>1 Company → \* Jobs               |
| **Branch**      | (Opcional) Subdivisão de `Company` para diferentes unidades físicas ou regionais. | id: Long<br/>name: String<br/>address: String                            | \* Branch → 1 Company                                      |
| **Job**         | Vaga de trabalho criada por uma empresa (ou filial).                              | id: Long<br/>title: String<br/>deadline: LocalDate<br/>jobType: Enum     | \* Job → 1 Company (ou Branch)<br/>1 Job → \* Applications |
| **Application** | Candidatura de um candidato a uma `Job`.                                          | id: Long<br/>appliedAt: DateTime                                         | \* Application → 1 Job<br/>\* Application → 1 Candidate    |

---

## 2. Diagrama de Classes (Mermaid)

```mermaid
classDiagram
    class Group {
      +Long id
      +String name
    }
    class Company {
      +Long id
      +String name
      +String location
      +String email
    }
    class Branch {
      +Long id
      +String name
      +String address
    }
    class Job {
      +Long id
      +String title
      +LocalDate deadline
      +JobType jobType
    }
    class Application {
      +Long id
      +LocalDateTime appliedAt
    }

    Group "1" -- "*" Company : hasCompanies
    Company "1" -- "*" Branch : hasBranches
    Company "1" -- "*" Job : creates
    Branch "1" -- "*" Job : creates
    Job "1" -- "*" Application : receives
    Application "*" -- "1" Candidate : byCandidate
```

---

## 3. Fluxos Principais (Behavioral Models)

### 3.1 Cadastro de Empresa / Grupo

**Ator:** Administrador do Grupo
**Fluxo:**

1. *Request* cria **Group** (`/groups`).
2. *Response* retorna `groupId`.
3. *Request* cria **Company** com `groupId` no body (`/companies`).
4. *Response* confirma cadastro.

<details>
<summary>Sequence Diagram</summary>

```mermaid
sequenceDiagram
    actor Admin
    Admin->>API: POST /groups { name }
    API-->>Admin: 201 { groupId }
    Admin->>API: POST /companies { name, location, email, password, groupId }
    API-->>Admin: 201 { companyId }
```

</details>

### 3.2 Gestão de Filiais (Branches)

**Ator:** Company Manager
**Fluxo:**

* Criar filial: `POST /companies/{companyId}/branches` com dados de address e name.
* Listar filiais: `GET /companies/{companyId}/branches`.

### 3.3 Criação de Vagas por Filial ou Matriz

**Ator:** Company Manager / Branch Manager
**Fluxo:**

1. Se empresa usa filiais, seleciona `branchId`; senão, usa `companyId`.
2. Envia `POST /jobs` com `companyId` ou `branchId` no body.
3. API cria `Job` vinculado à entidade correta.

### 3.4 Relatório Consolidado de Vagas

**Ator:** Grupo Corporativo
**Fluxo:**

* `GET /reports/jobs?groupId={groupId}` → retorna total de vagas e candidaturas agregadas por empresa/filial.

---

## 4. Considerações de Padrões

* **CRUD** para `Group`, `Company`, `Branch`, `Job`, `Application`.
* **DTOs**: diretórios `dto/group`, `dto/company`, `dto/branch`.
* **Validações** com Jakarta Validation em todos os Requests.
* **Segurança**: rotas de `Group` e `Company` exigem `ROLE_COMPANY` ou `ROLE_ADMIN`.

---

> Esse modelo garante que você tenha uma visão clara das **empresas** como parte de **grupos** e **filiais**, facilitando relatórios centralizados e gestão delegada.
