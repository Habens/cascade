package uk.co.malbec.welcometohell.ui.renderers;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.co.malbec.welcometohell.ui.Renderer;
import uk.co.malbec.welcometohell.ui.RenderingEngine;
import uk.co.malbec.welcometohell.wizard.domain.TitleMedium;

import java.io.StringWriter;
import java.util.Map;

import static uk.co.malbec.welcometohell.ui.RendererUtilities.generateClasses;
import static uk.co.malbec.welcometohell.ui.RendererUtilities.generateContent;

@Component
public class TitleMediumRenderer implements Renderer<TitleMedium> {

    @Autowired
    private RenderingEngine renderingEngine;

    @Override
    public boolean accepts(Class<?> clz) {
        return TitleMedium.class.equals(clz);
    }

    @Override
    public void render(VelocityEngine velocityEngine, String wizardSessionId, String pageId, TitleMedium element, StringWriter content, Map<String, Object> data) {
        VelocityContext context = new VelocityContext();

        generateClasses(element, context, "", "text");
        generateStyles(element, context);
        generateContent(wizardSessionId, pageId, renderingEngine, element, data, context);

        velocityEngine.getTemplate("views/title-medium.html").merge(context, content);
    }

    private void generateStyles(TitleMedium element, VelocityContext context) {
        StringBuilder styles = new StringBuilder();
        if (element.getRem() != null) {
            styles.append("font-size: " + element.getRem() + "rem; ");
        }

        if (element.getEm() != null) {
            styles.append("font-size: " + element.getEm() + "em; ");
        }
        context.put("styles", styles.toString());
    }
}