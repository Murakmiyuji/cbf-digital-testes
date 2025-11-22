from selenium import webdriver
from selenium.webdriver.common.by import By
import time

def test_search_netshoes():
    navegador = None
    search_term = "Tênis Nike"
    try:
        navegador = webdriver.Chrome()
        navegador.get("https://www.netshoes.com.br/")
        navegador.maximize_window()

        assert "Netshoes" in navegador.title

        busca = navegador.find_element(By.ID, "search")
        busca.send_keys(search_term)
        
        clicar = navegador.find_element(By.CLASS_NAME, "search__button")
        clicar.click()
        
        time.sleep(5)
        
        assert search_term.lower().replace(" ", "-") in navegador.current_url.lower()

        assert search_term in navegador.title
            
    except Exception as erro:
        raise erro
        
    finally:
        if navegador:
            navegador.quit()

test_search_netshoes()
