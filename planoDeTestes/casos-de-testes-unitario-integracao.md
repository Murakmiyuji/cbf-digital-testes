Com certeza. A seguir, estão todos os **Casos de Teste Unitários** e **Casos de Teste de Integração** detalhados em formato Markdown, extraídos do Plano de Testes,,.

Este formato facilitará a leitura e processamento pelo seu agente, garantindo que os requisitos (RFs) e as regras de negócio (RNs) sejam validados.

---

# Testes Unitários (CT01 - CT40)

O Nível de Teste Unitário utiliza principalmente a **Técnica de Caixa Preta**, focado em validar as funcionalidades isoladas do sistema, como as regras de negócio (RNs) e requisitos funcionais (RFs).

## 1. Unitários de Samuel (RF01 - RF04)

### CT01: Cadastro Básico com Nome e Abreviação
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF01: Cadastro de Times, RT01: O sistema deve permitir cadastro de times |
| **Objetivo** | Validar o cadastro de times. |
| **Localização** | Método `cadastrarTime()`. |
| **Pré-condições** | O usuário master está logado e o sistema está pronto. |
| **Entradas** | `cadastrarTime(“São Paulo”, “SAO”)`. |
| **Passos** | 1. Chamar `cadastrarTime()` com parâmetros válidos. 2. Verificar retorno (sucesso/true). 3. Chamar função de busca para confirmar que o time foi adicionado,. |
| **Resultado Esperado** | Exibição de mensagem de sucesso ("Time cadastrado com sucesso"). O novo time deve aparecer na lista de equipes. |
| **Prioridade** | Alta. |

### CT02: Verificação de Inicialização de Atributos
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF02: Definição de Atributos dos Times, RT02: O sistema deve permitir definições de atributos |
| **Objetivo** | Validar a definição de atributos dos times. |
| **Pré-condições** | Times cadastrados (Vasco, Palmeiras, Corinthians). Todos os atributos estão zerados. |
| **Entradas** | `vasco.mostrarAtributos()`. |
| **Resultado Esperado** | Os atributos: **Pontos, Jogos, Vitórias, Empates, Derrotas, GP, GC, SG, CA e CV devem ser exibidos com o valor 0**. |
| **Prioridade** | Alta. |

### CT03: Verificação de Manutenção de Atributos Não Afetados
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF02: Definição de Atributos dos Times |
| **Objetivo** | Validar a definição de atributos dos times. |
| **Entradas** | `registrarResultado(“Palmeiras”, “Corinthians”, 1, 1)`. |
| **Resultado Esperado** | O sistema deve retornar que os atributos **Vitórias e Derrotas** do Time A (Palmeiras) são iguais a **0** (após o empate). |
| **Prioridade** | Alta. |

### CT04: Registro de Placar com Cartões e Gols
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF03: Registro de Resultados de Partidas, RT03: O sistema deve permitir registros de partdidas |
| **Objetivo** | Validar registros de resultado. |
| **Entradas** | `registrarResultado("São Paulo", " Santos ", 3, 1, 2, 0, 1, 0)` (3x1, 2 CA São Paulo, 1 CA Santos). |
| **Resultado Esperado** | O sistema deve **aceitar e salvar o registro**. |
| **Prioridade** | Crítica. |

### CT05: Registro de Placar Extremo (Goleada)
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF03: Registro de Resultados de Partidas |
| **Objetivo** | Validar registros de resultado. |
| **Entradas** | `registrarResultado("Flamengo", "Botafogo", 10, 0, 0, 0, 0, 0)`. |
| **Resultado Esperado** | O sistema deve **aceitar o registro de 10x0 sem travar** ou emitir erro de limite. |
| **Prioridade** | Alta. |

### CT06: Cálculo Automático de Pontos (Vitória - 3 pts)
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF04: Atualização Automática de Atributos (Cálculo 3-1-0), RN01, RT04: O sistema deve atualizar automaticamente os atributos |
| **Objetivo** | Validar atualização automática de atributos. |
| **Entradas** | `registrarResultado("Grêmio", "Inter", 2, 0, 0, 0, 0, 0)`. |
| **Resultado Esperado** | O sistema deve retornar que `obterAtributo("Grêmio", "Pontos") = 3` e `obterAtributo("Inter", "Pontos") = 0`,. |
| **Prioridade** | Crítica. |

