# 🏥 SGHSS - Sistema de Gestão de Saúde Simplificado

## 📋 Visão Geral

Aplicação backend em Spring Boot para autenticação com JWT, cadastro de usuários (pacientes e profissionais de saúde) e gestão de consultas médicas.

## 🛠️ Requisitos

| Tecnologia | Versão                                                    |
| ---------- | --------------------------------------------------------- |
| Java       | 22 (JDK)                                                  |
| Maven      | 3.9+                                                      |
| PostgreSQL | Compatível com as configurações do application.properties |

## ⚙️ Configuração

### 1. Configuração do Banco de Dados

Configure o banco no arquivo `sghss/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://... # (mantenha sslmode conforme seu provedor)
spring.datasource.username=...
spring.datasource.password=...
spring.jpa.hibernate.ddl-auto=update
```

### 2. Configuração da Porta (Opcional)

```properties
server.port=8080
```

## 🚀 Como Executar

No diretório `sghss/`:

1. **Compilar**: `mvn clean package`
2. **Executar**: `mvn spring-boot:run`
   - Ou: `java -jar target/sghss-*.jar`

## 🔐 Segurança e Autenticação

- ✅ Autenticação via **JWT Bearer**
- 🔑 Obtenha o token através dos endpoints de login/cadastro
- 📤 Envie nas requisições protegidas: `Authorization: Bearer <seu_token>`

## 📡 Mapeamento de Endpoints

**Base URL**: `http://localhost:8080`

### 🔐 Autenticação

#### POST `/auth/register/paciente`

**Descrição**: Cadastra um paciente e retorna JWT.

**Body (JSON)**:

```json
{
  "nome": "João Silva",
  "email": "joao@email.com",
  "senha": "senha123",
  "cpf": "12345678901",
  "dataNascimento": "1990-05-15",
  "telefone": "(11) 99999-9999",
  "endereco": "Rua A, 123"
}
```

**Resposta 200 (JSON)**:

```json
{
  "token": "eyJhbGci..."
}
```

#### POST `/auth/register/profissional`

**Descrição**: Cadastra um profissional de saúde e retorna JWT.

**Body (JSON)**:

```json
{
  "nome": "Dra. Maria",
  "email": "maria@email.com",
  "senha": "senha123",
  "cpf": "98765432100",
  "dataNascimento": "1985-03-20",
  "telefone": "(11) 88888-8888",
  "endereco": "Av. B, 456",
  "codigoRegistroProfissional": "CRM123456",
  "especialidade": "Cardiologia"
}
```

**Resposta 200 (JSON)**:

```json
{
  "token": "eyJhbGci..."
}
```

#### POST `/auth/login`

**Descrição**: Realiza login e retorna JWT.

**Body (JSON)**:

```json
{
  "email": "...",
  "senha": "..."
}
```

**Resposta 200 (JSON)**:

```json
{
  "token": "eyJhbGci..."
}
```

### 👥 Pacientes

> **Acesso**: Apenas PROFISSIONAL

#### GET `/pacientes`

**Descrição**: Lista todos os pacientes.

**Headers**: `Authorization: Bearer <token_profissional>`

**Resposta 200 (JSON)**:

```json
[
  {
    "id": 1,
    "nome": "João Silva",
    "email": "joao@...",
    "cpf": "12345678901",
    "dataNascimento": "1990-05-15",
    "telefone": "(11) 99999-9999",
    "endereco": "Rua A, 123"
  }
]
```

#### GET `/pacientes/{id}`

**Descrição**: Busca paciente por ID.

**Headers**: `Authorization: Bearer <token_profissional>`

**Resposta 200 (JSON)**:

```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao@...",
  "cpf": "12345678901",
  "dataNascimento": "1990-05-15",
  "telefone": "(11) 99999-9999",
  "endereco": "Rua A, 123"
}
```

### 📅 Consultas

#### POST `/consultas`

**Descrição**: Paciente agenda uma consulta com um profissional.

**Headers**: `Authorization: Bearer <token_paciente>`

**Body (JSON)**:

```json
{
  "profissionalId": 2,
  "dataHora": "2025-01-15T14:30:00",
  "observacoes": "Dor de cabeça persistente"
}
```

**Resposta 200 (JSON)**:

