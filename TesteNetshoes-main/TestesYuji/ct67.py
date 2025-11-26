from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
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

    preco = WebDriverWait(navegador, 20).until(
        EC.element_to_be_clickable((By.XPATH, "//span[@class='filter-box__title' and contains(text(), 'Preço')]"))
    )
    navegador.execute_script("arguments[0].click();", preco)

    valorminimo = WebDriverWait(navegador, 20).until(
        EC.presence_of_element_located((By.ID, "inputValueMin"))
    )
    valorminimo.clear()
    valorminimo.send_keys("50")


    valormaximo = WebDriverWait(navegador, 20).until(
        EC.presence_of_element_located((By.ID, "inputValueMax"))
    )
    valormaximo.clear()
    valormaximo.send_keys("100")

    # Asserts para validar que os campos foram preenchidos corretamente
    assert preco is not None and preco.is_displayed(), "Filtro 'Preço' deve estar visível"
    assert valorminimo.get_attribute('value') == '50', "Valor mínimo deve ser 50"
    assert valormaximo.get_attribute('value') == '100', "Valor máximo deve ser 100"
    print("teste validado")

except Exception as erro:
    print("Ocorreu um erro durante o teste:")
    print(erro)
