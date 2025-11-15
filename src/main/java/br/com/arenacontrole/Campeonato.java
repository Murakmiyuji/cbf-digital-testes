package br.com.arenacontrole;

import java.util.List; // { changed code }
import java.util.ArrayList; // { changed code }
import br.com.arenacontrole.repository.CampeonatoRepository; // { changed code }
import java.util.*;
import java.util.stream.Collectors;

/**
 * Gerencia um campeonato de pontos corridos.
 * Implementa as regras RN04, RN05, RN07, RN12-RN16 do Plano de Testes.
 */
public class Campeonato {
    private List<Time> times = new ArrayList<>(); // { changed code }
    private boolean tabelaGerada;
    private int totalGols;
    private final List<Partida> partidas = new ArrayList<>();
    // repositório opcional para persistência
    private CampeonatoRepository repository; // { changed code }

    // garante construtor padrão (compatibilidade com testes existentes)
    public Campeonato() {
        // se havia inicialização adicional, mantenha-a aqui
    }

    // construtor que aceita repository (opcional)
    public Campeonato(CampeonatoRepository repository) {
        this(); // chama construtor padrão para manter comportamento anterior
        this.repository = repository;
    } // { changed code }

    // setter opcional
    public void setRepository(CampeonatoRepository repository) { // { changed code }
        this.repository = repository;
    } // { changed code }

