@Test
public void testCT72_CheckoutRequerLogin() {
    driver.get("https://www.netshoes.com.br");

driver.findElement(By.name("q")).sendKeys("tenis" + Keys.ENTER);
driver.findElement(By.cssSelector("a.item-card__description")).click();

// Seleciona tamanho se existir
List<WebElement> sizes = driver.findElements(By.cssSelector("label.sku-item"));
if (!sizes.isEmpty()) sizes.get(0).click();

driver.findElement(By.cssSelector("button.buy-button")).click();

driver.findElement(By.cssSelector(".continue-button")).click();

assertTrue(driver.getCurrentUrl().contains("login"));
assertTrue(driver.findElement(By.name("email")).isDisplayed());
assertTrue(driver.findElement(By.name("password")).isDisplayed());
}
