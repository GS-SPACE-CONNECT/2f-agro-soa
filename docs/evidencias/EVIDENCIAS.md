# Evidências de funcionamento — 2f-agro-soa

Exemplos **reais** de requisição/resposta capturados com a aplicação rodando
(`mvn spring-boot:run`, base `http://localhost:8080`).

> **Prints:** salve as capturas de tela (Postman/SoapUI) nesta pasta seguindo os nomes
> sugeridos em cada seção. Os corpos abaixo são os obtidos nos testes e servem de referência.

---

## 1. REST — CRUD de Propriedade

### 1.1 POST criar → `201 Created`
**Request** `POST /api/propriedades`
```json
{ "produtor": "Joao Silva", "cultura": "Soja", "latitude": -23.5,
  "longitude": -46.6, "areaHa": 120.5, "municipio": "Ribeirao Preto", "uf": "SP" }
```
**Response** `201`
```json
{ "id": 1, "produtor": "Joao Silva", "cultura": "Soja", "latitude": -23.5,
  "longitude": -46.6, "areaHa": 120.5, "municipio": "Ribeirao Preto", "uf": "SP" }
```
📷 `rest-01-post-201.png`

### 1.2 GET listar → `200 OK`
**Request** `GET /api/propriedades`
**Response** `200` — array de propriedades (4 do seed `data.sql` + criadas).
📷 `rest-02-get-lista-200.png`

### 1.3 GET por id → `200 OK`
**Request** `GET /api/propriedades/1` → `200` com o objeto.
📷 `rest-03-get-id-200.png`

### 1.4 PUT atualizar → `200 OK`
**Request** `PUT /api/propriedades/1` (cultura "Milho", areaHa 200) → `200` com o objeto atualizado.
📷 `rest-04-put-200.png`

### 1.5 DELETE → `204 No Content`
**Request** `DELETE /api/propriedades/1` → `204` (corpo vazio).
📷 `rest-05-delete-204.png`

### 1.6 POST inválido → `400 Bad Request`
**Request** `POST /api/propriedades`
```json
{ "produtor": "", "cultura": "Soja", "latitude": 999, "longitude": -46.6,
  "areaHa": -5, "municipio": "X", "uf": "SAO" }
```
**Response** `400`
```json
{
  "status": 400, "erro": "Bad Request",
  "mensagem": "Falha de validação nos campos enviados",
  "caminho": "/api/propriedades",
  "campos": {
    "latitude": "latitude deve ser <= 90",
    "uf": "uf deve ter exatamente 2 letras",
    "produtor": "produtor é obrigatório",
    "areaHa": "areaHa deve ser positiva"
  }
}
```
📷 `rest-06-post-400.png`

### 1.7 GET inexistente → `404 Not Found`
**Request** `GET /api/propriedades/999`
**Response** `404`
```json
{ "status": 404, "erro": "Not Found",
  "mensagem": "Propriedade não encontrada: id=999",
  "caminho": "/api/propriedades/999", "campos": null }
```
📷 `rest-07-get-404.png`

---

## 2. SOAP — Cadastro Rural

### 2.1 registrarCadastroRural
**Request** `POST /ws` (`Content-Type: text/xml`)
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:cad="http://fiap.com.br/agro/soa/cadastro-rural">
  <soapenv:Body>
    <cad:registrarCadastroRuralRequest>
      <cad:cpf>123.456.789-00</cad:cpf>
      <cad:car>CAR-SP-001</cad:car>
      <cad:nomeProdutor>Joao Silva</cad:nomeProdutor>
      <cad:municipio>Ribeirao Preto</cad:municipio>
      <cad:uf>SP</cad:uf>
      <cad:areaHa>120.5</cad:areaHa>
    </cad:registrarCadastroRuralRequest>
  </soapenv:Body>
