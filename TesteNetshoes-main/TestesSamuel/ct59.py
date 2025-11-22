from selenium import webdriver
from selenium.webdriver.common.by import By
import time

def test_invalid_search_netshoes():
    navegador = None
    search_term = "####"
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
        
        assert search_term in navegador.current_url

        try:
            no_results_element = navegador.find_element(By.CLASS_NAME, "search-content-header")
            assert "0 resultados" in no_results_element.text or "nenhum resultado" in no_results_element.text.lower()

        except:
             assert False, "Não foi possível validar a ausência de resultados para a busca inválida."
            
    except Exception as erro:
        raise erro
        
    finally:
        if navegador:
            navegador.quit()

test_invalid_search_netshoes()