```json
{
  "id": 1,
  "pacienteId": 1,
  "pacienteNome": "João Silva",
  "profissionalId": 2,
  "profissionalNome": "Dra. Maria",
  "profissionalEspecialidade": "Cardiologia",
  "dataHora": "2025-01-15T14:30:00",
  "status": "AGENDADA",
  "observacoes": "Dor de cabeça persistente",
  "createdAt": "2025-01-09T15:00:00",
  "updatedAt": "2025-01-09T15:00:00"
}
```

#### GET `/consultas/minhas`

**Descrição**: Paciente visualiza suas consultas agendadas.

**Headers**: `Authorization: Bearer <token_paciente>`

**Resposta 200 (JSON)**:

```json
[
  {
    "id": 1,
    "pacienteId": 1,
    "pacienteNome": "João Silva",
    "profissionalId": 2,
    "profissionalNome": "Dra. Maria",
    "profissionalEspecialidade": "Cardiologia",
    "dataHora": "2025-01-15T14:30:00",
    "status": "AGENDADA",
    "observacoes": "Dor de cabeça persistente",
    "createdAt": "2025-01-09T15:00:00",
    "updatedAt": "2025-01-09T15:00:00"
  }
]
```

#### GET `/consultas/profissional`

**Descrição**: Profissional visualiza suas consultas agendadas.

**Headers**: `Authorization: Bearer <token_profissional>`

**Resposta 200 (JSON)**:

```json
[
  {
    "id": 1,
    "pacienteId": 1,
    "pacienteNome": "João Silva",
    "profissionalId": 2,
    "profissionalNome": "Dra. Maria",
    "profissionalEspecialidade": "Cardiologia",
    "dataHora": "2025-01-15T14:30:00",
    "status": "AGENDADA",
    "observacoes": "Dor de cabeça persistente",
    "createdAt": "2025-01-09T15:00:00",
    "updatedAt": "2025-01-09T15:00:00"
  }
]
```

#### PUT `/consultas/{id}`

**Descrição**: Profissional atualiza uma consulta (status, data, observações).

**Headers**: `Authorization: Bearer <token_profissional>`

**Body (JSON)**:

```json
{
  "dataHora": "2025-01-15T15:00:00",
  "status": "REALIZADA",
  "observacoes": "Consulta realizada. Paciente com pressão normal."
}
```

**Resposta 200 (JSON)**:

```json
{
  "id": 1,
  "pacienteId": 1,
  "pacienteNome": "João Silva",
  "profissionalId": 2,
  "profissionalNome": "Dra. Maria",
  "profissionalEspecialidade": "Cardiologia",
  "dataHora": "2025-01-15T15:00:00",
  "status": "REALIZADA",
  "observacoes": "Consulta realizada. Paciente com pressão normal.",
  "createdAt": "2025-01-09T15:00:00",
  "updatedAt": "2025-01-09T15:30:00"
}
```

## 📝 Observações Importantes

### 🔐 Segurança

- ✅ Senhas são criptografadas com **BCrypt**
- 🔑 JWT assinado e validado via chave HMAC configurada em `JwtUtil`
- 👤 Usuários possuem campos: `ativo`, `tipo` (PACIENTE/PROFISSIONAL), `created_at` e `updated_at`

### 📊 Status das Consultas

| Status      | Descrição                    |
| ----------- | ---------------------------- |
| `AGENDADA`  | Consulta agendada e pendente |
| `REALIZADA` | Consulta já realizada        |
| `CANCELADA` | Consulta cancelada           |

### 🔒 Regras de Acesso

- 👥 **Pacientes**: Podem agendar consultas e visualizar suas próprias consultas
- 👨‍⚕️ **Profissionais**: Podem visualizar todos os pacientes e atualizar suas consultas
- 🚫 **Restrições**: Apenas profissionais podem atualizar consultas (e apenas as suas)

## 🛠️ Desenvolvimento

### 📚 Stack Tecnológica

| Tecnologia      | Versão |
| --------------- | ------ |
| Java            | 22     |
| Spring Boot     | 3.5    |
| Spring Security | 6      |
| Spring Data JPA | -      |
| PostgreSQL      | -      |

### 🏗️ Arquitetura

- **DTOs**: Para entrada/saída de dados
- **Controllers**: Enxutos, apenas roteamento
- **Services**: Contêm as regras de negócio
- **Repositories**: Interface com JPA para acesso aos dados

---

<div align="center">
  <p><strong>🏥 SGHSS - Sistema de Gestão de Saúde Simplificado</strong></p>
  <p>Desenvolvido com ❤️ para facilitar a gestão de consultas médicas</p>
</div>
