# 🚀 GUIA RÁPIDO - CBF Digital Testes

## ⚡ Inicializar Rapidamente

```powershell
# 1. Navegar até o projeto
cd c:\Users\yujif\Desktop\cbf-digital-testes

# 2. Compilar
mvn clean compile

# 3. Executar todos os testes
mvn test

# ✅ Resultado esperado: 64/64 testes passando
```

---

## 🧪 Executar Testes Específicos

### Por Responsável

```powershell
# Samuel (CT01-CT10, CT41-CT44)
mvn test -Dtest=TestesUnitariosSamuel,TestesIntegracaoSamuel

# Manuela (CT11-CT20, CT45-CT48)
mvn test -Dtest=TestesUnitariosManuela,TestesIntegracaoManuela

# Yuji (CT21-CT30, CT49-CT52)
mvn test -Dtest=TestesUnitariosYuji,TestesIntegracaoYuji

# Caio (CT31-CT40, CT53-CT56)
mvn test -Dtest=TestesUnitariosCaio,TestesIntegracaoCaio
```

### Por Tipo

```powershell
# Apenas testes unitários
mvn test -Dtest=TestesUnitarios*

# Apenas testes de integração
mvn test -Dtest=TestesIntegracao*
```

### Um Teste Específico

```powershell
# Teste único
mvn test -Dtest=TestesUnitariosSamuel#testCT01_CadastroBasicoComNomeEAbreviacao
```

---

## 📊 Gerar Relatório

```powershell
# Relatório de testes
mvn surefire-report:report

# Abrir relatório (Windows)
Start-Process "target\site\surefire-report.html"
```

---

## 🐳 Gerenciar Banco de Dados

```powershell
# Iniciar PostgreSQL (Docker)
docker-compose up -d

# Parar PostgreSQL
docker-compose down

# Ver logs
docker-compose logs -f

# Conectar ao BD (psql)
psql -h localhost -U postgres -d db_testes
```

---

## 📁 Estrutura de Arquivos Importantes

```
cbf-digital-testes/
├── README.md                    # 📖 Documentação principal
├── RELATORIO_FINAL.md          # 📋 Relatório detalhado
├── SUMARIO_EXECUTIVO.md        # 📊 Estatísticas finais
├── GUIA_RAPIDO.md              # ⚡ Este arquivo
├── pom.xml                     # Maven configuration
├── docker-compose.yml          # PostgreSQL config
├── src/
│   ├── main/java/...          # Código de produção
│   └── test/java/...          # Testes automatizados
└── target/                     # Arquivos compilados
```

---

## ✅ Checklist de Verificação

- [ ] Java 22 instalado (`java -version`)
- [ ] Maven 3.9+ instalado (`mvn -version`)
- [ ] Docker Desktop rodando (para testes de integração)
- [ ] PostgreSQL container iniciado (`docker-compose up`)
- [ ] Todos os testes passando (`mvn test`)
- [ ] Nenhuma falha ou erro

---

## 🛠️ Troubleshooting

### "Tests run but all fail"
```
✓ Verifique se PostgreSQL está rodando: docker-compose up -d
✓ Verifique conexão: jdbc:postgresql://localhost:5432/db_testes
✓ Limpe o cache: mvn clean
```

### "PostgreSQL connection refused"
```
✓ Inicie Docker: docker-compose up -d
✓ Aguarde ~10 segundos para inicializar
✓ Verifique porta: netstat -ano | findstr 5432
```

### "Tests skip or no-op"
```
✓ Recompile: mvn clean compile
✓ Limpe testes: mvn clean test-compile
✓ Execute: mvn test
```

---

## 📞 Contatos

**Equipe Arena Controle** (UDESC - Alto Vale)
- Samuel de Souza Marcelino
- Yuji Faruk Murakami Feles
- Manuela Westphal Córdova
- Caio Negrelli Fontalva

---

**Status Final**: ✅ **64/64 Testes Passando**  
**Última Atualização**: 15 de Novembro de 2025

*Projeto concluído com sucesso! 🏆*
