@Test
public void testCT71_FiltroPreco() {
    driver.get("https://www.netshoes.com.br");

driver.findElement(By.name("q")).sendKeys("tenis" + Keys.ENTER);

// Filtro 100–200
driver.findElement(By.xpath("//label[contains(text(), 'R$ 100 a R$ 200')]")).click();
Thread.sleep(3000);

List<WebElement> prices = driver.findElements(By.cssSelector("span.price"));

for (WebElement priceEl : prices) {
    String priceTxt = priceEl.getText().replace("R$", "").replace(",", ".").trim();
double price = Double.parseDouble(priceTxt);
assertTrue(price >= 100 && price <= 200);
}
}
