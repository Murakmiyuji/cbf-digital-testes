from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.action_chains import ActionChains
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import (
    TimeoutException, ElementClickInterceptedException, StaleElementReferenceException, NoSuchElementException
)
import time
import json

def start_driver():
    options = webdriver.ChromeOptions()
    options.add_argument("--disable-notifications")
    options.add_argument("--disable-popup-blocking")
    options.add_argument("--start-maximized")
    driver = webdriver.Chrome(options=options)
    return driver

def info_element(driver, el):
    try:
        displayed = el.is_displayed()
    except Exception:
        displayed = "erro is_displayed"
    try:
        enabled = el.is_enabled()
    except Exception:
        enabled = "erro is_enabled"
    try:
        rect = driver.execute_script("return arguments[0].getBoundingClientRect().toJSON();", el)
    except Exception:
        rect = None
    try:
        z = driver.execute_script("return window.getComputedStyle(arguments[0]).zIndex;", el)
    except Exception:
        z = None
    try:
        outer = driver.execute_script("return arguments[0].outerHTML.slice(0,400);", el)
    except Exception:
        outer = "<erro ao obter outerHTML>"
    info = {
        "displayed": displayed,
        "enabled": enabled,
        "rect": rect,
        "zIndex": z,
        "outerHTML_preview": outer
    }
    print("INFO ELEMENT:", json.dumps(info, indent=2, ensure_ascii=False))
    return info

def element_at_point(driver, x, y):
    try:
        return driver.execute_script("""
            const el = document.elementFromPoint(arguments[0], arguments[1]);
            if(!el) return null;
            return {tag: el.tagName, cls: el.className, id: el.id, txt: el.innerText ? el.innerText.slice(0,120) : ''};
        """, x, y)
    except Exception as e:
        return {"error": str(e)}

def close_known_popups(driver):
    """Fechar popups óbvios por seletores/texto."""
    try:
        candidates = driver.find_elements(By.XPATH, "//button[contains(., '×') or contains(., 'Fechar') or contains(., 'fechar') or contains(@class,'close')]")
        for b in candidates:
            try:
                if b.is_displayed():
                    print(">>> clicando no botão de fechar popup (x/Fechar) ...")
                    try:
                        b.click()
                    except:
                        driver.execute_script("arguments[0].click();", b)
                    time.sleep(0.4)
            except Exception:
                pass

        try:
            vans = driver.find_elements(By.XPATH, "//*[contains(text(),'Vans') or contains(text(),'Vans está') or contains(text(),'Vans está na área') ]")
            for v in vans:
                try:
                    print(">>> achou elemento com texto 'Vans' - tentando fechar ancestor...")
                    anc = v.find_element(By.XPATH, "ancestor::div[1]") 
                    driver.execute_script("arguments[0].style.display='none';", anc)
                    time.sleep(0.3)
                except Exception:
                    pass
        except Exception:
            pass

        driver.execute_script("""
            Array.from(document.querySelectorAll('body *')).forEach(el => {
                try {
                    const s = window.getComputedStyle(el);
                    const z = parseInt(s.zIndex) || 0;
                    if (z > 1000) { el.style.pointerEvents='none'; el.setAttribute('data-hidden-by-debug','1'); }
                } catch(e){}
            });
        """)
        time.sleep(0.2)
    except Exception as e:
        print("Erro close_known_popups:", e)

def robust_click_with_debug(driver, el, name="element"):
    print(f"-> Tentando clicar ({name}) com várias estratégias...")
    info_element(driver, el)
    try:
        driver.execute_script("arguments[0].scrollIntoView({block:'center'});", el)
    except Exception:
        pass
    time.sleep(0.25)

    try:
        el.click()
        print("  > click() normal executado.")
        return True
    except ElementClickInterceptedException as e:
        print("  > Interceptado (click normal).", e)
    except Exception as e:
        print("  > click() normal levantou exceção:", e)

    try:
        ActionChains(driver).move_to_element(el).pause(0.1).click(el).perform()
        print("  > ActionChains click OK.")
        return True
    except Exception as e:
        print("  > ActionChains falhou:", e)

    try:
        driver.execute_script("arguments[0].click();", el)
        print("  > JS click OK.")
        return True
    except Exception as e:
        print("  > JS click falhou:", e)


    try:
        rect = driver.execute_script("const r = arguments[0].getBoundingClientRect(); return {l:r.left, t:r.top, w:r.width, h:r.height};", el)
        cx = int(rect['l'] + rect['w']/2)
        cy = int(rect['t'] + rect['h']/2)
        elem_info = element_at_point(driver, cx, cy)
        print(f"  > Element at point before dispatch: {elem_info}")
        driver.execute_script("""
            function dispatchAt(x,y){
               const el = document.elementFromPoint(x,y);
               if(!el) return false;
               ['mousemove','mousedown','mouseup','click'].forEach(t=>{
                 el.dispatchEvent(new MouseEvent(t, {clientX:x, clientY:y, bubbles:true, cancelable:true}));
               });
               return true;
            }
            return dispatchAt(arguments[0], arguments[1]);
        """, cx, cy)
        print("  > Dispatch eventos tentado nas coordenadas.")
        return True
    except Exception as e:
        print("  > dispatch events falhou:", e)

    print("  -> Todas as estratégias falharam para este elemento.")
    return False

