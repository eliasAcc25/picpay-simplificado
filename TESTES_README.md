# 🧪 Guia Completo de Testes Unitários - PicPay Simplificado

## 📋 O que foi criado

### 1. **Testes de Serviço** (`TransferenciaServiceTest.java`)
- ✅ Transferência bem-sucedida
- ✅ Lojista não pode transferir
- ✅ Saldo insuficiente
- ✅ Usuário não encontrado
- ✅ Transferência para si mesmo
- ✅ Verificação de atualização de saldos

### 2. **Testes de Entidades** 
- **`SaldoTest.java`**: Testa métodos de negócio (debitar, creditar, verificar saldo)
- **`TransferenciaTest.java`**: Testa estados e regras de negócio

### 3. **Testes de Controller** (`TransferControllerTest.java`)
- ✅ Requisições HTTP bem-sucedidas
- ✅ Tratamento de erros
- ✅ Validação de JSON
- ✅ Content-Type e métodos HTTP

### 4. **Testes de Integração** (`SimplificadoApplicationTests.java`)
- ✅ Teste completo da aplicação (Controller → Service → Repository → Database)
- ✅ Cenários reais de uso

## 🚀 Como executar os testes

### Via Maven (Linha de comando):
```bash
# Executar todos os testes
mvn test

# Executar apenas uma classe de teste
mvn test -Dtest=TransferenciaServiceTest

# Executar com relatório detalhado
mvn test -Dtest.loglevel=DEBUG
```

### Via IDE (IntelliJ/Eclipse):
1. Clique com botão direito na pasta `src/test/java`
2. Selecione "Run All Tests"
3. Ou execute classes individuais

## 📊 Cobertura de Testes

### **Padrões Seguidos:**
- ✅ **AAA Pattern**: Arrange, Act, Assert
- ✅ **Given-When-Then**: Estrutura clara
- ✅ **Nomenclatura descritiva**: `deveRealizarTransferenciaComSucesso()`
- ✅ **Isolamento**: Cada teste independente
- ✅ **Mocks**: Dependências simuladas

### **Tipos de Teste:**
- 🔹 **Unitários**: Testam classes isoladamente
- 🔹 **Integração**: Testam camadas juntas
- 🔹 **End-to-End**: Testam fluxo completo

## 🛠️ Tecnologias Utilizadas

```xml
<!-- Dependências de teste -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

### Inclui:
- **JUnit 5**: Framework de testes
- **Mockito**: Criação de mocks
- **AssertJ**: Assertions fluentes
- **Spring Test**: Testes de integração
- **TestContainers**: Testes com banco real (se necessário)

## 📈 Métricas de Qualidade

### **Cobertura esperada:**
- Service: ~95% (lógica de negócio crítica)
- Controller: ~85% (endpoints principais)
- Entities: ~90% (métodos de negócio)

### **Cenários testados:**
- ✅ Casos de sucesso (happy path)
- ✅ Casos de erro (edge cases)
- ✅ Validações de entrada
- ✅ Regras de negócio
- ✅ Integração entre camadas

## 🔍 Estrutura dos Testes

```
src/test/java/
├── PicPay/Simplificado/
│   ├── SimplificadoApplicationTests.java    # Testes de integração
│   ├── controller/
│   │   └── TransferControllerTest.java      # Testes de API
│   ├── service/
│   │   └── TransferenciaServiceTest.java    # Testes de lógica
│   └── model/entity/
│       ├── SaldoTest.java                   # Testes de entidade
│       └── TransferenciaTest.java           # Testes de entidade
└── resources/
    └── application-test.properties          # Configurações de teste
```

## 💡 Conceitos Explicados

### **@Mock vs @MockBean**
- `@Mock`: Para testes unitários puros
- `@MockBean`: Para testes com contexto Spring

### **@ExtendWith vs @SpringBootTest**
- `@ExtendWith(MockitoExtension.class)`: Testes unitários rápidos
- `@SpringBootTest`: Testes de integração completos

### **when().thenReturn() vs verify()**
- `when()`: Define comportamento do mock
- `verify()`: Verifica se método foi chamado

### **Assertions importantes:**
```java
assertEquals(esperado, atual, "mensagem");
assertTrue(condicao, "mensagem");
assertThrows(Exception.class, () -> codigo);
assertNotNull(objeto, "mensagem");
```

## 🎯 Próximos Passos

1. **Executar os testes**: `mvn test`
2. **Verificar cobertura**: Usar JaCoCo ou IDE
3. **Adicionar mais cenários**: Conforme surgem bugs
4. **Testes de performance**: Para alta carga
5. **Testes de segurança**: Validar autenticação

## 🚨 Troubleshooting

### Problema: Testes falhando
```bash
# Limpar e recompilar
mvn clean compile test
```

### Problema: H2 não funciona
- Verificar `application-test.properties`
- Confirmar dependência H2 no `pom.xml`

### Problema: Mocks não funcionam
- Verificar `@ExtendWith(MockitoExtension.class)`
- Confirmar `@Mock` e `@InjectMocks`

## 📚 Recursos para Estudo

- [JUnit 5 Documentation](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Spring Boot Testing](https://spring.io/guides/gs/testing-web/)
- [AssertJ Documentation](https://assertj.github.io/doc/)

## ✅ Checklist de Qualidade

- [x] Testes unitários para todas as classes de serviço
- [x] Testes de integração para endpoints
- [x] Testes de entidades para regras de negócio
- [x] Mocks para dependências externas
- [x] Configuração separada para testes
- [x] Nomenclatura clara e descritiva
- [x] Documentação completa dos testes