### CT07: Cálculo Automático de Pontos (Empate - 1 pt)
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF04, RN02 |
| **Objetivo** | Validar atualização automática de atributos. |
| **Entradas** | `registrarResultado("Cruzeiro", "Atlético-MG", 1, 1, 0, 0, 0, 0)`. |
| **Resultado Esperado** | O sistema deve retornar `obterAtributo("Cruzeiro", "Pontos") = 1` e `obterAtributo("Atlético-MG", "Pontos") = 1`. |
| **Prioridade** | Crítica. |

### CT08: Cálculo Automático de Gols e Saldo
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF04, RN06 |
| **Objetivo** | Validar atualização automática de atributos. |
| **Entradas** | `registrarResultado("Fluminense", "Vasco", 5, 2, 0, 0, 0, 0)`. |
| **Resultado Esperado** | O sistema deve retornar `obterAtributo("Fluminense", "SG") = 3` e `obterAtributo("Vasco", "SG") = -3`. |
| **Prioridade** | Crítica. |

### CT09: Incremento Automático de Jogos Disputados
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF04 |
| **Objetivo** | Validar atualização automática de atributos. |
| **Entradas** | `registrarResultado("Bahia", "Vitória", 0, 0, 0, 0, 0, 0)`. |
| **Resultado Esperado** | O sistema deve retornar `obterAtributo("Bahia", "Jogos Disputados") = 1` e `obterAtributo("Vitória", "Jogos Disputados") = 1`. |
| **Prioridade** | Crítica. |

### CT10: Incremento Automático de V/E/D
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF04, RN03 |
| **Objetivo** | Validar atualização automática de atributos. |
| **Entradas** | `registrarResultado("Ceará", "Fortaleza", 4, 1, 0, 0, 0, 0)`. |
| **Resultado Esperado** | O sistema deve retornar `obterAtributo("Ceará", "Vitórias") = 1` e `obterAtributo("Fortaleza", "Derrotas") = 1`. |
| **Prioridade** | Crítica. |

## 2. Unitários de Manuela (RF05 - RF08)

### CT11: Exibição da Tabela Simples
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF05: Exibição da Tabela de Classificação, RT05: O sistema deve exibir a tabela de classificação |
| **Objetivo** | Validar exibição de tabela. |
| **Localização** | Método `exibirTabelaClassificacao()`. |
| **Entradas** | `exibirTabelaClassificacao()`. |
| **Resultado Esperado** | O método deve retornar uma estrutura de dados (lista/JSON) contendo o nome dos times e valores válidos para **PG, J, V, E, D, GP, GC, SG, CA, CV**. |
| **Prioridade** | Crítica. |

### CT12: Ordenação por Pontos (Prioridade Máxima)
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF06: Ordenação da Tabela (Critérios de Desempate), RT06: O sistema deve ordenar a tabela utilizando o critério de desempate, RN05 (1º PTs) |
| **Objetivo** | Validar ordenação da tabela. |
| **Localização** | Método `ordenarTabela()`. |
| **Entradas** | `ordenarTabela()` (A com PG=3, B com PG=1). |
| **Resultado Esperado** | O método deve retornar a lista com o **Time A (PG=3) posicionado acima do Time B (PG=1)**. |
| **Prioridade** | Crítica. |

### CT13: Desempate por Vitórias (1º Critério)
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF06, RN05 (2º V) |
| **Entradas** | `ordenarTabela()` (C com V=2, D com V=1, mesmo PG). |
| **Resultado Esperado** | O método deve posicionar o **Time C (V=2) acima do Time D (V=1)**. |
| **Prioridade** | Crítica. |

