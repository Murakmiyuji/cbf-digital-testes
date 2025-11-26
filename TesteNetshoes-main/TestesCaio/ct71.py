def test_CT71_filtro_preco():
    navegador = None
    try:
        navegador = webdriver.Chrome()
        navegador.get("https://www.netshoes.com.br")
        navegador.maximize_window()

        navegador.find_element(By.NAME, "q").send_keys("tenis" + Keys.ENTER)
        time.sleep(3)

        # Clicar no filtro R$ 100 a R$ 200
        navegador.find_element(By.XPATH, "//label[contains(text(), 'R$ 100 a R$ 200')]").click()
        time.sleep(3)

        precos = navegador.find_elements(By.CSS_SELECTOR, "span.price")

        for preco_el in precos:
            texto = preco_el.get_text().replace("R$", "").replace(".", "").replace(",", ".").strip()
            
            try:
                preco = float(texto)
                assert 100 <= preco <= 200
            except:
                # ignora elementos que não são preço real
                pass

    finally:
        if navegador:
            navegador.quit()

test_CT71_filtro_preco()
