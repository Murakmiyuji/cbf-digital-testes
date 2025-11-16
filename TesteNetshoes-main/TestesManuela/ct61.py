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

    print("teste validado")
    
except Exception as erro:
    print("Ocorreu um erro durante o teste:")
    print(erro)