### CT14: Desempate por Saldo de Gols (2º Critério)
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF06, RN05 (3º SG) |
| **Entradas** | `ordenarTabela()` (E com SG=4, F com SG=2, mesmos PG/V). |
| **Resultado Esperado** | O método deve posicionar o **Time E (SG=4) acima do Time F (SG=2)**. |
| **Prioridade** | Crítica. |

### CT15: Desempate por Gols Pró (3º Critério)
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF06, RN05 (4º GM) |
| **Entradas** | `ordenarTabela()` (G com GP=10, H com GP=8, mesmos PG/V/SG). |
| **Resultado Esperado** | O método deve posicionar o **Time G (GP=10) acima do Time H (GP=8)**. |
| **Prioridade** | Crítica. |

### CT16: Desempate por Cartões (4º e 5º Critérios)
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF06, RN05 (5º Menos CV, 6º Menos CA) |
| **Entradas** | `ordenarTabela()` (J com CV=0, I com CV=1, demais empatados). |
| **Resultado Esperado** | O método deve posicionar o **Time J (Menor CV=0) acima do Time I (CV=1)**, priorizando o 4º critério (Menor CV). |
| **Prioridade** | Crítica. |

### CT17: Edição de Resultado (Vitória para Derrota)
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF07: Edição de Resultados (com Recalibração), RT07: O sistema deve permitir/não permitir os resultados |
| **Objetivo** | Validar edição de resultado. |
| **Localização** | Método `editarResultado()`. |
| **Pré-condições** | Partida A 1 x 0 B registrada (A: PG=3, V=1, D=0, SG=+1). |
| **Entradas** | `editarResultado("A", "B", 0, 1, 0, 0, 0, 0)` (Mudando para B 1 x 0 A). |
| **Resultado Esperado** | O sistema deve recalibrar o **Time A para PG:0, V:0, D:1, SG:-1**. |
| **Prioridade** | Crítica. |

### CT18: Edição de Resultado com Cartões
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF07 |
| **Pré-condições** | Partida C 2 x 2 D registrada (C: PG=1, CA=1). |
| **Entradas** | `editarResultado("C", "D", 2, 2, 3, 0, 0, 0)` (Mantendo o placar, mudando CA para 3). |
| **Resultado Esperado** | O sistema deve recalibrar o **Time C para CA=3, mantendo o PG inalterado (1)**. |
| **Prioridade** | Alta. |

### CT19: Remoção de Time com Sucesso
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF08: Remoção de Times (Antes de Jogos), RT08: O sistema deve permitir/não permitir remoção de times |
| **Objetivo** | Validar remoção de times. |
| **Localização** | Método `removerTime()`. |
| **Pré-condições** | Time G cadastrado, **sem nenhum jogo registrado**. |
| **Entradas** | `removerTime("Time G")`. |
| **Resultado Esperado** | O sistema deve retornar **true (sucesso)** e o Time G não deve ser encontrado na lista de equipes. |
| **Prioridade** | Média. |

### CT20: Bloqueio de Remoção de Time
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF08 |
| **Pré-condições** | Time H cadastrado e possui **pelo menos 1 partida registrada**. |
| **Entradas** | `removerTime("Time H")`. |
| **Resultado Esperado** | O sistema deve retornar **false (falha)** e exibir mensagem de erro ("Não é possível remover time com partidas registradas"). |
| **Prioridade** | Alta. |

## 3. Unitários de Yuji (RF09 - RF12)

### CT21: Bloqueio de Placar Negativo (Gols Pró)
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF09: Bloqueio de Placar Negativo, RT09: O sistema deve bloquear placares negativos, RN08: Todos os valores inseridos... devem ser valores inteiros, não negativos |
| **Objetivo** | Validar bloqueio de placar negativo. |
| **Localização** | Método `registrarResultado()`. |
| **Entradas** | `registrarResultado("Time A", "Time B", -1, 3, 0, 0, 0, 0)`. |
| **Resultado Esperado** | O sistema deve **bloquear o registro** e exibir mensagem de erro ("Valor de gols deve ser não negativo"). |
| **Prioridade** | Alta. |

