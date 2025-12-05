# 📄 Leitura e Processamento de Arquivos SPC, Serasa e Protestos

Este projeto tem como objetivo **ler, interpretar e processar arquivos** provenientes do **SPC**, **Serasa** e **cartórios de protesto**, permitindo identificar clientes negativados e padronizar as informações para uso interno em análises, auditorias e processos de cobrança.

---

## ✨ Funcionalidades

- 📥 Importação de arquivos nos formatos suportados  
- 🔍 Validação da estrutura e do layout de cada arquivo  
- 🧹 Padronização e limpeza dos dados  
- 📝 Identificação de clientes negativados  
- ⚙️ Base para integração com sistemas internos

---

## 📁 Tipos de Arquivos Suportados

- **SPC** — arquivos texto estruturados  
- **Serasa** — geralmente TXT ou CSV  
- **Protestos** — dados de cartório com registros de pendências  

> Cada arquivo segue um layout específico, e o sistema faz o tratamento adequado conforme o tipo escolhido.

---

## 🚀 Tecnologias Utilizadas

- Linguagem: **Java, SQL**  
- Bibliotecas principais:  
  - Ex.: `opencsv`, `mysql-connector-java`, `org.openjfx`   
 
 ---

## ▶️ Como Executar

1. Clone o repositório:
   ```bash
   git clone git@github.com:mateusdemirandapereira/read-file.git
   cd read-file
