# 🟧 2f-agro-soa

> Serviços distribuídos em **Java + Spring Boot** do 2F-AGRO.
> Matéria: **SOA — Service-Oriented Architecture** · FIAP 3ESPZ · GS 2026.1

[![Hub](https://img.shields.io/badge/hub-2f--agro-success)](https://github.com/GS-SPACE-CONNECT/2f-agro)
[![Java](https://img.shields.io/badge/Java-17-orange)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F)](https://spring.io/projects/spring-boot)

## 🎯 Objetivo

Demonstrar os princípios da **Arquitetura Orientada a Serviços** (baixo acoplamento, reutilização, interoperabilidade, contratos de serviço, integração e separação de responsabilidades) integrando um **app moderno (REST)** com um **sistema legado de governo (SOAP)** e dados **espaciais externos (NASA/CPTEC)** — tudo no contexto Space Connect do 2F-AGRO.

## 🛰️ Narrativa Space Connect

```
   📱 App 2F-AGRO                  🏛️ Governo legado            🛰️ Dado espacial
   (cliente moderno)              (EMATER / MAPA / CAR)        (satélite / clima)
        │                                ▲                            ▲
        ▼ REST/JSON                      │ SOAP/XML                   │ REST/JSON
 ┌──────────────────┐   cliente SOAP    ┌──────────────────┐   cliente HTTP   ┌─────────────┐
 │  API REST        │ ────────────────▶ │  Web Service     │                  │  NASA POWER │
 │  Spring Boot     │                   │  SOAP (Spring-WS)│                  │  / CPTEC-INPE│
 │  CRUD Propriedade│ ◀──────────────── │  Cadastro Rural  │ ◀─────────────── │  (externo)  │
 └────────┬─────────┘                   └──────────────────┘                  └─────────────┘
          │
          ▼
      🗄️ H2 (JPA)
```

> *"O app moderno (REST) conversa com o sistema legado do governo (SOAP) e enriquece a decisão do agricultor com dados de satélite. Interoperabilidade de verdade."*

## 🧱 Componentes

| Componente | Tecnologia | O que faz |
|---|---|---|
| **API REST** | Spring Boot + Spring Web | CRUD completo de `Propriedade` (GET/POST/PUT/DELETE, JSON, validação, tratamento de erro) |
| **Web Service SOAP** | Spring-WS (contract-first) | Simula cadastro rural do governo: `consultarCadastroRural` + `registrarCadastroRural`, WSDL publicado |
| **Integração externa** | `RestClient` / `WebClient` | REST consome **NASA POWER** (clima/solar por lat/long) → enriquece a propriedade |
| **Integração interna** | cliente SOAP (JAXB) | Ao cadastrar no REST, registra/valida no "governo" via SOAP |
| **Persistência** | Spring Data JPA + **H2** | CRUD persistente em banco H2 (file/in-memory) |

## 🏛️ POO (requisito obrigatório)

- **Abstração** → `interface ServicoClimatico` (NASA / CPTEC intercambiáveis)
- **Encapsulamento** → entidades JPA com campos privados + validação
- **Herança** → `Alerta` (abstrato) → `AlertaSeca`, `AlertaGeada`, `AlertaPraga`
- **Polimorfismo** → `alerta.gerarMensagem()` sobrescrito por subtipo

## 🗂️ Estrutura sugerida

```
src/main/java/br/com/fiap/agro/soa/
├── Application.java
├── rest/                 # Controllers REST
│   ├── PropriedadeController.java
│   └── handler/GlobalExceptionHandler.java   # @ControllerAdvice
├── soap/                 # Endpoints SOAP (Spring-WS)
│   ├── CadastroRuralEndpoint.java
│   └── config/WebServiceConfig.java
├── domain/               # POO: entidades + Alerta abstrato + herdeiros
├── repository/           # Spring Data JPA
├── service/              # Regras + ServicoClimatico (abstração)
│   └── client/           # cliente NASA/CPTEC + cliente SOAP
└── dto/
src/main/resources/
├── application.yml       # config H2
└── xsd/cadastro-rural.xsd  # contract-first → gera o WSDL
```

## 🚀 Setup local

```bash
# requer Java 17+ e Maven
mvn spring-boot:run

# REST            → http://localhost:8080/api/propriedades
# SOAP WSDL       → http://localhost:8080/ws/cadastro-rural.wsdl
# H2 console      → http://localhost:8080/h2-console
# Swagger UI      → http://localhost:8080/swagger-ui.html
```

### 🗄️ Banco H2 (console)

A persistência usa um banco **H2 em memória** (recriado a cada start) com um seed inicial
(`src/main/resources/data.sql`) de 4 propriedades de exemplo.

Acesse o console em **http://localhost:8080/h2-console** com:

| Campo | Valor |
|---|---|
| **JDBC URL** | `jdbc:h2:mem:agrodb` |
| **User Name** | `sa` |
| **Password** | *(em branco)* |

> Use exatamente a JDBC URL acima (igual à de `application.yml`), senão o console abre um
> banco vazio diferente. A tabela mapeada é `PROPRIEDADE`.

## 🔗 Integração entre serviços (issue #7)

O endpoint de integração orquestra os três serviços de forma **resiliente** (falha de um
serviço externo não derruba o fluxo — vira aviso no resultado):

```
POST /api/integracao/propriedades
   │
   ├─(1) cria a propriedade ........... REST  → H2 (Spring Data JPA)
   ├─(2) registra no "governo" ........ SOAP  → WebServiceTemplate (cliente JAXB) → /ws
   ├─(3) consulta clima do ponto ...... REST  → NASA POWER (RestClient, timeout+fallback)
   └─(4) deriva alerta agroclimático .. POO   → Alerta.gerarMensagem() (polimorfismo)
   ▼
   CadastroIntegradoResponse { propriedade, protocoloGoverno, clima, alerta, avisos[] }
```

| Endpoint | O que faz |
|---|---|
| `GET /api/integracao/propriedades/{id}/clima` | Propriedade **enriquecida** com clima da NASA POWER (fallback se indisponível) |
| `POST /api/integracao/propriedades` | Fluxo completo: cria (REST) → registra (SOAP) → enriquece (NASA) → alerta |

- **Serviço externo (espacial):** `NasaPowerServicoClimatico` implementa `ServicoClimatico`, consome a API de *climatology* da NASA POWER via `RestClient` (timeouts de 5s/8s).
- **REST ↔ SOAP interno:** `CadastroRuralClient` (`WebServiceTemplate` + JAXB) chama o próprio Web Service SOAP pela rede.
- **Resiliência:** falha da NASA → `climaDisponivel=false` + aviso; falha do SOAP → `protocoloGoverno=null` + aviso; a propriedade é criada de qualquer forma.
- **Config:** `soa.nasa.power-base-url` e `soa.governo.soap-uri` permitem apontar/simular os serviços.

## 📄 Documentação & 🧪 Testes

- **Documentação completa** (arquitetura, diagrama SOA, explicação REST/SOAP/integração, **evidências e prints** dos testes): **[`docs/DOCUMENTACAO-SOA.pdf`](docs/DOCUMENTACAO-SOA.pdf)**
- **Coleção Postman** (todos os verbos REST + integração + SOAP, prontos pra rodar): [`docs/postman/2f-agro-soa.postman_collection.json`](docs/postman/2f-agro-soa.postman_collection.json)
- **Testes SOAP** (instruções + XMLs de requisição): [`docs/soapui/`](docs/soapui/)
- **Swagger UI** (documentação interativa da API REST): `http://localhost:8080/swagger-ui.html` (contrato em `/v3/api-docs`)

> Para chamar o SOAP no Postman: **POST** `http://localhost:8080/ws` com header
> `Content-Type: text/xml; charset=utf-8` e o envelope no corpo (raw/XML).

## 📊 Mapa da rubrica (25% cada)

| Critério | Peso | Onde está |
|---|---|---|
| Implementação da API REST | 25% | `rest/` + CRUD `Propriedade` |
| Implementação do Web Service SOAP | 25% | `soap/` + WSDL + testes SoapUI |
| Integração entre serviços | 25% | REST↔SOAP + consumo NASA/CPTEC |
| Documentação da arquitetura | 25% | [`docs/`](docs/) + PDF final |

## ✨ Diferenciais (bônus)

✅ **Swagger/OpenAPI** (implementado) · Docker Compose · Testes JUnit · Mensageria (RabbitMQ) · Deploy nuvem

## 👥 Owners

[@brnleao](https://github.com/brnleao) · [@DevRuanVieira](https://github.com/DevRuanVieira) · apoio [@jota0802](https://github.com/jota0802) · Team [`backend`](https://github.com/orgs/GS-SPACE-CONNECT/teams/backend)

## 🔗 Links

- [Hub 2f-agro](https://github.com/GS-SPACE-CONNECT/2f-agro) · [Backend C# (.NET)](https://github.com/GS-SPACE-CONNECT/2f-agro-backend)
- [NASA POWER API](https://power.larc.nasa.gov/docs/services/api/) · [CPTEC/INPE](https://www.cptec.inpe.br/)
- ODS 1 · 2 · 3 (relacionadas)