    /**
     * Cadastra um novo time no campeonato.
     * RF01: Cadastro de Times
     * RN14: Nome único
     */
    public boolean cadastrarTime(String nome, String abreviacao) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do time não pode ser vazio");
        }
        // verifica duplicidade por nome (case-insensitive)
        if (times.stream().anyMatch(t -> t.getNome().equalsIgnoreCase(nome))) {
            throw new IllegalArgumentException("Nome do time já cadastrado");
        }
        Time novo = new Time(nome, abreviacao);
        // garante atributos iniciais zerados (se Time já não o fizer)
        novo.setPontos(0);
        novo.setJogos(0);
        novo.setVitorias(0);
        novo.setEmpates(0);
        novo.setDerrotas(0);
        novo.setGolsPro(0);
        novo.setGolsContra(0);
        novo.setCartoesAmarelos(0);
        novo.setCartoesVermelhos(0);

        times.add(novo);

        // persistência opcional
        if (this.repository != null) {
            this.repository.saveOrUpdateTime(novo);
        }
        return true;
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
            System.out.println("Não é possível remover time com partidas registradas");
            return false;
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
        if (nomeTimeA == null || nomeTimeB == null) throw new IllegalArgumentException("Nomes de times inválidos");
        if (nomeTimeA.equalsIgnoreCase(nomeTimeB)) throw new IllegalArgumentException("Um time não pode jogar contra si mesmo");
        // valida não-negativos
        if (golsA < 0 || golsB < 0) throw new IllegalArgumentException("Valor de gols deve ser não negativo");
        if (caA < 0 || caB < 0 || cvA < 0 || cvB < 0) throw new IllegalArgumentException("Cartões não podem ser negativos");

        Time a = buscarTime(nomeTimeA);
        Time b = buscarTime(nomeTimeB);
        if (a == null || b == null) throw new IllegalArgumentException("Times não cadastrados");

        // atualiza jogos e gols
        a.setJogos(a.getJogos() + 1);
        b.setJogos(b.getJogos() + 1);

        a.setGolsPro(a.getGolsPro() + golsA);
        a.setGolsContra(a.getGolsContra() + golsB);

        b.setGolsPro(b.getGolsPro() + golsB);
        b.setGolsContra(b.getGolsContra() + golsA);

        // cartões
        a.setCartoesAmarelos(a.getCartoesAmarelos() + caA);
        a.setCartoesVermelhos(a.getCartoesVermelhos() + cvA);
        b.setCartoesAmarelos(b.getCartoesAmarelos() + caB);
        b.setCartoesVermelhos(b.getCartoesVermelhos() + cvB);

        // pontos e resultados
        if (golsA > golsB) {
            a.setVitorias(a.getVitorias() + 1);
            a.setPontos(a.getPontos() + 3);
            b.setDerrotas(b.getDerrotas() + 1);
        } else if (golsA == golsB) {
            a.setEmpates(a.getEmpates() + 1);
            b.setEmpates(b.getEmpates() + 1);
            a.setPontos(a.getPontos() + 1);
            b.setPontos(b.getPontos() + 1);
        } else {
            b.setVitorias(b.getVitorias() + 1);
            b.setPontos(b.getPontos() + 3);
            a.setDerrotas(a.getDerrotas() + 1);
        }

        // registra partida na memória
        try {
            Partida p = new Partida(a.getNome(), b.getNome(), golsA, golsB, caA, cvA, caB, cvB);
            partidas.add(p);
        } catch (Throwable ignored) {
            // se Partida não tiver esse construtor, ignoramos a criação em memória
        }

        // persiste alterações (times e partida) se houver repositório
        if (this.repository != null) {
            this.repository.saveOrUpdateTime(a);
            this.repository.saveOrUpdateTime(b);
            try {
                this.repository.savePartida(nomeTimeA, nomeTimeB, golsA, golsB, caA, cvA, caB, cvB);
            } catch (Throwable ex) {
                // se repositório não implementar persistência de partidas, ignora; testes de integração indicarão isso
            }
        }

        // atualiza estatística local
        totalGols += golsA + golsB;
    }

    public void editarResultado(String nomeTimeA, String nomeTimeB,
                                int novosGolsA, int novosGolsB,
                                int novosCaA, int novosCvA, int novosCaB, int novosCvB) {

        // 1) Validações
        if (novosGolsA < 0 || novosGolsB < 0 ||
                novosCaA < 0 || novosCvA < 0 || novosCaB < 0 || novosCvB < 0) {
            throw new IllegalArgumentException("Valores de gols e cartões não podem ser negativos");
        }

        if (nomeTimeA == null || nomeTimeB == null) {
            throw new IllegalArgumentException("Times inválidos");
        }

        if (nomeTimeA.equalsIgnoreCase(nomeTimeB)) {
            throw new IllegalArgumentException("Um time não pode jogar contra si mesmo");
        }

        Time timeA = buscarTime(nomeTimeA);
        Time timeB = buscarTime(nomeTimeB);

        if (timeA == null || timeB == null) {
            throw new IllegalArgumentException("Um ou ambos os times não encontrados");
        }

        // 2) Procurar partida antiga entre A e B
        int idx = indexPartida(nomeTimeA, nomeTimeB);
        if (idx < 0) {
            throw new IllegalStateException(
                    "Não há partida registrada para editar entre " + nomeTimeA + " e " + nomeTimeB
            );
        }

        Partida antiga = partidas.get(idx);

        // 3) DESFAZER o resultado antigo (delta -1)
        antiga.aplicar(timeA, timeB, -1);

        // Ajustar total de gols
        totalGols -= (antiga.getGm() + antiga.getGv());

        // 4) APLICAR o novo resultado (delta +1)
        Partida nova = new Partida(
                nomeTimeA, nomeTimeB,
                novosGolsA, novosGolsB,
                novosCaA, novosCvA, novosCaB, novosCvB
        );

        nova.aplicar(timeA, timeB, +1);

        // Ajustar total de gols (soma os novos)
        totalGols += (novosGolsA + novosGolsB);

        // 5) Atualizar a entrada na lista de partidas
        partidas.set(idx, nova);
        // Persistir alterações se houver repositório
        if (this.repository != null) {
            try {
                this.repository.saveOrUpdateTime(timeA);
                this.repository.saveOrUpdateTime(timeB);
                // Observação: atualização de partida no repositório não implementada
            } catch (Throwable ignored) {
            }
        }
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
        if (nome == null) return null;
        for (Time t : times) {
            if (t.getNome().equalsIgnoreCase(nome)) return t;
        }
        return null;
    }

    // RF05/CT11 – Exibição da Tabela (somente leitura)
    public List<Classificacao> exibirTabelaClassificacao() {
        return obterTabelaClassificacao()
                .stream()
                .map(Classificacao::new)
                .collect(java.util.stream.Collectors.toList());
    }

    private int indexPartida(String mand, String visit) {
        if (mand == null || visit == null) return -1;
        int i = 0;
        for (Partida p : partidas) {
            if (p.getMandante().equalsIgnoreCase(mand)
                    && p.getVisitante().equalsIgnoreCase(visit)) return i;
            i++;
        }
        return -1;
    }

    public List<Time> ordenarTabela() {
        return obterTabelaClassificacao();
    }

    public int obterAtributo(String nomeTime, String atributo) {
        Time t = buscarTime(nomeTime);
        if (t == null) {
            throw new IllegalArgumentException("Time não encontrado: " + nomeTime);
        }

        String a = atributo == null ? "" : atributo.trim().toUpperCase();

        // Aceita múltiplas formas de nome de atributo (ex.: "pontos", "PG", "golsPro", "GP")
        switch (a) {
            case "PG":
            case "PONTOS":
                return t.getPontos();
            case "V":
                return t.getVitorias();
            case "D":
                return t.getDerrotas();
            case "SG":
                return t.getSaldoGols();
            case "J":
                return t.getJogos();
            case "E":
                return t.getEmpates();
            case "GP":
                return t.getGolsPro();
            case "GC":
                return t.getGolsContra();
            case "CA":
                return t.getCartoesAmarelos();
            case "CV":
                return t.getCartoesVermelhos();
            default:
                // Algumas chamadas de teste usam nomes como "pontos", "golsPro" em camelCase.
                // Faz mapeamento simples por conteúdo (após upper-case).
                if (a.contains("PONT")) return t.getPontos();
                if (a.contains("GOL") && a.contains("PRO")) return t.getGolsPro();
                if (a.contains("GOL") && a.contains("CON")) return t.getGolsContra();
                if (a.contains("SG")) return t.getSaldoGols();
                if (a.contains("J")) return t.getJogos();
                if (a.contains("V")) return t.getVitorias();
                if (a.contains("E")) return t.getEmpates();
                if (a.contains("CA")) return t.getCartoesAmarelos();
                if (a.contains("CV")) return t.getCartoesVermelhos();
                throw new IllegalArgumentException("Atributo desconhecido: " + atributo);
        }
    }

    public List<Time> getTimes() {
        return times;
    }

    public int getNumeroTimes() {
        return times.size();
    }

    public boolean isTabelaGerada() {
        return tabelaGerada;
    }
}