### CT22: Bloqueio de Placar Negativo (Cartões Amarelos)
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF09, RN08 |
| **Entradas** | `registrarResultado("Time C", "Time D", 1, 1, -2, 0, 0, 0)`. |
| **Resultado Esperado** | O sistema deve **bloquear o registro** e exibir mensagem de erro ("Cartões não podem ser negativos"). |
| **Prioridade** | Alta. |

### CT23: Bloqueio de Times Duplicados (Registro)
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF10: Bloqueio de Times Duplicados em Partida, RT10: O sistema deve bloquear adição de times duplicados na mesma partida, RN15: O sistema deve garantir que as equipes de um confronto não sejam iguais |
| **Objetivo** | Validar bloqueio de times duplicados em partida. |
| **Entradas** | `registrarResultado("Time E", "Time E", 2, 2, 0, 0, 0, 0)`. |
| **Resultado Esperado** | O sistema deve **bloquear o registro** e exibir mensagem de erro ("Um time não pode jogar contra si mesmo"). |
| **Prioridade** | Alta. |

### CT24: Validação de Cadastro de Times (Nome em Branco)
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF11: Validação de Cadastro de Times (Nome Vazio), RT11: O sistema deve validar o cadastro de time, RN14: O nome de cada equipe cadastrada deve ser único |
| **Objetivo** | Validar cadastro de time com nome vazio. |
| **Entradas** | `cadastrarTime("", "SEM")`. |
| **Resultado Esperado** | O sistema deve **impedir o cadastro** e exibir uma mensagem de erro ("O nome do time é obrigatório"). |
| **Prioridade** | Média. |

### CT25: Bloqueio de Placar Parcial (Gols A Vazio)
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF12: Bloqueio de Placar Parcial (Campo Vazio), RT12: O sistema deve bloquear o placar parcial |
| **Objetivo** | Validar bloqueio de placar em branco. |
| **Entradas** | `registrarResultado("Time F", "Time G", null, 3, 0, 0, 0, 0)`. |
| **Resultado Esperado** | O sistema deve **bloquear o registro** e exibir mensagem de erro ("O placar deve ser um valor numérico válido"). |
| **Prioridade** | Alta. |

### CT26: Bloqueio de Placar Parcial (Cartões CV Faltando)
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF12 |
| **Entradas** | `registrarResultado("Time H", "Time I", 1, 1, 1, null, 0, 0)`. |
| **Resultado Esperado** | O sistema deve **bloquear o registro** e exibir mensagem de erro ("Todos os campos de cartões devem ser preenchidos"),. |
| **Prioridade** | Alta. |

### CT27: Bloqueio de Placar (Entrada Não Numérica)
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF12, RN08 |
| **Entradas** | `registrarResultado("Time J", "Time K", "dois", 1, 0, 0, 0, 0)`. |
| **Resultado Esperado** | O sistema deve **bloquear o registro** e exibir mensagem de erro ("O placar deve ser um valor numérico inteiro"). |
| **Prioridade** | Alta. |

### CT28: Validação de Cadastro de Times (Abreviatura com Espaço)
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF12 |
| **Entradas** | `cadastrarTime("Time Validação", "TV A")`. |
| **Resultado Esperado** | O sistema deve **impedir o cadastro** e exibir uma mensagem de erro ("A abreviatura deve ser única e não pode conter espaços"). |
| **Prioridade** | Média. |

### CT29: Bloqueio de Cadastro de Times no Meio (Requisito Compartilhado)
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF12 e RF14: Bloqueio de Cadastro de Times no Meio |
| **Pré-condições** | Pelo menos uma partida já foi registrada. |
| **Entradas** | `cadastrarTime("Time Z", "Z")`. |
| **Resultado Esperado** | O sistema deve **impedir o cadastro** e exibir uma mensagem de erro ("Não é permitido adicionar novos times após o início da competição"). |
| **Prioridade** | Alta. |

