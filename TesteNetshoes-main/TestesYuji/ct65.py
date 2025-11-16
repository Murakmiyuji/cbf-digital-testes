from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.action_chains import ActionChains
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import time


def start_driver():
    options = webdriver.ChromeOptions()
    options.add_argument("--disable-notifications")
    options.add_argument("--disable-popup-blocking")
    options.add_argument("--start-maximized")
    driver = webdriver.Chrome(options=options)
    return driver


def fechar_popups(driver):
    try:
        botoes = driver.find_elements(By.XPATH, "//button[contains(., '×') or contains(., 'Fechar') or contains(.,'fechar')]")
        for b in botoes:
            try:
                if b.is_displayed():
                    driver.execute_script("arguments[0].click();", b)
                    time.sleep(0.3)
            except:
                pass

        driver.execute_script("""
            document.querySelectorAll('[class*=\"overlay\"], [id*=\"overlay\"], .modal, .popup')
            .forEach(e => e.style.display='none');
        """)

        driver.execute_script("""
            document.querySelectorAll('*').forEach(e=>{
                if(e.innerText && e.innerText.includes('Vans')){
                    e.style.display='none';
                }
            });
        """)

        driver.execute_script("""
            document.querySelectorAll('body *').forEach(el=>{
                const z = parseInt(window.getComputedStyle(el).zIndex) || 0;
                if(z > 999){
                    el.style.pointerEvents='none';
                }
            });
        """)

    except:
        pass


def clique_agressivo(driver, elemento):
    try:
        driver.execute_script("arguments[0].scrollIntoView({block:'center'});", elemento)
        time.sleep(0.4)
        elemento.click()
        return True
    except:
        pass

    try:
        ActionChains(driver).move_to_element(elemento).pause(0.1).click().perform()
        return True
    except:
        pass

    try:
        driver.execute_script("arguments[0].click();", elemento)
        return True
    except:
        pass

    try:
        driver.execute_script("""
            function fire(el){
                ['mousedown','mouseup','click'].forEach(ev=>{
                    el.dispatchEvent(new MouseEvent(ev,{bubbles:true,cancelable:true}));
                });
            }
            fire(arguments[0]);
        """, elemento)
        return True
    except:
        pass

    return False


try:
    driver = start_driver()
    wait = WebDriverWait(driver, 12)


    driver.get("https://www.netshoes.com.br/")
    time.sleep(2)
    fechar_popups(driver)

    topo = driver.find_element(By.CLASS_NAME, "user__label")
    ActionChains(driver).move_to_element(topo).perform()
    time.sleep(1.5)

    entrar = driver.find_element(By.CLASS_NAME, "user__box__link")
    entrar.click()

    email = driver.find_element(By.ID, "user")
    senha = driver.find_element(By.ID, "password")

    email.send_keys("samu21oioi@gmail.com")
    senha.send_keys("kamillelinda")

    bt_entrar = driver.find_element(By.CSS_SELECTOR, "button.btn.btn--primary.btn--block.icon--right")
    bt_entrar.click()

    time.sleep(4)
    fechar_popups(driver)


    campo_busca = wait.until(EC.presence_of_element_located((By.ID, "search")))
    campo_busca.send_keys("Tênis Nike")
    time.sleep(0.3)

    bt_busca = wait.until(EC.element_to_be_clickable((By.CLASS_NAME, "search__button")))
    bt_busca.click()

    time.sleep(2)
    fechar_popups(driver)


    xpath_prod = "//a[contains(@href,'tenis-infantil-nike-revolution-7')]"
    produto = wait.until(EC.presence_of_element_located((By.XPATH, xpath_prod)))
    driver.execute_script("arguments[0].scrollIntoView({block:'center'});", produto)
    produto.click()

    time.sleep(2)
    fechar_popups(driver)


    tamanho = wait.until(EC.element_to_be_clickable((By.XPATH, "//button[contains(text(),'31')]")))
    tamanho.click()

    time.sleep(1)
    fechar_popups(driver)

    add_cart = wait.until(EC.element_to_be_clickable((By.CLASS_NAME, "add-cart-button")))
    add_cart.click()

    time.sleep(2)

    driver.get("https://www.netshoes.com.br/cart")
    time.sleep(2)
    fechar_popups(driver)

    bt_checkout = wait.until(EC.element_to_be_clickable((By.XPATH, "//button[contains(.,'Continuar')]")))
    bt_checkout.click()

    time.sleep(4)
    fechar_popups(driver)



    print("Procurando botão FINALIZAR PEDIDO COM PIX...")

    seletores_pix = [
        "//button[contains(.,'Pix')]",
        "//button[contains(@class,'pix')]",
        "//button[contains(translate(., 'PIX', 'pix'),'pix')]",
        "//button[contains(.,'finalizar') and contains(.,'Pix')]"
    ]

    botao_pix = None

    for xp in seletores_pix:
        try:
            botao_pix = driver.find_element(By.XPATH, xp)
            if botao_pix.is_displayed():
                break
        except:
            pass

    if not botao_pix:
        raise Exception("❌ NÃO ENCONTREI O BOTÃO DE FINALIZAR COM PIX!")

    print("Botão encontrado → tentando clique agressivo...")
    ok = clique_agressivo(driver, botao_pix)

    if not ok:
        raise Exception("❌ Falhou ao clicar no botão do PIX!")

    print("✅ Clique no botão PIX executado com sucesso!")

    time.sleep(6)

    print("TESTE FINALIZADO")

except Exception as e:
    print("ERRO:")
    print(e)

finally:
    time.sleep(3)
    driver.quit()
