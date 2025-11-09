# Guia de Instalação do Maven

## Windows

### Opção 1: Download Manual

1. Baixe o Maven: https://maven.apache.org/download.cgi
2. Extraia para `C:\Program Files\Apache\maven`
3. Adicione ao PATH:
   - Variável `MAVEN_HOME`: `C:\Program Files\Apache\maven`
   - Adicione ao PATH: `%MAVEN_HOME%\bin`
4. Reinicie o terminal
5. Teste: `mvn -version`

### Opção 2: Chocolatey

```powershell
choco install maven
```

## Executar Testes

```bash
cd cbf-digital-testes
mvn test
```

Ou execute: `executar-testes.bat`

