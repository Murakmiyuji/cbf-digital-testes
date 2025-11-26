from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
import time

def test_CT69_detalhes_produto():
    navegador = None
    try:
        navegador = webdriver.Chrome()
        navegador.get("https://www.netshoes.com.br")
        navegador.maximize_window()

        busca = navegador.find_element(By.NAME, "q")
        busca.send_keys("tenis masculino")
        busca.send_keys(Keys.ENTER)

        time.sleep(3)

        primeiro = navegador.find_element(By.CSS_SELECTOR, "a.item-card__description")
        primeiro.click()

        time.sleep(3)

        assert navegador.find_element(By.CSS_SELECTOR, "h1[itemprop='name']").is_displayed()
        assert navegador.find_element(By.CSS_SELECTOR, "img[itemprop='image']").is_displayed()
        assert navegador.find_element(By.CSS_SELECTOR, "span.price").is_displayed()
        assert navegador.find_element(By.CSS_SELECTOR, "button.buy-button").is_displayed()

    finally:
        if navegador:
            navegador.quit()

test_CT69_detalhes_produto()
