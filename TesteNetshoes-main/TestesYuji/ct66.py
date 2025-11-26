from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.action_chains import ActionChains
import time

try:
    navegador = webdriver.Chrome()
    navegador.get("https://www.netshoes.com.br/")
    navegador.maximize_window()

    busca = WebDriverWait(navegador, 20).until(
        EC.presence_of_element_located((By.ID, "search"))
    )
    busca.send_keys("Tênis Nike")

    clicar = navegador.find_element(By.CLASS_NAME, "search__button")
    clicar.click()


    masculino = WebDriverWait(navegador, 20).until(
        EC.element_to_be_clickable((By.ID, "genero-Masculino"))
    )
    assert masculino.is_displayed(), "Filtro 'Masculino' deve estar visível"
    navegador.execute_script("arguments[0].click();", masculino) 

    time.sleep(5)

    # Verifica que existem produtos após aplicar o filtro
    produtos = navegador.find_elements(By.XPATH, "//a[contains(@href,'tenis') or contains(@href,'tenis-')]")
    assert len(produtos) > 0, "Devem existir produtos listados após aplicar o filtro 'Masculino'"

    print("teste validado")

except Exception as erro:
    print("Ocorreu um erro durante o teste:")
    print(erro)
