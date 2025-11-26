def test_CT72_checkout_requer_login():
    navegador = None
    try:
        navegador = webdriver.Chrome()
        navegador.get("https://www.netshoes.com.br")
        navegador.maximize_window()

        navegador.find_element(By.NAME, "q").send_keys("tenis" + Keys.ENTER)
        time.sleep(3)

        navegador.find_element(By.CSS_SELECTOR, "a.item-card__description").click()
        time.sleep(3)

        tamanhos = navegador.find_elements(By.CSS_SELECTOR, "label.sku-item")
        if len(tamanhos) > 0:
            tamanhos[0].click()

        navegador.find_element(By.CSS_SELECTOR, "button.buy-button").click()
        time.sleep(3)

        navegador.find_element(By.CSS_SELECTOR, ".continue-button").click()
        time.sleep(3)

        assert "login" in navegador.current_url
        assert navegador.find_element(By.NAME, "email").is_displayed()
        assert navegador.find_element(By.NAME, "password").is_displayed()

    finally:
        if navegador:
            navegador.quit()

test_CT72_checkout_requer_login()
