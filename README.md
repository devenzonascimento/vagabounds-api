# VagaBounds API — Pitch de Vendas

## 🌟 Visão Geral

**VagaBounds** é uma plataforma backend para centralizar e profissionalizar todo o fluxo de recrutamento em grupos de empresas de tecnologia. Imagine um único ponto de entrada para:

1. **Empresas** criam e gerenciam vagas de estágio, trainee e pleno/sênior.
2. **Candidatos** se inscrevem, fazem upload de currículos e acessam um feed personalizado.
3. **Gestores** de cada filial ou grupo acompanham métricas e relatórios em tempo real.

Com **autenticação JWT**, **autorização por perfis** (`COMPANY`, `CANDIDATE`) e **API RESTful** em Java Spring Boot, entregamos uma solução segura, escalável e pronta para integração com qualquer portal web ou mobile.

---

## 🚀 Funcionalidades Entregues

### Para **Companies**

* ✔️ **Cadastro / Login / Perfil** (CRUD completo de conta)
* ✔️ **Grupos de Empresas**

  * Criar grupo
  * Adicionar / remover membros
  * Remover grupo
  * Proprietário e Administradores podem visualizar todas as vagas dos membros do grupo e ter acesso a relatórios personalizados
* ✔️ **Gerenciamento de Vagas**

  * Criar, atualizar, fechar manualmente e prorrogar prazo
  * Listar suas vagas com filtros (abertas, fechadas, período, tipo)
  * Visualizar detalhes de cada vaga, quantidade de aplicações e candidatos.
* ✔️ **Candidaturas**

  * Aprovar ou rejeitar manualmente (com feedback por e‑mail)
  * Auto‑rejeição por regras de negócio (prazo vencido, requisitos não atendidos)
  * Visualizar lista de candidatos e baixar currículos

### Para **Candidates**

* ✔️ **Cadastro / Login / Perfil** (CRUD completo de conta)
* ✔️ **Upload & Visualização de Currículo** (MinIO/AWS S3 compatível)
* ✔️ **Feed Personalizado**

  * Exibe vagas compatíveis com educação e habilidades
  * Ordenado por percentual de match e título
* ✔️ **Gestão de Candidaturas**

  * Aplicar-se a vagas (com envio de currículo)
  * Listar suas aplicações e visualizar status

### **Reports** Centralizados

1. 🎯 **Taxa de Conversão por Vaga**
   Percentual de candidaturas que avançaram de PENDING → APPROVED/REJECTED/AUTO\_REJECTED.
2. 📊 **Performance de Empresas/Grupos**
   Vagas abertas, candidaturas totais e aprovações em período.
3. ⏱️ **Tempo Médio de Decisão**
   Média (em horas) entre aplicação e aprovação/rejeição por vaga.

Todos acessíveis via endpoints seguros, ex.:

```
GET /reports/conversion-rate  
GET /reports/company-performance?from=YYYY-MM-DD&to=YYYY-MM-DD  
GET /reports/decision-time?from=…&to=…
```

---

## 💡 Proposta de Valor

* **Centralização**: uma única API para todas as filiais e grupos da empresa.
* **Automação**: envio automático de e‑mails em cada transição de status, liberando tempo dos recrutadores.
* **Transparência**: candidatos acompanham o ciclo de seleção; gestores veem métricas completas.
* **Flexibilidade**: filtros avançados, feed inteligente e arquitetura modular para extensão rápida.

---

## 🛠️ Tecnologias

* **Backend**: Java 21, Spring Boot 3.5, Spring Data JPA, Spring Security (JWT)
* **Storage**: PostgreSQL, MinIO / AWS S3
* **Validação**: Jakarta Validation (anotações em DTOs e Entities)

---

## 📂 Organização do Código

```
src
 └── main
     ├── java
     │   └── vagabounds
     │       ├── controllers   # Endpoints REST
     │       ├── dtos          # DTOs por domínio (auth, job, report…)
     │       ├── exceptions    # Handler global e custom exceptions
     │       ├── models        # JPA Entities
     │       ├── repositories  # Spring Data + ReportRepository
     │       ├── security      # JWT, roles, SecurityUtils
     │       ├── services      # Lógica de negócio + ReportService
     │       ├── specifications# Filtros dinâmicos (Specification API)
     │       └── config        # Beans e configuração geral
     └── resources
         ├── application.properties
         └── docker-compose.yml
```

---

**VagaBounds API** – Conectando candidatos e empresas de tecnologia de forma inteligente e eficiente.
