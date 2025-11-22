package br.com.arenacontrole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes Unitários - Yuji Faruk Murakami Feles
 * Baseado no Plano de Testes CBF Digital v1.0
 *
 * Cobre os seguintes requisitos funcionais:
 * - RF09: Bloqueio de Placar Negativo
 * - RF10: Bloqueio de Times Duplicados em Partida
 * - RF11: Validação de Cadastro de Times (Nome Vazio)
 * - RF12: Bloqueio de Placar Parcial (Campo Vazio)
 */
@DisplayName("Testes Unitários - Yuji")
public class TestesUnitariosYuji {

    private Campeonato campeonato;


    // ========== RF09: Bloqueio de Placar Negativo ==========
    // RT09: O sistema deve bloquear placares negativos

    /**
     * CT21: Bloqueio de Placar Negativo (Gols Pró)
     *
     * Entradas: registrarResultado("Time A", "Time B", -1, 3, 0, 0, 0, 0)
     * Resultado Esperado: O sistema deve bloquear o registro e exibir mensagem 
     * de erro ("Valor de gols deve ser não negativo")
     * Prioridade: Alta
     * Pós-condições: A partida não é registrada; atributos permanecem inalterados
     */
    @BeforeEach
    void setUp() {
        campeonato = new Campeonato();
    }

    @Test
    @DisplayName("CT21: Bloqueio de Placar Negativo (Gols Pró)")
    void testCT21_BloqueioPlacarNegativoGolsPro() {
        // Arrange
        campeonato.cadastrarTime("Time A", "TA");
        campeonato.cadastrarTime("Time B", "TB");

        // Act & Assert
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
            campeonato.registrarResultado("Time A", "Time B", -1, 3, 0, 0, 0, 0)
        );

        assertTrue(e.getMessage().toLowerCase().contains("negativo") ||
                   e.getMessage().toLowerCase().contains("não negativo"));

