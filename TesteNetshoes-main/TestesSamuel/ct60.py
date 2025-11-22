from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.action_chains import ActionChains
import time

def test_login_and_product_navigation():
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

        time.sleep(5)
        
        try:
            logado_element = navegador.find_element(By.CLASS_NAME, "user__label")
            assert logado_element.is_displayed()
        except:
            assert False, "Login falhou: Elemento de usuário logado não encontrado."
        
        navegador.execute_script("window.scrollBy(0, 800)")
        time.sleep(3)
        
        camisa_argentina = navegador.find_element(
            By.XPATH,
            "//a[contains(@href, 'camisa-selecao-argentina-home-torcedor-2026')]"
        )
        
        camisa_argentina.click()
        
        time.sleep(5)
        
        expected_product_slug = "camisa-selecao-argentina"
        assert expected_product_slug in navegador.current_url.lower()

        assert "camisa" in navegador.title.lower()

    except Exception as erro:
        raise erro
        
    finally:
        if navegador:
            navegador.quit()

test_login_and_product_navigation()
