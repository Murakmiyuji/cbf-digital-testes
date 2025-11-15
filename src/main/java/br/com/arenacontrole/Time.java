package br.com.arenacontrole;

import static java.lang.String.format;


/**
 * Representa um time no campeonato.
 * Baseado nas regras de negócio RN01-RN16 do Plano de Testes CBF Digital.
 */
public class Time {
    private String nome;
    private String abreviacao;
    private int pontos;              // PG - Pontos Ganhos
    private int jogos;               // J - Jogos Disputados
    private int vitorias;            // V - Vitórias
    private int empates;             // E - Empates
    private int derrotas;            // D - Derrotas
    private int golsPro;             // GP - Gols Marcados
    private int golsContra;          // GC - Gols Sofridos
    private int saldoGols;           // SG - Saldo de Gols
    private int cartoesAmarelos;     // CA - Cartões Amarelos
    private int cartoesVermelhos;    // CV - Cartões Vermelhos

    public Time(String nome, String abreviacao) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do time é obrigatório");
        }
        if (abreviacao == null || abreviacao.trim().isEmpty() || abreviacao.contains(" ")) {
            throw new IllegalArgumentException("A abreviatura deve ser única e não pode conter espaços");
        }
        
        this.nome = nome;
        this.abreviacao = abreviacao;
        this.pontos = 0;
        this.jogos = 0;
        this.vitorias = 0;
        this.empates = 0;
        this.derrotas = 0;
        this.golsPro = 0;
        this.golsContra = 0;
        this.saldoGols = 0;
        this.cartoesAmarelos = 0;
        this.cartoesVermelhos = 0;
    }

    // Getters
    public String getNome() { return nome; }
    public String getAbreviacao() { return abreviacao; }
    public int getPontos() { return pontos; }
    public int getJogos() { return jogos; }
    public int getVitorias() { return vitorias; }
    public int getEmpates() { return empates; }
    public int getDerrotas() { return derrotas; }
    public int getGolsPro() { return golsPro; }
    public int getGolsContra() { return golsContra; }
    public int getSaldoGols() { return saldoGols; }
    public int getCartoesAmarelos() { return cartoesAmarelos; }
    public int getCartoesVermelhos() { return cartoesVermelhos; }

    // Métodos para atualizar estatísticas após uma partida
    public void registrarVitoria(int gm, int gs, int ca, int cv) {
        validarValores(gm, gs, ca, cv);
        this.pontos += 3;  // RN01
        this.vitorias++;
        this.jogos++;
        atualizarGols(gm, gs);
        atualizarCartoes(ca, cv);
    }

    public void registrarEmpate(int gm, int gs, int ca, int cv) {
        validarValores(gm, gs, ca, cv);
        this.pontos += 1;  // RN02
        this.empates++;
        this.jogos++;
        atualizarGols(gm, gs);
        atualizarCartoes(ca, cv);
    }

    public void registrarDerrota(int gm, int gs, int ca, int cv) {
        validarValores(gm, gs, ca, cv);
        // RN03: 0 pontos por derrota
        this.derrotas++;
        this.jogos++;
        atualizarGols(gm, gs);
        atualizarCartoes(ca, cv);
    }

    private void validarValores(int gm, int gs, int ca, int cv) {
        // RN08: Todos os valores devem ser inteiros não negativos
        if (gm < 0 || gs < 0 || ca < 0 || cv < 0) {
            throw new IllegalArgumentException("Valores de gols e cartões não podem ser negativos");
        }
    }

    private void atualizarGols(int gm, int gs) {
        this.golsPro += gm;
        this.golsContra += gs;
        this.saldoGols = this.golsPro - this.golsContra;  // RN06
    }

    private void atualizarCartoes(int ca, int cv) {
        // RN11: Contagem de cartões por total da equipe
        this.cartoesAmarelos += ca;
        this.cartoesVermelhos += cv;
    }

    public double getAproveitamento() {
        // RN09: Aproveitamento sobre os pontos possíveis
        if (jogos == 0) return 0.0;
        return (pontos * 100.0) / (jogos * 3);
    }

    void ajustarJogos(int delta) { this.jogos += delta; }

    void ajustarVitoria(int delta) {
        this.vitorias += delta;
        this.pontos += 3 * delta;
    }

    void ajustarEmpate(int delta) {
        this.empates += delta;
        this.pontos += 1 * delta;
    }

    void ajustarDerrota(int delta) { this.derrotas += delta; }

    void ajustarGols(int proDelta, int contraDelta) {
        this.golsPro += proDelta;
        this.golsContra += contraDelta;
        this.saldoGols = this.golsPro - this.golsContra;
    }

    void ajustarCartoes(int caDelta, int cvDelta) {
        this.cartoesAmarelos += caDelta;
        this.cartoesVermelhos += cvDelta;
    }

    // adicionei setters públicos mínimos para persistência (se já existirem, não duplique)
    public void setPontos(int pontos) { this.pontos = pontos; } // { changed code }
    public void setJogos(int jogos) { this.jogos = jogos; } // { changed code }
    public void setVitorias(int vitorias) { this.vitorias = vitorias; } // { changed code }
    public void setEmpates(int empates) { this.empates = empates; } // { changed code }
    public void setDerrotas(int derrotas) { this.derrotas = derrotas; } // { changed code }
    public void setGolsPro(int golsPro) { 
        this.golsPro = golsPro; 
        this.saldoGols = this.golsPro - this.golsContra; // recalcula saldoGols
    } // { changed code }
    public void setGolsContra(int golsContra) { 
        this.golsContra = golsContra; 
        this.saldoGols = this.golsPro - this.golsContra; // recalcula saldoGols
    } // { changed code }
    public void setCartoesAmarelos(int ca) { this.cartoesAmarelos = ca; } // { changed code }
    public void setCartoesVermelhos(int cv) { this.cartoesVermelhos = cv; } // { changed code }
    public void setSaldoGols(int saldoGols) { this.saldoGols = saldoGols; } // { changed code }

    @Override
    public String toString() {
        return format("%s (%s) - PG:%d J:%d V:%d E:%d D:%d GP:%d GC:%d SG:%d CA:%d CV:%d",
            nome, abreviacao, pontos, jogos, vitorias, empates, derrotas, 
            golsPro, golsContra, saldoGols, cartoesAmarelos, cartoesVermelhos);
    }
}

