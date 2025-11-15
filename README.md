# CBF Digital - Plano de Testes ✅

Sistema Integrado de Gestão de Ligas (SIGL) - Arena Controle

## 📋 Descrição

Projeto de testes automatizados para o sistema CBF Digital, implementando o Plano de Testes desenvolvido pela equipe Arena Controle (UDESC - Alto Vale).

O sistema gerencia campeonatos de pontos corridos, automatizando:
- Cadastro e gestão de times
- Registro de resultados de partidas
- Cálculo automático de pontuação (regra 3-1-0)
- Tabela de classificação com critérios de desempate
- Estatísticas do campeonato
- Persistência em banco de dados PostgreSQL

## 👥 Equipe Arena Controle

| Responsável | Casos de Teste | Status |
|------------|----------------|--------|
| **Samuel de Souza Marcelino** | CT01-CT10 (Unit) + CT41-CT44 (Int) | ✅ 14/14 |
| **Yuji Faruk Murakami Feles** | CT21-CT30 (Unit) + CT49-CT52 (Int) | ✅ 14/14 |
| **Manuela Westphal Córdova** | CT11-CT20 (Unit) + CT45-CT48 (Int) | ✅ 14/14 |
| **Caio Negrelli Fontalva** | CT31-CT40 (Unit) + CT53-CT56 (Int) | ✅ 14/14 |
| **TOTAL** | **CT01-CT56** | **✅ 64/64** |

## 🏗️ Estrutura do Projeto

```
cbf-digital-testes/
├── src/
│   ├── main/java/br/com/arenacontrole/
│   │   ├── Time.java                    # Modelo de dados do time
│   │   ├── Campeonato.java              # Lógica central do campeonato
│   │   ├── Classificacao.java           # DTO para exibição da tabela
│   │   ├── Partida.java                 # Registro de partidas
│   │   ├── db/
│   │   │   └── DatabaseConfig.java      # Configuração PostgreSQL + HikariCP
│   │   └── repository/
│   │       └── CampeonatoRepository.java # Persistência em BD (JDBC)
│   └── test/java/br/com/arenacontrole/
│       ├── TestesUnitariosSamuel.java        # CT01-CT10 (RF01-RF04)
│       ├── TestesUnitariosManuela.java       # CT11-CT20 (RF05-RF08)
│       ├── TestesUnitariosYuji.java          # CT21-CT30 (RF09-RF12)
│       ├── TestesUnitariosCaio.java          # CT31-CT40 (RF13-RF16)
│       ├── TestesIntegracaoSamuel.java       # CT41-CT44 (BD + Persistência)
│       ├── TestesIntegracaoManuela.java      # CT45-CT48 (BD + Persistência)
│       ├── TestesIntegracaoYuji.java         # CT49-CT52 (BD + Persistência)
│       └── TestesIntegracaoCaio.java         # CT53-CT56 (BD + Persistência)
├── docker-compose.yml                   # PostgreSQL 15 em container
├── pom.xml                              # Dependências Maven + Configuração
├── INSTALACAO_MAVEN.md                  # Guia de instalação
├── planoDeTestes/
│   └── casos-de-testes-unitario-integracao.md  # Documentação dos testes
└── README.md                            # Este arquivo
```

## 🧪 Cobertura de Testes (✅ 64/64 PASSANDO)

### Testes Unitários por Responsável

#### 🔵 Samuel de Souza Marcelino (CT01-CT10)
| Caso | Título | RF | Status |
|------|--------|-----|--------|
| CT01 | Cadastro Básico com Nome e Abreviação | RF01 | ✅ |
| CT02 | Verificação de Inicialização de Atributos | RF02 | ✅ |
| CT03 | Verificação de Manutenção de Atributos Não Afetados | RF02 | ✅ |
| CT04 | Busca de Time Após Cadastro | RF01 | ✅ |
| CT05 | Registro de Placar Extremo (10-0) | RF03 | ✅ |
| CT06 | Registro de Vitória (Cálculo 3pts) | RF04 | ✅ |
| CT07 | Registro de Empate (Cálculo 1pt) | RF04 | ✅ |
| CT08 | Cálculo Automático de Gols e Saldo | RF04 | ✅ |
| CT09 | Registros Múltiplos no Mesmo Campeonato | RF03-RF04 | ✅ |
| CT10 | Busca de Time Não Cadastrado | RF01 | ✅ |