### CT30: Bloqueio de Placar (Cartões Múltiplos Faltando)
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF12 |
| **Entradas** | `registrarResultado("Time L", "Time M", null, 1, 1, 0, null, null)`. |
| **Resultado Esperado** | O sistema deve **bloquear o registro** e exibir uma mensagem de erro ("Todos os campos de placar e cartões devem ser preenchidos"). |
| **Prioridade** | Alta. |

## 4. Unitários de Caio (RF13 - RF16)

### CT31: Cálculo de Total de Gols (Partida Única)
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF13: Cálculo de Total de Gols (Estatística), RT13: O sistema deve realizar o cálculo do total de gols |
| **Objetivo** | Validar cálculo de gols. |
| **Localização** | Métodos `registrarResultado()`, `calcularTotalGols()`. |
| **Pré-condições** | Nenhuma partida registrada (Total Gols = 0). |
| **Entradas** | 1. `registrarResultado("A", "B", 2, 1, 0, 0, 0, 0)`; 2. `calcularTotalGols()`. |
| **Resultado Esperado** | O sistema deve retornar **3** como o total geral de gols marcados. |
| **Prioridade** | Média. |

### CT32: Cálculo de Total de Gols (Acúmulo)
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF13 |
| **Pré-condições** | Total de Gols anterior = 3 (do CT31). |
| **Entradas** | 1. `registrarResultado("C", "D", 3, 3, 0, 0, 0, 0)`; 2. `calcularTotalGols()`. |
| **Resultado Esperado** | O sistema deve retornar **9** (3 + 6) como o total geral de gols marcados. |
| **Prioridade** | Média. |

### CT33: Bloqueio de Cadastro de Times no Meio
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF14: Bloqueio de Cadastro de Times no Meio, RT14: O sistema deve bloquear o cadastro de time no meio do campeonato |
| **Objetivo** | Validar bloqueio de cadastro de times. |
| **Pré-condições** | O campeonato está **em andamento** (Pelo menos 1 partida registrada). |
| **Entradas** | `cadastrarTime("Time Z", "Z")`. |
| **Resultado Esperado** | O sistema deve **impedir o cadastro** e exibir uma mensagem de erro ("Não é permitido adicionar novos times após o início da competição"). |
| **Prioridade** | Alta. |

### CT34: Permitir Cadastro de Times
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF14 |
| **Pré-condições** | O campeonato **ainda não começou** (Nenhuma partida registrada). |
| **Entradas** | `cadastrarTime("Time Y", "Y")`. |
| **Resultado Esperado** | O sistema deve **permitir o cadastro com sucesso**. |
| **Prioridade** | Alta. |

### CT35: Bloqueio de Times Ímpares (Valor Limite Inferior)
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF15: Bloqueio de Times Ímpares (Geração de Tabela), RT15: O sistema deve bloquear o cadastro de um número ímpar de times, RN12: O sistema só deve permitir a criação de um campeonato se o número de equipes for par |
| **Objetivo** | Validar bloqueio de cadastro de número de times ímpares. |
| **Localização** | Métodos `gerarTabelaJogos()`. |
| **Pré-condições** | **3 Times** cadastrados (ímpar). |
| **Entradas** | `gerarTabelaJogos()`. |
| **Resultado Esperado** | O sistema deve **impedir a geração** e exibir mensagem de erro ("O número de times deve ser par"). |
| **Prioridade** | Alta. |

### CT36: Bloqueio de Times Ímpares (Valor Alto)
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF15, RN12 |
| **Pré-condições** | **21 Times** cadastrados (ímpar). |
| **Entradas** | `gerarTabelaJogos()`. |
| **Resultado Esperado** | O sistema deve **impedir a geração** e exibir mensagem de erro ("O número de times deve ser par"). |
| **Prioridade** | Alta. |

### CT37: Permissão de Times Pares
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF15, RN12 |
| **Pré-condições** | **4 Times** cadastrados (par). |
| **Entradas** | `gerarTabelaJogos()`. |
| **Resultado Esperado** | O sistema deve **gerar a tabela de jogos com sucesso**. |
| **Prioridade** | Crítica. |

