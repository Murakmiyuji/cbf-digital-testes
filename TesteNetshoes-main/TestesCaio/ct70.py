def test_CT70_adicionar_ao_carrinho():
    navegador = None
    try:
        navegador = webdriver.Chrome()
        navegador.get("https://www.netshoes.com.br")
        navegador.maximize_window()

        navegador.find_element(By.NAME, "q").send_keys("camiseta masculina" + Keys.ENTER)
        time.sleep(3)

        navegador.find_element(By.CSS_SELECTOR, "a.item-card__description").click()
        time.sleep(3)

        # Seleciona tamanho caso exista
        tamanhos = navegador.find_elements(By.CSS_SELECTOR, "label.sku-item")
        if len(tamanhos) > 0:
            tamanhos[0].click()

        navegador.find_element(By.CSS_SELECTOR, "button.buy-button").click()
        time.sleep(4)

        assert "checkout/cart" in navegador.current_url
        assert navegador.find_element(By.CSS_SELECTOR, ".basket-product").is_displayed()

    finally:
        if navegador:
            navegador.quit()

test_CT70_adicionar_ao_carrinho()
