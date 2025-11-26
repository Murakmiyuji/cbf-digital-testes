from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.action_chains import ActionChains
import time

try:
    navegador = webdriver.Chrome()
    navegador.get("https://www.netshoes.com.br/p/####")
    navegador.maximize_window()

    time.sleep(10)

    assert "netshoes.com.br" in navegador.current_url, \
        "Usuário não permaneceu no site após acessar produto inexistente."

    mensagem_erro = navegador.find_elements(
        By.XPATH,
        "//*[contains(translate(text(),'NÃO FOI ENCONTRADO','não foi encontrado'),'não foi encontrado') \
            or contains(translate(text(),'PÁGINA NÃO ENCONTRADA','página não encontrada'),'página não encontrada') \
            or contains(translate(text(),'PÁGINA NÃO DISPONÍVEL','página não disponível'),'página não disponível') \
            or contains(translate(text(),'NÃO ESTÁ DISPONÍVEL','não está disponível'),'não está disponível')]"
    )

    assert len(mensagem_erro) > 0, \
        "Não foi exibida uma mensagem de erro indicando que o produto/página não foi encontrado ou está indisponível."

    print("teste validado")

except Exception as erro:
    print("Ocorreu um erro durante o teste:")
    print(erro)