### CT38: Mensagem de Tabela Completa
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF16: Mensagem de Tabela Completa (Notificação), RT16: O sistema deve mostrar uma mensagem de “tabela completa” |
| **Objetivo** | Validar mensagem de tabela completa gerada. |
| **Pré-condições** | 4 Times, Tabela gerada (12 jogos), **11 jogos já registrados** (último jogo pendente). |
| **Entradas** | `registrarResultado("Jogo 12", 1, 0, 0, 0, 0, 0)`. |
| **Resultado Esperado** | O sistema deve exibir uma notificação de sucesso informando que o campeonato foi finalizado ou a **tabela está completa**. O status do campeonato é marcado como "Finalizado". |
| **Prioridade** | Média. |

### CT39: Mensagem de Tabela Incompleta
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF16 |
| **Pré-condições** | 4 Times, Tabela gerada, **9 jogos registrados**. |
| **Entradas** | `registrarResultado("Jogo 10", 1, 0, 0, 0, 0, 0)`. |
| **Resultado Esperado** | O sistema deve exibir mensagem de sucesso do registro, mas **NÃO deve exibir a notificação de "Tabela Completa"**. O status é "Em Andamento". |
| **Prioridade** | Média. |

### CT40: Bloqueio de Cadastro de Times (Cenário Negativo Múltiplo)
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF16 |
| **Pré-condições** | Campeonato finalizado (status "Finalizado"). |
| **Entradas** | `cadastrarTime("Time W", "W")`. |
| **Resultado Esperado** | O sistema deve **impedir o cadastro** e exibir uma mensagem de erro ("Não é permitido adicionar novos times..."). |
| **Prioridade** | Alta. |

---

# Testes de Integração (CT41 - CT56)

O Nível de Teste de Integração também utiliza a **Técnica de Caixa Preta** e foca na validação da comunicação e persistência dos dados no **Banco de Dados (BD)** após as operações do usuário.

## 1. Integração de Samuel (RF01 - RF04)

### CT41: Cadastro de Times com Persistência
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF01: Cadastro de Times, RT01: O sistema deve permitir cadastro de times |
| **Objetivo** | Validar que o cadastro de um novo time é **persistido corretamente no Banco de Dados**. |
| **Entradas** | `cadastrarTime("Time Z", "Z")`. |
| **Passos** | 1. Chamar `cadastrarTime()`. 2. Chamar método de busca de time no BD (ex: `obterTimeBD("Time Z")`). |
| **Resultado Esperado** | O sistema deve **salvar o Time Z na base de dados** e o método de busca deve retornar o objeto Time Z. |
| **Prioridade** | Alta. |

### CT42: Persistência dos Atributos Iniciais
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF02: Definição de Atributos dos Times, RT02: O sistema deve permitir definições de atributos dos times |
| **Objetivo** | Validar a persistência dos atributos iniciais. |
| **Entradas** | `cadastrarTime("Time Alpha", "A")`. |
| **Resultado Esperado** | O registro do Time Alpha no BD deve ter **todos os campos de atributos (PG, V, E, D, GP, GC, SG, CA, CV) salvos com o valor 0**. |
| **Prioridade** | Crítica. |

### CT43: Registro e Persistência da Partida
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF03: Registro de Resultados de Partidas, RT03: O sistema deve permitir registros de partdidas |
| **Objetivo** | Validar registros de resultado. |
| **Entradas** | `registrarResultado("A", "B", 3, 2, 1, 0, 0, 0)`. |
| **Passos** | 1. Chamar `registrarResultado()`. 2. Chamar método de consulta ao **histórico de jogos no BD**. |
| **Resultado Esperado** | Um **novo registro deve ser criado na tabela de histórico de jogos no BD**, contendo: Time A (3), Time B (2), CA A (1), CV A (0), etc.,. |
| **Prioridade** | Crítica. |

