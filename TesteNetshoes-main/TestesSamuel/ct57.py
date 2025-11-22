from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.action_chains import ActionChains
import time

def test_login_netshoes():
    navegador = None
    try:
        navegador = webdriver.Chrome()
        navegador.get("https://www.netshoes.com.br/")
        navegador.maximize_window()
        
        assert "Netshoes" in navegador.title
        
        cima = navegador.find_element(By.CLASS_NAME, "user__label")
        actions = ActionChains(navegador)
        actions.move_to_element(cima).perform()
        time.sleep(2)  

        clicar = navegador.find_element(By.CLASS_NAME, "user__box__link")
        clicar.click()

        time.sleep(2)
        assert "login" in navegador.current_url

        email = navegador.find_element(By.ID, "user")
        senha = navegador.find_element(By.ID, "password")

        email.send_keys("samu21oioi@gmail.com")
        senha.send_keys("kamillelinda")

        botao_entrar = navegador.find_element(By.CSS_SELECTOR, "button.btn.btn--primary.btn--block.icon--right")
        botao_entrar.click()

        time.sleep(10)
        
        assert "Netshoes" in navegador.title
        
        try:
            logado_element = navegador.find_element(By.CLASS_NAME, "user__label")
            assert logado_element.is_displayed() 
        except:
            assert False, "Elemento de usuário logado não encontrado."
            
    except Exception as erro:
        raise erro
        
    finally:
        if navegador:
            navegador.quit()

test_login_netshoes()
