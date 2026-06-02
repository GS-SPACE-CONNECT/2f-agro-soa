# Testes SoapUI — Web Service Cadastro Rural

## Pré-requisito
App rodando: `mvn spring-boot:run` (WSDL em `http://localhost:8080/ws/cadastro-rural.wsdl`).

## Criar o projeto (importa o WSDL → gera as 2 operações)
1. SoapUI → **File ▸ New SOAP Project**.
2. **Project Name:** `2f-agro-soa-cadastro-rural`.
3. **Initial WSDL:** `http://localhost:8080/ws/cadastro-rural.wsdl`.
4. Marque **Create Requests** ▸ **OK**.
5. SoapUI cria o binding com `consultarCadastroRural` e `registrarCadastroRural`.
6. Salve o projeto **nesta pasta** (`docs/soapui/2f-agro-soa-cadastro-rural-soapui-project.xml`).

## Executar
- Abra **registrarCadastroRural ▸ Request 1**, cole o corpo de
  [`requests/registrarCadastroRural.xml`](requests/registrarCadastroRural.xml) e ▶ **Run**.
  → resposta traz `protocolo`, `situacao=REGISTRADO`, `mensagem`.
- Abra **consultarCadastroRural ▸ Request 1**, cole
  [`requests/consultarCadastroRural.xml`](requests/consultarCadastroRural.xml) e ▶ **Run**.
  → resposta traz `encontrado=true` + bloco `cadastro`.

## Evidências
Salve os prints (request + response lado a lado) em
[`../evidencias/`](../evidencias/) — ver nomes sugeridos no
[`../evidencias/EVIDENCIAS.md`](../evidencias/EVIDENCIAS.md).

> Os corpos de req/resp reais já capturados estão documentados em `EVIDENCIAS.md`.