try:
    navegador = start_driver()
    wait = WebDriverWait(navegador, 12)

    print("Abrindo homepage...")
    navegador.get("https://www.netshoes.com.br/")
    time.sleep(2)

    close_known_popups(navegador)

    print("Procurando campo de busca...")
    busca = wait.until(EC.presence_of_element_located((By.ID, "search")))
    print("Campo de busca encontrado.")
    busca.clear()
    busca.send_keys("Tênis Nike")
    time.sleep(0.3)

    print("Localizando botão de busca...")
    search_btn = wait.until(EC.element_to_be_clickable((By.CLASS_NAME, "search__button")))
    print("Botão de busca pronto. Debug antes do clique:")
    info_element(navegador, search_btn)
    if not robust_click_with_debug(navegador, search_btn, name="search_btn"):
        raise Exception("Falha ao clicar no botão de busca")

    time.sleep(2)
    close_known_popups(navegador)

    produto_xpath = "//a[contains(@href, 'tenis-infantil-nike-revolution-7-JD8') or contains(@href, 'tenis-infantil-nike-revolution-7')]"
    print("Procurando link do produto com xpath:", produto_xpath)
    tenis_elem = wait.until(EC.presence_of_element_located((By.XPATH, produto_xpath)))
    print("Produto encontrado. Info:")
    info_element(navegador, tenis_elem)

    navegador.execute_script("arguments[0].scrollIntoView({block:'center'});", tenis_elem)
    time.sleep(0.6)
    close_known_popups(navegador)
    if not robust_click_with_debug(navegador, tenis_elem, name="produto"):
        raise Exception("Falha ao clicar no produto.")

    print("Aguardando bloco de tamanhos aparecer...")
    wait.until(EC.presence_of_element_located((By.XPATH, "//ul[contains(@class,'size-list') or contains(@class,'size-list__item') or contains(@class,'size-list__item')]")))
    time.sleep(1)
    close_known_popups(navegador)

    tamanho_xpath = "//button[contains(text(), '31')]"
    print("Localizando botão tamanho 31...")
    tamanho_btn = wait.until(EC.presence_of_element_located((By.XPATH, tamanho_xpath)))
    print("Tamanho 31 encontrado. Info:")
    info_element(navegador, tamanho_btn)
    navegador.execute_script("arguments[0].scrollIntoView({block:'center'});", tamanho_btn)
    time.sleep(0.4)
    close_known_popups(navegador)
    if not robust_click_with_debug(navegador, tamanho_btn, name="tamanho_31"):
        raise Exception("Falha ao clicar no tamanho 31.")

    time.sleep(0.8)
    close_known_popups(navegador)

    add_selector = "add-cart-button"
    print("Procurando botão 'add-cart-button'...")
    botao_carrinho = wait.until(EC.presence_of_element_located((By.CLASS_NAME, add_selector)))
    print("Botão 'Adicionar ao Carrinho' presente. Info:")
    info_element(navegador, botao_carrinho)
    navegador.execute_script("arguments[0].scrollIntoView({block:'center'});", botao_carrinho)
    time.sleep(0.4)
    close_known_popups(navegador)

    if not robust_click_with_debug(navegador, botao_carrinho, name="botao_carrinho"):
        raise Exception("Falha ao clicar no botão 'Adicionar ao Carrinho'.")

    time.sleep(1)
    print("Tentando abrir /cart para validar...")
    navegador.get("https://www.netshoes.com.br/cart")
    time.sleep(3)

    print("URL atual:", navegador.current_url)
    try:
        cart_marker = navegador.find_elements(By.XPATH, "//*[contains(text(),'Carrinho') or contains(text(),'Meu carrinho') or contains(@class,'cart')]")
        print("Verificação simples por texto/classes no /cart - encontrados:", len(cart_marker))
    except Exception as e:
        print("Erro ao checar o carrinho:", e)

    print("FIM DO SCRIPT DE DEBUG - cole o output completo aqui para eu analisar.")
except Exception as erro:
    print("OCORREU UM ERRO NO FLUXO:")
    print(erro)
finally:
    print("teste validado")
    time.sleep(3)
    try:
        navegador.quit()
    except:
        pass
