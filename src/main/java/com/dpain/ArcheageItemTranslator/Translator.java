package com.dpain.ArcheageItemTranslator;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.google.common.base.Function;

public class Translator {

	public Translator() {
		System.out.println("Translator Initialized!");
	}

	WebDriver driver;

	public String getTranslation(String param, boolean useChrome) throws Exception {

		System.out.println("Input: " + param);

		String parseLink = null;
		boolean isEnglish = true;

		if (driver == null) {
			try {
				if (useChrome) {
					System.setProperty("webdriver.chrome.driver", "rsc/chromedriver.exe");
					driver = new ChromeDriver();
				} else {
					System.setProperty("phantomjs.binary.path", "rsc/phantomjs.exe");
					driver = new PhantomJSDriver();
				}
			} catch (Exception e) {
				throw new Exception("크롬이 없거나 내장 브라우저가 작동되지 않음!");
			}

			driver.manage().window().maximize();

			// Only works for Korean and English
			if (param.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")) {
				isEnglish = false;
				parseLink = "http://archeagedatabase.net/kr/search/";
			} else {
				isEnglish = true;
				parseLink = "http://archeagedatabase.net/en/search/";
			}
			driver.get(parseLink);
		} else {
			driver.manage().window().maximize();
			// Only works for Korean and English
			if (param.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")) {
				isEnglish = false;
				if (driver.getCurrentUrl().contains("/us/")) {
					parseLink = "http://archeagedatabase.net/kr/search/";
					driver.get(parseLink);
				}
			} else {
				isEnglish = true;
				if (driver.getCurrentUrl().contains("/kr/")) {
					parseLink = "http://archeagedatabase.net/en/search/";
					driver.get(parseLink);
				}
			}
		}

		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(5, TimeUnit.SECONDS)
				.pollingEvery(500, TimeUnit.MILLISECONDS).ignoring(NoSuchElementException.class);

		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[id=searchfield]")));

		WebElement searchBar = driver.findElement(By.cssSelector("input[id=searchfield]"));

		System.out.println("Loaded Search Bar!");

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		searchBar.sendKeys(param);

		try {
			WebElement firstSuggestion = wait.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver driver) {
					List<WebElement> list = driver.findElements(By.cssSelector("p[class='tt-suggestion tt-selectable']"));
					if (!list.isEmpty()) {
						return list.get(0).findElement(By.tagName("a"));
					}
					return null;
				}
			});
			
			firstSuggestion.click();

			if (isEnglish) {
				driver.get(driver.getCurrentUrl().replace("/us/", "/kr/"));
			} else {
				driver.get(driver.getCurrentUrl().replace("/kr/", "/us/"));
			}

			String name = driver.findElement(By.id("item_name")).findElement(By.tagName("b")).getText();

			System.out.println("Result: " + name);

			return name;
		} catch(Exception e) {
			// No result
			System.out.println("No result!");
			
			throw new Exception("결과 없음!");
		}
	}
}
