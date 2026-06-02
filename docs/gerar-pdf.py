import markdown, pathlib
src = pathlib.Path("docs/DOCUMENTACAO-SOA.md").read_text(encoding="utf-8")
body = markdown.markdown(src, extensions=["tables","fenced_code","toc","sane_lists"])
css = """
@page { size: A4; margin: 18mm 16mm; }
* { box-sizing: border-box; }
body { font-family: 'Segoe UI', Arial, sans-serif; font-size: 11pt; line-height: 1.5; color: #1a1a1a; }
h1 { color: #c2410c; border-bottom: 3px solid #f97316; padding-bottom: 6px; font-size: 22pt; }
h2 { color: #9a3412; border-bottom: 1px solid #fdba74; padding-bottom: 3px; margin-top: 24px; font-size: 15pt; page-break-after: avoid; }
h3 { color: #7c2d12; font-size: 12.5pt; margin-top: 16px; page-break-after: avoid; }
table { border-collapse: collapse; width: 100%; margin: 10px 0; font-size: 10pt; }
th, td { border: 1px solid #d4d4d4; padding: 6px 9px; text-align: left; vertical-align: top; }
th { background: #fff7ed; color: #9a3412; }
tr:nth-child(even) td { background: #fafafa; }
code { background: #f4f4f5; padding: 1px 5px; border-radius: 3px; font-family: 'Consolas','Courier New',monospace; font-size: 9.5pt; }
pre { background: #1e1e1e; color: #e4e4e7; padding: 12px 14px; border-radius: 6px; overflow-x: auto; page-break-inside: avoid; }
pre code { background: transparent; color: inherit; padding: 0; font-size: 9pt; line-height: 1.45; }
blockquote { border-left: 4px solid #f97316; margin: 10px 0; padding: 4px 14px; background: #fff7ed; color: #57534e; }
hr { border: none; border-top: 1px solid #e7e5e4; margin: 22px 0; }
a { color: #c2410c; text-decoration: none; }
"""
html = f"""<!DOCTYPE html><html lang="pt-BR"><head><meta charset="utf-8">
<title>Documentacao SOA - 2F-AGRO</title><style>{css}</style></head><body>{body}</body></html>"""
pathlib.Path("docs/DOCUMENTACAO-SOA.html").write_text(html, encoding="utf-8")
print("HTML gerado:", len(html), "bytes")
