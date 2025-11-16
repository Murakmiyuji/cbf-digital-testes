from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.action_chains import ActionChains
import time

try:
    navegador = webdriver.Chrome()
    navegador.get("https://www.netshoes.com.br/")
    navegador.maximize_window()

    cima = navegador.find_element(By.CLASS_NAME, "user__label")

    actions = ActionChains(navegador)

    actions.move_to_element(cima).perform()
    time.sleep(2)  

    #clicar no botão q aparecer
    clicar = navegador.find_element(By.CLASS_NAME, "user__box__link")
    clicar.click()

    #logar
    email = navegador.find_element(By.ID, "user")
    senha = navegador.find_element(By.ID, "password")

    email.send_keys("samu21oioi@gmail.com")
    senha.send_keys("kamillelinda")

    botao_entrar = navegador.find_element(By.CSS_SELECTOR, "button.btn.btn--primary.btn--block.icon--right")
    botao_entrar.click()

    time.sleep(10)

    print("teste validado")

except Exception as erro:
    print("Ocorreu um erro durante o teste:")
    print(erro)