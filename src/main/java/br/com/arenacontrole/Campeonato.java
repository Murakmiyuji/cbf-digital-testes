package br.com.arenacontrole;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Gerencia um campeonato de pontos corridos.
 * Implementa as regras RN04, RN05, RN07, RN12-RN16 do Plano de Testes.
 */
public class Campeonato {
    private List<Time> times;
    private boolean tabelaGerada;
    private int totalGols;

    public Campeonato() {
        this.times = new ArrayList<>();
        this.tabelaGerada = false;
        this.totalGols = 0;
    }

    /**
     * Cadastra um novo time no campeonato.
     * RF01: Cadastro de Times
     * RN14: Nome único
     */
    public boolean cadastrarTime(String nome, String abreviacao) {
        // RN14: Verificar se o nome já existe
        if (times.stream().anyMatch(t -> t.getNome().equalsIgnoreCase(nome))) {
            throw new IllegalArgumentException("Nome do time já cadastrado");
        }

        try {
            Time novoTime = new Time(nome, abreviacao);
            times.add(novoTime);
            return true;
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Remove um time que não possui histórico de jogos.
     * RF08: Remoção de Times (Antes de Jogos)
     */
    public boolean removerTime(String nome) {
        Time time = buscarTime(nome);
        if (time == null) {
            return false;
        }

        if (time.getJogos() > 0) {
            throw new IllegalStateException("Não é possível remover time com partidas registradas");
        }

        return times.remove(time);
    }

    /**
     * Registra o resultado de uma partida.
     * RF03: Registro de Resultados de Partidas
     * RF04: Atualização Automática de Atributos (Cálculo 3-1-0)
     * RN15: Times diferentes em uma partida
     */
    public void registrarResultado(String nomeTimeA, String nomeTimeB, 
                                  int golsA, int golsB, int caA, int cvA, int caB, int cvB) {
        // Validações
        if (golsA < 0 || golsB < 0 || caA < 0 || cvA < 0 || caB < 0 || cvB < 0) {
            throw new IllegalArgumentException("Valores de gols e cartões não podem ser negativos");
        }

        // RN15: Times devem ser diferentes
        if (nomeTimeA.equalsIgnoreCase(nomeTimeB)) {
            throw new IllegalArgumentException("Um time não pode jogar contra si mesmo");
        }

        Time timeA = buscarTime(nomeTimeA);
        Time timeB = buscarTime(nomeTimeB);

        if (timeA == null || timeB == null) {
            throw new IllegalArgumentException("Um ou ambos os times não encontrados");
        }

        // Atualizar estatísticas dos times baseado no resultado
        if (golsA > golsB) {
            // Time A venceu
            timeA.registrarVitoria(golsA, golsB, caA, cvA);
            timeB.registrarDerrota(golsB, golsA, caB, cvB);
        } else if (golsA < golsB) {
            // Time B venceu
            timeA.registrarDerrota(golsA, golsB, caA, cvA);
            timeB.registrarVitoria(golsB, golsA, caB, cvB);
        } else {
            // Empate
            timeA.registrarEmpate(golsA, golsB, caA, cvA);
            timeB.registrarEmpate(golsB, golsA, caB, cvB);
        }

        // Atualizar total de gols do campeonato
        totalGols += (golsA + golsB);
    }

    /**
     * Calcula o total de gols do campeonato.
     * RF13: Cálculo de Total de Gols (Estatística)
     */
    public int calcularTotalGols() {
        return totalGols;
    }

    /**
     * Retorna a tabela de classificação ordenada.
     * RF05: Exibição da Tabela de Classificação
     * RF06: Ordenação da Tabela (Critérios de Desempate)
     * RN05: 1º PTs, 2º V, 3º SG, 4º GM, 5º Menos CV, 6º Menos CA
     */
    public List<Time> obterTabelaClassificacao() {
        return times.stream()
            .sorted(new Comparator<Time>() {
                @Override
                public int compare(Time t1, Time t2) {
                    // 1º critério: Pontos (decrescente)
                    if (t1.getPontos() != t2.getPontos()) {
                        return Integer.compare(t2.getPontos(), t1.getPontos());
                    }
                    // 2º critério: Vitórias (decrescente)
                    if (t1.getVitorias() != t2.getVitorias()) {
                        return Integer.compare(t2.getVitorias(), t1.getVitorias());
                    }
                    // 3º critério: Saldo de Gols (decrescente)
                    if (t1.getSaldoGols() != t2.getSaldoGols()) {
                        return Integer.compare(t2.getSaldoGols(), t1.getSaldoGols());
                    }
                    // 4º critério: Gols Pró (decrescente)
                    if (t1.getGolsPro() != t2.getGolsPro()) {
                        return Integer.compare(t2.getGolsPro(), t1.getGolsPro());
                    }
                    // 5º critério: Menos Cartões Vermelhos (crescente)
                    if (t1.getCartoesVermelhos() != t2.getCartoesVermelhos()) {
                        return Integer.compare(t1.getCartoesVermelhos(), t2.getCartoesVermelhos());
                    }
                    // 6º critério: Menos Cartões Amarelos (crescente)
                    return Integer.compare(t1.getCartoesAmarelos(), t2.getCartoesAmarelos());
                }
            })
            .collect(Collectors.toList());
    }

    /**
     * Gera a tabela de jogos (turno e returno).
     * RF15: Bloqueio de Times Ímpares (Geração de Tabela)
     * RN12: Número de times deve ser par
     */
    public boolean gerarTabelaJogos() {
        // RN12: Verificar se o número de times é par
        if (times.size() % 2 != 0) {
            throw new IllegalStateException("O número de times deve ser par");
        }

        if (times.isEmpty()) {
            return false;
        }

        tabelaGerada = true;
        return true;
    }

    public Time buscarTime(String nome) {
        return times.stream()
            .filter(t -> t.getNome().equalsIgnoreCase(nome))
            .findFirst()
            .orElse(null);
    }

    public List<Time> getTimes() {
        return new ArrayList<>(times);
    }

    public int getNumeroTimes() {
        return times.size();
    }

    public boolean isTabelaGerada() {
        return tabelaGerada;
    }
}

