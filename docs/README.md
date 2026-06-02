# 📚 docs/ — Documentação e evidências (issues #8 e #9)

| Arquivo | Conteúdo |
|---|---|
| [`DOCUMENTACAO-SOA.md`](DOCUMENTACAO-SOA.md) | Documento final da arquitetura SOA (fonte do PDF) |
| [`DOCUMENTACAO-SOA.pdf`](DOCUMENTACAO-SOA.pdf) | **PDF final** entregável (gerado do markdown) |
| [`evidencias/EVIDENCIAS.md`](evidencias/EVIDENCIAS.md) | Req/resp reais (REST + SOAP + integração + fallback) |
| [`postman/`](postman/) | Coleção Postman (todos os verbos REST + SOAP) |
| [`soapui/`](soapui/) | Passo a passo SoapUI + XMLs de requisição prontos |

## Regenerar o PDF

Requer Python com `markdown` e o Chrome instalado.

```bash
# 1) Markdown -> HTML estilizado
python docs/gerar-pdf.py

# 2) HTML -> PDF (Chrome headless)
chrome --headless --disable-gpu --no-pdf-header-footer \
  --print-to-pdf="docs/DOCUMENTACAO-SOA.pdf" \
  "file:///CAMINHO/ABSOLUTO/docs/DOCUMENTACAO-SOA.html"
```

> ⚠️ **Antes de gerar a versão final:** preencha **nome e RM dos integrantes** na seção
> *Integrantes* de `DOCUMENTACAO-SOA.md` e insira os **prints** dos testes na seção 7.
