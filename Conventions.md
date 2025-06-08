# Padrões de Desenvolvimento - VagaBounds

## Convenções de Rotas

* Todas as rotas devem ser definidas em **minúsculo**.
* Palavras compostas devem ser separadas por hífen (`-`).

**Exemplos:**

```
GET /company-jobs
POST /register-candidate
DELETE /delete-application
```

## Requisições com Dados Complexos

* **Evitar** o uso de `@PathVariable` para objetos complexos.
* Utilizar o `@RequestBody` e criar classes de request com validação apropriada.
* Essas classes devem seguir o seguinte padrão:

    * Criadas dentro da pasta `dtos/<domínio>`.
    * Nomeadas como `*Request` e implementadas como `record`.

**Exemplo:**

```
src/main/java/com/vagabounds/dtos/company/RegisterCompanyRequest.java
```

```java
package com.vagabounds.controller.dto.company;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterCompanyRequest(
    @NotBlank String name,
    @NotBlank String location,
    @Email String email,
    @Size(min = 8) String password
) {}
```

## Nomenclatura

* **camelCase** para variáveis, atributos e métodos;
* **PascalCase** para classes, enums e interfaces;
* **UPPER\_SNAKE\_CASE** para constantes.

```java
public class Pessoa {
    public static void main(String[] args) {
        String primeiroNome = "Maria";
        int idade = 22;
        double alturaAtual = 1.65;
        final String MENSAGEM_PADRAO = "Olá";
    }
}
```

## Validação

* Utilizar **Jakarta Validation** (`jakarta.validation`) com anotações como:

    * `@NotNull`
    * `@NotBlank`
    * `@Size`
    * `@Email`
* Anotar tanto entidades (modelos) quanto DTOs.

## Branch Strategy

* `main` é protegida, apenas aceita merge via Pull Request.
* Branches de funcionalidades devem seguir o padrão:

  ```
  feature/nome-da-feature
  fix/ajuste-especifico
  refactor/modificacao-interna
  ```
* Todo PR deve passar por revisão de outro integrante.