### CT44: Fluxo: Registro - Cálculo - BD
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF04: Atualização Automática de Atributos (Cálculo 3-1-0), RT04: O sistema deve atualizar automaticamente os atributos |
| **Objetivo** | Validar atualização automática de atributos e persistência. |
| **Entradas** | `registrarResultado("C", "D", 4, 0, 0, 0, 0, 0)` (Vitória para C). |
| **Passos** | 1. Chamar `registrarResultado()`. 2. Chamar método de consulta direta ao registro do **Time C no BD**. |
| **Resultado Esperado** | O sistema deve atualizar o campo **Pontos do Time C para 3 no BD**, garantindo a persistência do cálculo (Vitória +3). |
| **Prioridade** | Crítica. |

## 2. Integração de Manuela (RF05 - RF08)

### CT45: Exibição da Tabela Integrada
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF05: Exibição da Tabela de Classificação, RT05: O sistema deve exibir a tabela de classificação |
| **Objetivo** | Validar exibição de tabela. |
| **Pré-condições** | 4 Times com atributos de desempenho (PG, SG, etc.) previamente atualizados e **salvos no BD**. |
| **Entradas** | `exibirTabelaClassificacao()`. |
| **Resultado Esperado** | A tabela retornada deve ter os valores de PG, SG, GP, etc. de cada time **idênticos aos valores armazenados no BD**. |
| **Prioridade** | Crítica. |

### CT46: Ordenação com Dados Persistidos
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF06: Ordenação da Tabela (Critérios de Desempate), RT06: O sistema deve ordenar a tabela utilizando o critário de desempate |
| **Objetivo** | Validar ordenação da tabela. |
| **Pré-condições** | Times E e F empatados em PG e V no BD. **Time E: SG=+2. Time F: SG=+1**. |
| **Entradas** | `ordenarTabela()`. |
| **Resultado Esperado** | A função deve buscar os dados SG do BD e **posicionar o Time E (SG=+2) acima do Time F**. |
| **Prioridade** | Crítica. |

### CT47: Edição e Persistência do Recálculo
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF07: Edição de Resultados (com Recalibração), RT07: O sistema deve permitir/não permitir os resultados |
| **Objetivo** | Validar edição de resultado. |
| **Pré-condições** | Partida A 1x0 B registrada. Time A: PG=3 **no BD**. |
| **Entradas** | `editarResultado("A", "B", 0, 1, 0, 0, 0, 0)`. |
| **Resultado Esperado** | O campo **Pontos do Time A deve ser atualizado para 0 no BD** e o registro da partida no histórico deve ser alterado. |
| **Prioridade** | Crítica. |

### CT48: Remoção de Time sem Histórico
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF08: Remoção de Times (Antes de Jogos), RT08: O sistema deve permitir/não permitir remoção de times |
| **Objetivo** | Validar remoção de times. |
| **Pré-condições** | Time G cadastrado **no BD**, mas sem histórico de jogos. |
| **Entradas** | `removerTime("Time G")`. |
| **Resultado Esperado** | O registro do Time G deve ser **removido da tabela de times no BD** e o método deve retornar true. |
| **Prioridade** | Média. |

## 3. Integração de Yuji (RF09 - RF12)

### CT49: Validação de Bloqueio de Placar Negativo em Serviço
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF09: Bloqueio de Placar Negativo, RT09: O sistema deve bloquear placares negativos |
| **Objetivo** | Validar bloqueio de placar negativo. |
| **Entradas** | `registrarResultado("H", "I", -1, 3, 0, 0, 0, 0)`. |
| **Passos** | 1. Chamar `registrarResultado()`. 2. Consultar a tabela de histórico de jogos no BD. |
| **Resultado Esperado** | O sistema deve retornar uma falha de validação e **NENHUM novo registro de partida deve ser inserido na tabela de histórico de jogos no BD**. |
| **Prioridade** | Alta. |

### CT50: Bloqueio de Times Duplicados
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF10: Bloqueio de Times Duplicados em Partida, RT10: O sistema deve bloquear adição de times duplicados na mesma partida |
| **Objetivo** | Validar bloqueio de times duplicados em partida. |
| **Entradas** | `registrarResultado("Time J", "Time J", 1, 1, 0, 0, 0, 0)`. |
| **Resultado Esperado** | O sistema deve retornar uma falha de validação e **NENHUM novo registro de partida deve ser inserido no BD**. |
| **Prioridade** | Alta. |

