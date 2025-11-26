@Test
public void testCT69_DetalhesProduto() {
    driver.get("https://www.netshoes.com.br");

WebElement search = driver.findElement(By.name("q"));
search.sendKeys("tenis masculino");
search.sendKeys(Keys.ENTER);

WebElement firstProduct = driver.findElement(By.cssSelector("a.item-card__description"));
firstProduct.click();

assertTrue(driver.findElement(By.cssSelector("h1[itemprop='name']")).isDisplayed());
assertTrue(driver.findElement(By.cssSelector("img[itemprop='image']")).isDisplayed());
assertTrue(driver.findElement(By.cssSelector("span.price")).isDisplayed());
assertTrue(driver.findElement(By.cssSelector("button.buy-button")).isDisplayed());
}
