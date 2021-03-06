package com.github.robindevilliers.welcometohell;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import com.github.robindevilliers.cascade.annotations.Demands;
import com.github.robindevilliers.cascade.annotations.Supplies;
import com.github.robindevilliers.cascade.events.Handler;
import com.github.robindevilliers.cascade.exception.CascadeException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class TakeScreenshot implements Handler {

    @Demands
    private WebDriver webDriver;

    @Supplies(stateRenderer = ScreenshotStateRendering.class)
    private String screenshot;

    @Demands
    static File REPORTS_DIRECTORY;

    @Override
    public void handle(Object step) {
        TakesScreenshot takesScreenshot = (TakesScreenshot) webDriver;
        byte[] bytes = takesScreenshot.getScreenshotAs(OutputType.BYTES);

        screenshot = "./screenshots/" + UUID.randomUUID().toString() + ".png";

        try {
            File screenshotsDir = new File(REPORTS_DIRECTORY, "screenshots");

            if (!screenshotsDir.exists()) {
                screenshotsDir.mkdir();
            }

            FileOutputStream fos = new FileOutputStream(new File(REPORTS_DIRECTORY, screenshot));
            fos.write(bytes);
        } catch (IOException e) {
            throw new CascadeException("Unable to write screen shot to file: " + screenshot);
        }
    }
}
