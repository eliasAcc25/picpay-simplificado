# 💰 PicPay Simplificado

Uma API REST simplificada do PicPay desenvolvida com Spring Boot para realizar transferências entre usuários e lojistas.

## 🚀 Funcionalidades

- ✅ **Transferências entre usuários**: Usuários comuns podem transferir dinheiro entre si
- ✅ **Recebimento para lojistas**: Lojistas podem receber transferências (mas não enviar)
- ✅ **Validação de saldo**: Verificação automática de saldo suficiente
- ✅ **Validação de regras de negócio**: Lojistas não podem enviar dinheiro
- ✅ **Simulação de autorização externa**: Mock de serviço autorizador
- ✅ **Simulação de notificação**: Mock de serviço de notificação
- ✅ **API REST completa**: Endpoints para realizar transferências

## 🛠️ Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **H2 Database** (em memória para desenvolvimento)
- **Maven** (gerenciamento de dependências)
- **JUnit 5** (testes unitários)
- **Mockito** (mocks para testes)

## 📋 Pré-requisitos

- Java 17 ou superior
- Maven 3.6 ou superior
- IDE de sua preferência (IntelliJ IDEA, Eclipse, VS Code)

## 🔧 Instalação e Execução

### 1. Clone o repositório
```bash
git clone https://github.com/eliasAcc25/picpay-simplificado.git
cd picpay-simplificado
```

### 2. Execute o projeto
```bash
mvn spring-boot:run
```

### 3. Acesse a aplicação
- **URL base**: `http://localhost:8080`
- **Console H2**: `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:picpaydb`
  - Username: `sa`
  - Password: (vazio)

## 📡 Endpoints da API

### Realizar Transferência
```http
POST /transfer
Content-Type: application/json

{
  "value": 100.00,
  "payer": 1,
  "payee": 2
}
```

**Resposta de Sucesso (200 OK):**
```json
{
  "id": 1,
  "valor": 100.00,
  "pagador": {
    "id": 1,
    "nomeCompleto": "João Silva",
    "tipo": "COMUM"
  },
  "recebedor": {
    "id": 2,
    "nomeCompleto": "Maria Santos",
    "tipo": "COMUM"
  },
  "status": "AUTORIZADA",
  "dataTransferencia": "2023-09-23T14:30:00"
}
```

**Resposta de Erro (400 Bad Request):**
```json
"Saldo insuficiente para transferência."
```

## 👥 Dados de Teste

A aplicação carrega automaticamente os seguintes usuários para teste:

| ID | Nome | CPF/CNPJ | Email | Tipo | Saldo Inicial |
|----|------|----------|-------|------|---------------|
| 1 | João Silva | 12345678901 | joao@email.com | COMUM | R$ 1.000,00 |
| 2 | Maria Santos | 98765432109 | maria@email.com | COMUM | R$ 500,00 |
| 3 | Loja do João | 12345678000195 | loja@email.com | LOJISTA | R$ 0,00 |

## 🧪 Executando os Testes

### Todos os testes
```bash
mvn test
```

### Apenas testes unitários
```bash
mvn test -Dtest="*Test"
```

### Apenas testes de integração
```bash
mvn test -Dtest="*IT"
```

### Relatório de cobertura
```bash
mvn jacoco:report
```

## 📊 Cobertura de Testes

O projeto possui **testes unitários e de integração** abrangentes:

- ✅ **TransferenciaServiceTest**: Testa toda a lógica de negócio
- ✅ **SaldoTest**: Testa métodos de débito/crédito
- ✅ **TransferenciaTest**: Testa estados e regras
- ✅ **TransferControllerTest**: Testa endpoints REST
- ✅ **SimplificadoApplicationTests**: Testes de integração completos

**Cobertura atual**: ~95% das linhas de código

## 🏗️ Arquitetura

```
src/main/java/PicPay/Simplificado/
├── controller/          # Controladores REST
├── service/            # Lógica de negócio
├── repository/         # Acesso a dados
├── model/
│   ├── entity/         # Entidades JPA
│   └── enums/          # Enumerações
├── dto/                # Objetos de transferência
└── config/             # Configurações
```

## 🔄 Regras de Negócio

1. **Usuários comuns** podem enviar e receber transferências
2. **Lojistas** podem apenas receber transferências
3. **Validação de saldo** antes de realizar transferência
4. **Não é possível** transferir para si mesmo
5. **Autorização externa** é simulada (sempre aprovada)
6. **Notificação** é simulada (log no console)

## 🚦 Status do Projeto

- ✅ API REST funcional
- ✅ Testes unitários e integração
- ✅ Validações de negócio
- ✅ Documentação completa
- ⏳ Deploy em produção (próxima fase)
- ⏳ Autenticação JWT (próxima fase)
- ⏳ Banco de dados PostgreSQL (próxima fase)


## 👨‍💻 Autor

**Elias Andre Torres**
- GitHub: [@eliasAcc25](https://github.com/eliasAcc25)
- LinkedIn: [Elias Andre Torres](https://linkedin.com/in/elias-andre-torres)