### CT51: Validação de Cadastro de Nome
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF11: Validação de Cadastro de Times (Nome Vazio), RT11: O sistema deve validar o cadastro de time |
| **Objetivo** | Validar cadastro de time com nome vazio. |
| **Entradas** | `cadastrarTime("", "SEM")`. |
| **Resultado Esperado** | O sistema deve retornar uma falha de validação e **NENHUM registro deve ser criado na tabela de times no BD**. |
| **Prioridade** | Média. |

### CT52: Bloqueio de Placar Parcial
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF12: Bloqueio de Placar Parcial (Campo Vazio), RT12: O sistema deve bloquear o placar parcial |
| **Objetivo** | Validar bloqueio de placar em branco. |
| **Entradas** | `registrarResultado("K", "L", 1, null, 0, 0, 0, 0)`. |
| **Resultado Esperado** | O sistema deve retornar uma falha de validação e **NENHUM novo registro de partida deve ser inserido no BD**. |
| **Prioridade** | Alta. |

## 4. Integração de Caio (RF13 - RF16)

### CT53: Cálculo de Total de Gols (Consulta Integrada)
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF13: Cálculo de Total de Gols (Estatística), RT13: O sistema deve realizar o cálculo do total de gols |
| **Objetivo** | Validar cálculo de gols. |
| **Pré-condições** | Partidas registradas no BD (Total acumulado: 5 gols). |
| **Entradas** | `calcularTotalGols()`. |
| **Passos** | O método deve consultar o BD. |
| **Resultado Esperado** | O método deve executar uma **consulta SUM na tabela de histórico de jogos e retornar o valor 5**, provando que a consulta integrou o histórico corretamente. |
| **Prioridade** | Alta. |

### CT54: Bloqueio de Cadastro de Times no Meio
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF14: Bloqueio de Cadastro de Times no Meio, RT14: O sistema deve bloquear o cadastro de time no meio do campeonato |
| **Objetivo** | Validar bloqueio de cadastro de times. |
| **Pré-condições** | **Existe histórico de jogos no BD**. |
| **Entradas** | `cadastrarTime("Time Novo", "NV")`. |
| **Passos** | 1. Chamar `cadastrarTime()`. 2. Verificar se o método consulta o BD. |
| **Resultado Esperado** | A camada de serviço deve consultar o BD e retornar uma falha, **impedindo a inserção do novo time na tabela de times no BD**. |
| **Prioridade** | Alta. |

### CT55: Bloqueio de Times Ímpares
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF15: Bloqueio de Times Ímpares (Geração de Tabela), RT15: O sistema deve bloquear o cadastro de um número ímpar de times |
| **Objetivo** | Validar bloqueio de cadastro de número de times ímpares. |
| **Pré-condições** | **5 Times (ímpar) cadastrados no BD**. |
| **Entradas** | `gerarTabelaJogos()`. |
| **Resultado Esperado** | O sistema deve retornar uma falha de validação e **NENHUM registro de partida deve ser criado na tabela de jogos no BD**. |
| **Prioridade** | Alta. |

### CT56: Mensagem de Tabela Completa
| Atributo | Detalhe |
| :--- | :--- |
| **Requisito (RF)** | RF16: Mensagem de Tabela Completa (Notificação), RT16: O sistema deve mostrar uma mensagem de “tabela completa” |
| **Objetivo** | Validar mensagem de tabela completa gerada. |
| **Pré-condições** | Tabela gerada (12 jogos). **11 jogos registrados no BD**. |
| **Entradas** | `registrarResultado("Último Jogo", 1, 0, 0, 0, 0, 0)`. |
| **Resultado Esperado** | A camada de serviço deve retornar uma notificação de **"Campeonato Finalizado"**, confirmando que a contagem de jogos no BD está completa. O status do campeonato é "Finalizado" no BD. |
| **Prioridade** | Média. |