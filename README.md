# CBF Digital - Plano de Testes

Sistema Integrado de Gestão de Ligas (SIGL) - Arena Controle

## 📋 Descrição

Projeto de testes automatizados para o sistema CBF Digital, implementando o Plano de Testes desenvolvido pela equipe Arena Controle (UDESC - Alto Vale).

O sistema gerencia campeonatos de pontos corridos, automatizando:
- Cadastro e gestão de times
- Registro de resultados de partidas
- Cálculo automático de pontuação (regra 3-1-0)
- Tabela de classificação com critérios de desempate
- Estatísticas do campeonato

## 👥 Equipe Arena Controle

- **Samuel de Souza Marcelino**
- **Yuji Faruk Murakami Feles**
- **Manuela Westphal Córdova**
- **Caio Negrelli Fontalva**

## 🏗️ Estrutura do Projeto

```
cbf-digital-testes/
├── src/
│   ├── main/java/br/com/arenacontrole/
│   │   ├── Time.java                    # Classe representando um time
│   │   └── Campeonato.java              # Gerenciador do campeonato
│   └── test/java/br/com/arenacontrole/
│       ├── TestesUnitariosSamuel.java   # CT01-CT10 (RF01-RF04)
│       └── TestesUnitariosComplementares.java  # CT12-CT37 (RF05-RF15)
├── pom.xml                               # Configuração Maven
└── README.md
```

## 🧪 Cobertura de Testes

### Testes Unitários Implementados

| Requisito | Descrição | Casos de Teste |
|-----------|-----------|----------------|
| RF01 | Cadastro de Times | CT01 |
| RF02 | Definição de Atributos | CT02, CT03 |
| RF03 | Registro de Resultados | CT04, CT05 |
| RF04 | Atualização Automática (3-1-0) | CT06-CT10 |
| RF05 | Exibição da Tabela | CT11 |
| RF06 | Ordenação (Desempate) | CT12-CT16 |
| RF08 | Remoção de Times | CT19-CT20 |
| RF09 | Bloqueio Placar Negativo | CT21-CT22 |
| RF10 | Bloqueio Times Duplicados | CT23 |
| RF11 | Validação de Cadastro | CT24, CT28 |
| RF13 | Cálculo Total de Gols | CT31-CT32 |
| RF15 | Bloqueio Times Ímpares | CT35, CT37 |

## 📐 Regras de Negócio Implementadas

- **RN01-RN03**: Pontuação (3 pts vitória, 1 pt empate, 0 pts derrota)
- **RN05**: Critérios de desempate (1º PTs, 2º V, 3º SG, 4º GM, 5º Menos CV, 6º Menos CA)
- **RN06**: Cálculo de Saldo de Gols (GM - GS)
- **RN08**: Valores não negativos
- **RN11**: Cartões por equipe (não por jogador)
- **RN12**: Número par de times
- **RN14**: Nome único de time
- **RN15**: Times diferentes em uma partida

## 🚀 Como Executar

### Pré-requisitos

- Java 11 ou superior
- Maven 3.6 ou superior

### Verificar instalação

```bash
java -version
mvn -version
```

### Executar todos os testes

```bash
cd cbf-digital-testes
mvn test
```

### Executar teste específico

```bash
# Executar apenas testes do Samuel (RF01-RF04)
mvn test -Dtest=TestesUnitariosSamuel

# Executar apenas testes complementares
mvn test -Dtest=TestesUnitariosComplementares

# Executar um teste específico
mvn test -Dtest=TestesUnitariosSamuel#testCT01_CadastroBasicoComNomeEAbreviacao
```

### Compilar o projeto

```bash
mvn clean compile
```

### Gerar relatório de testes

```bash
mvn clean test surefire-report:report
```

## 📊 Exemplo de Uso das Classes

```java
// Criar campeonato
Campeonato campeonato = new Campeonato();

// Cadastrar times
campeonato.cadastrarTime("São Paulo", "SAO");
campeonato.cadastrarTime("Santos", "SAN");

// Registrar resultado: São Paulo 3x1 Santos
campeonato.registrarResultado("São Paulo", "Santos", 3, 1, 2, 0, 1, 0);

// Obter tabela classificada
List<Time> tabela = campeonato.obterTabelaClassificacao();

// Exibir classificação
for (Time time : tabela) {
    System.out.println(time);
}

// Calcular total de gols do campeonato
int totalGols = campeonato.calcularTotalGols();
System.out.println("Total de gols: " + totalGols);
```

## ✅ Resultados Esperados

Ao executar `mvn test`, você deve ver:

```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running br.com.arenacontrole.TestesUnitariosSamuel
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running br.com.arenacontrole.TestesUnitariosComplementares
[INFO] Tests run: 14, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 24, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] BUILD SUCCESS
```

## 📖 Documentação Adicional

- **Plano de Testes Completo**: `65TESPlano de Testes2.pdf`
- **Total de Casos de Teste Planejados**: 80
  - Unitários: 40
  - Integração: 16
  - Sistema: 16
  - Aceitação: 8

## 🎯 Próximos Passos

1. Implementar testes de integração (RF01-RF16, CT41-CT56)
2. Implementar testes de sistema com Selenium
3. Implementar testes de aceitação
4. Adicionar persistência em banco de dados
5. Implementar camada de interface web

## 📝 Notas

Este projeto foi desenvolvido como parte da disciplina de Teste de Software da UDESC - Alto Vale, seguindo rigorosamente o Plano de Testes documentado pela equipe Arena Controle.

---

**Arena Controle** - Profissionalizando a gestão de competições esportivas 🏆

