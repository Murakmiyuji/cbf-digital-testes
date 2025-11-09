@echo off
echo ========================================
echo CBF Digital - Plano de Testes
echo Arena Controle - UDESC Alto Vale
echo ========================================
echo.

REM Verificar se Maven está instalado
where mvn >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo [ERRO] Maven nao encontrado!
    echo.
    echo Por favor, instale o Maven:
    echo 1. Baixe em: https://maven.apache.org/download.cgi
    echo 2. Extraia e adicione ao PATH do sistema
    echo 3. Ou instale via Chocolatey: choco install maven
    echo.
    pause
    exit /b 1
)

echo Maven encontrado! Executando testes...
echo.

REM Compilar e executar testes
mvn clean test

echo.
echo ========================================
echo Testes finalizados!
echo ========================================
pause

