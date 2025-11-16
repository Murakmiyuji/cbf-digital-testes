from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.action_chains import ActionChains
import time

try:
    navegador = webdriver.Chrome()
    navegador.get("https://www.netshoes.com.br/p/####")
    navegador.maximize_window()

    time.sleep(10)

    print("teste validado")

except Exception as erro:
    print("Ocorreu um erro durante o teste:")
    print(erro)