from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.action_chains import ActionChains
import time

try:
    navegador = webdriver.Chrome()
    navegador.get("https://www.netshoes.com.br/")
    navegador.maximize_window()

    busca = navegador.find_element(By.ID, "search")
    busca.send_keys("Tênis Nike")
    clicar = navegador.find_element(By.CLASS_NAME, "search__button")
    clicar.click()

    tenis = navegador.find_element(
        By.XPATH,
        "//a[contains(@href, 'tenis-infantil-nike-revolution-7-JD8')]"
    )

    tenis.click()

    navegador.execute_script("window.scrollBy(0, 800)")

    time.sleep(10)

    nome_produto = navegador.find_element(By.TAG_NAME, "h1")
    assert nome_produto.is_displayed(), "Nome do produto não está visível na página de detalhes."

    imagens = navegador.find_elements(By.TAG_NAME, "img")
    assert len(imagens) > 0, "Nenhuma imagem foi encontrada na página de detalhes do produto."

    precos = navegador.find_elements(
        By.XPATH, "//*[contains(@class, 'price') or contains(@class, 'preco')]"
    )
    assert len(precos) > 0, "Não foi encontrado nenhum elemento que aparente ser o preço do produto."

    assert "nike" in navegador.current_url.lower() or "tenis" in navegador.current_url.lower(), \
        "O usuário não permaneceu na página de detalhes do produto."

    print("teste validado")
    
except Exception as erro:
    print("Ocorreu um erro durante o teste:")
    print(erro)