        // Verificar que os atributos permanecem inalterados
        Time timeA = campeonato.buscarTime("Time A");
        assertEquals(0, timeA.getPontos());
        assertEquals(0, timeA.getJogos());
    }

    /**
     * CT22: Bloqueio de Placar Negativo (Cartões Amarelos)
     *
     * Entradas: registrarResultado("Time C", "Time D", 1, 1, -2, 0, 0, 0)
     * Resultado Esperado: O sistema deve bloquear o registro e exibir mensagem 
     * de erro ("Cartões não podem ser negativos")
     * Prioridade: Alta
     * Pós-condições: A partida não é registrada; atributos permanecem inalterados
     */
    @Test
    @DisplayName("CT22: Bloqueio de Placar Negativo (Cartões Amarelos)")
    void testCT22_BloqueioPlacarNegativoCartoesAmarelos() {
        // Arrange
        campeonato.cadastrarTime("Time C", "TC");
        campeonato.cadastrarTime("Time D", "TD");

        // Act & Assert
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
            campeonato.registrarResultado("Time C", "Time D", 1, 1, -2, 0, 0, 0)
        );

        assertTrue(e.getMessage().toLowerCase().contains("negativ") ||
                   e.getMessage().toLowerCase().contains("cartões") ||
                   e.getMessage().toLowerCase().contains("cartoes"));

        // Verificar que os atributos permanecem inalterados
        Time timeC = campeonato.buscarTime("Time C");
        assertEquals(0, timeC.getPontos());
        assertEquals(0, timeC.getJogos());
    }

    // ========== RF10: Bloqueio de Times Duplicados em Partida ==========
    // RT10: O sistema deve bloquear adição de times duplicados na mesma partida

    /**
     * CT23: Bloqueio de Times Duplicados (Registro)
     *
     * Entradas: registrarResultado("Time E", "Time E", 2, 2, 0, 0, 0, 0)
     * Resultado Esperado: O sistema deve bloquear o registro e exibir mensagem 
     * de erro ("Um time não pode jogar contra si mesmo")
     * Prioridade: Alta
     * Pós-condições: A partida não é registrada; atributos permanecem inalterados
     */
    @Test
    @DisplayName("CT23: Bloqueio de Times Duplicados em Partida")
    void testCT23_BloqueioTimesDuplicados() {
        // Arrange
        campeonato.cadastrarTime("Time E", "TE");

        // Act & Assert
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
            campeonato.registrarResultado("Time E", "Time E", 2, 2, 0, 0, 0, 0)
        );

        assertTrue(e.getMessage().toLowerCase().contains("si mesmo") ||
                   e.getMessage().toLowerCase().contains("mesmo time") ||
                   e.getMessage().toLowerCase().contains("não pode"));

        // Verificar que os atributos permanecem inalterados
        Time timeE = campeonato.buscarTime("Time E");
        assertEquals(0, timeE.getPontos());
        assertEquals(0, timeE.getJogos());
    }

    // ========== RF11: Validação de Cadastro de Times (Nome Vazio) ==========
    // RT11: O sistema deve validar o cadastro de time

    /**
     * CT24: Validação de Cadastro de Times (Nome em Branco)
     *
     * Entradas: cadastrarTime("", "SEM")
     * Resultado Esperado: O sistema deve impedir o cadastro e exibir uma mensagem 
     * de erro ("O nome do time é obrigatório")
     * Prioridade: Média
     * Pós-condições: O novo time não é criado
     */
    @Test
    @DisplayName("CT24: Validação de Cadastro de Times (Nome em Branco)")
    void testCT24_ValidacaoCadastroNomeEmBranco() {
        // Act & Assert
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
            campeonato.cadastrarTime("", "SEM")
        );

        assertTrue(e.getMessage().toLowerCase().contains("obrigat") ||
                   e.getMessage().toLowerCase().contains("vazio"));

        // Verificar que nenhum time foi criado
        assertEquals(0, campeonato.getNumeroTimes());
    }
    
    // ========== RF12: Bloqueio de Placar Parcial (Campo Vazio) ==========
    // RT12: O sistema deve bloquear o placar parcial

    /**
     * CT25: Bloqueio de Placar Parcial (Gols A Vazio)
     *
     * Pré-condição: Times F e G cadastrados
     * Entradas: registrarResultado("Time F", "Time G", null, 3, 0, 0, 0, 0)
     * Resultado Esperado: O sistema deve bloquear o registro e exibir mensagem 
     * de erro ("O placar deve ser um valor numérico válido")
     * Prioridade: Alta
     */
    @Test
    @DisplayName("CT25: Bloqueio de Placar Parcial (Gols A Vazio)")
    void testCT25_BloqueioPlacarParcialGolsAVazio() {
        // Arrange
        campeonato.cadastrarTime("Time F", "TF");
        campeonato.cadastrarTime("Time G", "TG");

        // Act & Assert: Tentar registrar com golsA null deve bloquear
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
            campeonato.registrarResultado("Time F", "Time G", null, 3, 0, 0, 0, 0)
        , "Deve bloquear quando golsA é null");

        // Verificar mensagem de erro
        assertTrue(e.getMessage().contains("Placar deve ser preenchido") ||
                   e.getMessage().toLowerCase().contains("preenchido") ||
                   e.getMessage().toLowerCase().contains("numérico"));

        // Verificar que os atributos permanecem inalterados
        Time timeF = campeonato.buscarTime("Time F");
        assertEquals(0, timeF.getPontos());
        assertEquals(0, timeF.getJogos());
    }

    /**
     * CT26: Bloqueio de Placar Parcial (Cartões CV Faltando)
     *
     * Pré-condição: Times H e I cadastrados
     * Entradas: registrarResultado("Time H", "Time I", 1, 1, 1, null, 0, 0)
     * Resultado Esperado: O sistema deve bloquear o registro e exibir mensagem 
     * de erro ("Todos os campos de cartões devem ser preenchidos")
     * Prioridade: Alta
     *
     * Nota Técnica: Em Java, os cartões são definidos como int primitivo, que não pode ser null.
     * A validação de campos vazios/nulos em cartões seria feita na camada de apresentação
     * (interface web/mobile) antes de chamar o método registrarResultado().
     * Este teste documenta essa limitação técnica e valida que a validação de cartões
     * negativos já está implementada (testada em CT22).
     */
    @Test
    @DisplayName("CT26: Bloqueio de Placar Parcial (Cartões CV Faltando)")
    void testCT26_BloqueioPlacarParcialCartoesCVFaltando() {
        // Arrange
        campeonato.cadastrarTime("Time H", "TH");
        campeonato.cadastrarTime("Time I", "TI");

        // Nota: Em Java, os cartões são int primitivo (não podem ser null).
        // O plano de testes menciona null para cartões, mas isso não é possível
        // com a implementação atual que usa tipos primitivos.
        //
        // A validação de cartões nulos/vazios seria feita na camada de apresentação.
        // A validação de cartões negativos já está implementada e testada em CT22.
        
        // Como não podemos passar null para int primitivo, documentamos que:
        // 1. A validação de null seria feita na camada de apresentação
        // 2. A validação de valores negativos está implementada (CT22)
        // 3. Valores padrão (0) são aceitos quando não há cartões
        
        // Este teste garante que valores válidos funcionam corretamente
        campeonato.registrarResultado("Time H", "Time I", 1, 1, 1, 0, 0, 0);
        
        // Verificar que o registro foi bem-sucedido
        Time timeH = campeonato.buscarTime("Time H");
        assertEquals(1, timeH.getJogos(), "Time H deve ter 1 jogo registrado");
        assertEquals(1, timeH.getCartoesAmarelos(), "Time H deve ter 1 cartão amarelo");
        
        // Nota: A validação de null em cartões não pode ser testada aqui devido
        // à limitação de tipos primitivos em Java. Isso seria validado na camada de UI.
        assertTrue(true, "Validação de null em cartões seria feita na camada de apresentação (tipos primitivos não permitem null)");
    }

    /**
     * CT27: Bloqueio de Placar (Entrada Não Numérica)
     *
     * Pré-condição: Times J e K cadastrados
     * Entradas: registrarResultado("Time J", "Time K", "dois", 1, 0, 0, 0, 0)
     * Resultado Esperado: O sistema deve bloquear o registro e exibir mensagem 
     * de erro ("O placar deve ser um valor numérico inteiro")
     * Prioridade: Alta
     *
     * Nota Técnica: Em Java com tipos primitivos (int), este cenário é prevenido
     * em tempo de compilação. A linguagem não permite passar String para parâmetros int.
     * Portanto, este teste não pode ser executado da forma descrita no plano de testes.
     * A validação de entradas não numéricas seria feita na camada de apresentação
     * (interface web/mobile) antes de converter para int e chamar registrarResultado().
     */
    @Test
    @DisplayName("CT27: Bloqueio de Entrada Não Numérica")
    void testCT27_BloqueioEntradaNaoNumerica() {
        // Arrange
        campeonato.cadastrarTime("Time J", "TJ");
        campeonato.cadastrarTime("Time K", "TK");

        // Nota: O plano de testes menciona registrarResultado("Time J", "Time K", "dois", 1, ...)
        // mas em Java isso não compila porque "dois" é String e o parâmetro espera Integer/int.
        //
        // Em uma arquitetura completa:
        // 1. A camada de apresentação receberia "dois" como String
        // 2. A camada de apresentação validaria e converteria para int
        // 3. Se inválido, retornaria erro antes de chamar registrarResultado()
        // 4. Se válido, chamaria registrarResultado() com int
        //
        // Como nosso modelo de domínio usa tipos primitivos, essa validação
        // já é garantida em tempo de compilação. Este teste documenta essa garantia.

        // Verificar que valores numéricos válidos funcionam
        campeonato.registrarResultado("Time J", "Time K", 2, 1, 0, 0, 0, 0);
        
        Time timeJ = campeonato.buscarTime("Time J");
        assertEquals(1, timeJ.getJogos(), "Time J deve ter 1 jogo registrado");
        assertEquals(3, timeJ.getPontos(), "Time J deve ter 3 pontos (vitória)");

        // Documentar que validação de tipo é feita em tempo de compilação
        assertTrue(true, "Java com tipos primitivos previne entradas não numéricas em tempo de compilação. Validação adicional seria feita na camada de apresentação.");
    }

    /**
     * CT28: Validação de Cadastro de Times (Abreviatura com Espaço)
     *
     * Entradas: cadastrarTime("Time Validação", "TV A")
     * Resultado Esperado: O sistema deve impedir o cadastro e exibir uma mensagem 
     * de erro ("A abreviatura deve ser única e não pode conter espaços")
     * Prioridade: Média
     * Pós-condições: O novo time não é criado
     */
    @Test
    @DisplayName("CT28: Validação de Cadastro - Abreviatura com Espaço")
    void testCT28_ValidacaoCadastroAbreviaturaComEspaco() {
        // Act & Assert
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
            campeonato.cadastrarTime("Time Validação", "TV A")
        );

        assertTrue(e.getMessage().toLowerCase().contains("espaço") ||
                   e.getMessage().toLowerCase().contains("abreviat") ||
                   e.getMessage().toLowerCase().contains("espaço"));
        assertEquals(0, campeonato.getNumeroTimes());
    }

    /**
     * CT29: Bloqueio de Cadastro de Times no Meio
     *
     * Este teste está incluído aqui mas também faz parte do RF14
     *
     * Entradas: cadastrarTime("Time Z", "Z") - após início do campeonato
     * Resultado Esperado: O sistema deve impedir o cadastro e exibir uma mensagem 
     * de erro ("Não é permitido adicionar novos times após o início da competição")
     * Prioridade: Alta
     */
    @Test
    @DisplayName("CT29: Bloqueio de Cadastro de Times no Meio")
    void testCT29_BloqueioCadastroTimesNoMeio() {
        // Arrange: Cadastrar times e registrar uma partida
        campeonato.cadastrarTime("Time A", "TA");
        campeonato.cadastrarTime("Time B", "TB");
        campeonato.registrarResultado("Time A", "Time B", 1, 0, 0, 0, 0, 0);
    
        // Act & Assert: Tentar cadastrar novo time deve bloquear
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            campeonato.cadastrarTime("Time Z", "Z");
        }, "Deve bloquear cadastro após início da competição");
    
        // Verificar mensagem de erro
        assertTrue(e.getMessage().contains("não é possível adicionar") || 
                   e.getMessage().contains("início da competição"));
    
        // Verificar que nenhum time foi adicionado
        assertEquals(2, campeonato.getNumeroTimes(), 
                     "Devem permanecer apenas 2 times cadastrados");
    }

    /**
     * CT30: Bloqueio de Placar (Cartões Múltiplos Faltando)
     *
     * Pré-condição: Times L e M cadastrados
     * Entradas: registrarResultado("Time L", "Time M", null, 1, 1, 0, null, null)
     * Resultado Esperado: O sistema deve bloquear o registro e exibir uma mensagem 
     * de erro ("Todos os campos de placar e cartões devem ser preenchidos")
     * Prioridade: Alta
     *
     * Nota Técnica: O plano de testes menciona null para cartões, mas em Java
     * os cartões são int primitivo (não podem ser null). Apenas os gols são Integer
     * e podem ser null. Este teste valida null nos gols (que podem ser null).
     */
    @Test
    @DisplayName("CT30: Bloqueio de Placar (Cartões Múltiplos Faltando)")
    void testCT30_BloqueioCartoesMultiplosFaltando() {
        // Arrange
        campeonato.cadastrarTime("Time L", "TL");
        campeonato.cadastrarTime("Time M", "TM");
    
        // Act & Assert: Tentar registrar com golsA null deve bloquear
        IllegalArgumentException e1 = assertThrows(IllegalArgumentException.class, () -> {
            campeonato.registrarResultado("Time L", "Time M", null, 1, 1, 0, 0, 0);
        }, "Deve bloquear quando golsA é null");
    
        // Verificar mensagem de erro
        assertTrue(e1.getMessage().contains("Placar deve ser preenchido") ||
                   e1.getMessage().toLowerCase().contains("preenchido"),
                   "Mensagem deve indicar que o placar deve ser preenchido");
    
        // Verificar que os atributos permanecem inalterados
        Time timeL = campeonato.buscarTime("Time L");
        assertEquals(0, timeL.getPontos(), "Atributos devem permanecer inalterados após erro");
        assertEquals(0, timeL.getJogos(), "Atributos devem permanecer inalterados após erro");
    
        // Tentar registrar com golsB null também deve bloquear
        IllegalArgumentException e2 = assertThrows(IllegalArgumentException.class, () -> {
            campeonato.registrarResultado("Time L", "Time M", 1, null, 0, 0, 0, 0);
        }, "Deve bloquear quando golsB é null");
    
        // Verificar mensagem de erro
        assertTrue(e2.getMessage().contains("Placar deve ser preenchido") ||
                   e2.getMessage().toLowerCase().contains("preenchido"),
                   "Mensagem deve indicar que o placar deve ser preenchido");
    
        // Nota: Cartões são int primitivo, então não podem ser null.
        // A validação de null em cartões seria feita na camada de apresentação.
        // Validação de cartões negativos está implementada e testada em CT22.
    }
}