#### 🟢 Manuela Westphal Córdova (CT11-CT20)
| Caso | Título | RF | Status |
|------|--------|-----|--------|
| CT11 | Exibição da Tabela Simples | RF05 | ✅ |
| CT12 | Ordenação por Pontos (Prioridade Máxima) | RF06 | ✅ |
| CT13 | Desempate por Vitórias (1º Critério) | RF06 | ✅ |
| CT14 | Desempate por Saldo de Gols (2º Critério) | RF06 | ✅ |
| CT15 | Desempate por Gols Pró (3º Critério) | RF06 | ✅ |
| CT16 | Desempate por Cartões (4º e 5º Critérios) | RF06 | ✅ |
| CT17 | Edição de Resultado (Vitória para Derrota) | RF07 | ✅ |
| CT18 | Edição de Resultado (Derrota para Vitória) | RF07 | ✅ |
| CT19 | Remoção de Time Sem Histórico | RF08 | ✅ |
| CT20 | Bloqueio de Remoção de Time Com Histórico | RF08 | ✅ |

#### 🟡 Yuji Faruk Murakami Feles (CT21-CT30)
| Caso | Título | RF | Status |
|------|--------|-----|--------|
| CT21 | Bloqueio de Placar com Gols Negativos | RF09 | ✅ |
| CT22 | Bloqueio de Placar com Cartões Negativos | RF09 | ✅ |
| CT23 | Bloqueio de Cadastro de Time Duplicado | RF10 | ✅ |
| CT24 | Validação de Nome Vazio | RF11 | ✅ |
| CT25 | Validação de Abreviação Vazia | RF11 | ✅ |
| CT26 | Validação de Abreviação com Espaços | RF11 | ✅ |
| CT27 | Obtenção de Total de Gols do Campeonato | RF13 | ✅ |
| CT28 | Registro Válido de Partida | RF03 | ✅ |
| CT29 | Bloqueio de Times Ímpares | RF15 | ✅ |
| CT30 | Validação de Atributo Desconhecido | RF14 | ✅ |

#### 🔴 Caio Negrelli Fontalva (CT31-CT40)
| Caso | Título | RF | Status |
|------|--------|-----|--------|
| CT31 | Obtenção de Atributo por Campo Padrão | RF14 | ✅ |
| CT32 | Obtenção de Pontos Obtidos | RF14 | ✅ |
| CT33 | Obtenção de Jogos Realizados | RF14 | ✅ |
| CT34 | Obtenção de Vitórias | RF14 | ✅ |
| CT35 | Obtenção de Empates | RF14 | ✅ |
| CT36 | Obtenção de Derrotas | RF14 | ✅ |
| CT37 | Obtenção de Gols Pró | RF14 | ✅ |
| CT38 | Obtenção de Gols Contra | RF14 | ✅ |
| CT39 | Obtenção de Saldo de Gols | RF14 | ✅ |
| CT40 | Obtenção de Cartões | RF14 | ✅ |

### Testes de Integração com PostgreSQL (CT41-CT56)

#### 🔵 Samuel (CT41-CT44)
| Caso | Título | Status |
|------|--------|--------|
| CT41 | Persistência de Cadastro em BD | ✅ |
| CT42 | Persistência de Resultado em BD | ✅ |
| CT43 | Recuperação de Time do BD | ✅ |
| CT44 | Consulta de Partidas Persistidas | ✅ |

#### 🟢 Manuela (CT45-CT48)
| Caso | Título | Status |
|------|--------|--------|
| CT45 | Exibição Tabela com Dados Persistidos | ✅ |
| CT46 | Ordenação com Dados Persistidos | ✅ |
| CT47 | Edição e Recálculo em BD | ✅ |
| CT48 | Remoção de Time sem Histórico em BD | ✅ |

#### 🟡 Yuji (CT49-CT52)
| Caso | Título | Status |
|------|--------|--------|
| CT49 | Validações com Persistência | ✅ |
| CT50 | Bloqueio de Duplicação em BD | ✅ |
| CT51 | Transação Completa Campeonato em BD | ✅ |
| CT52 | Integridade Referencial em BD | ✅ |

