@Test
public void testCT70_AdicionarAoCarrinho() {
    driver.get("https://www.netshoes.com.br");

driver.findElement(By.name("q")).sendKeys("camiseta masculina" + Keys.ENTER);
driver.findElement(By.cssSelector("a.item-card__description")).click();

// Seleciona tamanho se tiver
List<WebElement> sizes = driver.findElements(By.cssSelector("label.sku-item"));
if (!sizes.isEmpty()) sizes.get(0).click();

driver.findElement(By.cssSelector("button.buy-button")).click();

assertTrue(driver.getCurrentUrl().contains("checkout/cart"));
assertTrue(driver.findElement(By.cssSelector(".basket-product")).isDisplayed());
}
