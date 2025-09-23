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


%3CmxGraphModel%3E%3Croot%3E%3CmxCell%20id%3D%220%22%2F%3E%3CmxCell%20id%3D%221%22%20parent%3D%220%22%2F%3E%3CmxCell%20id%3D%222%22%20value%3D%22%22%20style%3D%22edgeStyle%3DorthogonalEdgeStyle%3Brounded%3D0%3BorthogonalLoop%3D1%3BjettySize%3Dauto%3Bhtml%3D1%3B%22%20edge%3D%221%22%20source%3D%2213%22%20target%3D%225%22%20parent%3D%221%22%3E%3CmxGeometry%20relative%3D%221%22%20as%3D%22geometry%22%2F%3E%3C%2FmxCell%3E%3CmxCell%20id%3D%223%22%20value%3D%22%26lt%3Bfont%20style%3D%26quot%3Bfont-size%3A%2018px%3B%26quot%3B%26gt%3BUsu%C3%A1rio%26lt%3B%2Ffont%26gt%3B%22%20style%3D%22rounded%3D1%3BwhiteSpace%3Dwrap%3Bhtml%3D1%3B%22%20vertex%3D%221%22%20parent%3D%221%22%3E%3CmxGeometry%20x%3D%22-80%22%20y%3D%2230%22%20width%3D%22160%22%20height%3D%2260%22%20as%3D%22geometry%22%2F%3E%3C%2FmxCell%3E%3CmxCell%20id%3D%224%22%20value%3D%22%22%20style%3D%22edgeStyle%3DorthogonalEdgeStyle%3Brounded%3D0%3BorthogonalLoop%3D1%3BjettySize%3Dauto%3Bhtml%3D1%3B%22%20edge%3D%221%22%20source%3D%225%22%20target%3D%227%22%20parent%3D%221%22%3E%3CmxGeometry%20relative%3D%221%22%20as%3D%22geometry%22%2F%3E%3C%2FmxCell%3E%3CmxCell%20id%3D%225%22%20value%3D%22%26lt%3Bdiv%20style%3D%26quot%3B%26quot%3B%26gt%3B%26lt%3Bspan%20style%3D%26quot%3Bbackground-color%3A%20transparent%3B%20color%3A%20light-dark(rgb(0%2C%200%2C%200)%2C%20rgb(255%2C%20255%2C%20255))%3B%26quot%3B%26gt%3BController%26lt%3B%2Fspan%26gt%3B%26lt%3B%2Fdiv%26gt%3B%26lt%3Bdiv%20style%3D%26quot%3B%26quot%3B%26gt%3B%26lt%3Bspan%20style%3D%26quot%3Bbackground-color%3A%20transparent%3B%20color%3A%20light-dark(rgb(0%2C%200%2C%200)%2C%20rgb(255%2C%20255%2C%20255))%3B%26quot%3B%26gt%3B%26lt%3Bfont%20style%3D%26quot%3Bfont-size%3A%2010px%3B%26quot%3B%26gt%3B%40RestController%26lt%3B%2Ffont%26gt%3B%26lt%3B%2Fspan%26gt%3B%26lt%3B%2Fdiv%26gt%3B%26lt%3Bdiv%20style%3D%26quot%3B%26quot%3B%26gt%3B%26lt%3Bspan%20style%3D%26quot%3Bbackground-color%3A%20transparent%3B%20color%3A%20light-dark(rgb(0%2C%200%2C%200)%2C%20rgb(255%2C%20255%2C%20255))%3B%26quot%3B%26gt%3B%26lt%3Bfont%20style%3D%26quot%3Bfont-size%3A%2010px%3B%26quot%3B%26gt%3BTransferController%26lt%3B%2Ffont%26gt%3B%26lt%3B%2Fspan%26gt%3B%26lt%3B%2Fdiv%26gt%3B%22%20style%3D%22whiteSpace%3Dwrap%3Bhtml%3D1%3Brounded%3D1%3Balign%3Dcenter%3B%22%20vertex%3D%221%22%20parent%3D%221%22%3E%3CmxGeometry%20x%3D%22-80%22%20y%3D%22150%22%20width%3D%22160%22%20height%3D%2260%22%20as%3D%22geometry%22%2F%3E%3C%2FmxCell%3E%3CmxCell%20id%3D%226%22%20value%3D%22%22%20style%3D%22edgeStyle%3DorthogonalEdgeStyle%3Brounded%3D0%3BorthogonalLoop%3D1%3BjettySize%3Dauto%3Bhtml%3D1%3B%22%20edge%3D%221%22%20source%3D%2215%22%20target%3D%229%22%20parent%3D%221%22%3E%3CmxGeometry%20relative%3D%221%22%20as%3D%22geometry%22%2F%3E%3C%2FmxCell%3E%3CmxCell%20id%3D%227%22%20value%3D%22%26lt%3Bfont%20style%3D%26quot%3Bfont-size%3A%2012px%3B%26quot%3B%26gt%3BService%26lt%3B%2Ffont%26gt%3B%26lt%3Bdiv%26gt%3B%26lt%3Bspan%20style%3D%26quot%3Bfont-size%3A%2010px%3B%26quot%3B%26gt%3B%40Service%26lt%3B%2Fspan%26gt%3B%26lt%3B%2Fdiv%26gt%3B%26lt%3Bdiv%26gt%3B%26lt%3Bspan%20style%3D%26quot%3Bfont-size%3A%2010px%3B%26quot%3B%26gt%3BTransferService%26lt%3B%2Fspan%26gt%3B%26lt%3B%2Fdiv%26gt%3B%22%20style%3D%22whiteSpace%3Dwrap%3Bhtml%3D1%3Brounded%3D1%3B%22%20vertex%3D%221%22%20parent%3D%221%22%3E%3CmxGeometry%20x%3D%22-80%22%20y%3D%22290%22%20width%3D%22160%22%20height%3D%2260%22%20as%3D%22geometry%22%2F%3E%3C%2FmxCell%3E%3CmxCell%20id%3D%228%22%20value%3D%22%22%20style%3D%22edgeStyle%3DorthogonalEdgeStyle%3Brounded%3D0%3BorthogonalLoop%3D1%3BjettySize%3Dauto%3Bhtml%3D1%3B%22%20edge%3D%221%22%20source%3D%229%22%20target%3D%2210%22%20parent%3D%221%22%3E%3CmxGeometry%20relative%3D%221%22%20as%3D%22geometry%22%2F%3E%3C%2FmxCell%3E%3CmxCell%20id%3D%229%22%20value%3D%22%26lt%3Bfont%20style%3D%26quot%3Bfont-size%3A%2012px%3B%26quot%3B%26gt%3BRepository%26lt%3B%2Ffont%26gt%3B%26lt%3Bdiv%26gt%3B%26lt%3Bfont%20style%3D%26quot%3B%26quot%3B%26gt%3B%26lt%3Bfont%20style%3D%26quot%3Bfont-size%3A%2010px%3B%26quot%3B%26gt%3B%40Repository%26lt%3B%2Ffont%26gt%3B%26lt%3B%2Ffont%26gt%3B%26lt%3B%2Fdiv%26gt%3B%26lt%3Bdiv%26gt%3B%26lt%3Bfont%20style%3D%26quot%3B%26quot%3B%26gt%3B%26lt%3Bfont%20style%3D%26quot%3Bfont-size%3A%2010px%3B%26quot%3B%26gt%3BUserRepository%26lt%3B%2Ffont%26gt%3B%26lt%3B%2Ffont%26gt%3B%26lt%3B%2Fdiv%26gt%3B%22%20style%3D%22whiteSpace%3Dwrap%3Bhtml%3D1%3Brounded%3D1%3B%22%20vertex%3D%221%22%20parent%3D%221%22%3E%3CmxGeometry%20x%3D%22-80%22%20y%3D%22430%22%20width%3D%22160%22%20height%3D%2260%22%20as%3D%22geometry%22%2F%3E%3C%2FmxCell%3E%3CmxCell%20id%3D%2210%22%20value%3D%22%26lt%3Bfont%20style%3D%26quot%3Bfont-size%3A%2012px%3B%26quot%3B%26gt%3BBanco%26lt%3B%2Ffont%26gt%3B%26lt%3Bdiv%26gt%3B%26lt%3Bfont%20style%3D%26quot%3Bfont-size%3A%2010px%3B%26quot%3B%26gt%3BH2%26lt%3B%2Ffont%26gt%3B%26lt%3B%2Fdiv%26gt%3B%26lt%3Bdiv%26gt%3B%26lt%3Bfont%20style%3D%26quot%3Bfont-size%3A%2010px%3B%26quot%3B%26gt%3Busers%26lt%3B%2Ffont%26gt%3B%26lt%3B%2Fdiv%26gt%3B%26lt%3Bdiv%26gt%3B%26lt%3Bfont%20style%3D%26quot%3Bfont-size%3A%2010px%3B%26quot%3B%26gt%3Btransactions%26lt%3B%2Ffont%26gt%3B%26lt%3B%2Fdiv%26gt%3B%22%20style%3D%22whiteSpace%3Dwrap%3Bhtml%3D1%3Brounded%3D1%3B%22%20vertex%3D%221%22%20parent%3D%221%22%3E%3CmxGeometry%20x%3D%22-80%22%20y%3D%22570%22%20width%3D%22160%22%20height%3D%2260%22%20as%3D%22geometry%22%2F%3E%3C%2FmxCell%3E%3CmxCell%20id%3D%2211%22%20value%3D%22%26lt%3Bfont%20style%3D%26quot%3Bfont-size%3A%2010px%3B%26quot%3B%26gt%3BChama%20a%20service%26lt%3B%2Ffont%26gt%3B%26lt%3Bdiv%26gt%3B%26lt%3Bfont%20style%3D%26quot%3Bfont-size%3A%2010px%3B%26quot%3B%26gt%3BTransferService.transfer()%26lt%3B%2Ffont%26gt%3B%26lt%3B%2Fdiv%26gt%3B%22%20style%3D%22text%3Bhtml%3D1%3Balign%3Dcenter%3BverticalAlign%3Dmiddle%3Bresizable%3D0%3Bpoints%3D%5B%5D%3Bautosize%3D1%3BstrokeColor%3Dnone%3BfillColor%3Dnone%3B%22%20vertex%3D%221%22%20parent%3D%221%22%3E%3CmxGeometry%20x%3D%22-80%22%20y%3D%22230%22%20width%3D%22140%22%20height%3D%2240%22%20as%3D%22geometry%22%2F%3E%3C%2FmxCell%3E%3CmxCell%20id%3D%2212%22%20value%3D%22%22%20style%3D%22edgeStyle%3DorthogonalEdgeStyle%3Brounded%3D0%3BorthogonalLoop%3D1%3BjettySize%3Dauto%3Bhtml%3D1%3B%22%20edge%3D%221%22%20source%3D%223%22%20target%3D%2213%22%20parent%3D%221%22%3E%3CmxGeometry%20relative%3D%221%22%20as%3D%22geometry%22%3E%3CmxPoint%20y%3D%2290%22%20as%3D%22sourcePoint%22%2F%3E%3CmxPoint%20y%3D%22150%22%20as%3D%22targetPoint%22%2F%3E%3C%2FmxGeometry%3E%3C%2FmxCell%3E%3CmxCell%20id%3D%2213%22%20value%3D%22%26lt%3Bspan%20style%3D%26quot%3Bcolor%3A%20rgb(255%2C%20255%2C%20255)%3B%20font-family%3A%20Helvetica%3B%20font-style%3A%20normal%3B%20font-variant-ligatures%3A%20normal%3B%20font-variant-caps%3A%20normal%3B%20font-weight%3A%20400%3B%20letter-spacing%3A%20normal%3B%20orphans%3A%202%3B%20text-align%3A%20center%3B%20text-indent%3A%200px%3B%20text-transform%3A%20none%3B%20widows%3A%202%3B%20word-spacing%3A%200px%3B%20-webkit-text-stroke-width%3A%200px%3B%20white-space%3A%20normal%3B%20background-color%3A%20rgb(27%2C%2029%2C%2030)%3B%20text-decoration-thickness%3A%20initial%3B%20text-decoration-style%3A%20initial%3B%20text-decoration-color%3A%20initial%3B%20float%3A%20none%3B%20display%3A%20inline%20!important%3B%26quot%3B%26gt%3B%26lt%3Bfont%20style%3D%26quot%3Bfont-size%3A%209px%3B%26quot%3B%26gt%3BHTTP%20Request%26lt%3B%2Ffont%26gt%3B%26lt%3B%2Fspan%26gt%3B%26lt%3Bdiv%20style%3D%26quot%3Bforced-color-adjust%3A%20none%3B%20color%3A%20rgb(255%2C%20255%2C%20255)%3B%20font-family%3A%20Helvetica%3B%20font-style%3A%20normal%3B%20font-variant-ligatures%3A%20normal%3B%20font-variant-caps%3A%20normal%3B%20font-weight%3A%20400%3B%20letter-spacing%3A%20normal%3B%20orphans%3A%202%3B%20text-align%3A%20center%3B%20text-indent%3A%200px%3B%20text-transform%3A%20none%3B%20widows%3A%202%3B%20word-spacing%3A%200px%3B%20-webkit-text-stroke-width%3A%200px%3B%20white-space%3A%20normal%3B%20background-color%3A%20rgb(27%2C%2029%2C%2030)%3B%20text-decoration-thickness%3A%20initial%3B%20text-decoration-style%3A%20initial%3B%20text-decoration-color%3A%20initial%3B%20box-shadow%3A%20none%20!important%3B%26quot%3B%26gt%3B%26lt%3Bfont%20style%3D%26quot%3Bfont-size%3A%2010px%3B%26quot%3B%26gt%3BPOST%2Ftransfer%26lt%3B%2Ffont%26gt%3B%26lt%3B%2Fdiv%26gt%3B%22%20style%3D%22text%3BwhiteSpace%3Dwrap%3Bhtml%3D1%3B%22%20vertex%3D%221%22%20parent%3D%221%22%3E%3CmxGeometry%20x%3D%22-30%22%20y%3D%22100%22%20width%3D%22110%22%20height%3D%2250%22%20as%3D%22geometry%22%2F%3E%3C%2FmxCell%3E%3CmxCell%20id%3D%2214%22%20value%3D%22%22%20style%3D%22edgeStyle%3DorthogonalEdgeStyle%3Brounded%3D0%3BorthogonalLoop%3D1%3BjettySize%3Dauto%3Bhtml%3D1%3B%22%20edge%3D%221%22%20source%3D%227%22%20target%3D%2215%22%20parent%3D%221%22%3E%3CmxGeometry%20relative%3D%221%22%20as%3D%22geometry%22%3E%3CmxPoint%20y%3D%22350%22%20as%3D%22sourcePoint%22%2F%3E%3CmxPoint%20y%3D%22430%22%20as%3D%22targetPoint%22%2F%3E%3C%2FmxGeometry%3E%3C%2FmxCell%3E%3CmxCell%20id%3D%2215%22%20value%3D%22%26lt%3Bfont%20style%3D%26quot%3Bfont-size%3A%2010px%3B%26quot%3B%26gt%3BAcessa%20Dados%26lt%3B%2Ffont%26gt%3B%26lt%3Bdiv%26gt%3B%26lt%3Bfont%20style%3D%26quot%3Bfont-size%3A%2010px%3B%26quot%3B%26gt%3BuserRepository.save()%26lt%3B%2Ffont%26gt%3B%26lt%3B%2Fdiv%26gt%3B%22%20style%3D%22text%3Bhtml%3D1%3Balign%3Dcenter%3BverticalAlign%3Dmiddle%3Bresizable%3D0%3Bpoints%3D%5B%5D%3Bautosize%3D1%3BstrokeColor%3Dnone%3BfillColor%3Dnone%3B%22%20vertex%3D%221%22%20parent%3D%221%22%3E%3CmxGeometry%20x%3D%22-60%22%20y%3D%22363%22%20width%3D%22120%22%20height%3D%2240%22%20as%3D%22geometry%22%2F%3E%3C%2FmxCell%3E%3CmxCell%20id%3D%2216%22%20value%3D%22%26lt%3Bfont%20style%3D%26quot%3Bfont-size%3A%2010px%3B%26quot%3B%26gt%3BSQL%20Query%26lt%3B%2Ffont%26gt%3B%26lt%3Bdiv%26gt%3B%26lt%3Bfont%20style%3D%26quot%3Bfont-size%3A%2010px%3B%26quot%3B%26gt%3BINSERT%2FUPDATE%26lt%3B%2Ffont%26gt%3B%26lt%3B%2Fdiv%26gt%3B%22%20style%3D%22text%3Bhtml%3D1%3Balign%3Dcenter%3BverticalAlign%3Dmiddle%3Bresizable%3D0%3Bpoints%3D%5B%5D%3Bautosize%3D1%3BstrokeColor%3Dnone%3BfillColor%3Dnone%3B%22%20vertex%3D%221%22%20parent%3D%221%22%3E%3CmxGeometry%20x%3D%22-50%22%20y%3D%22503%22%20width%3D%22100%22%20height%3D%2240%22%20as%3D%22geometry%22%2F%3E%3C%2FmxCell%3E%3CmxCell%20id%3D%2217%22%20value%3D%22%26lt%3Bbr%26gt%3B%26lt%3Bdiv%26gt%3B%26lt%3Bfont%20style%3D%26quot%3Bfont-size%3A%2014px%3B%26quot%3B%26gt%3B%26lt%3Bb%26gt%3B%26lt%3Bbr%26gt%3B%26lt%3B%2Fb%26gt%3B%26lt%3B%2Ffont%26gt%3B%26lt%3B%2Fdiv%26gt%3B%22%20style%3D%22text%3Bhtml%3D1%3Balign%3Dcenter%3BverticalAlign%3Dmiddle%3Bresizable%3D0%3Bpoints%3D%5B%5D%3Bautosize%3D1%3BstrokeColor%3Dnone%3BfillColor%3Dnone%3B%22%20vertex%3D%221%22%20parent%3D%221%22%3E%3CmxGeometry%20x%3D%22-345%22%20y%3D%2275%22%20width%3D%2220%22%20height%3D%2240%22%20as%3D%22geometry%22%2F%3E%3C%2FmxCell%3E%3CmxCell%20id%3D%2218%22%20value%3D%22%26lt%3Bb%20style%3D%26quot%3Bfont-size%3A%2014px%3B%20text-wrap-mode%3A%20nowrap%3B%26quot%3B%26gt%3BTECNOLOGIAS%26lt%3Bbr%26gt%3B%26amp%3Bnbsp%3BUTILIZADAS%26lt%3B%2Fb%26gt%3B%22%20style%3D%22shape%3Dhexagon%3Bperimeter%3DhexagonPerimeter2%3BwhiteSpace%3Dwrap%3Bhtml%3D1%3BfixedSize%3D1%3B%22%20vertex%3D%221%22%20parent%3D%221%22%3E%3CmxGeometry%20x%3D%22-420%22%20y%3D%2230%22%20width%3D%22180%22%20height%3D%2240%22%20as%3D%22geometry%22%2F%3E%3C%2FmxCell%3E%3CmxCell%20id%3D%2219%22%20value%3D%22%26lt%3Bdiv%20style%3D%26quot%3Btext-wrap-mode%3A%20nowrap%3B%26quot%3B%26gt%3B%26lt%3Bul%26gt%3B%26lt%3Bli%26gt%3B%26lt%3Bb%20style%3D%26quot%3Bbackground-color%3A%20transparent%3B%20color%3A%20light-dark(rgb(0%2C%200%2C%200)%2C%20rgb(255%2C%20255%2C%20255))%3B%26quot%3B%26gt%3BJava%2021%26lt%3B%2Fb%26gt%3B%26lt%3B%2Fli%26gt%3B%26lt%3Bli%26gt%3B%26lt%3Bb%20style%3D%26quot%3Bbackground-color%3A%20transparent%3B%20color%3A%20light-dark(rgb(0%2C%200%2C%200)%2C%20rgb(255%2C%20255%2C%20255))%3B%26quot%3B%26gt%3BSpring%20Boot%203%26lt%3B%2Fb%26gt%3B%26lt%3B%2Fli%26gt%3B%26lt%3Bli%26gt%3B%26lt%3Bb%20style%3D%26quot%3Bbackground-color%3A%20transparent%3B%20color%3A%20light-dark(rgb(0%2C%200%2C%200)%2C%20rgb(255%2C%20255%2C%20255))%3B%26quot%3B%26gt%3BJpa%26lt%3B%2Fb%26gt%3B%26lt%3B%2Fli%26gt%3B%26lt%3Bli%26gt%3B%26lt%3Bb%20style%3D%26quot%3Bbackground-color%3A%20transparent%3B%20color%3A%20light-dark(rgb(0%2C%200%2C%200)%2C%20rgb(255%2C%20255%2C%20255))%3B%26quot%3B%26gt%3BH2%26lt%3B%2Fb%26gt%3B%26lt%3B%2Fli%26gt%3B%26lt%3Bli%26gt%3B%26lt%3Bb%20style%3D%26quot%3Bbackground-color%3A%20transparent%3B%20color%3A%20light-dark(rgb(0%2C%200%2C%200)%2C%20rgb(255%2C%20255%2C%20255))%3B%26quot%3B%26gt%3BMaven%26lt%3B%2Fb%26gt%3B%26lt%3B%2Fli%26gt%3B%26lt%3B%2Ful%26gt%3B%26lt%3B%2Fdiv%26gt%3B%22%20style%3D%22rounded%3D1%3BwhiteSpace%3Dwrap%3Bhtml%3D1%3Balign%3Dleft%3B%22%20vertex%3D%221%22%20parent%3D%221%22%3E%3CmxGeometry%20x%3D%22-420%22%20y%3D%2282.5%22%20width%3D%22180%22%20height%3D%2290%22%20as%3D%22geometry%22%2F%3E%3C%2FmxCell%3E%3CmxCell%20id%3D%2220%22%20value%3D%22%26lt%3Bb%26gt%3B%26lt%3Bfont%20style%3D%26quot%3Bfont-size%3A%2014px%3B%26quot%3B%26gt%3BFUNCIONALIDADES%26lt%3B%2Ffont%26gt%3B%26lt%3B%2Fb%26gt%3B%22%20style%3D%22rhombus%3BwhiteSpace%3Dwrap%3Bhtml%3D1%3B%22%20vertex%3D%221%22%20parent%3D%221%22%3E%3CmxGeometry%20x%3D%22130%22%20y%3D%2225%22%20width%3D%22180%22%20height%3D%2245%22%20as%3D%22geometry%22%2F%3E%3C%2FmxCell%3E%3CmxCell%20id%3D%2221%22%20value%3D%22%26lt%3Bb%26gt%3B%26lt%3Bfont%20style%3D%26quot%3Bfont-size%3A%2014px%3B%26quot%3B%26gt%3BComo%20Funciona%26lt%3B%2Ffont%26gt%3B%26lt%3B%2Fb%26gt%3B%22%20style%3D%22rhombus%3BwhiteSpace%3Dwrap%3Bhtml%3D1%3B%22%20vertex%3D%221%22%20parent%3D%221%22%3E%3CmxGeometry%20x%3D%22140%22%20y%3D%22350%22%20width%3D%22180%22%20height%3D%2240%22%20as%3D%22geometry%22%2F%3E%3C%2FmxCell%3E%3CmxCell%20id%3D%2222%22%20value%3D%22%26lt%3Bul%26gt%3B%26lt%3Bli%26gt%3BCliente%20envia%20POST%26lt%3B%2Fli%26gt%3B%26lt%3Bli%26gt%3BController%20recebe%26lt%3B%2Fli%26gt%3B%26lt%3Bli%26gt%3BService%20processa%26lt%3B%2Fli%26gt%3B%26lt%3Bli%26gt%3BRepository%20salva%26lt%3B%2Fli%26gt%3B%26lt%3Bli%26gt%3BBanco%20armazena%26lt%3B%2Fli%26gt%3B%26lt%3B%2Ful%26gt%3B%22%20style%3D%22rounded%3D1%3BwhiteSpace%3Dwrap%3Bhtml%3D1%3Balign%3Dleft%3B%22%20vertex%3D%221%22%20parent%3D%221%22%3E%3CmxGeometry%20x%3D%22140%22%20y%3D%22403%22%20width%3D%22200%22%20height%3D%2287%22%20as%3D%22geometry%22%2F%3E%3C%2FmxCell%3E%3CmxCell%20id%3D%2223%22%20value%3D%22%26lt%3Bul%26gt%3B%26lt%3Bli%26gt%3BTransferir%20dinheiro%26lt%3B%2Fli%26gt%3B%26lt%3Bli%26gt%3BValidar%20Usu%C3%A1rios%26lt%3B%2Fli%26gt%3B%26lt%3Bli%26gt%3BVerificar%20Saldo%26lt%3B%2Fli%26gt%3B%26lt%3Bli%26gt%3BSalvar%20Transa%C3%A7%C3%A3o%26lt%3B%2Fli%26gt%3B%26lt%3Bli%26gt%3BRetornar%20Resposta%26lt%3B%2Fli%26gt%3B%26lt%3B%2Ful%26gt%3B%22%20style%3D%22rounded%3D1%3BwhiteSpace%3Dwrap%3Bhtml%3D1%3Balign%3Dleft%3B%22%20vertex%3D%221%22%20parent%3D%221%22%3E%3CmxGeometry%20x%3D%22130%22%20y%3D%2282.5%22%20width%3D%22200%22%20height%3D%2285%22%20as%3D%22geometry%22%2F%3E%3C%2FmxCell%3E%3CmxCell%20id%3D%2224%22%20value%3D%22%26lt%3Bspan%20style%3D%26quot%3Bfont-family%3A%20monospace%3B%20font-size%3A%2011px%3B%26quot%3B%26gt%3B%26amp%3Bnbsp%3B%26lt%3B%2Fspan%26gt%3B%26lt%3Bbr%20style%3D%26quot%3Bfont-family%3A%20monospace%3B%20font-size%3A%2011px%3B%26quot%3B%26gt%3B%26lt%3Bspan%20style%3D%26quot%3Bfont-family%3A%20monospace%3B%20font-size%3A%2011px%3B%26quot%3B%26gt%3B%7B%26lt%3B%2Fspan%26gt%3B%26lt%3Bbr%20style%3D%26quot%3Bfont-family%3A%20monospace%3B%20font-size%3A%2011px%3B%26quot%3B%26gt%3B%26lt%3Bspan%20style%3D%26quot%3Bfont-family%3A%20monospace%3B%20font-size%3A%2011px%3B%26quot%3B%26gt%3B%26quot%3BsenderId%26quot%3B%3A%201%2C%26lt%3B%2Fspan%26gt%3B%26lt%3Bbr%20style%3D%26quot%3Bfont-family%3A%20monospace%3B%20font-size%3A%2011px%3B%26quot%3B%26gt%3B%26lt%3Bspan%20style%3D%26quot%3Bfont-family%3A%20monospace%3B%20font-size%3A%2011px%3B%26quot%3B%26gt%3B%26quot%3BreceiverId%26quot%3B%3A%202%2C%26lt%3B%2Fspan%26gt%3B%26lt%3Bbr%20style%3D%26quot%3Bfont-family%3A%20monospace%3B%20font-size%3A%2011px%3B%26quot%3B%26gt%3B%26lt%3Bspan%20style%3D%26quot%3Bfont-family%3A%20monospace%3B%20font-size%3A%2011px%3B%26quot%3B%26gt%3B%26quot%3Bamount%26quot%3B%3A%20100.0%26lt%3B%2Fspan%26gt%3B%26lt%3Bbr%20style%3D%26quot%3Bfont-family%3A%20monospace%3B%20font-size%3A%2011px%3B%26quot%3B%26gt%3B%26lt%3Bspan%20style%3D%26quot%3Bfont-family%3A%20monospace%3B%20font-size%3A%2011px%3B%26quot%3B%26gt%3B%7D%26lt%3B%2Fspan%26gt%3B%22%20style%3D%22rounded%3D1%3BwhiteSpace%3Dwrap%3Bhtml%3D1%3Balign%3Dleft%3B%22%20vertex%3D%221%22%20parent%3D%221%22%3E%3CmxGeometry%20x%3D%22-425%22%20y%3D%22403%22%20width%3D%22177.5%22%20height%3D%22107%22%20as%3D%22geometry%22%2F%3E%3C%2FmxCell%3E%3CmxCell%20id%3D%2225%22%20value%3D%22%26lt%3Bspan%20style%3D%26quot%3Bfont-family%3A%20monospace%3B%20text-align%3A%20left%3B%26quot%3B%26gt%3B%26lt%3Bfont%20style%3D%26quot%3Bfont-size%3A%2014px%3B%26quot%3B%26gt%3B%26lt%3Bb%26gt%3BEXEMPLO%20JSON%26lt%3B%2Fb%26gt%3B%26lt%3B%2Ffont%26gt%3B%26lt%3B%2Fspan%26gt%3B%22%20style%3D%22shape%3Dhexagon%3Bperimeter%3DhexagonPerimeter2%3BwhiteSpace%3Dwrap%3Bhtml%3D1%3BfixedSize%3D1%3B%22%20vertex%3D%221%22%20parent%3D%221%22%3E%3CmxGeometry%20x%3D%22-425%22%20y%3D%22350%22%20width%3D%22180%22%20height%3D%2240%22%20as%3D%22geometry%22%2F%3E%3C%2FmxCell%3E%3C%2Froot%3E%3C%2FmxGraphModel%3E



## 👨‍💻 Autor

**Elias Andre Torres**
- GitHub: [@eliasAcc25](https://github.com/eliasAcc25)
- LinkedIn: [Elias Andre Torres](https://linkedin.com/in/elias-andre-torres)