</soapenv:Envelope>
```
**Response**
```xml
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
  <SOAP-ENV:Body>
    <ns2:registrarCadastroRuralResponse xmlns:ns2="http://fiap.com.br/agro/soa/cadastro-rural">
      <ns2:protocolo>CAR-1</ns2:protocolo>
      <ns2:situacao>REGISTRADO</ns2:situacao>
      <ns2:mensagem>Cadastro rural registrado com sucesso para Joao Silva</ns2:mensagem>
    </ns2:registrarCadastroRuralResponse>
  </SOAP-ENV:Body>
</SOAP-ENV:Envelope>
```
📷 `soap-01-registrar.png`

### 2.2 consultarCadastroRural (encontrado)
**Request** `POST /ws`
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:cad="http://fiap.com.br/agro/soa/cadastro-rural">
  <soapenv:Body>
    <cad:consultarCadastroRuralRequest><cad:cpf>123.456.789-00</cad:cpf></cad:consultarCadastroRuralRequest>
  </soapenv:Body>
</soapenv:Envelope>
```
**Response**
```xml
<ns2:consultarCadastroRuralResponse xmlns:ns2="http://fiap.com.br/agro/soa/cadastro-rural">
  <ns2:encontrado>true</ns2:encontrado>
  <ns2:cadastro>
    <ns2:cpf>123.456.789-00</ns2:cpf>
    <ns2:car>CAR-SP-001</ns2:car>
    <ns2:nomeProdutor>Joao Silva</ns2:nomeProdutor>
    <ns2:municipio>Ribeirao Preto</ns2:municipio>
    <ns2:uf>SP</ns2:uf>
    <ns2:areaHa>120.5</ns2:areaHa>
    <ns2:situacao>REGISTRADO</ns2:situacao>
  </ns2:cadastro>
</ns2:consultarCadastroRuralResponse>
```
📷 `soap-02-consultar-encontrado.png`

### 2.3 consultarCadastroRural (não encontrado)
**Response** — `<ns2:encontrado>false</ns2:encontrado>` (sem bloco `cadastro`).
📷 `soap-03-consultar-naoencontrado.png`

---

## 3. Integração (NASA POWER + REST↔SOAP)

### 3.1 GET enriquecida (clima real da NASA) → `200`
**Request** `GET /api/integracao/propriedades/1/clima`
**Response** `200`
```json
{
  "propriedade": { "id": 1, "produtor": "João Silva", "municipio": "Ribeirão Preto", "uf": "SP", "latitude": -21.1767, "longitude": -47.8208, "...": "..." },
  "climaDisponivel": true,
  "clima": { "temperaturaMediaC": 23.45, "precipitacaoMm": 3.48, "umidadeRelativa": 68.97, "fonte": "NASA POWER" },
  "alerta": null, "aviso": null
}
```
📷 `integ-01-enriquecida-200.png`

### 3.2 POST cadastro integrado (REST → SOAP → NASA) → `201`
**Request** `POST /api/integracao/propriedades` (corpo de propriedade)
**Response** `201`
```json
{
  "propriedade": { "id": 5, "produtor": "Pedro Geada", "uf": "RS", "...": "..." },
  "protocoloGoverno": "CAR-1", "situacaoGoverno": "REGISTRADO",
  "climaDisponivel": true,
  "clima": { "temperaturaMediaC": 17.13, "precipitacaoMm": 4.56, "umidadeRelativa": 82.06, "fonte": "NASA POWER" },
  "alerta": null, "avisos": []
}
```
📷 `integ-02-cadastro-integrado-201.png`

### 3.3 Fallback resiliente (serviços externos fora do ar) → `201`
Com NASA e SOAP indisponíveis, o fluxo **não quebra**:
```json
{
  "propriedade": { "id": 5, "produtor": "Teste Resiliencia", "...": "..." },
  "protocoloGoverno": null, "situacaoGoverno": null,
  "climaDisponivel": false, "clima": null, "alerta": null,
  "avisos": [
    "Registro no governo (SOAP) indisponível: I/O error: Connection refused: getsockopt",
    "Dado climático indisponível (NASA POWER): NASA POWER indisponível para (-23.5, -46.6)"
  ]
}
```
📷 `integ-03-fallback-201.png`
