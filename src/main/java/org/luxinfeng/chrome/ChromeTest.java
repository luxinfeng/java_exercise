package org.luxinfeng.chrome;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class ChromeTest {

    public static void main(String[] argss) {

        Playwright playwright = Playwright.create();
        BrowserType chromium = playwright.chromium();
        Browser browser = chromium.launch(new BrowserType.LaunchOptions().setHeadless(false));
        BrowserContext context = browser.newContext();
        context.exposeFunction("sha256", args -> {
            String text = (String) args[0];
            MessageDigest crypto;
            try {
                crypto = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e) {
                return null;
            }
            byte[] token = crypto.digest(text.getBytes(StandardCharsets.UTF_8));
            System.out.println("66666666");
            return Base64.getEncoder().encodeToString(token);
        });
        Page page = context.newPage();
        page.setContent("<script>\n" +
                "  async function onClick() {\n" +
                "    document.querySelector('div').textContent = await window.sha256('PLAYWRIGHT');\n" +
                "  }\n" +
                "</script>\n" +
                "<button onclick=\"onClick()\">Click me</button>\n" +
                "<div></div>\n");
        page.getByRole(AriaRole.BUTTON).click();
    }

}
