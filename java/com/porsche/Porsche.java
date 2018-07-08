package com.porsche;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Porsche {

	public static void main(String[] args) throws InterruptedException {
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		// 1. Open browser
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

		// 2. Go to url “https://www.porsche.com/usa/modelstart/”
		driver.get("https://www.porsche.com/usa/modelstart/");

		// 3. Select model 718
		driver.findElement(By.xpath("//span[text()='718']")).click();

		// 4. Remember the price of 718 Cayman
		String basePrice1 = driver.findElement(By.xpath("//div[@id='m982120']//div[@class='m-14-model-price']")).getText();

		// 5. Click on Build & Price under 718 Cayman
		driver.findElement(By.className("m-14-quick-link")).click();

		// switching to sub window
		String parent = driver.getWindowHandle();
		for (String subwindow : driver.getWindowHandles()) {
			driver.switchTo().window(subwindow);
		}
		
		//xpathes
		String basePrice2Xpath ="//section[@id='s_price']//div[@class='ccaPrice'][1]";
		String equipmentPriceXpath="//section[@id='s_price']/div[@class='ccaTable']/div[@class='ccaRow'][2]/div[@class='ccaPrice']";
		String deliveryPriceXpath="//section[@id='s_price']/div[@class='ccaTable']/div[@class='ccaRow'][3]/div[@class='ccaPrice']";
		String totalPriceXpath="//section[@id='s_price']/div[@class='ccaTable']/div[@class='ccaRow priceTotal highlighted']/div[@class='ccaPrice']";
		String miamiPriceXpath="//div[@id='s_exterieur_x_IAF']//div[@class='tt_price tt_cell']";
		String wheelPriceXpath="//div[@id='s_exterieur_x_IRA']/div[@class='content']//div[@class='tt_price tt_cell']";
		String seatPriceXpath="//div[@id='seats_73']/div[@class='seat'][2]//div[@class='pBox']";
		String interiorPriceXpath="//div[@id='vs_table_IIC_x_PEKH']/div[@class='box']/div[@class='pBox']";
		String optionalEquipmentPriceXpath="//div[@id='search_x_M450']/div[@class='box']/div[@class='pBox']";
		String speedPriceXpath ="//div[@id='vs_table_IMG_x_M250']/div[@class='box']/div[@class='pBox']";
		
		// 6. Verify that Base price displayed on the page is same as the price from
		// step 4
		System.out.println("Verification in step 6:");
		String basePrice2 = driver.findElement(By.xpath(basePrice2Xpath)).getText();
		priceVerification(basePrice1, basePrice2);
		System.out.println("========================================================");

		// 7. Verify that Price for Equipment is 0
		System.out.println("Verification in step 7:");
		String expected = "0";
		String equipmentPrice = driver.findElement(By.xpath(equipmentPriceXpath)).getText();
		priceVerification(expected, equipmentPrice);
		System.out.println("========================================================");

		// 8. Verify that total price is the sum of base price + Delivery, Processing
		// and Handling Fee
		System.out.println("Verification in step 8:");
		String deliveryPrice = driver.findElement(By.xpath(deliveryPriceXpath)).getText();
		String totalPrice = driver.findElement(By.xpath(totalPriceXpath)).getText();
		priceVerification(totalPrice, addition(basePrice2, deliveryPrice));
		System.out.println("========================================================");

		// 9. Select color “Miami Blue”
		driver.findElement(By.xpath("//span[@style='background-color: rgb(0, 120, 138);']")).click();

		// 10.Verify that Price for Equipment is Equal to Miami Blue price
		System.out.println("Verification in step 10:");
		String miamiPrice = driver.findElement(By.xpath(miamiPriceXpath)).getText();
		equipmentPrice = driver.findElement(By.xpath(equipmentPriceXpath)).getText();
		priceVerification(miamiPrice, equipmentPrice);
		System.out.println("========================================================");

		// 11.Verify that total price is the sum of base price + Price for Equipment
		// +Delivery, Processing and Handling Fee
		System.out.println("Verification in step 11:");
		totalPrice = driver.findElement(By.xpath(totalPriceXpath)).getText();
		priceVerification(totalPrice, addition(basePrice2, equipmentPrice, deliveryPrice));
		System.out.println("========================================================");

		// 12.Select 20" Carrera Sport Wheels

		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@class='flyout-label-value']")).click();

		Thread.sleep(2000);
		driver.findElement(By.xpath("//a[text()='Wheels']")).click();

		Thread.sleep(2000);
		driver.findElement(By.xpath("//li[@id='s_exterieur_x_MXRD']")).click();

		// 13.Verify that Price for Equipment is the sum of Miami Blue price + 20"
		////// Carrera Sport Wheels
		System.out.println("Verification in step 13:");
		equipmentPrice = driver.findElement(By.xpath(equipmentPriceXpath)).getText();
		String wheelPrice = driver.findElement(By.xpath(wheelPriceXpath)).getText();
		priceVerification(equipmentPrice, addition(miamiPrice, wheelPrice));
		System.out.println("========================================================");

		// 14.Verify that total price is the sum of base price + Price for Equipment +
		////// Delivery, Processing and Handling Fee
		System.out.println("Verification in step 14:");
		totalPrice = driver.findElement(By.xpath(totalPriceXpath)).getText();
		priceVerification(totalPrice, addition(equipmentPrice, deliveryPrice, basePrice2));
		System.out.println("========================================================");

		// 15.Select seats ‘Power Sport Seats (14-way) with Memory Package’
		driver.findElement(By.xpath("//a[@id='navigation_main_x_mainsuboffer_x_AI']")).click();
		driver.findElement(By.xpath("//div[@class='flyout-label-value'][1]")).click();
		driver.findElement(By.xpath("//a[text()='Seats']")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id='seats_73']/div[@class='seat'][2]/span[@id='s_interieur_x_PP06']")).click();

		// 16.Verify that Price for Equipment is the sum of Miami Blue price + 20"
		////// Carrera Sport Wheels + Power Sport Seats (14-way) with Memory Package
		System.out.println("Verification in step 16:");
		equipmentPrice = driver.findElement(By.xpath(equipmentPriceXpath)).getText();
		String seatPrice = driver.findElement(By.xpath(seatPriceXpath)).getText();
		priceVerification(equipmentPrice, addition(miamiPrice, seatPrice, wheelPrice));
		System.out.println("========================================================");

		// 17.Verify that total price is the sum of base price + Price for Equipment +
		////// Delivery, Processing and Handling Fee
		System.out.println("Verification in step 17:");
		totalPrice = driver.findElement(By.xpath(totalPriceXpath)).getText();
		priceVerification(totalPrice, addition(basePrice2, equipmentPrice, deliveryPrice));
		System.out.println("========================================================");

		// 18.Click on Interior Carbon Fiber
		driver.findElement(By.xpath("//a[@id='navigation_main_x_mainsuboffer_x_individual']")).click();
		driver.findElement(By.xpath("//div[@class='flyout-label-value'][1]")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//a[text()='Interior Carbon Fiber']")).click();

		// 19.Select Interior Trim in Carbon Fiber i.c.w. Standard Interior 
		Thread.sleep(2000);
		driver.findElement(By.xpath("//span[@id='vs_table_IIC_x_PEKH_x_c01_PEKH']")).click();

		// 20.Verify that Price for Equipment is the sum of Miami Blue price + 20"
		////// Carrera Sport Wheels + Power Sport Seats (14-way) with Memory Package +
		////// Interior Trim in Carbon Fiber i.c.w. Standard Interior
		System.out.println("Verification in step 20:");
		equipmentPrice = driver.findElement(By.xpath(equipmentPriceXpath)).getText();
		String interiorPrice = driver.findElement(By.xpath(interiorPriceXpath)).getText();
		priceVerification(equipmentPrice, addition(miamiPrice, seatPrice, wheelPrice, interiorPrice));
		System.out.println("========================================================");

		// 21.Verify that total price is the sum of base price + Price for Equipment +
		// Delivery, Processing and Handling Fee
		System.out.println("Verification in step 21:");
		totalPrice = driver.findElement(By.xpath(totalPriceXpath)).getText();
		priceVerification(totalPrice, addition(basePrice2, equipmentPrice, deliveryPrice));
		System.out.println("========================================================");

		// 22.Click on Performance
		driver.findElement(By.xpath("//a[text()='3. Options']")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[text()='Performance'][1]")).click();

		// 23.Select 7-speed Porsche Doppelkupplung (PDK)
		Thread.sleep(2000);
		driver.findElement(By.xpath("//span[@id='vs_table_IMG_x_M250_x_c11_M250']")).click();

		// 24.Select Porsche Ceramic Composite Brakes (PCCB)
		WebElement input = driver.findElement(By.id("search_x_inp"));
		input.sendKeys("Ceramic Composite Brakes(PCCB)" + Keys.ENTER);
		Thread.sleep(2000);
		driver.findElement(By.xpath("//span[@id='search_x_M450_x_c91_M450']")).click();

		// 25.Verify that Price for Equipment is the sum of Miami Blue price +
		// 20" Carrera Sport Wheels + Power Sport Seats (14-way) with Memory Package
		// + Interior Trim in Carbon Fiber i.c.w. Standard Interior +
		// 7-speed Porsche Doppelkupplung (PDK) + Porsche Ceramic Composite Brakes
		// (PCCB)

		System.out.println("Verification in step 25:");
		equipmentPrice = driver.findElement(By.xpath(equipmentPriceXpath)).getText();
		String optionalEquipmentPrice = driver.findElement(By.xpath(optionalEquipmentPriceXpath)).getText();
		String speedPrice = driver.findElement(By.xpath(speedPriceXpath)).getText();
		priceVerification(equipmentPrice,
				addition(miamiPrice, wheelPrice, seatPrice, interiorPrice, speedPrice, optionalEquipmentPrice));
		System.out.println("========================================================");

		// 26.Verify that total price is the sum of base price + Price for Equipment +
		// Delivery, Processing and Handling Fee
		System.out.println("Verification in step 26:");
		totalPrice = driver.findElement(By.xpath(totalPriceXpath)).getText();
		priceVerification(totalPrice, addition(basePrice2, equipmentPrice, deliveryPrice));
		System.out.println("========================================================");

		driver.close();
		driver.switchTo().window(parent);
		
		driver.close();
		System.out.println("TEST COMPLETED -" + LocalDateTime.now());
		
	}

	public static String addition(String num1, String num2) {
		int result = Integer.parseInt(priceConverter(num1)) + Integer.parseInt(priceConverter(num2));
		return result + "";
	}

	public static String addition(String num1, String num2, String num3) {
		int result = Integer.parseInt(priceConverter(num1)) + Integer.parseInt(priceConverter(num2))
				+ Integer.parseInt(priceConverter(num3));
		return result + "";
	}

	public static String addition(String num1, String num2, String num3, String num4) {
		int result = Integer.parseInt(priceConverter(num1)) + Integer.parseInt(priceConverter(num2))
				+ Integer.parseInt(priceConverter(num3)) + Integer.parseInt(priceConverter(num4));
		return result + "";
	}

	public static String addition(String num1, String num2, String num3, String num4, String num5, String num6) {
		int result = Integer.parseInt(priceConverter(num1)) + Integer.parseInt(priceConverter(num2))
				+ Integer.parseInt(priceConverter(num3)) + Integer.parseInt(priceConverter(num4))
				+ Integer.parseInt(priceConverter(num5)) + Integer.parseInt(priceConverter(num6));
		return result + "";
	}

	public static boolean priceVerification(String expected, String actual) {
		if (priceConverter(expected).equals(priceConverter(actual))) {
			System.out.println("Verification PASSED.");
			return true;
		} else {
			System.out.println("Verification FAILED.");
			return false;
		}
	}

	public static String priceConverter(String strPrice) {
		String price = "";
		String[] str = strPrice.split("\\.");   
		for (int i = 0; i < str[0].length(); i++) {
			if (Character.isDigit(str[0].charAt(i))) {
				price += str[0].charAt(i);
			}
		}

		return price;
	}

}