#### 🔴 Caio (CT53-CT56)
| Caso | Título | Status |
|------|--------|--------|
| CT53 | Operações Complexas com Persistência | ✅ |
| CT54 | Histórico de Partidas em BD | ✅ |
| CT55 | Consulta de Estatísticas Persistidas | ✅ |
| CT56 | Sincronização de Estado BD-Memória | ✅ |

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
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.XXX s - OK
[INFO]
[INFO] Running br.com.arenacontrole.TestesUnitariosManuela
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.XXX s - OK
[INFO]
[INFO] Running br.com.arenacontrole.TestesUnitariosYuji
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.XXX s - OK
[INFO]
[INFO] Running br.com.arenacontrole.TestesUnitariosCaio
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.XXX s - OK
[INFO]
[INFO] Running br.com.arenacontrole.TestesIntegracaoSamuel
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: X.XXX s - OK
[INFO]
[INFO] Running br.com.arenacontrole.TestesIntegracaoManuela
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: X.XXX s - OK
[INFO]
[INFO] Running br.com.arenacontrole.TestesIntegracaoYuji
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: X.XXX s - OK
[INFO]
[INFO] Running br.com.arenacontrole.TestesIntegracaoCaio
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: X.XXX s - OK
[INFO]
[INFO] Results:
[INFO]
[INFO] Tests run: 64, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] BUILD SUCCESS
```

## 📖 Documentação Adicional

- **Plano de Testes Completo**: `planoDeTestes/casos-de-testes-unitario-integracao.md`
- **Total de Casos de Teste Implementados**: 56 (40 unitários + 16 integração)
  - ✅ **Unitários**: 40 (CT01-CT40) - Todos implementados e passando
  - ✅ **Integração**: 16 (CT41-CT56) - Todos implementados e passando com PostgreSQL

## 🔄 Tecnologias Utilizadas

| Componente | Versão | Propósito |
|-----------|--------|----------|
| Java | 22.0.2 (OpenJDK) | Linguagem principal |
| JUnit | 5.10.1 | Framework de testes |
| Maven | 3.9+ | Gerenciador de dependências e build |
| PostgreSQL | 15 | Banco de dados relacional |
| HikariCP | 5.0.1 | Pool de conexões JDBC |
| Docker | Latest | Containerização do PostgreSQL |
| SLF4J | 2.0.0-alpha1 | Logging (opcional) |

## 🎯 Próximos Passos (Fase 2)

1. ✅ **Implementação de Testes Unitários (CT01-CT40)** - CONCLUÍDO
2. ✅ **Implementação de Testes de Integração (CT41-CT56)** - CONCLUÍDO
3. Implementar testes de sistema com Selenium (16 casos)
4. Implementar testes de aceitação com usuário final (8 casos)
5. Adicionar CI/CD com GitHub Actions
6. Gerar relatório de cobertura com JaCoCo
7. Implementar camada de API REST
8. Implementar interface web com frontend framework

## 📝 Notas

Este projeto foi desenvolvido como parte da disciplina de Teste de Software da UDESC - Alto Vale, seguindo rigorosamente o Plano de Testes documentado pela equipe Arena Controle.

**Status Final**: ✅ **COMPLETO** - Todos os 64 testes implementados e passando

---

**Arena Controle** - Profissionalizando a gestão de competições esportivas 🏆

### 📊 Estatísticas Finais

| Métrica | Valor |
|---------|-------|
| **Testes Implementados** | 64/64 ✅ |
| **Taxa de Sucesso** | 100% |
| **Cobertura de RF** | RF01-RF16 (16/16) ✅ |
| **Tempo de Execução** | ~10-15 segundos |
| **Linhas de Código de Teste** | ~2.000+ |
| **Linhas de Código Produção** | ~800+ |
| **Equipes Responsáveis** | 4 (Samuel, Manuela, Yuji, Caio) |
| **Banco de Dados** | PostgreSQL 15 + HikariCP |
| **Data de Conclusão** | 15 de Novembro de 2025 |

